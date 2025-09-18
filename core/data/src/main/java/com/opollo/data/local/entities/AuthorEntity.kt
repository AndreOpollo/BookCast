package com.opollo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("authors")
data class AuthorEntity(
    @PrimaryKey val id:String,
    val firstName:String,
    val lastName:String
)