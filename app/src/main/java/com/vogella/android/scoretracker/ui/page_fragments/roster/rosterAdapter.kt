package com.vogella.android.scoretracker.ui.page_fragments.roster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vogella.android.scoretracker.R
import com.vogella.android.scoretracker.data.StatsNhlApi
import com.vogella.android.scoretracker.data.response.roster_info.playerStats
import com.vogella.android.scoretracker.data.response.roster_info.roster
import com.vogella.android.scoretracker.data.response.roster_info.rosterInfo
import kotlinx.android.synthetic.main.roster_item_defenseman.view.*
import kotlinx.android.synthetic.main.roster_item_forward.view.*
import kotlinx.android.synthetic.main.roster_item_goalie.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class rosterAdapter(val rosterFull : rosterInfo) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return rosterFull.roster.size
    }

    companion object {
        const val FORWARD = 0
        const val DEFENSEMAN = 1
        const val GOALIE = 2
    }

    override fun getItemViewType(position: Int): Int {
        val toReturn = when (rosterFull.roster[position].position.type) {
            "Forward" -> {
                FORWARD
            }
            "Defenseman" -> {
                DEFENSEMAN
            }
            "Goalie" -> {
                GOALIE
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
        return toReturn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val myView : View
        val inflater = LayoutInflater.from(parent.context)


        when (viewType) {
            FORWARD -> {
                val forwardView = inflater.inflate(R.layout.roster_item_forward, parent, false)
                myView = forwardView
            }
            DEFENSEMAN -> {
                val defenseView = inflater.inflate(R.layout.roster_item_defenseman, parent, false)
                myView = defenseView
            }
            GOALIE -> {
                val goalieView = inflater.inflate(R.layout.roster_item_goalie, parent, false)
                myView = goalieView
            }
            else -> throw IllegalArgumentException()
        }
        return myViewHolder(myView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            FORWARD -> {
                configureForwardInfo(holder.itemView, position)
            }
            DEFENSEMAN ->{
                configureDefenseInfo(holder.itemView, position)
            }
            GOALIE ->{
                configureGoalieInfo(holder.itemView, position)
            }
            else -> throw IllegalArgumentException()
        }
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://statsapi.web.nhl.com/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val statsNhlApi = retrofit.create(StatsNhlApi::class.java)

    fun configureForwardInfo(view : View, position: Int){
        val jerseyNumber = "#${rosterFull.roster[position].jerseyNumber}"
        val playerID = rosterFull.roster[position].person.id
        view.forwardPlayerName.text = rosterFull.roster[position].person.fullName
        view.forwardJerseyNumber.text = jerseyNumber

        Picasso.get()
            .load("https://nhl.bamcontent.com/images/headshots/current/60x60/"+ playerID +".png")
            .into(view.forwardPortrait);

        val forwardInfoRequest : Call<playerStats> = statsNhlApi
            .grabPlayerInfo("https://statsapi.web.nhl.com/api/v1/people/"+ playerID +"/stats?stats=statsSingleSeason&season=20192020")

        forwardInfoRequest.enqueue(object : Callback<playerStats>{
            override fun onResponse(call: Call<playerStats>, response: Response<playerStats>) {
                val returnedData = response.body()!!
                if(!returnedData.stats.get(0).splits.isEmpty()) {
                    view.forwardGPCount.text = returnedData.stats[0].splits[0].stat.games.toString()
                    view.forwardGoalCount.text = returnedData.stats[0].splits[0].stat.goals.toString()
                    view.forwardAssistCount.text = returnedData.stats[0].splits[0].stat.assists.toString()
                    if(returnedData.stats[0].splits[0].stat.plusMinus > 0){
                        val plus ="+" + returnedData.stats[0].splits[0].stat.plusMinus.toString()
                        view.forwardPlusMin.text = plus
                    }
                    else
                        view.forwardPlusMin.text = returnedData.stats[0].splits[0].stat.plusMinus.toString()
                }
            }
            override fun onFailure(call: Call<playerStats>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun configureDefenseInfo(view: View, position: Int){
        val playerID = rosterFull.roster[position].person.id
        val jerseyNumber = "#${rosterFull.roster[position].jerseyNumber}"
        view.defensePlayerName.text = rosterFull.roster[position].person.fullName
        view.defenseJerseyNumber.text = jerseyNumber

        Picasso.get()
            .load("https://nhl.bamcontent.com/images/headshots/current/60x60/"+ playerID +".png")
            .into(view.defensePortrait)

        val defensiveInfoRequest : Call<playerStats> = statsNhlApi.grabPlayerInfo(
            "https://statsapi.web.nhl.com/api/v1/people/"+ playerID +"/stats?stats=statsSingleSeason&season=20192020"
        )

        defensiveInfoRequest.enqueue(object: Callback<playerStats>{
            override fun onResponse(call: Call<playerStats>, response: Response<playerStats>) {
                val returnedData = response.body()!!
                if(!returnedData.stats.get(0).splits.isEmpty()){
                    view.defenseGPCount.text = returnedData.stats[0].splits[0].stat.games.toString()
                    view.defenseGoalsCount.text = returnedData.stats[0].splits[0].stat.goals.toString()
                    view.defenseAssistsCount.text = returnedData.stats[0].splits[0].stat.assists.toString()
                    if(returnedData.stats[0].splits[0].stat.plusMinus > 0){
                        val plus ="+" + returnedData.stats[0].splits[0].stat.plusMinus.toString()
                        view.defensePlusMinus.text = plus
                    }
                    else
                        view.defensePlusMinus.text = returnedData.stats[0].splits[0].stat.plusMinus.toString()
                }

            }
            override fun onFailure(call: Call<playerStats>, t: Throwable) {
                    Toast.makeText(view.context, "Error! Could not connect to NHL.com! Please try again", Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    fun configureGoalieInfo(view: View, position: Int) {
        val playerID = rosterFull.roster[position].person.id
        val jerseyNumber = "#${rosterFull.roster[position].jerseyNumber}"
        view.goaliePlayerName.text = rosterFull.roster[position].person.fullName
        view.goalieJerseyNumber.text = jerseyNumber

        Picasso.get()
            .load("https://nhl.bamcontent.com/images/headshots/current/60x60/" + playerID + ".png")
            .into(view.goaliePortrait);
        val goalieInfoRequest: Call<playerStats> = statsNhlApi
            .grabPlayerInfo("https://statsapi.web.nhl.com/api/v1/people/" + playerID + "/stats?stats=statsSingleSeason&season=20192020")

        goalieInfoRequest.enqueue(object : Callback<playerStats> {
            override fun onResponse(call: Call<playerStats>, response: Response<playerStats>) {
                val returnedData = response.body()!!
                if (!returnedData.stats.get(0).splits.isEmpty()) {
                    view.goalieGPCount.text = returnedData.stats[0].splits[0].stat.games.toString()
                    view.goalieSavePercentage.text =
                        returnedData.stats[0].splits[0].stat.savePercentage.toString()
                    view.goalieGoalsAgainstCount.text =
                        returnedData.stats[0].splits[0].stat.goalAgainstAverage.toString()
                }
            }
            override fun onFailure(call: Call<playerStats>, t: Throwable) {
                Toast.makeText(view.context, "Error! Could not connect to NHL.com! Please try again", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}

class myViewHolder(view: View) : RecyclerView.ViewHolder(view)