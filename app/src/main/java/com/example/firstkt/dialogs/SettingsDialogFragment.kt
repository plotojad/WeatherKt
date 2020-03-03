package com.example.firstkt.dialogs

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.firstkt.R
import kotlinx.android.synthetic.main.settingsdialog.*

class SettingsDialogFragment() : DialogFragment() {

    companion object {
        const val KEY_FORMAT = "keyTFormat"
        const val NAME_SETTINGS = "mSettings"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var activity: Activity
    private var tempsVariants = arrayOf("Цельсий", "Фаренгейт", "Кельвин")
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.settingsdialog, null)
        sharedPreferences = context!!.getSharedPreferences(NAME_SETTINGS, Context.MODE_PRIVATE)
        val sEd: SharedPreferences.Editor = sharedPreferences.edit()
        adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, tempsVariants)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnTempFormat.adapter = adapter
        spinnTempFormat.setSelection(
            adapter.getPosition(
                sharedPreferences.getString(
                    KEY_FORMAT,
                    ""
                )
            )
        )
        spinnTempFormat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }



        return v
    }
}