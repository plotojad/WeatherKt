package com.example.firstkt

import android.content.Context

interface MainContract {
    interface View {
        fun showResult(nameCity: String, typeCity: String, season: String, midTemp: String)
        fun initAdapters()
        fun updateAdapters()
    }

    interface Presenter {
        fun loadCityList(): ArrayList<String>?
        fun addInfo(data: Map<String, String>)
        fun onCityWasSelected(name: String, season: String)
        fun onDestroy()
    }

    interface Repository {
        fun initial(context: Context)
        fun loadCityListNames(): ArrayList<String>?
        fun loadCityInfo(name: String, season: String): Map<String, Any>
        fun writeCityInfo(
            nameCity: String,
            typeCity: String,
            winterT: Float,
            springT: Float,
            summerT: Float,
            autumnT: Float
        )

        fun onDestroy()
    }
}