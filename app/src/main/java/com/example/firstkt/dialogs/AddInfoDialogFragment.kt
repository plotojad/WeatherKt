package com.example.firstkt.dialogs

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.firstkt.MainContract
import com.example.firstkt.MainPresenter
import com.example.firstkt.R
import kotlinx.android.synthetic.main.addinfodialog.view.*

class AddInfoDialogFragment(val mView: MainContract.View) : DialogFragment() {

    companion object {
        const val KEY_FORMAT = "keyTFormat"
        const val NAME_SETTINGS = "mSettings"
        const val KEY_C = "Цельсий"
        const val KEY_F = "Фаренгейт"
        const val KEY_K = "Кельвин"
    }

    private lateinit var adapterSp: ArrayAdapter<String>

    private lateinit var mPresenter: MainContract.Presenter
    private lateinit var sharedPreferences: SharedPreferences
    private val types = arrayOf("Крупный", "Средний", "Малый")
    private lateinit var data: MutableMap<String, String>
    private lateinit var viewsMap: Map<String, EditText>
    private lateinit var typeName: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.addinfodialog, null)
        sharedPreferences = context!!.getSharedPreferences(NAME_SETTINGS, Context.MODE_PRIVATE)


        viewsMap = mapOf(
            "name" to v.etCityNameAdd,
            "decTemp" to v.etDecTempAdd,
            "janTemp" to v.etJanTempAdd,
            "febTemp" to v.etFebTempAdd,
            "marTemp" to v.etMarTempAdd,
            "aprTemp" to v.etAprTempAdd,
            "mayTemp" to v.etMayTempAdd,
            "junTemp" to v.etJunTempAdd,
            "julTemp" to v.etJulTempAdd,
            "augTemp" to v.etAugTempAdd,
            "sepTemp" to v.etSeptTempAdd,
            "ocTemp" to v.etOctTempAdd,
            "novTemp" to v.etNovTempAdd
        )

        adapterSp = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, types)
        adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        v.spinnTypeAdd.adapter = adapterSp
        v.spinnTypeAdd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                typeName = parent!!.getItemAtPosition(position).toString()
            }

        }

        v.btAddInfo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(it: View) {
                mPresenter = MainPresenter(mView, context!!)
                data = mutableMapOf()
                for (vi in viewsMap) {
                    if (vi.value.text == null || vi.value.text.toString() == "") {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.enterTheNameToast),
                            Toast.LENGTH_SHORT
                        ).show()
                        vi.value.isFocusable = true
                        vi.value.setBackgroundColor(resources.getColor(android.R.color.holo_orange_light))
                        return
                    } else {
                        vi.value.setBackgroundColor(resources.getColor(android.R.color.white))
                        data.put(vi.key, vi.value.text.toString())
//                        data[vi.key] = vi.value.text.toString()
                    }
                }
                data.put("type", typeName)
//                data["type"] = typeName
                mPresenter.addInfo(data)
                Toast.makeText(
                    context,
                    resources.getString(R.string.cityIsAdded),
                    Toast.LENGTH_SHORT
                )
                    .show()
                dismiss()
            }
        })
        return v
    }

    override fun onResume() {
        super.onResume()
        var hint = ""
        when (sharedPreferences.getString(KEY_FORMAT, "")) {
            KEY_C -> hint = "°C"
            KEY_F -> hint = "°F"
            KEY_K -> hint = "°K"
        }
        for (vi in viewsMap) {
            vi.value.hint = hint
        }
        viewsMap.getValue("name").hint = "Название города"
    }

    override fun onCancel(dialog: DialogInterface) {
        dismiss()
        super.onCancel(dialog)
    }
}
