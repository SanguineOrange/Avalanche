package com.vogella.android.scoretracker.ui

import com.vogella.android.scoretracker.R

//tfw the api doesn't have hex codes for colors so you have to dedicate 32 lines and 30 minutes to doing it yourself
class colorChanger(){
    fun colorChangeByName(teamName: String?) : Int {
        when(teamName){
            "Anaheim Ducks" -> return R.color.ana_primary
            "Arizona Coyotes" -> return R.color.ari_primary
            "Boston Bruins" -> return R.color.bos_primary
            "Buffalo Sabres" -> return R.color.buf_primary
            "Carolina Hurricanes" -> return R.color.car_primary
            "Calgary Flames" -> return R.color.cgy_primary
            "Columbus Blue Jackets" -> return R.color.cbj_primary
            "Chicago Blackhawks" -> return R.color.chi_primary
            "Colorado Avalanche" -> return R.color.colorPrimary
            "Dallas Stars" -> return R.color.dal_primary
            "Detroit Red Wings" -> return R.color.det_primary
            "Edmonton Oilers" -> return R.color.edm_primary
            "Florida Panthers" -> return R.color.fla_primary
            "Los Angeles Kings" -> return R.color.lak_primary
            "Minnesota Wild" -> return R.color.min_primary
            "Montréal Canadiens" -> return R.color.mtl_primary
            "Nashville Predators" -> return R.color.nsh_primary
            "New Jersey Devils" -> return R.color.njd_primary
            "New York Islanders" -> return R.color.nyi_primary
            "New York Rangers" -> return R.color.nyr_primary
            "Ottawa Senators" -> return R.color.ott_primary
            "Philadelphia Flyers" -> return R.color.phi_primary
            "Pittsburgh Penguins" -> return R.color.pit_primary
            "St. Louis Blues" -> return R.color.stl_primary
            "San Jose Sharks" -> return R.color.sjs_primary
            "Tampa Bay Lightning" -> return R.color.tbl_primary
            "Toronto Maple Leafs" -> return R.color.tor_primary
            "Vancouver Canucks" -> return R.color.van_primary
            "Vegas Golden Knights" -> return R.color.vgk_primary
            "Washington Capitals" -> return R.color.wsh_primary
            "Winnipeg Jets" -> return R.color.wpg_primary
            else -> return R.color.colorPrimary
        }
    }
}