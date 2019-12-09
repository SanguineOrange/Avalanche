package com.vogella.android.scoretracker.data.response.team_info

import com.google.gson.annotations.SerializedName
import com.vogella.android.scoretracker.data.response.score_info.away
import com.vogella.android.scoretracker.data.response.score_info.home


data class teamInfoGrab(
    val teams : List<teams>
)


data class teamInfo(
    val id : Int,
    val name : String,
    val nextGameSchedule: gameSchedule,
    val previousGameSchedule: gameSchedule
)

data class teamSkaterStats(
    val goals : Int,
    val shots : Int,
    val hits : Int
)

data class teamStats(
    val teamSkaterStats : teamSkaterStats
)

data class team(
    val name : String,
    val link : String,
    val abbreviation : String
)

data class teams(
    val away : away,
    val home : home,

    val id : Int,
    val name : String,
    val nextGameSchedule: gameSchedule,
    val previousGameSchedule: gameSchedule
)

data class gameSchedule(
    val totalGames : Int,
    val dates : List<dates>
)

data class dates(
    val games : List<games>
)

data class games(
    val link: String
)