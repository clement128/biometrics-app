package com.example.faceidapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

                    val isRooted = CommonUtils.isRooted()
                    val isEmulator = CommonUtils.isEmulator()

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Rooted: $isRooted")
                    Text(text = "Emulator: $isEmulator")
                    Text(text = "RootBeer(rooted): ${rootBeer.isRooted}")
                    Text(text = "RootBeer with busybox(rooted): ${rootBeer.isRootedWithBusyBoxCheck}")

                    TextButton(onClick = {
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
                        Text(text = "Authenticate")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = message)
                }
            }
        }
    }
}