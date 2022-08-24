package com.bchmsl.midterm_weather.ui

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bchmsl.midterm_weather.R

class ProcessingDialog(val fragment: Fragment) {
    private lateinit var dialog: AlertDialog
    fun startProcessing(){
        // set View
        val inflater = fragment.layoutInflater
        val dialogView = inflater.inflate(R.layout.processing_info, null)
        // set Dialog
        val builder = AlertDialog.Builder(fragment.requireContext())
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }
    fun stopProcessing(){
        dialog.dismiss()
    }
}