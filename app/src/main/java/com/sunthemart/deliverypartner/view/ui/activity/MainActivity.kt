package com.sunthemart.deliverypartner.view.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.sunthemart.deliverypartner.R
import com.sunthemart.deliverypartner.models.datamodels.Order
import com.sunthemart.deliverypartner.utils.SharedPreferenceInstance
import com.sunthemart.deliverypartner.utils.isPermissionGranted
import com.sunthemart.deliverypartner.utils.requestPermission
import com.sunthemart.deliverypartner.utils.shouldShowPermissionRationale
import com.sunthemart.deliverypartner.view.adapter.OrderListAdapter
import com.sunthemart.deliverypartner.viewModel.OrderList
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Home activity
 */
class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mTvPartnerName: TextView
    private lateinit var mTvWorkingAreaCode: TextView
    private lateinit var mIvDeliveryProfile: ImageView
    private lateinit var mHomeMapView: GoogleMap
    private lateinit var mProgressBar: View
    private lateinit var bottomSheet: LinearLayout
    private lateinit var mRecycHomeBottomSheet: RecyclerView
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    private val tag = "MainActivity"
    private lateinit var lastLocation: Location

    private lateinit var orderListAdapter: OrderListAdapter
    private lateinit var orderListViewModel: OrderList

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var orderList: ArrayList<Order>
    private val requestFine = 4
    /**
     * this method is called when to create activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Toast.makeText(this, "welcome to sunthe mart\nHave a nice day", Toast.LENGTH_LONG).show()

        initObjects()
        initWidgets()

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.homeMapView) as SupportMapFragment
        mapFragment.getMapAsync(this)


        setProfile()


        orderListViewModel = ViewModelProviders.of(this).get(OrderList::class.java)

        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestFineLocationPermission()
        } else {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
            Log.i(tag, "Fine Location permission has already been granted.")

        }

    }

    private fun setUpRecycler() {
        orderListAdapter = OrderListAdapter(orderList, this)
        mRecycHomeBottomSheet.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecycHomeBottomSheet.adapter = orderListAdapter
        orderListAdapter.notifyDataSetChanged()
    }

    private fun observeViewModel(orderListViewModel: OrderList, latitude: String, longitude: String) {

        mProgressBar.visibility = View.VISIBLE
        Log.i("addressorderlist", "entered")
        orderListViewModel.getOrderListObservable(latitude, longitude)
                .observe(this, Observer<ArrayList<Order>> { orderList ->
                    this.orderList = orderList

                    val builder = LatLngBounds.Builder()
                    for (marker in 0 until orderList.size) {
                        val currentLatLng =
                                LatLng(orderList[marker].latitude.toDouble(), orderList[marker].longitude.toDouble())
                        mHomeMapView.addMarker(MarkerOptions().position(currentLatLng).title(orderList[marker].orderNo))
                        builder.include(currentLatLng)
                    }

                    mHomeMapView.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300))
                    mProgressBar.visibility = View.GONE
                })


    }


    /**
     * this method is used to create and put markers on the map
     */

    override fun onMapReady(googleMap: GoogleMap) {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return
        } else {
            mHomeMapView = googleMap
            mHomeMapView.uiSettings.isZoomControlsEnabled = true
            mHomeMapView.setOnMarkerClickListener(this)
            setUpMap()
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        mHomeMapView.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                observeViewModel(orderListViewModel, location.latitude.toString(), location.longitude.toString())

            }
        }
    }

    /**
     * This method is used to make map markers clickable
     */
    override fun onMarkerClick(marker: Marker?): Boolean {
        mProgressBar.visibility = View.VISIBLE

        marker?.title
        setUpRecycler()
        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }
        mProgressBar.visibility = View.GONE
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun setProfile() {
        mTvPartnerName.text =
                "Hey ${SharedPreferenceInstance.getStringValue(getString(R.string.fName_pref)).toString()}"
        mTvWorkingAreaCode.text = SharedPreferenceInstance.getStringValue(getString(R.string.pincode_pref)).toString()
    }

    private fun requestFineLocationPermission() {
        if (shouldShowPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.i(tag, "Displaying ACCESS_FINE_LOCATION rationale to provide additional context.")
            Snackbar.make(
                    mainLayout, "Fine location permission is needed",
                    Snackbar.LENGTH_INDEFINITE
            )
                    .setAction(getString(R.string.ok)) {
                        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, requestFine)

                    }
                    .show()
        } else {

            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, requestFine)
        }
    }

    private fun initWidgets() {
        mTvPartnerName = findViewById(R.id.xTvPartnerName)
        mTvWorkingAreaCode = findViewById(R.id.xTvWorkingAreaCode)
        mIvDeliveryProfile = findViewById(R.id.xIvDeliveryProfile)
        mRecycHomeBottomSheet = findViewById(R.id.xRecycHomeBottomSheet)
        mProgressBar = findViewById(R.id.xProgressBar)
    }

    private fun initObjects() {
        bottomSheet = findViewById(R.id.bottom_sheet)
        sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }


    /**
     * call back for permission request
     */
    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>,
            grantResults: IntArray
    ) {

        if (requestCode == requestFine) {
            Log.i(tag, "Received response for fine permission request.")

            // Check if the permission has been granted
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                // internet permission has been granted, preview can be displayed
                Log.i(tag, "Fine permission has now been granted. You can login now.")
                Snackbar.make(
                        mainLayout, "Fine location permission has now been granted. You can login now.",
                        Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Log.i(tag, "Fine location permission is not granted.")
                Snackbar.make(
                        mainLayout, "Fine Location permission is not granted.",
                        Snackbar.LENGTH_SHORT
                ).show()

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}
