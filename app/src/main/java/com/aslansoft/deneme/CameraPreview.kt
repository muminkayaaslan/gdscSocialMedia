package com.aslansoft.deneme

import android.annotation.SuppressLint
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation", "ClickableViewAccessibility")
@Composable
fun CameraPreview(
    controller: LifecycleCameraController
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(modifier = Modifier.fillMaxSize(), factory = { it ->
        val previewView = PreviewView(it)

        // CameraController'ı burada ayarlamak daha doğru
        previewView.controller = controller
        controller.bindToLifecycle(lifecycleOwner)
        previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        previewView
    })
}
