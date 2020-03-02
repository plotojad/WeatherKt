package com.example.firstkt

import android.content.Context

class MainRepository: MainContract.Repository {
    override fun init(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadCityListNames(): ArrayList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadCityInfo(name: String, season: String): Map<String, Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeCityInfo(
        nameCity: String,
        typeCity: String,
        winterT: Float,
        springT: Float,
        summerT: Float,
        autumnT: Float
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}