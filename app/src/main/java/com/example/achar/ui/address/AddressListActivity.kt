package com.example.achar.ui.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.achar.R
import com.example.achar.adapters.AddressAdapter
import com.example.achar.base.BaseActivity
import com.example.achar.webService.ApiService
import com.example.achar.webService.models.sucsess.AddressModel
import kotlinx.android.synthetic.main.activity_address_list.*
import org.kodein.di.generic.instance

class AddressListActivity : BaseActivity(layout = R.layout.activity_address_list) {
    private val apiService: ApiService by instance()
    private val adapter = AddressAdapter {

    }

    override fun viewIsReady(savedInstanceState: Bundle?) {

        loading.observe(this, Observer { isLoading ->
            pb.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        rv_address.layoutManager = LinearLayoutManager(this)
        rv_address.adapter = adapter

        callService(apiService.addresses(),
            onSuccess = { address ->
                adapter.submit(address)
            }
        )

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
