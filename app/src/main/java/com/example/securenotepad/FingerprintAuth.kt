package com.example.securenotepad


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_fingerprint_auth.*


class FingerprintAuth : AppCompatActivity(), View.OnClickListener {

    private lateinit var biometricManager: BiometricManager
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var title: String? = null
    private var description: String? = null
    private var password: String? = null
    private var emailCount: Int? = 0
    private var enableFingerPrint: String? = null
    private var sendEmailID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint_auth)

        init()

        biometricManager = BiometricManager.from(this)
        val executor = ContextCompat.getMainExecutor(this)

        checkBiometricStatus(biometricManager)


        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    emailCount = emailCount!! + 1
                    Toast.makeText(
                        applicationContext,
                        "Authentication error $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val snack =
                        Snackbar.make(checkBoxFinger, "Fingerprint success", Snackbar.LENGTH_LONG)
                    snack.show()

                    callActivity()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication Fail", Toast.LENGTH_SHORT)
                        .show()
                    emailCount = emailCount!! + 1
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Scan your finger print")
            .setDescription("To read the note scan the fingerprint!!")
            .setNegativeButtonText("Cancel")
            .build()


    }

    private fun callActivity() {
        val intent = Intent(this, CrateNewNotes::class.java)
        intent.putExtra("title", title)
        intent.putExtra("description", description)
        intent.putExtra("data", "Yes")
        finish()
        startActivity(intent)
    }

    private fun init() {

        setData()

        if (enableFingerPrint == "true") {
            checkBoxFinger.visibility = View.VISIBLE
        } else {
            checkBoxFinger.visibility = View.GONE
        }

        clickListeners()

    }

    private fun setData() {

        title = intent.getStringExtra("title")
        description = intent.getStringExtra("description")
        password = intent.getStringExtra("password")
        enableFingerPrint = intent.getStringExtra("finger")
        sendEmailID = intent.getStringExtra("email")
    }

    private fun clickListeners() {

        checkBoxFinger.setOnClickListener(this)
        verify.setOnClickListener(this)
        backHome.setOnClickListener(this)

    }


    override fun onClick(p0: View?) {

        when (p0!!.id) {

            R.id.backHome -> {
                finish()
            }

            R.id.checkBoxFinger -> {

                if (emailCount!! <= 3) {
                    biometricPrompt.authenticate(promptInfo)
                } else {
                    sendEmail()
                }

            }

            R.id.verify -> {

                if (edPasswordCheck.text.toString().trim().isNotEmpty()) {
                    if (emailCount!! <= 3) {
                        if (edPasswordCheck.text.toString().trim() == password) {

                            val snack =
                                Snackbar.make(checkBoxFinger, "success", Snackbar.LENGTH_LONG)
                            snack.show()

                            callActivity()

                        } else {
                            emailCount = emailCount!! + 1
                            val snack =
                                Snackbar.make(checkBoxFinger, "Not success", Snackbar.LENGTH_LONG)
                            snack.show()
                        }

                    } else {
                        sendEmail()
                    }
                } else {
                    Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                }


            }

        }

    }

    private fun sendEmail() {

        val javaApi = JavaMailAPI(sendEmailID!!, "Someone is opening your $title", "Some is trying to access your notes without your permission!!. Multiple times password has been made incorrect")
        javaApi.execute()

    }


    private fun checkBiometricStatus(biometricManager: BiometricManager) {

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.e("Fingerprint present", "Success")

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("Fingerprint present", "No Hardware")

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("Fingerprint present", "Hardware Unavailable")

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e("Fingerprint present", "No fingerprint present")

        }

    }


}
