package com.hlandim.marvelheroes.view.details

import com.hlandim.marvelheroes.database.model.Participation

data class ParticipationParent(
    val title: String,
    val participation: List<Participation>
)

data class ParticipationChild(
    val title: String
)