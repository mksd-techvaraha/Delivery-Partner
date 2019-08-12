package com.sunthemart.deliverypartner.view.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bunkguru.registration.utils.VolleySingleton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sunthemart.deliverypartner.BuildConfig
import com.sunthemart.deliverypartner.R
import com.sunthemart.deliverypartner.utils.*
import com.sunthemart.deliverypartner.view.ui.utils.AlertDialogs
import com.sunthemart.deliverypartner.view.ui.utils.NetworkStatus
import com.sunthemart.deliverypartner.view.ui.utils.Validation
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.xProgressBar
import org.json.JSONObject

/**
 * login activity
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var mTilEmail: TextInputLayout
    private lateinit var mEtEmail: TextInputEditText
    private lateinit var mTilPassword: TextInputLayout
    private lateinit var mEtPassword: TextInputEditText
    private lateinit var mBtnSignUp: Button
    private lateinit var mTvOpenReg: TextView
    private val tag = "LoginActivity"
    private val requestInternet = 1
    private val requestfore = 2
    private lateinit var validate: Validation
    /**
     * onCreate method
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initWidget()
        initObject()

        if (SharedPreferenceInstance.getStringValue(getString(R.string.auth_id_pref)) != null) {
            openHome()
        }

        if (!isPermissionGranted(Manifest.permission.INTERNET)) {
            requestInternetPermission()
        } else {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
            Log.i(tag, "internet permission has already been granted.")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            if (!isPermissionGranted(Manifest.permission.FOREGROUND_SERVICE)) {
                requestForegroundServicePermission()
            } else {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
                Log.i(tag, "foreground permission has already been granted.")
            }
        }
        mEtEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validate.validateEmail(mTilEmail)
            }
        }

        mEtPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validate.validate(mTilPassword)
            }
        }
    }


    private fun initWidget() {
        mTilEmail = findViewById(R.id.xTILEmail)
        mEtEmail = findViewById(R.id.xEtEmail)
        mTilPassword = findViewById(R.id.xTILPassword)
        mEtPassword = findViewById(R.id.xEtPassowrd)
        mBtnSignUp = findViewById(R.id.xBtnSignUp)
        mTvOpenReg = findViewById(R.id.xTvOpenReg)
    }

    private fun initObject() {
        validate = Validation()
    }

    /**
     * this method is starting login process
     */
    fun startLogin(view: View) {
        if (!validate.validateEmail(mTilEmail) && !validate.validate(mTilPassword))
            Toast.makeText(this, "Please enter highlighted details", Toast.LENGTH_LONG).show()
        else {
            xProgressBar.visibility = View.VISIBLE
            Log.i(tag, "Starting sending the data to our sunthemart servers")

            //TODO 2 handle network status
            if (!NetworkStatus(this).isNetworkAvailable(applicationContext)) {
                val alert = AlertDialogs(
                        this, "Please check Your Internet Connectivity",
                        "Thank you have a Nice Day", "Open Status bar and check", true
                        , enableNegativeButton = false
                )

                alert.showAlert()
                Log.i(tag, "network error")
                return
            } else {
                Log.i(tag, "sending request")
                val jsonString = "{" +
                        "\"Email_Id\":\"${mEtEmail.text}\"," +
                        "\"Password\":\"${mEtPassword.text}\"" +
                        "}"

                val jsonObj = JSONObject(jsonString)


                val url = Apis.login

                Log.i(tag, "starting request $jsonObj")

                val request = JsonObjectRequest(Request.Method.POST, url, jsonObj, Response.Listener { response ->
                    xProgressBar.visibility = View.GONE
                    val obj = JSONObject(response.toString())

                    when (obj.get("Message")) {
                        "Ok" -> {
                            val data = obj.getJSONObject("Data")

                            Log.i(tag, obj.get("Message").toString())
                            storeSharedPreferences(data)

                            Toast.makeText(this, "welcome to sunthe mart\nHave a nice day", Toast.LENGTH_LONG).show()

                            openHome()
                        }
                        "Account Is Not Verified Yet" -> {
                            Log.i(tag, obj.get("Message").toString())
                            val alert = AlertDialogs(
                                    this,
                                    "Please check the Following:",
                                    "Our Address: #255 1st Floor 1st cross, Sri, Ganganagar, Karnataka",
                                    "Please Verify your account at our office",
                                    enablePositiveButton = false
                                    ,
                                    enableNegativeButton = true
                            )

                            val negativeButton = alert.setNegativeButton("Get Directions")
                            negativeButton.setOnClickListener {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
                                        "geo:13.0228348,77.5855007?z=15"
                                )))
                            }
                            alert.showAlert()
                        }
                        else -> {
                            Log.i(tag, obj.get("Message").toString())
                            Toast.makeText(this, obj.get("Message").toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }, Response.ErrorListener {
                    xProgressBar.visibility = View.GONE
                    if (BuildConfig.DEBUG) {
                        Log.i(tag, "Volley error $it")
                    }
                })
                request.retryPolicy = DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
                VolleySingleton.getInstance(this).addToRequestQueue(request)

            }
        }
    }


    private fun storeSharedPreferences(data: JSONObject) {

        SharedPreferenceInstance.setStringValue(data.getString("Agent_Id"), getString(R.string.agent_id_pref))
        SharedPreferenceInstance.setStringValue(data.getString("Auth_Token"), getString(R.string.auth_id_pref))
        SharedPreferenceInstance.setStringValue(data.getString("Fname"), getString(R.string.fName_pref))
        SharedPreferenceInstance.setStringValue(data.getString("Lname"), getString(R.string.lName_pref))
        SharedPreferenceInstance.setStringValue(data.getString("Phone_No"), getString(R.string.phone_number_pref))
        SharedPreferenceInstance.setStringValue(data.getString("Pincode"), getString(R.string.pincode_pref))
        SharedPreferenceInstance.setStringValue(mEtEmail.text.toString(), getString(R.string.email_id_pref))
    }

    /**
     * open registration activity
     */
    fun openRegistration(view: View) {
        startActivity(Intent(this, RegistrationActivity::class.java))
        finish()
    }

    /**
     * opens home activity
     */
    fun openHome() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun requestInternetPermission() {
        if (shouldShowPermissionRationale(Manifest.permission.INTERNET)) {
            Log.i(tag, "Displaying internet permission rationale to provide additional context.")
            Snackbar.make(
                    loginLayout, "Internet Permissions is needed",
                    Snackbar.LENGTH_INDEFINITE
            )
                    .setAction(getString(R.string.ok)) {
                        requestPermission(Manifest.permission.INTERNET, requestInternet)
                    }
                    .show()
        } else {

            // Camera permission has not been granted yet. Request it directly.
            requestPermission(Manifest.permission.INTERNET, requestInternet)
        }
    }

    private fun requestForegroundServicePermission() {
        if (shouldShowPermissionRationale(Manifest.permission.FOREGROUND_SERVICE)) {
            Log.i(tag, "Displaying foreground permission rationale to provide additional context.")
            Snackbar.make(
                    loginLayout, "foreground permission is needed",
                    Snackbar.LENGTH_INDEFINITE
            )
                    .setAction(getString(R.string.ok)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            requestPermission(Manifest.permission.FOREGROUND_SERVICE, requestfore)
                        }
                    }
                    .show()
        } else {

            // Camera permission has not been granted yet. Request it directly.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                requestPermission(Manifest.permission.FOREGROUND_SERVICE, requestfore)
            }
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>,
            grantResults: IntArray
    ) {

        if (requestCode == requestInternet) {
            Log.i(tag, "Received response for internet permission request.")

            // Check if the permission has been granted
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                // internet permission has been granted, preview can be displayed
                Log.i(tag, "Internet permission has now been granted. You can login now.")
                Snackbar.make(
                        loginLayout, "Internet permission has now been granted. You can login now.",
                        Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Log.i(tag, "Internet permission is not granted.")
                Snackbar.make(
                        loginLayout, "Internet permission is not granted.",
                        Snackbar.LENGTH_SHORT
                ).show()

            }
        } else if (requestCode == requestfore) {
            Log.i(tag, "Received response for internet permission request.")

            // Check if the permission has been granted
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                // internet permission has been granted, preview can be displayed
                Log.i(tag, "foreground permission has now been granted. You can login now.")
                Snackbar.make(
                        loginLayout, "foreground permission has now been granted. You can login now.",
                        Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Log.i(tag, "foreground permission is not granted.")
                Snackbar.make(
                        loginLayout, "foreground permission is not granted.",
                        Snackbar.LENGTH_SHORT
                ).show()

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}