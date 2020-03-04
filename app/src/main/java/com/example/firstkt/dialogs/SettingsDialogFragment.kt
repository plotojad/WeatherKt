package com.example.firstkt.dialogs

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.firstkt.MainActivity
import com.example.firstkt.MainContract
import com.example.firstkt.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.settingsdialog.*
import kotlinx.android.synthetic.main.settingsdialog.view.*

class SettingsDialogFragment(mView: MainContract.View) : DialogFragment() {

    companion object {
        const val KEY_FORMAT = "keyTFormat"
        const val NAME_SETTINGS = "mSettings"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private var activity = mView as MainActivity
    private var tempsVariants = arrayOf("Цельсий", "Фаренгейт", "Кельвин")
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.settingsdialog, null)
//        activity = MainActivity()
        sharedPreferences = context!!.getSharedPreferences(NAME_SETTINGS, Context.MODE_PRIVATE)
        val sEd: SharedPreferences.Editor = sharedPreferences.edit()
        adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, tempsVariants)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        v.spinnTempFormat.adapter = adapter
        v.spinnTempFormat.setSelection(
            adapter.getPosition(
                sharedPreferences.getString(
                    KEY_FORMAT,
                    ""
                )
            )
        )
        v.spinnTempFormat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

        }

        v.btnSelect.setOnClickListener {
            sEd.putString(KEY_FORMAT, spinnTempFormat.selectedItem.toString())
            sEd.apply()
            activity.mPresenter.onCityWasSelected(
                activity.spinnCity.selectedItem.toString(),
                activity.spinnSeason.selectedItem.toString()
            )
            dismiss()
        }



        return v
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (context is Activity) this@SettingsDialogFragment.activity = context as MainActivity
    }

    override fun onCancel(dialog: DialogInterface?) {
        dismiss()
        super.onCancel(dialog)
    }
}