package com.aslansoft.deneme

import android.annotation.SuppressLint
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraPreview(
    controller: LifecycleCameraController
) {

    val lifecycleOwner = LocalLifecycleOwner.current
        AndroidView(modifier = Modifier.fillMaxSize(),factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        })
}