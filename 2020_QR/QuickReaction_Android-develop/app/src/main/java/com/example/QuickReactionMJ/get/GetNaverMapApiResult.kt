package com.example.QuickReactionMJ.get

import com.example.QuickReactionMJ.domain.addresses
import com.example.QuickReactionMJ.domain.meta

data class GetNaverMapApiResult(
        val status : String,
        val meta : meta,
        val addresses : List<addresses>,
        val errorMessage : String
)