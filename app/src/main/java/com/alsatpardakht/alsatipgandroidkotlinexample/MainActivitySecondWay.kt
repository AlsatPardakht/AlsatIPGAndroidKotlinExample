package com.alsatpardakht.alsatipgandroidkotlinexample

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.alsatpardakht.alsatipgandroid.AlsatIPG
import com.alsatpardakht.ipg.data.remote.model.PaymentSignRequest

class MainActivitySecondWay : AppCompatActivity() {

    private val API = "ENTER YOUR API KEY HERE"

    private val alsatIPG = AlsatIPG.getInstance()

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
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let { data ->
            log("intent and Uri is not null")
            alsatIPG.validation(API, data) { paymentValidationResult ->
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
        } ?: log("intent or Uri is null")
    }

    private fun signPaymentButtonOnClick() {
        val paymentSignRequest = PaymentSignRequest(
            Api = API,
            Amount = "10000",
            InvoiceNumber = "12345",
            RedirectAddress = "http://www.example.com/some_path"
        )
        alsatIPG.sign(paymentSignRequest) { paymentSignResult ->
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
    }

    private fun log(message: String) {
        logTextView.text = "${logTextView.text}$message\n"
    }
}