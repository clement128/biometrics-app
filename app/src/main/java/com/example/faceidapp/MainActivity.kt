package com.example.faceidapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.faceidapp.ui.theme.FaceIDAppTheme
import com.google.firebase.crashlytics.internal.common.CommonUtils
import com.scottyab.rootbeer.RootBeer

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = this
        val biometricAuthenticator = BiometricAuthenticator(context)
        val rootBeer = RootBeer(context)

        enableEdgeToEdge()
        setContent {
            FaceIDAppTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val activity = LocalContext.current as FragmentActivity
                    var message by remember { mutableStateOf("") }
                    var checkMessages by remember { mutableStateOf(listOf<String>()) }



                    Button(
                        onClick = {
                            val isRooted = CommonUtils.isRooted()
                            val isEmulator = CommonUtils.isEmulator()
                            val isRootBeerRooted = rootBeer.isRooted
                            val isRootBeerRootedWithBusyBoxCheck = rootBeer.isRootedWithBusyBoxCheck
                            checkMessages = listOf(
                                "Firebase(rooted): $isRooted",
                                "Firebase Emulator: $isEmulator",
                                "RootBeer(rooted): $isRootBeerRooted",
                                "RootBeer with busybox(rooted): $isRootBeerRootedWithBusyBoxCheck"
                            )
                        }
                    ) {
                        Text(text = "Check")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    for (txt in checkMessages) {
                        Text(text = txt)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = {
                        biometricAuthenticator.promptBiometricAuth(
                            title = "Login",
                            subTitle = "Use your fingerprint or face ID",
                            negativeButtonText = "Cancel",
                            fragmentActivity = activity,
                            onSuccess = {
                                message = "Success"
                            },
                            onFailed = {
                                message = "Wrong fingerprint or face ID"
                            },
                            onError = { id, error ->
                                message = error + "" + id.toString()
                            }
                        )
                    }) {
                        Text(text = "Face ID or Fingerprint")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = message)
                }
            }
        }
    }
}