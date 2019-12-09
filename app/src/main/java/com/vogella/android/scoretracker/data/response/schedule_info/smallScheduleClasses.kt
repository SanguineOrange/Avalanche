package com.vogella.android.scoretracker.data.response.schedule_info

data class schedule(
    val dates : List<dates>
)

data class dates(
    val date : String,
    val games: List<games>
)

data class games(
    val status : status,
    val teams : teams,
    val link : String,

    //this is exclusively for the next game's info, usually null
    val tickets : List<tickets>
)

data class status(
    val detailedState : String
)

data class teams(
    val away : away,
    val home : home,

    //this is exclusively for getting the next game's ticket info, otherwise null
    val nextGameSchedule : nextGameSchedule
)

data class home(
    val score: Int,
    val team : team,
    val leagueRecord : leagueRecord
)

data class away(
    val score : Int,
    val team : team,
    val leagueRecord : leagueRecord
)

data class team(
    val name : String,
    val id : Int
)

data class leagueRecord(
    val wins : Int,
    val losses : Int,
    val ot: Int
)