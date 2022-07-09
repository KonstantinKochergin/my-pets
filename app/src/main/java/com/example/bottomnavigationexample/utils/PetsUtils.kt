package com.example.bottomnavigationexample.utils

class PetsUtils {

    companion object {
        fun getItemIndex(array: Array<String>, item: String) : Int {
            return array.indexOf(item)
        }
    }
}