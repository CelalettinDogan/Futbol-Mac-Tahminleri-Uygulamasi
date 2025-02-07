package com.example.bankokuponpro.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Match(
      // Firebase'de key olabilir
    val Team1: String = "",
    val Team2: String = "",
    val DateTime: String = "",
    val League: String = "",
    val prediction:String="",

){

}
