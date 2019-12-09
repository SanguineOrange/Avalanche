package com.vogella.android.scoretracker.data.response.score_info

//These are for determining if it is game day
data class todaysGame(
    val totalItems : Int,
    val dates : List<dates>
)

data class dates(
    val games : List<games>
)

data class games(
    val link : String
)

//These are for finding previous game info if it is not game day
data class previousGameInfo(
    val teams : List<teams>
)

data class previousGameSchedule(
    val dates: List<dates>
)

data class teams(   //Note, teams object used for both retrieving previous game info and displaying current game info
    val previousGameSchedule : previousGameSchedule, //for fetching previous game info

    val away : away,
    val home : home
)


//These are for displaying info of the most recent game or todays game if it is game day

data class gameInfoGrab(
    val liveData: liveData
)

data class liveData(
    val linescore : linescore,      //linescore contains period-by-period info and time based events
    val boxscore : boxscore         //boxscore contains team scores, team information, and team stats
)

//linescore handling
data class linescore(
    val currentPeriod : Int,
    val currentPeriodOrdinal : String,
    val currentPeriodTimeRemaining : String,

    val periods : List<periods>,
    val intermissionInfo : intermissionInfo,

    val powerPlayStrength : String,
    val powerPlayInfo : powerPlayInfo
)

data class periods(
    val num : Int,
    val ordinalNum : String,
    val home : home,
    val away : away
)

data class intermissionInfo(
    val intermissionTimeRemaining : Int,
    val inIntermission : Boolean
)

data class powerPlayInfo(
    val situationTimeRemaining : Int,
    val inSituation : Boolean
)

//boxscore handling
data class boxscore(
    val teams : teams
)

data class away(
    val team : team,
    val teamStats : teamStats,

    //these are used for linescore methods for gathering period info
    val goals : Int,
    val shotsOnGoal : Int
)

data class home(
    val team : team,
    val teamStats : teamStats,

    //these are used for linescore methods for gathering period info
    val goals : Int,
    val shotsOnGoal : Int
)

data class teamStats(
    val teamSkaterStats : teamSkaterStats
)

data class teamSkaterStats(
    val goals: Int,
    val shots: Int,
    val powerPlayGoals : Int,
    val powerPlayOpportunities : Int,
    val hits : Int
)


data class team(
    val name : String,
    val abbreviation : String,
    val id : Int
)



