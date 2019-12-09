package com.vogella.android.scoretracker.ui.page_fragments.standings


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.vogella.android.scoretracker.R
import com.vogella.android.scoretracker.data.response.standings_info.allStandings
import kotlinx.android.synthetic.main.standings_list_item.view.*

class standingsAdapter(val allStandings: allStandings) : RecyclerView.Adapter<standingsAdapter.standingsHolder>() {
    override fun getItemCount(): Int {
        return allStandings.records[2].teamRecords.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): standingsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val standingsCell = layoutInflater.inflate(R.layout.standings_list_item, parent, false)
        return standingsHolder(standingsCell)
    }

    override fun onBindViewHolder(holder: standingsHolder, position: Int) {
        val rank: String = "#" + allStandings.records[2].teamRecords[position].divisionRank

        val record: String =
                    allStandings.records[2].teamRecords[position].leagueRecord.wins.toString() +
                    " - " + allStandings.records[2].teamRecords[position].leagueRecord.losses.toString() +
                    " - " + allStandings.records[2].teamRecords[position].leagueRecord.ot.toString()

        val imageUrl =
            "https://www-league.nhlstatic.com/images/logos/teams-current-primary-light/"+
                    allStandings.records[2].teamRecords[position].team.id + ".svg"

        GlideToVectorYou
            .init()
            .with(holder.itemView.context)
            .load(Uri.parse(imageUrl), holder.itemView.standingsLogo)


        holder.itemView.standingsName.text = allStandings.records[2].teamRecords[position].team.name
        holder.itemView.standingsRank.text = rank
        holder.itemView.standingsRecord.text = record
    }


    class standingsHolder(view: View) : RecyclerView.ViewHolder(view)
}