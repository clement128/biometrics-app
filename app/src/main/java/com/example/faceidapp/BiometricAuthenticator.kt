package com.example.faceidapp

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

class BiometricAuthenticator(context: Context) {
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val biometricManager = BiometricManager.from(context)
    private lateinit var biometricPrompt: BiometricPrompt

    private fun isBiometricAuthAvailable(): BiometricAuthStatus {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthStatus.Ready
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAuthStatus.NotAvailable
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAuthStatus.TemporaryNotAvailable
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthStatus.AvailableButNotEnrolled
            else -> BiometricAuthStatus.NotAvailable
        }
    }

    fun promptBiometricAuth(
        title: String,
        subTitle: String,
        negativeButtonText: String,
        fragmentActivity: FragmentActivity,
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onFailed: () -> Unit,
        onError: (errorCode: Int, errorMessage: String) -> Unit
    ) {
        when (isBiometricAuthAvailable()) {
            BiometricAuthStatus.NotAvailable -> {
                onError(
                    BiometricAuthStatus.NotAvailable.id,
                    "Biometric authentication is not available"
                )
            }

            BiometricAuthStatus.TemporaryNotAvailable -> {
                onError(
                    BiometricAuthStatus.TemporaryNotAvailable.id,
                    "Biometric authentication is not available at this moment"
                )
            }

            BiometricAuthStatus.AvailableButNotEnrolled -> {
                onError(
                    BiometricAuthStatus.AvailableButNotEnrolled.id,
                    "You should add a fingerprint or a face ID first"
                )
            }

            else -> Unit
        }

        biometricPrompt = BiometricPrompt(
            fragmentActivity,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString.toString())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setNegativeButtonText(negativeButtonText).build()

        biometricPrompt.authenticate(promptInfo)
    }
}