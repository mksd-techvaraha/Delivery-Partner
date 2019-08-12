package com.sunthemart.deliverypartner.view.ui.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.sunthemart.deliverypartner.R
import kotlinx.android.synthetic.main.widget_dialog.view.*

/**
 * This class is used to show alertDialog
 */
class AlertDialogs(
        /**
         * context from application
         */
        private val context: Context,
        /**
         * note for the text
         */
        private val note: String,

        /**
         * greeting message
         */
        private val greeting: String,
        /**
         * information message
         */
        private val information: String,

        /**
         * if +ve button are enabled
         */
        private val enablePositiveButton: Boolean,

        /**
         * if neg button are enabled
         */
        private val enableNegativeButton: Boolean
) {


    private var mDialogView: View = LayoutInflater.from(context).inflate(R.layout.widget_dialog, null)
    private lateinit var mAlertDialog: AlertDialog

    /**
     * setting the positive button
     */
    fun setPositiveButton(text: String): Button {
        mDialogView.xBtnPositive.text = text
        return mDialogView.xBtnPositive
    }

    /**
     * setting the negative button
     */
    fun setNegativeButton(text: String): View {
        mDialogView.xBtnNegative.text = text
        return mDialogView.xBtnNegative
    }

    /**
     * this method is used to show Alerts in called activity
     */
    fun showAlert() {

        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)

        if (enablePositiveButton)
            mDialogView.xBtnPositive.visibility = View.VISIBLE
        else
            mDialogView.xBtnPositive.visibility = View.GONE

        if (enableNegativeButton)
            mDialogView.xBtnNegative.visibility = View.VISIBLE
        else
            mDialogView.xBtnNegative.visibility = View.GONE

        mDialogView.xTvNote.text = note
        mDialogView.xTvGreetings.text = greeting
        mDialogView.xTvInfoValue.text = information
        //show dialog
        mAlertDialog = mBuilder.show()
        //login button click of custom layout

    }


    fun dismiss() {
        mAlertDialog.dismiss()
    }
}
