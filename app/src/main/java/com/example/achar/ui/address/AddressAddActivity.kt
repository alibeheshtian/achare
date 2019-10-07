package com.example.achar.ui.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.RegexUtils
import com.example.achar.R
import com.example.achar.base.BaseActivity
import com.example.achar.ui.map.MapsActivity
import com.example.achar.webService.ApiService
import kotlinx.android.synthetic.main.activity_address_add.*
import org.kodein.di.generic.instance

class AddressAddActivity : BaseActivity(layout = R.layout.activity_address_add) {
    private val apiService: ApiService by instance()

    override fun viewIsReady(savedInstanceState: Bundle?) {

        bt_next.setOnClickListener {

            val mobile = tv_mobile.getText().toString()
            val name = tv_name.getText().toString()
            val family = tv_family.getText().toString()
            val phone = tv_phone.getText().toString()
            val address = et_address.getText().toString()

            val isNotMobile = RegexUtils.isMatch("^09\\d{9}\$", mobile).not()

            // region validate fields
            if (name.isEmpty()) {
                tv_name.setError("نام اجباری می باشد.")
                return@setOnClickListener
            }

            if (family.isEmpty()) {
                tv_family.setError("نام خانوادگی اجباری می باشد.")
                return@setOnClickListener
            }

            if (mobile.isEmpty()) {
                tv_mobile.setError("شماره موبایل اجباری می باشد.")
                return@setOnClickListener
            }
            if (isNotMobile) {
                tv_mobile.setError("شماره موبایل صحیح نمی باشد.")
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                tv_phone.setError("شماره ثابت اجباری می باشد.")
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                et_address.setError("آدرس اجباری می باشد.")
                return@setOnClickListener
            }

            //endregion

            val intent = Intent(this, MapsActivity::class.java)
            startActivityForResult(intent, RESULT_FROM_MAP)

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_FROM_MAP -> {
                    data?.let {

                        val lat = data.getDoubleExtra(KEY_LAT, 0.0)
                        val lng = data.getDoubleExtra(KEY_LNG, 0.0)

                        val mobile = tv_mobile.getText().toString()
                        val name = tv_name.getText().toString()
                        val family = tv_family.getText().toString()
                        val phone = tv_phone.getText().toString()
                        val address = et_address.getText().toString()
                        val gender = if (tb_gender.isChecked) "Male" else "Female"

                        addAddress(address, lat, lng, mobile, phone, name, family, gender)

                    }


                }
            }
        }
    }

    private fun addAddress(
        address: String,
        lat: Double,
        lng: Double,
        mobile: String,
        phone: String,
        name: String,
        family: String,
        gender: String
    ) {
        val param = HashMap<String, String>().apply {
            put("region", "1")
            put("address", address)
            put("lat", lat.toString())
            put("lng", lng.toString())
            put("coordinate_mobile", mobile)
            put("coordinate_phone_number", phone)
            put("first_name", name)
            put("last_name", family)
            put("gender", gender)
        }

        callService(apiService.addressAdd(param),
            onSuccess = { addressRes ->
                addressRes?.let {
                    val returnIntent = Intent(this,AddressListActivity::class.java)
                    returnIntent.putExtra(
                        AddressListActivity.KEY_ADDRESS,
                        addressRes
                    )
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }

            }, onError = {

            })
    }

    companion object {
        private const val RESULT_FROM_MAP = 201
        const val KEY_LAT = "LAT"
        const val KEY_LNG = "LNG"
    }
}