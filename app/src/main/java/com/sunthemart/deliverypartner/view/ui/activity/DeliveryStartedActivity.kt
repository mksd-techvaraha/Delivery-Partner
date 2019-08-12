package com.sunthemart.deliverypartner.view.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sunthemart.deliverypartner.R

/**
 * delivery started activity
 */
class DeliveryStartedActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private lateinit var mIncldCustomerDetails: View

    // mIncldCustomerDetails Include views
    private lateinit var mTvOrderNum: TextView
    private lateinit var mTvDeliverTime: TextView
    //to be set as R.string.hey_you
    private lateinit var mTvAdrsDialoge: TextView
    //customer name
    private lateinit var mTvCustomerName: TextView
    // to be set as customer address
    private lateinit var mTvAdrsCustomer: TextView
    private lateinit var mBtnCallCustomer: Button
    private lateinit var mBtnCustomerDirection: Button

    private lateinit var mDeliveryStartedMapView: GoogleMap

    private lateinit var bottomSheet: CardView
    private lateinit var mIncldStatus: View

    // mIncldStatus views
    private lateinit var mTvStatusMsg: TextView
    private lateinit var mTvStatusQuestion: TextView
    private lateinit var mBtnPositive: TextView
    private lateinit var mBtnNegative: TextView

    private lateinit var mTvSPName: TextView
    private lateinit var mBtnCallSp: Button
    private lateinit var mTvSpAddress: TextView
    private lateinit var mBtnGetSPDirection: Button
    private lateinit var mIncldGreetings: View

    private lateinit var sheetBehavior: BottomSheetBehavior<CardView>
    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_started)

        initObjects()
        initWidgets()

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.homeMapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }

    private fun initWidgets() {
        mIncldCustomerDetails = findViewById(R.id.xIncldCustomerDetails)
        bottomSheet = findViewById(R.id.stock_bottom_sheet)
        mIncldStatus = findViewById(R.id.xIncldStatus)
        mTvSPName = findViewById(R.id.xTvSPName)
        mBtnCallSp = findViewById(R.id.xBtnCallSp)
        mTvSpAddress = findViewById(R.id.xTvAddress)
        mBtnGetSPDirection = findViewById(R.id.xBtnSeeDetails)
        mIncldGreetings = findViewById(R.id.xIncldGreetings)
        mTvOrderNum = mIncldCustomerDetails.findViewById(R.id.xTvOrderNum)
        mTvDeliverTime = mIncldCustomerDetails.findViewById(R.id.xTvDeliverTime)
        mTvAdrsDialoge = mIncldCustomerDetails.findViewById(R.id.xTvAdrsDialoge)
        mTvCustomerName = mIncldCustomerDetails.findViewById(R.id.xTvCustName)
        mTvAdrsCustomer = mIncldCustomerDetails.findViewById(R.id.xTvAdrs)
        mBtnCallCustomer = mIncldCustomerDetails.findViewById(R.id.xBtnCall)
        mBtnCallCustomer.visibility = View.GONE
        mBtnCustomerDirection = mIncldCustomerDetails.findViewById(R.id.xBtnGetDirection)
        mBtnCustomerDirection.visibility = View.GONE

        mTvStatusMsg = mIncldStatus.findViewById(R.id.xTvStatusMsg)
        mTvStatusQuestion = mIncldStatus.findViewById(R.id.xTvStatusQuestion)
        mBtnPositive = mIncldStatus.findViewById(R.id.xBtnPositive)
        mBtnNegative = mIncldStatus.findViewById(R.id.xBtnNegative)

    }

    private fun initObjects() {
        sheetBehavior = BottomSheetBehavior.from(bottomSheet)
    }


    /**
     * this method is used to create and put markers on the map
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mDeliveryStartedMapView = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mDeliveryStartedMapView.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mDeliveryStartedMapView.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    /**
     * This method is used to make map markers clickable
     */
    override fun onMarkerClick(p0: Marker?): Boolean {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
