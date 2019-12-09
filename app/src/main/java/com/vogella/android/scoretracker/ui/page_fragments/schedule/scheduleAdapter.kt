package com.vogella.android.scoretracker.ui.page_fragments.schedule


import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.vogella.android.scoretracker.R
import com.vogella.android.scoretracker.data.response.schedule_info.schedule
import com.vogella.android.scoretracker.data.response.score_info.todaysGame
import kotlinx.android.synthetic.main.schedule_list_item.view.*


class scheduleAdapter(val schedule : schedule?, val todaysDate: String) : RecyclerView.Adapter<ScheduleViewHolder>() {
    var otlCount : Int = 0

    override fun getItemCount(): Int {
        return schedule?.dates?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val scheduleCell = layoutInflater.inflate(R.layout.schedule_list_item, parent, false)
        return ScheduleViewHolder(scheduleCell)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.itemView.scheduleDate.text = schedule!!.dates.get(position).date

        if (schedule.dates.get(position).games.get(0).teams.away.team.name != "Colorado Avalanche") {
            holder.itemView.scheduleOpposingName.text =
            ("vs " + schedule.dates.get(position).games.get(0).teams.away.team.name)

            val avsScore: Int = schedule.dates.get(position).games[0].teams.home.score
            val oppScore: Int = schedule.dates.get(position).games[0].teams.away.score
            holder.itemView.scheduleScore.text = ("$oppScore - $avsScore")
            val imageUrl =
                "https://www-league.nhlstatic.com/images/logos/teams-current-primary-light/"+
                        schedule.dates[position].games[0].teams.away.team.id +".svg"

            GlideToVectorYou
                .init()
                .with(holder.itemView.context)
                .load(
                    Uri.parse(imageUrl),
                    holder.itemView.scheduleLogo)

            if(schedule.dates[position].date == todaysDate){
                holder.itemView.scheduleResult.setText("")
            }
            else{
                if (avsScore > oppScore) {
                    holder.itemView.scheduleResult.setText("W")
                } else if(oppScore > avsScore){
                    holder.itemView.scheduleResult.setText("L")
                }
                else{
                    holder.itemView.scheduleResult.setText("")
                }
            }

        } else {

            holder.itemView.scheduleOpposingName.text =
                ("@ " + schedule.dates.get(position).games.get(0).teams.home.team.name)

            val avsScore: Int = schedule.dates.get(position).games[0].teams.away.score
            val oppScore: Int = schedule.dates.get(position).games[0].teams.home.score
            holder.itemView.scheduleScore.text = ("$avsScore - $oppScore")

            val imageUrl =
                "https://www-league.nhlstatic.com/images/logos/teams-current-primary-light/"+
                        schedule.dates[position].games[0].teams.home.team.id +".svg"

            GlideToVectorYou
                .init()
                .with(holder.itemView.context)
                .load(
                    Uri.parse(imageUrl),
                    holder.itemView.scheduleLogo)

            if(schedule.dates[position].date == todaysDate){
                holder.itemView.scheduleResult.setText("")
            }
            else {
                if (avsScore > oppScore) {
                    holder.itemView.scheduleResult.setText("W")
                } else if (oppScore > avsScore) {
                    holder.itemView.scheduleResult.setText("L")
                } else {
                    holder.itemView.scheduleResult.setText("")
                }
            }
        }
    }
}

class ScheduleViewHolder(schedView : View) : RecyclerView.ViewHolder(schedView)
