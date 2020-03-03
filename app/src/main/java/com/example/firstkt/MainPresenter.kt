package com.example.firstkt

import android.content.Context
import android.content.SharedPreferences

class MainPresenter(private val mView: MainContract.View, context: Context) : MainContract.Presenter {


    companion object {
        const val KEY_FORMAT = "keyTFormat"
        const val NAME_SETTINGS = "mSettings"
        const val KEY_ADD_CONVERT = 0
        const val KEY_SHOW_CONVERT = 1
    }

    private var sharedPreferences: SharedPreferences
    private var mRepository: MainContract.Repository = MainRepository.instance

    private lateinit var name: String
    private lateinit var type: String
    private lateinit var season: String
    private lateinit var middleTemp: String
    private lateinit var cityInfoMap: Map<String, Any>


    init {
        mRepository.initial(context)
        sharedPreferences = context.getSharedPreferences(NAME_SETTINGS, Context.MODE_PRIVATE)
    }


    override fun loadCityList(): ArrayList<String>? {
        return mRepository.loadCityListNames()
    }

    override fun addInfo(data: Map<String, String>) {
        val name = data["name"].toString()
        val type = data["type"].toString()
        val winterT = calculateToMiddle(
            data.getValue("decTemp").toFloat(),
            data.getValue("janTemp").toFloat(),
            data.getValue("febTemp").toFloat()
        )
        val springT = calculateToMiddle(
            data.getValue("marTemp").toFloat(),
            data.getValue("aprTemp").toFloat(),
            data.getValue("mayTemp").toFloat()
        )
        val summerT = calculateToMiddle(
            data.getValue("junTemp").toFloat(),
            data.getValue("julTemp").toFloat(),
            data.getValue("augTemp").toFloat()
        )
        val autumnT = calculateToMiddle(
            data.getValue("sepTemp").toFloat(),
            data.getValue("ocTemp").toFloat(),
            data.getValue("novTemp").toFloat()
        )
        mRepository.writeCityInfo(
            name, type, converterTemp(winterT, KEY_ADD_CONVERT),
            converterTemp(springT, KEY_ADD_CONVERT),
            converterTemp(summerT, KEY_ADD_CONVERT),
            converterTemp(autumnT, KEY_ADD_CONVERT)
        )
        mView.initAdapters()
        mView.updateAdapters()

    }

    override fun onCityWasSelected(name: String, season: String) {
        cityInfoMap = mRepository.loadCityInfo(name, season)
        this.name = name
        this.type = cityInfoMap["type"].toString()
        this.season = season
        this.middleTemp = convertTempToString(cityInfoMap["midTemp"] as Float)
        mView.showResult(this.name, this.type, this.season, this.middleTemp)
    }

    override fun onDestroy() {
        mRepository.onDestroy()
    }


    private fun calculateToMiddle(first: Float, second: Float, third: Float): Float {
        return (first + second + third) / 3.0f
    }

    private fun convertTempToString(arg: Float): String {
        var tempStr = "No data"
        when (sharedPreferences.getString(KEY_FORMAT, "")) {
            "Цельсий" -> tempStr = (String.format("%.1f", arg) + "°C")
            "Фаренгейт" -> tempStr =
                (String.format("%.1f", converterTemp(arg, KEY_SHOW_CONVERT))) + "°F"
            "Кельвин" -> tempStr =
                (String.format("%.1f", converterTemp(arg, KEY_SHOW_CONVERT))) + "°K"
        }

        return tempStr
    }

    private fun converterTemp(argt: Float, reverse: Int): Float {
        var conversTemp = 0.0f
        when (reverse) {
            KEY_SHOW_CONVERT -> when (sharedPreferences.getString(KEY_FORMAT, "")) {
                "Цельсий" -> conversTemp = argt
                "Фаренгейт" -> conversTemp = (argt * 1.8f) + 32.0f
                "Кельвин" -> conversTemp = argt + 273.15f
            }
            KEY_ADD_CONVERT -> when (sharedPreferences.getString(KEY_FORMAT, "")) {
                "Цельсий" -> conversTemp = argt
                "Фаренгейт" -> conversTemp = (argt - 32.0f) / 1.8f
                "Кельвин" -> conversTemp = argt - 273.15f
            }
        }
        return conversTemp
    }
}