package com.vogella.android.scoretracker.ui.page_fragments.roster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vogella.android.scoretracker.R
import com.vogella.android.scoretracker.data.StatsNhlApi
import com.vogella.android.scoretracker.data.response.roster_info.rosterInfo
import kotlinx.android.synthetic.main.roster_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RosterFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewFinal = inflater.inflate(R.layout.roster_fragment, container, false)

        val rosterRecycler= viewFinal.rosterRecycler
        rosterRecycler.layoutManager = LinearLayoutManager(activity)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://statsapi.web.nhl.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val statsNhlApi = retrofit.create(StatsNhlApi::class.java)
        val retrieveRoster : Call<rosterInfo> = statsNhlApi.grabRoster()

        retrieveRoster.enqueue(object : Callback<rosterInfo> {
            override fun onResponse(call: Call<rosterInfo>, response: Response<rosterInfo>) {
                val returnedInfo = response.body()!!
                rosterRecycler.adapter = rosterAdapter(returnedInfo)
            }

            override fun onFailure(call: Call<rosterInfo>, t: Throwable) {
                Toast.makeText(activity, "Could Not Connect to NHL.com, please try again", Toast.LENGTH_LONG)
                    .show()
            }
        })

        return viewFinal
    }
}