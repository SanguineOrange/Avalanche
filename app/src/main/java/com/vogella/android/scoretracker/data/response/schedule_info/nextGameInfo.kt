package com.vogella.android.scoretracker.data.response.schedule_info

//these are for fetching the link for the next game's tickets

data class nextGameInfo(
    val teams: List<teams>
)

data class nextGameSchedule(
    val dates : List<dates>
)

data class tickets(
    val ticketLink : String
)