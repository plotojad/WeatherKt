package com.example.firstkt

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.firstkt.dialogs.AddInfoDialogFragment
import com.example.firstkt.dialogs.SettingsDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), MainContract.View {


    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val KEY_FORMAT = "keyTFormat"
        const val NAME_SETTINGS = "mSettings"

    }

    private var adapterCityList: ArrayAdapter<String>? = null
    private lateinit var adapterSeasonList: ArrayAdapter<String>
    lateinit var mPresenter: MainContract.Presenter

    private var cities: ArrayList<String>? = null
    private val seasons = arrayOf("Зима", "Весна", "Лето", "Осень")

    private lateinit var mSettingsDialogFragment: SettingsDialogFragment
    private lateinit var mAddInfoDialogFragment: AddInfoDialogFragment
    var isSelected = false
    private var cityName: String? = null
    private var seasonName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter = MainPresenter(this, baseContext)
        cities = mPresenter.loadCityList()
        mSettingsDialogFragment = SettingsDialogFragment(this)
        mAddInfoDialogFragment = AddInfoDialogFragment(this)

        if (cities == null) {
            sharedPreferences = this.getSharedPreferences(NAME_SETTINGS, Context.MODE_PRIVATE)
            val sEd = sharedPreferences.edit()
            sEd.putString(KEY_FORMAT, "Цельсий")
            sEd.apply()
            mAddInfoDialogFragment.show(
                supportFragmentManager,
                mAddInfoDialogFragment.javaClass.name
            )
        } else {
            initAdapters()
        }


    }

    override fun showResult(nameCity: String, typeCity: String, season: String, midTemp: String) {
        tvCityName.text = nameCity
        tvType.text = typeCity
        tvSeason.text = season
        tvTemp.text = midTemp
    }

    override fun initAdapters() {
        if (cities != null) {
            adapterCityList = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities!!)
            adapterCityList!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnCity.adapter = adapterCityList
            spinnCity.prompt = "Выберите город:"
            spinnCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    cityName = parent!!.getItemAtPosition(position).toString()
                    if (isSelected && seasonName != null) mPresenter.onCityWasSelected(
                        cityName!!,
                        seasonName!!
                    )
                    isSelected = true
                }

            }

            adapterSeasonList =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, seasons)
            adapterSeasonList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnSeason.adapter = adapterSeasonList
            spinnSeason.prompt = "Выберите сезон:"
            spinnSeason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(baseContext, "выберите сезон!", Toast.LENGTH_SHORT).show()
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    seasonName = parent!!.getItemAtPosition(position).toString()
                    if (isSelected && cityName != null) mPresenter.onCityWasSelected(
                        cityName!!,
                        seasonName!!
                    )
                    isSelected = true
                }

            }

        } else {
            cities = mPresenter.loadCityList()!!
            initAdapters()
            updateAdapters()
        }
    }

    override fun updateAdapters() {
        if (adapterCityList != null) {
            adapterCityList!!.clear()
            adapterCityList!!.addAll(mPresenter.loadCityList()!!)
            adapterCityList!!.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemSettings -> mSettingsDialogFragment.show(
                supportFragmentManager,
                mSettingsDialogFragment.javaClass.name
            )
            R.id.itemAddInfo -> mAddInfoDialogFragment.show(
                supportFragmentManager,
                mAddInfoDialogFragment.javaClass.name
            )
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }
}
