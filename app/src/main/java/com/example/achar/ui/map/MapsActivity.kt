package com.example.achar.ui.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.achar.R
import com.example.achar.base.BaseActivity
import com.example.achar.ui.address.AddressAddActivity
import com.example.achar.ui.address.AddressListActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : BaseActivity(layout = R.layout.activity_maps), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap


    override fun viewIsReady(savedInstanceState: Bundle?) {
        toolbar.title = "موقعیت روی نقشه"

        map_view.onCreate(null)
        map_view.getMapAsync(this)

        bt_submit.setOnClickListener {
            val target = mMap.cameraPosition.target

            val returnIntent = Intent(this, AddressAddActivity::class.java)
            returnIntent.putExtra(AddressAddActivity.KEY_LAT, target.latitude)
            returnIntent.putExtra(AddressAddActivity.KEY_LNG, target.longitude)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }


    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onStart() {
        super.onStart()
        map_view.onStart()
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val tehran = LatLng(35.715298, 51.404343)

        mMap = googleMap.apply {
            setMinZoomPreference(12f)
            moveCamera(CameraUpdateFactory.newLatLng(tehran))
        }

    }




}
