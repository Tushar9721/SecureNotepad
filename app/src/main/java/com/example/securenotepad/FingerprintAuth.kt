package com.example.securenotepad


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_fingerprint_auth.*


class FingerprintAuth : AppCompatActivity() {

    private lateinit var biometricManager: BiometricManager
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint_auth)


        biometricManager = BiometricManager.from(this)
        val executor = ContextCompat.getMainExecutor(this)

        checkBiometricStatus(biometricManager)


        biometricPrompt = BiometricPrompt(this,executor,object : BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(applicationContext,"Authentication error",Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(applicationContext,"Success",Toast.LENGTH_SHORT).show()

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


            biometricPrompt.authenticate(promptInfo)



    }

    private fun checkBiometricStatus(biometricManager: BiometricManager){

        when(biometricManager.canAuthenticate()){
            BiometricManager.BIOMETRIC_SUCCESS ->
                Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(this,"No Hardware",Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(this,"Hardware Unavailable",Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Toast.makeText(this,"None Enrolled",Toast.LENGTH_SHORT).show()
        }

    }




}
