package com.vogella.android.scoretracker.ui.page_fragments.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vogella.android.scoretracker.R
import com.vogella.android.scoretracker.data.StatsNhlApi
import com.vogella.android.scoretracker.data.response.standings_info.allStandings
import kotlinx.android.synthetic.main.standings_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StandingsFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val finalView = inflater.inflate(R.layout.standings_fragment, container, false)

        val recycView = finalView.standingsRecycler
        recycView.layoutManager = LinearLayoutManager(activity)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://statsapi.web.nhl.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val statsNhlApi = retrofit.create(StatsNhlApi::class.java)
        val standingsRequest : Call<allStandings> =  statsNhlApi.grabCentralRecord()

        standingsRequest.enqueue(object : Callback<allStandings>{
            override fun onResponse(call: Call<allStandings>, response: Response<allStandings>) {
                val returnedInfo = response.body()!!
                recycView.adapter = standingsAdapter(returnedInfo)
            }

            override fun onFailure(call: Call<allStandings>, t: Throwable) {
                Toast.makeText(activity, "Could Not Connect to NHL.com, please try again", Toast.LENGTH_LONG)
                    .show()
            }
        })

        return finalView
    }
}