package com.vogella.android.scoretracker.ui.page_fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.vogella.android.scoretracker.R
import com.vogella.android.scoretracker.data.StatsNhlApi
import com.vogella.android.scoretracker.data.response.schedule_info.nextGameInfo
import com.vogella.android.scoretracker.data.response.schedule_info.team
import kotlinx.android.synthetic.main.next_game_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class nextGameFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val finalView = inflater.inflate(com.vogella.android.scoretracker.R.layout.next_game_fragment, container, false)

        //creates an API call for the next game info
        val retrofit = Retrofit.Builder()
            .baseUrl("https://statsapi.web.nhl.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val statsNhlApi = retrofit.create(StatsNhlApi::class.java)
        val nextGameCall : Call<nextGameInfo> = statsNhlApi.grabNextGame()
        var ticketUrl : String = "https://www.nhl.com/avalanche/tickets"

        nextGameCall.enqueue(object : Callback<nextGameInfo>{
            override fun onResponse(call: Call<nextGameInfo>, response: Response<nextGameInfo>) {
                val returnedInfo = response.body()!!
                //grabs most appropriate ticket sales link from the NHL's API
                ticketUrl = returnedInfo.teams[0].nextGameSchedule.dates[0].games[0].tickets[0].ticketLink

                //grabs and displays team info for the avalanche's opponent as well as game date
                finalView.nextGameDate.text = returnedInfo.teams[0].nextGameSchedule.dates[0].date
                if(returnedInfo.teams[0].nextGameSchedule.dates[0].games[0].teams.home.team.id != 21) {
                    //if the home team is not colorado (id:21) it displays the proper home team's info
                    val teamCity = "@ " + returnedInfo.teams[0].nextGameSchedule.dates[0].games[0].teams.home.team.name.split(' ')[0]
                    finalView.nextGameVersus.text = teamCity



                    val imageUrl = "https://www-league.nhlstatic.com/images/logos/teams-current-primary-light/"+
                            returnedInfo.teams[0].nextGameSchedule.dates[0].games[0].teams.home.team.id+".svg"

                    //library from https://github.com/corouteam/GlideToVectorYou
                    GlideToVectorYou
                        .init()
                        .with(activity)
                        .load(
                            Uri.parse(imageUrl),
                            finalView.nextGameLogo)
                }
                else {
                    //otherwise it displays the away team, as the avalanche would logically be home
                    val teamCity = "vs " + returnedInfo.teams[0].nextGameSchedule.dates[0].games[0].teams.away.team.name.split(' ')[0]
                    finalView.nextGameVersus.text = teamCity

                    val imageUrl = "https://www-league.nhlstatic.com/images/logos/teams-current-primary-light/"+
                            returnedInfo.teams[0].nextGameSchedule.dates[0].games[0].teams.away.team.id+".svg"

                    //library from https://github.com/corouteam/GlideToVectorYou
                    GlideToVectorYou
                        .init()
                        .with(activity)
                        .load(
                            Uri.parse(imageUrl),
                            finalView.nextGameLogo)
                }
            }
            override fun onFailure(call: Call<nextGameInfo>, t: Throwable) {
                Toast.makeText(activity, "Could Not Connect to NHL.com, please try again", Toast.LENGTH_LONG)
                    .show()
            }
        })

        //finally sets a listener for the button that opens the browser with a URL and an intent
        finalView.nextGameTicketButton.setOnClickListener{
            val ticketIntent = Intent(Intent.ACTION_VIEW)
            ticketIntent.setData(Uri.parse(ticketUrl))
            startActivity(ticketIntent)
        }
        return finalView
    }
}