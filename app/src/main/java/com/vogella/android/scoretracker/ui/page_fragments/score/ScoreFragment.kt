package com.vogella.android.scoretracker.ui.page_fragments.score

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.vogella.android.scoretracker.R
import com.vogella.android.scoretracker.data.StatsNhlApi
import com.vogella.android.scoretracker.data.response.score_info.gameInfoGrab
import com.vogella.android.scoretracker.data.response.score_info.previousGameInfo
import com.vogella.android.scoretracker.data.response.score_info.todaysGame
import com.vogella.android.scoretracker.ui.colorChanger
import kotlinx.android.synthetic.main.away_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.score_fragment.*
import kotlinx.android.synthetic.main.score_stats_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.fixedRateTimer


class ScoreFragment : Fragment() {
    lateinit var mainHandler : Handler

    //this is a simple runnable object for calling the score refresh function every 10 seconds
    //code adapted from https://stackoverflow.com/questions/55570990/kotlin-call-a-function-every-second/55571277
    private val refreshScoreTimerTask = object : Runnable{
        override fun run() {
            refreshScore()
            mainHandler.postDelayed(this, 10000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewFinal = inflater.inflate(R.layout.score_fragment, container, false)
        refreshScore()

        //refreshes the score manually if you swipe down
        val swiper = viewFinal.findViewById<SwipeRefreshLayout>(R.id.swiperScore)
        swiper.setOnRefreshListener {
            refreshScore()
            swiper.isRefreshing = false
        }

        //start a timer to refresh the score every 10 seconds
        mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(refreshScoreTimerTask)

        return viewFinal
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(refreshScoreTimerTask)      //removes the refresh timer when closed, was causing a crash before
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(refreshScoreTimerTask)                 //puts the timer back on the handler when fragment comes back into view
    }

    //this refreshes the score fragment by making an api call with retrofit and storing the data into a returned data object
    fun refreshScore(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://statsapi.web.nhl.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val statsNhlApi = retrofit.create(StatsNhlApi::class.java)
        val getTodaysGame: Call<todaysGame> = statsNhlApi.isGameToday()


        getTodaysGame.enqueue(object : Callback<todaysGame> {
            override fun onFailure(call: Call<todaysGame>, t: Throwable) {
                Toast.makeText(activity, "Could Not Connect to NHL.com, please try again", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onResponse(call: Call<todaysGame>, response: Response<todaysGame>) {
                val todayGameInfo = response.body()!!

                if (todayGameInfo.totalItems != 0) {
                    //if this is true, then the avs have a game today and the next api read will be for today's game
                    val getInfoOnTodaysGame : Call<gameInfoGrab> = statsNhlApi.grabGameInfo(
                        todayGameInfo.dates[0].games[0].link    //note the NHL stats API only refreshes this data every day at 4AM MST
                    )

                    getInfoOnTodaysGame.enqueue(object : Callback<gameInfoGrab>{
                        override fun onResponse(call: Call<gameInfoGrab>, response: Response<gameInfoGrab>) {
                            val gameInfo = response.body()
                            populateFragment(gameInfo)

                        }
                        override fun onFailure(call: Call<gameInfoGrab>, t: Throwable) {
                            Toast.makeText(activity, "Could Not Connect to NHL.com, please try again", Toast.LENGTH_LONG)
                                .show()
                        }
                    })

                } else {
                    //else the previous game's info will be used, which the link will be grabbed via another api call
                    val getPreviousGame: Call<previousGameInfo> = statsNhlApi.grabPreviousGame()
                    getPreviousGame.enqueue(object : Callback<previousGameInfo> {
                        override fun onResponse(call: Call<previousGameInfo>, response: Response<previousGameInfo>) {
                            val lastGameInfo = response.body()!!

                            val getInfoOnLastGame : Call<gameInfoGrab> = statsNhlApi.grabGameInfo(
                                lastGameInfo.teams[0].previousGameSchedule.dates[0].games[0].link
                            )

                            getInfoOnLastGame.enqueue(object : Callback<gameInfoGrab>{
                                override fun onResponse(call: Call<gameInfoGrab>, response: Response<gameInfoGrab>) {
                                    val gameInfo = response.body()!!
                                    populateFragment(gameInfo)
                                }

                                override fun onFailure(call: Call<gameInfoGrab>, t: Throwable) {
                                    Toast.makeText(activity, "Could Not Connect to NHL.com, please try again", Toast.LENGTH_LONG)
                                        .show()
                                }
                            })
                        }
                        override fun onFailure(call: Call<previousGameInfo>, t: Throwable) {
                            Toast.makeText(activity, "Could Not Connect to NHL.com, please try again", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
                }
            }
        })

    }

    //this function takes a data object returned from an API call and populates the fragment with that info
    fun populateFragment(gameInfo : gameInfoGrab?) {
        //Displays the game's current status (IE what period it is, or if it is over)
        when (gameInfo?.liveData?.linescore?.currentPeriodTimeRemaining) {
            null -> {
                period.text = getString(R.string.waiting_for_game_message)
                gameClock.text = " "
            }
            "Final" -> {
                period.text = gameInfo.liveData.linescore.currentPeriodTimeRemaining
                gameClock.text = " "
            }
            else -> {
                if(!gameInfo.liveData.linescore.intermissionInfo.inIntermission){
                    period.text = gameInfo.liveData.linescore.currentPeriodOrdinal
                    gameClock.text = gameInfo.liveData.linescore.currentPeriodTimeRemaining
                }
                else{
                    period.text = gameInfo.liveData.linescore.currentPeriodOrdinal
                    gameClock.text = getString(R.string.int_clock)
                }
            }
        }

        homeScore.text =
            gameInfo!!.liveData.boxscore.teams.home.teamStats.teamSkaterStats.goals.toString()
        awayScore.text =
            gameInfo.liveData.boxscore.teams.away.teamStats.teamSkaterStats.goals.toString()

        homeAbrv.text = gameInfo.liveData.boxscore.teams.home.team.abbreviation
        awayAbrv.text = gameInfo.liveData.boxscore.teams.away.team.abbreviation

        AwaySog.text =
            gameInfo.liveData.boxscore.teams.away.teamStats.teamSkaterStats.shots.toString()
        homeSog.text =
            gameInfo.liveData.boxscore.teams.home.teamStats.teamSkaterStats.shots.toString()

        val awayPPOstr : String =
            gameInfo.liveData.boxscore.teams.away.teamStats.teamSkaterStats.powerPlayGoals.toString() + "/" +
                    gameInfo.liveData.boxscore.teams.away.teamStats.teamSkaterStats.powerPlayOpportunities.toString()
        val homePPOstr : String =
            gameInfo.liveData.boxscore.teams.home.teamStats.teamSkaterStats.powerPlayGoals.toString() + "/" +
                    gameInfo.liveData.boxscore.teams.home.teamStats.teamSkaterStats.powerPlayOpportunities.toString()

        awayPPO.text = awayPPOstr
        homePPO.text = homePPOstr

        //invokes a color changer object, which I kept a seperate object for keeping this particular page of code less than 300 lines
        val colorChanger = colorChanger()
        homeScore.setBackgroundResource(colorChanger.colorChangeByName(
            gameInfo.liveData.boxscore.teams.home.team.name))
        awayScore.setBackgroundResource(colorChanger.colorChangeByName(
            gameInfo.liveData.boxscore.teams.away.team.name)
        )

        //GlideToVectorYou is an third party library that lets you use SVGs natively in a TextView
        //sourced from https://github.com/corouteam/GlideToVectorYou
        GlideToVectorYou
            .init()
            .with(context)
            .load(Uri.parse(
                "https://www-league.nhlstatic.com/images/logos/teams-current-primary-light/" +
                        gameInfo.liveData.boxscore.teams.away.team.id +".svg"),
                awayLogo)

        GlideToVectorYou
            .init()
            .with(context)
            .load(Uri.parse(
                "https://www-league.nhlstatic.com/images/logos/teams-current-primary-light/" +
                        gameInfo.liveData.boxscore.teams.home.team.id +".svg"),
                homeLogo)
    }
}


