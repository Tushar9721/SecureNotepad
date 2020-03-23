package com.example.securenotepad


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint_auth)

        init()

        biometricManager = BiometricManager.from(this)
        val executor = ContextCompat.getMainExecutor(this)

        checkBiometricStatus(biometricManager)


        biometricPrompt = BiometricPrompt(this,executor,object : BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext, "Authentication error $errString",Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val snack = Snackbar.make(checkBoxFinger, "Fingerprint success", Snackbar.LENGTH_LONG)
                snack.show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(applicationContext,"Authentication Fail",Toast.LENGTH_SHORT).show()

            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Scan your finger print")
            .setDescription("To read the note scan the fingerprint!!")
            .setNegativeButtonText("Cancel")
            .build()



    }

    private fun init(){

        clickListeners()

    }

    private fun clickListeners() {

        checkBoxFinger.setOnClickListener(this)
        verify.setOnClickListener(this)

    }


    override fun onClick(p0: View?) {

        when(p0!!.id){

            R.id.checkBoxFinger -> {

                biometricPrompt.authenticate(promptInfo)

            }

            R.id.verify ->{



            }

        }

    }


    private fun checkBiometricStatus(biometricManager: BiometricManager){

        when(biometricManager.canAuthenticate()){
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.e("Fingerprint present","Success")

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e("Fingerprint present","No Hardware")

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e("Fingerprint present","Hardware Unavailable")

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e("Fingerprint present","No fingerprint present")

        }

    }




}
