package com.alsatpardakht.alsatipgandroidkotlinexample

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.alsatpardakht.alsatipgandroid.AlsatIPG
import com.alsatpardakht.alsatipgandroid.core.callback.PaymentSignCallback
import com.alsatpardakht.alsatipgandroid.core.callback.PaymentValidationCallback
import com.alsatpardakht.alsatipgandroid.domain.model.PaymentSignResult
import com.alsatpardakht.alsatipgandroid.domain.model.PaymentValidationResult
import com.alsatpardakht.ipg.data.remote.model.PaymentSignRequest

class MainActivityFirstWay : AppCompatActivity(), PaymentSignCallback, PaymentValidationCallback {

    private val API = "ENTER YOUR API KEY HERE"

    private val alsatIPG = AlsatIPG.getInstance(this, this)

    private lateinit var logTextView: TextView
    private lateinit var signPaymentButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logTextView = findViewById(R.id.logTextView)
        signPaymentButton = findViewById(R.id.signPaymentButton)

        signPaymentButton.setOnClickListener {
            signPaymentButtonOnClick()
        }

        configurePaymentValidation()
    }

    override fun onPaymentSignResult(paymentSignResult: PaymentSignResult) {
        when {
            paymentSignResult.isSuccessful -> {
                log("payment Sign Success url = ${paymentSignResult.url}")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentSignResult.url))
                startActivity(intent)
            }
            paymentSignResult.isLoading -> {
                log("payment Sign Loading ...")
            }
            else -> {
                log("payment Sign error = ${paymentSignResult.errorMessage}")
            }
        }
    }

    override fun onPaymentValidationResult(paymentValidationResult: PaymentValidationResult) {
        when {
            paymentValidationResult.isSuccessful -> {
                log("payment Validation Success data = ${paymentValidationResult.data}")
                if (
                    (paymentValidationResult.data?.PSP?.IsSuccess == true) &&
                    (paymentValidationResult.data?.VERIFY?.IsSuccess == true)
                ) {
                    log("money transferred")
                } else {
                    log("money has not been transferred")
                }
            }
            paymentValidationResult.isLoading -> {
                log("payment Validation Loading ...")
            }
            else -> {
                log("payment Validation error = ${paymentValidationResult.errorMessage}")
            }
        }
    }

    private fun signPaymentButtonOnClick() {
        val paymentSignRequest = PaymentSignRequest(
            Api = API,
            Amount = "10000",
            InvoiceNumber = "12345",
            RedirectAddress = "http://www.example.com/some_path"
        )
        alsatIPG.sign(paymentSignRequest)
    }

    private fun configurePaymentValidation() {
        val data = intent.data
        if (data != null) {
            alsatIPG.validation(API, data)
        }
    }

    private fun log(message: String) {
        logTextView.text = "${logTextView.text}$message\n"
    }
}