package com.vogella.android.scoretracker.ui.page_fragments.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vogella.android.scoretracker.R
import com.vogella.android.scoretracker.data.StatsNhlApi
import com.vogella.android.scoretracker.data.response.roster_info.rosterInfo
import com.vogella.android.scoretracker.data.response.schedule_info.schedule
import kotlinx.android.synthetic.main.schedule_fragment.*
import kotlinx.android.synthetic.main.schedule_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ScheduleFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val finalView = inflater.inflate(R.layout.schedule_fragment, container, false)

        val schedRecyc = finalView.findViewById<RecyclerView>(R.id.scheduleRecycler)
        schedRecyc.layoutManager = LinearLayoutManager(activity)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://statsapi.web.nhl.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val dateToday = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateTodayString = dateFormat.format(dateToday)

        val statsNhlApi = retrofit.create(StatsNhlApi::class.java)
        val scheduleInfo : Call<schedule> = statsNhlApi.grabSchedule("schedule?teamId=21&startDate=2019-10-01&endDate=" + dateTodayString)

        scheduleInfo.enqueue(object : Callback<schedule>{
            override fun onResponse(call: Call<schedule>, response: Response<schedule>) {
                val returnedInfo = response.body()
                schedRecyc.adapter = scheduleAdapter(returnedInfo, dateTodayString)
                if (returnedInfo!!.dates.last().games[0].teams.home.team.name == "Colorado Avalanche") {
                    val record = returnedInfo.dates.last().games[0].teams.home.leagueRecord
                    val finalRecord: String =
                        record.wins.toString() + " - " + record.losses.toString() + " - " + record.ot.toString()
                    finalView.recordOverall.text = finalRecord
                } else {
                    val record = returnedInfo.dates.last().games[0].teams.away.leagueRecord
                    val finalRecord: String =
                        record.wins.toString() + " - " + record.losses.toString() + " - " + record.ot.toString()
                    finalView.recordOverall.text = finalRecord
                }
            }
            override fun onFailure(call: Call<schedule>, t: Throwable) {
                Toast.makeText(activity, "Could Not Connect to NHL.com, please try again", Toast.LENGTH_LONG)
                    .show()
            }
        })
        return finalView
    }
}