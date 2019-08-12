package com.sunthemart.deliverypartner.view.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bunkguru.registration.utils.VolleySingleton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sunthemart.deliverypartner.BuildConfig
import com.sunthemart.deliverypartner.R
import com.sunthemart.deliverypartner.utils.Apis
import com.sunthemart.deliverypartner.view.ui.utils.AlertDialogs
import com.sunthemart.deliverypartner.view.ui.utils.NetworkStatus
import com.sunthemart.deliverypartner.view.ui.utils.Validation
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject


/**
 * registration activity
 */
class RegistrationActivity : AppCompatActivity() {


    private lateinit var mTilFName: TextInputLayout
    private lateinit var mEtFName: TextInputEditText
    private lateinit var mTilLName: TextInputLayout
    private lateinit var mEtLName: TextInputEditText
    private lateinit var mTilPhone: TextInputLayout
    private lateinit var mEtPhone: TextInputEditText
    private lateinit var mTilEmail: TextInputLayout
    private lateinit var mEtEmail: TextInputEditText
    private lateinit var mTilPinCode: TextInputLayout
    private lateinit var mEtPinCode: TextInputEditText
    private lateinit var mTilPassword: TextInputLayout
    private lateinit var mEtPassword: TextInputEditText
    private lateinit var mBtnRegister: Button
    private lateinit var validate: Validation

    private var checkBoxFlag: Int = 0
    private val tag = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initWidget()
        initObject()

        mEtFName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validate.validate(mTilFName)
            }
        }

        mEtLName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validate.validate(mTilLName)
            }
        }


        mEtPhone.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validate.validateNumber(mTilPhone)
            }
        }


        mEtEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validate.validateEmail(mTilEmail)
            }
        }

        mEtPinCode.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validate.validate(mTilPinCode)
            }
        }
        mEtPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validate.validate(mTilPassword)
            }
        }

        xCbAadhar.setOnClickListener {
            if (xCbAadhar.isChecked)
                checkBoxFlag += 1
            else
                checkBoxFlag -= 1

            enableButton(checkBoxFlag)
        }
        xCbDl.setOnClickListener {
            if (xCbDl.isChecked)
                checkBoxFlag += 1
            else
                checkBoxFlag -= 1

            enableButton(checkBoxFlag)
        }


    }

    /**
     * enabling the register button
     */
    private fun enableButton(flag: Int) {
        if (flag == 2) {
            mBtnRegister.setBackgroundResource(R.drawable.bg_button)
        } else {
            mBtnRegister.setBackgroundResource(R.drawable.bg_button_disable)
        }
    }

    private fun initWidget() {
        mTilFName = findViewById(R.id.xTilFName)
        mEtFName = findViewById(R.id.xEtFName)
        mTilLName = findViewById(R.id.xTilLName)
        mEtLName = findViewById(R.id.xEtLName)
        mTilPhone = findViewById(R.id.xTILPhone)
        mEtPhone = findViewById(R.id.xEtPhone)
        mTilEmail = findViewById(R.id.xTILEmail)
        mEtEmail = findViewById(R.id.xEtEmail)
        mTilPinCode = findViewById(R.id.xTILPinCode)
        mEtPinCode = findViewById(R.id.xEtPinCode)
        mTilPassword = findViewById(R.id.xTILPassword)
        mEtPassword = findViewById(R.id.xEtPassowrd)
        mBtnRegister = findViewById(R.id.xBtnRegister)
    }

    private fun initObject() {
        validate = Validation()
    }


    /**
     * this method is used to register a user - delivery partner
     */
    fun startRegistration(view: View) {
        if (checkBoxFlag != 2 && !validate.validate(mTilPassword) && !validate.validate(mTilPinCode) && !validate.validateEmail(
                        mTilEmail
                ) &&
                !validate.validate(mTilLName) && !validate.validate(mTilFName) && !validate.validate(mTilPhone)
        )
            Toast.makeText(
                    this,
                    "Please enter the details and check the boxes if you have both",
                    Toast.LENGTH_LONG
            ).show()
        else {
            xProgressBar.visibility = View.VISIBLE
            Log.i(tag, "Starting sending the data to our sunthemart servers")

            //TODO 1 handle network status
            if (!NetworkStatus(this).isNetworkAvailable(applicationContext)) {
                val alert = AlertDialogs(
                        applicationContext, "Please check Your Internet Connectivity",
                        "Thank you have a Nice Day", "Open Status bar and check", true
                        , enableNegativeButton = false
                )

                alert.showAlert()
                Log.i(tag, "network error")
                return
            } else {
                Log.i(tag, "sending request")
                val jsonString = "{" +
                        "\"Fname\":\"${mEtFName.text}\"," +
                        "\"Lname\":\"${mEtLName.text}\"," +
                        "\"Phone_No\":\"${mEtPhone.text}\"," +
                        "\"Email_Id\":\"${mEtEmail.text}\"," +
                        "\"Dl_No\" :\"\"," +
                        "\"Adhar_No\":\"\"," +
                        "\"Pincode\":\"${mEtPinCode.text}\"," +
                        "\"Password\":\"${mEtPassword.text}\"" +
                        "}"

                val jsonObj = JSONObject(jsonString)


                val url = Apis.register

                Log.i(tag, "starting request $jsonObj")

                val request = JsonObjectRequest(Request.Method.POST, url, jsonObj, Response.Listener { response ->
                    xProgressBar.visibility = View.GONE
                    val obj = JSONObject(response.toString())
                    val message = obj.get("Message")

                    if (message == "Ok") {
                        val verifyId = obj.getJSONObject("Data").getString("Verify_Id")
                        Log.i(tag, obj.get("Message").toString())
                        val alert = AlertDialogs(
                                this,
                                getString(R.string.please_verification_note),
                                "Did you note the Verification Code?",
                                verifyId,
                                enablePositiveButton = true,
                                enableNegativeButton = false
                        )

                        alert.showAlert()
                        val positiveButton = alert.setPositiveButton("Noted")
                        positiveButton.setOnClickListener {
                            alert.dismiss()
                            startActivity(Intent(this, LoginActivity::class.java))
                        }

                    } else {
                        Log.i(tag, obj.get("Message").toString())
                        Toast.makeText(this, obj.get("Message").toString(), Toast.LENGTH_LONG).show()
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

    /**
     * Open login activity
     */
    fun openLogin(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }
}