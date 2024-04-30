package com.example.puzzelpluz

class Tile (
    private var number: Int? = null
){
    fun number():Int{
        return number !!
    }
}