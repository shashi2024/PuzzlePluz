package com.example.puzzelpluz

interface BoardChangeListener {
    fun tileSlid(from: Place?, to:Place?, numOfMoves:Int)
    fun solved(numOfMoves: Int)

}