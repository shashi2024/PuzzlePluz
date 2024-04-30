package com.example.puzzelpluz

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SettingsDialogFragment(private var size: Int): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
            .setTitle("Define the size of the puzzle")
            .setSingleChoiceItems(R.array.size_options, size-2){
                    _, which->
                size = which + 2
            }
            .setPositiveButton("change"){
                    _, _ ->
                (activity as MainActivity).changeSize(size)
            }
            .setNegativeButton("cancel"){
                dialog,_-> dialog.cancel()
            }
        val settingsDialog = builder.create()
        settingsDialog.show()
        return settingsDialog
    }
}