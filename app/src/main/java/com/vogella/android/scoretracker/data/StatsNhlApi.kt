package com.vogella.android.scoretracker.data


import com.vogella.android.scoretracker.data.response.roster_info.playerStats
import com.vogella.android.scoretracker.data.response.roster_info.rosterInfo
import com.vogella.android.scoretracker.data.response.schedule_info.nextGameInfo
import com.vogella.android.scoretracker.data.response.schedule_info.schedule
import com.vogella.android.scoretracker.data.response.score_info.gameInfoGrab
import com.vogella.android.scoretracker.data.response.score_info.previousGameInfo
import com.vogella.android.scoretracker.data.response.score_info.todaysGame
import com.vogella.android.scoretracker.data.response.standings_info.allStandings
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

//This interface will be utilized as it auto-generates the code from the @command, a link, and an abstract function
interface StatsNhlApi {
    @GET("/api/v1/schedule?teamId=21")
    fun isGameToday() : Call<todaysGame>

    @GET("/api/v1/teams/21?expand=team.schedule.previous")
    fun grabPreviousGame() : Call<previousGameInfo>

    @GET("api/v1/teams/21?expand=team.schedule.next&expand=schedule.ticket")
    fun grabNextGame() : Call<nextGameInfo>

    @GET
    fun grabGameInfo(@Url url: String) : Call <gameInfoGrab>

    @GET
    fun grabSchedule(@Url url: String) : Call<schedule>

    @GET("/api/v1/teams/21/roster")
    fun grabRoster() : Call<rosterInfo>

    @GET
    fun grabPlayerInfo(@Url url: String) : Call<playerStats>

    @GET("standings")
    fun grabCentralRecord() : Call<allStandings>
}