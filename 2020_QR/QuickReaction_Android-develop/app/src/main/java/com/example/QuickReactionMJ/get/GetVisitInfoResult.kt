package com.example.QuickReactionMJ.get

import com.example.QuickReactionMJ.domain.Spot

data class GetVisitInfoResult(
        val localDateTime : String,
        val spot : Spot
)