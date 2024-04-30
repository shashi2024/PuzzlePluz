package com.example.puzzelpluz

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private   var mainView:ViewGroup?=null
    private   var board:Board?=null
    private   var boardView:BoardView?=null
    private lateinit var moves: TextView
    private var boardSize = 4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /**set findId*/
        mainView = findViewById(R.id.mainView)
        moves = findViewById(R.id.moves)
        moves.setTextColor(Color.BLUE)
        moves.textSize = 22f
        newGame()
    }

    @SuppressLint("SetTextI18n")
    private fun newGame(){
        board = Board(boardSize)
        board!!.addBoardChangeListener(boardChangeListener)
        board!!.rearrnge()
        mainView!!.removeView(boardView)
        boardView = BoardView(this,board!!)
        mainView!!.addView(boardView)
        moves.text = "Number of movements : 10"
    }

    fun changeSize(newSize:Int) {
        if (newSize != boardSize){
            boardSize = newSize
            newGame()
            boardView!!.invalidate()
        }
    }


    private val boardChangeListener :BoardChangeListener = object : BoardChangeListener{
        @SuppressLint("SetTextI18n")
        override fun tileSlid(from: Place?, to: Place?, numOfMoves: Int) {
            moves.text = "Number of movements : $numOfMoves"
        }

        @SuppressLint("SetTextI18n")
        override fun solved(numOfMoves: Int) {
            moves.text = "Solved in $numOfMoves moves"

            AlertDialog.Builder(this@MainActivity)
                .setTitle("Congratulations, You won... !!!")
                .setIcon(R.drawable.ic_celebration)
                .setMessage("you are win in $numOfMoves moves...!! \nIf you want a new Game..!!")
                .setPositiveButton("yes"){
                    dialog,_->
                    board!!.rearrnge()
                    moves.text = "Number of movements : 0"
                    this@MainActivity.boardView!!.invalidate()
                    dialog.dismiss()
                }
                .setNegativeButton("No"){
                    dialog,_->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)

        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_new_game -> {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("New Game")
                    .setIcon(R.drawable.ic_new_game)
                    .setMessage("Are you sure you want to begin a New game???")
                    .setPositiveButton("yes") { dialog, _ ->
                        board!!.rearrnge()
                        moves.text = "Number of movements : 0"
                        this.boardView!!.invalidate()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()

                    }
                    .create()
                    .show()

                true
            }

            R.id.action_setting -> {
                val settings = SettingsDialogFragment(boardSize)
                settings.show(supportFragmentManager, "fragment settings")
                true
            }

            R.id.action_help -> {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Instruction")
                    .setMessage("The goal of the puzzle is to place the tiles in order by making sliding moves that use the empty space. The only valid moves are to move a tile which is immediately adjacent to the blank into the location of the blank.")
                    .setPositiveButton("Understood, Let's play!!") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}
