package com.trateg.bepresensimobile.util

object TextHelper: TextHelperContract() {
    override fun captEachWord(text: String): String {
        val str = text.toLowerCase()
        val words = str.split(" ").toMutableList()
        var output = ""
        for(word in words){
            output += word.capitalize() +" "
        }
        output = output.trim()
        return output
    }
}