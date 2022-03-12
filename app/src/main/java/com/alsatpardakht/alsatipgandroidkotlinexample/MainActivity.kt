package com.alsatpardakht.alsatipgandroidkotlinexample

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.alsatpardakht.alsatipgandroid.AlsatIPG
import com.alsatpardakht.alsatipgcore.domain.model.PaymentType
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.Normalizer

class MainActivity : AppCompatActivity() {

    private val API = "ENTER YOUR API KEY HERE"
    private val paymentType = PaymentType.Mostaghim

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

        observeToPaymentSignStatus()
        observeToPaymentValidationStatus()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.data?.let { data ->
            log("intent and Uri is not null")
            when (paymentType) {
                PaymentType.Mostaghim -> alsatIPG.validationMostaghim(API, data)
                PaymentType.Vaset -> alsatIPG.validationVaset(API, data)
            }
        } ?: log("intent or Uri is null")
    }

    private fun observeToPaymentSignStatus() {
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

    private fun observeToPaymentValidationStatus() {
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
        when (paymentType) {
            PaymentType.Mostaghim -> alsatIPG.signMostaghim(
                Api = API,
                Amount = 10_000,
                InvoiceNumber = "123456",
                RedirectAddress = "http://www.example.com/some_path"
            )
            PaymentType.Vaset -> alsatIPG.signVaset(
                Api = API,
                Amount = 20_000,
                RedirectAddress = "http://www.example.com/some_path",
                Tashim = emptyList()
            )
        }
    }

    private fun log(message: String) {
        logTextView.text = "${logTextView.text}$message\n"
    }
}