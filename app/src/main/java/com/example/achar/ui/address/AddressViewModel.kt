package com.example.achar.ui.address

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.achar.base.BaseViewModel
import com.example.achar.webService.models.sucsess.AddressModel

class AddressViewModel(context: Context) : BaseViewModel(context) {

    val addAddressLiveData = MutableLiveData<AddressModel>()
    val addressesLiveData = MutableLiveData<List<AddressModel>>()

    fun getAddressFromServer() {
        callService(apiService.addresses(),
            onSuccess = { address ->
                address.let {
                    addressesLiveData.postValue(address.body())
                }
            }
        )
    }

    fun addAddressFromServer(
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
                addressRes.let {
                    addAddressLiveData.postValue(addressRes.body())
                }

            }, onError = {

            })
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddressViewModel(context) as T
        }
    }
}