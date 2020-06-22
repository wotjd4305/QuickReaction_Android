package com.example.QuickReactionMJ.post

import com.example.QuickReactionMJ.domain.Address

data class PostSpotSaveResult(

        val lat: String,
        val lng : String,
        val name : String,
        val address : Address

)