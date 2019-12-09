package com.vogella.android.scoretracker.data.response.roster_info

//These are for fetching the full roster from the avs' roster page
data class rosterInfo(
    val roster : List<roster>
)

data class roster(
   val person   : person,
   val position : position,
   val jerseyNumber : Int
)

data class person(
    val id : Int,
    val fullName : String,
    val link : String
)

data class position(
    val type : String
)

//these are for gathering the full info from each team member's individual stat pages

data class playerStats(
    val stats : List<stats>
)

data class stats(
    val splits : List<splits>
)

data class splits(
    val stat : stat
)

data class stat(
    val games : Int,

    //for forwards and d-men
    val assists : Int,
    val goals : Int,
    val plusMinus : Int,


    //for goaltenders
    val savePercentage : Float,
    val goalAgainstAverage : Float,

    //forward/d stats I may use later but don't collect for display yet
    val hits : Int,
    val shots : Int,
    val timeOnIcePerGame: String,
    val gameWinningGoals : Int,

    //goalie stats I may use later but don't collect for display yet
    val wins : Int,
    val losses : Int,
    val shutouts : Int,
    val shotsAgainst: Int,
    val goalsAgainst: Int,
    val gamesStarted : Int
)