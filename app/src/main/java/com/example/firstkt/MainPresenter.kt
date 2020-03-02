package com.example.firstkt

import android.content.Context
import android.content.SharedPreferences

class MainPresenter(val mView: MainContract.View, val context: Context) : MainContract.Presenter {


    private val KEY_FORMAT = "keyTFormat"
    private val NAME_SETTINGS = "mSettings"
    private val KEY_ADD_CONVERT = 0
    private val KEY_SHOW_CONVERT = 1
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mRepository: MainContract.Repository

    private lateinit var name: String
    private lateinit var type: String
    private lateinit var season: String
    private lateinit var middleTemp: String
    private lateinit var cityInfoMap: Map<String, Any>


    init {
        mRepository.init(context)
        sharedPreferences = context.getSharedPreferences(NAME_SETTINGS, Context.MODE_PRIVATE)
    }


    override fun loadCityList(): ArrayList<String> {
        return mRepository.loadCityListNames()
    }

    override fun addInfo(data: Map<String, String>) {
        if (data != null) {
            var name = data.get("name").toString()
            var type = data.get("type").toString()
            var winterT = calculateToMiddle(
                data.getValue("decTemp").toFloat(),
                data.getValue("janTemp").toFloat(),
                data.getValue("febTemp").toFloat()
            )
            var springT = calculateToMiddle(
                data.getValue("marTemp").toFloat(),
                data.getValue("aprTemp").toFloat(),
                data.getValue("mayTemp").toFloat()
            )
            var summerT = calculateToMiddle(
                data.getValue("junTemp").toFloat(),
                data.getValue("julTemp").toFloat(),
                data.getValue("augTemp").toFloat()
            )
            var autumnT = calculateToMiddle(
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
    }

    override fun onCityWasSelected(name: String, season: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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