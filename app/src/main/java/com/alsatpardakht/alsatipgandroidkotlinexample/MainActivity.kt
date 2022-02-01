package com.alsatpardakht.alsatipgandroidkotlinexample

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.alsatpardakht.alsatipgandroid.AlsatIPG
import com.alsatpardakht.alsatipgandroid.data.remote.model.PaymentSignRequest

class MainActivity : AppCompatActivity() {

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

        observeOnPaymentSignStatus()
        observeOnPaymentValidationStatus()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let { data ->
            log("intent and Uri is not null")
            alsatIPG.validation(API, data)
        } ?: log("intent or Uri is null")
    }

    private fun observeOnPaymentSignStatus() {
        alsatIPG.paymentSignStatus.observe(this) { paymentSignResult ->
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
                    log("payment Sign error = ${paymentSignResult.error?.message}")
                }
            }
        }
    }

    private fun observeOnPaymentValidationStatus() {
        alsatIPG.paymentValidationStatus.observe(this) { paymentValidationResult ->
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
                    log("payment Validation error = ${paymentValidationResult.error?.message}")
                }
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

    private fun log(message: String) {
        logTextView.text = "${logTextView.text}$message\n"
    }
}