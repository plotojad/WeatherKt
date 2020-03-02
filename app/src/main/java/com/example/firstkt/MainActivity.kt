package com.example.firstkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), MainContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun showResult(nameCity: String, typeCity: String, season: String, midTemp: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initAdapters() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAdapters() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
