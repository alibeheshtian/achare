package com.example.achar.ui.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.RegexUtils
import com.example.achar.R
import com.example.achar.base.BaseActivity
import com.example.achar.ui.map.MapsActivity
import kotlinx.android.synthetic.main.activity_address_add.*

class AddressAddActivity : BaseActivity(layout = R.layout.activity_address_add) {
    private val viewModel by viewModels<AddressViewModel> { AddressViewModel.Factory(this) }

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

        viewModel.addAddressLiveData.observe(this, Observer { address ->
            val returnIntent = Intent(this, AddressActivity::class.java)
            returnIntent.putExtra(
                AddressActivity.KEY_ADDRESS,
                address
            )
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        })

        viewModel.loading.observe(this, Observer {
            pb.visibility = if (it) View.VISIBLE else View.GONE
        })
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

                        viewModel.addAddressFromServer(
                            address,
                            lat,
                            lng,
                            mobile,
                            phone,
                            name,
                            family,
                            gender
                        )

                    }


                }
            }
        }
    }


    companion object {
        private const val RESULT_FROM_MAP = 201
        const val KEY_LAT = "LAT"
        const val KEY_LNG = "LNG"
    }
}