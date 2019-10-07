package com.example.achar.ui.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.example.achar.R
import com.example.achar.adapters.AddressAdapter
import com.example.achar.base.BaseActivity
import com.example.achar.webService.models.sucsess.AddressModel
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressActivity : BaseActivity(layout = R.layout.activity_address_list) {
    private val viewModel by viewModels<AddressViewModel> { AddressViewModel.Factory(this) }


    private val adapter = AddressAdapter {
        ToastUtils.showLong(it.firstName)
    }

    override fun viewIsReady(savedInstanceState: Bundle?) {
        initView()

        initViewModel()

    }

    private fun initViewModel() {
        viewModel.getAddressFromServer()

        viewModel.addressesLiveData.observe(this, Observer { addresses ->
            adapter.submit(addresses)
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            pb.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    private fun initView() {
        rv_address.layoutManager = LinearLayoutManager(this)
        rv_address.adapter = adapter

        fab.setOnClickListener {
            val intent = Intent(this, AddressAddActivity::class.java)
            startActivityForResult(intent, RESULT_FROM_ADD_ADDRESS)
        }
    }

    companion object {
        private const val RESULT_FROM_ADD_ADDRESS = 200
        const val KEY_ADDRESS = "ADDRESS"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_FROM_ADD_ADDRESS -> {

                    data?.let {
                        val address = data.getParcelableExtra(KEY_ADDRESS) as AddressModel
                        adapter.submit(address)
                    }

                }
            }
        }

    }

}
