package com.example.achar.adapters

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.achar.R
import com.example.achar.webService.models.sucsess.AddressModel
import kotlinx.android.synthetic.main.item_address.view.*

class AddressAdapter(val onClick: (AddressModel) -> Unit) :
    RecyclerView.Adapter<AddressAdapter.Holder>() {

    private val addresses = mutableListOf<AddressModel>()


    override fun getItemCount() = addresses.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            from(parent.context).inflate(R.layout.item_address, parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val address = addresses[position]
        holder.address = address
        holder.tvFullName.text = address.firstName
        holder.tvAddress.text = address.address
        holder.tvMobile.text = address.coordinateMobile


    }


    fun submit(addressList: List<AddressModel>?) {
        addressList?.let {
            val oldSize = addresses.size
            addresses.addAll(addressList)
            notifyItemRangeInserted(oldSize, addressList.size)
        }

    }

    fun submit(address: AddressModel) {
        addresses.add(address)
        notifyItemInserted(addresses.size)
    }

    inner class Holder(
        itemView: View,
        onClick: (AddressModel) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        lateinit var address: AddressModel
        val tvFullName: AppCompatTextView = itemView.tv_fullName
        val tvAddress: AppCompatTextView = itemView.tv_address
        val tvMobile: AppCompatTextView = itemView.tv_mobile

        init {

            itemView.setOnClickListener {
                onClick.invoke(address)
            }


        }
    }
}