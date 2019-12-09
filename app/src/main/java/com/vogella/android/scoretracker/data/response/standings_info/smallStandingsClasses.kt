package com.vogella.android.scoretracker.data.response.standings_info

data class allStandings(
    val records : List<records>
)

data class records(
    val teamRecords : List<teamRecords>
)

data class teamRecords(
    val team: team,
    val leagueRecord : leagueRecord,
    val divisionRank : String,
    val leagueRank : String
)

data class team(
    val id : Int,
    val name : String,
    val link : String
)

data class leagueRecord(
    val wins : Int,
    val losses : Int,
    val ot : Int
)
