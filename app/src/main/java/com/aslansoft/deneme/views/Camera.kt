
    package com.aslansoft.deneme.views

    import android.Manifest
    import android.content.pm.PackageManager
    import android.graphics.Bitmap
    import android.net.Uri
    import android.os.Build
    import android.widget.Toast
    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.annotation.RequiresApi
    import androidx.camera.core.CameraSelector
    import androidx.camera.core.ImageCapture.OnImageCapturedCallback
    import androidx.camera.core.ImageCaptureException
    import androidx.camera.core.ImageProxy
    import androidx.camera.view.CameraController
    import androidx.camera.view.LifecycleCameraController
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.ArrowBack
    import androidx.compose.material.icons.filled.Check
    import androidx.compose.material.icons.filled.Clear
    import androidx.compose.material3.AlertDialog
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Surface
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.ImageBitmap
    import androidx.compose.ui.graphics.vector.ImageVector
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.imageResource
    import androidx.compose.ui.res.vectorResource
    import androidx.compose.ui.unit.dp
    import androidx.core.content.ContextCompat
    import androidx.navigation.NavHostController
    import com.aslansoft.deneme.CameraPreview
    import com.aslansoft.deneme.R


    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun Camera(navController: NavHostController) {
        val onPhotoTaken: (Bitmap) -> Unit = {}
        val context = LocalContext.current
        val controller = remember{
            LifecycleCameraController(context).apply {
                setEnabledUseCases(
                    CameraController.IMAGE_CAPTURE
                )
            }
        }
        val pickPhotoLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){uri ->
            //burda butona tıklanınca eğer galeriden bir görsel seçildiyse sendPost.kt yönlendirilecek  fotoğraf

        }
        val multiplePermissionsLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val myVersion = Build.VERSION.SDK_INT
            if (myVersion >= Build.VERSION_CODES.S) {
                if (permissions[Manifest.permission.READ_MEDIA_IMAGES] == true) {
                    pickPhotoLauncher.launch("image/*")
                } else {
                    Toast.makeText(
                        context,
                        "Dosya Erişimi İçin İzin Vermelisin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else { // myVersion > Build.VERSION_CODES.R
                if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                    pickPhotoLauncher.launch("image/*")
                } else {
                    Toast.makeText(
                        context,
                        "Dosya Erişimi İçin İzin Vermelisin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
        Surface(Modifier.fillMaxSize()) {




                /*
                AlertDialog(onDismissRequest = { !cameraGranted.value }, confirmButton = { IconButton(onClick = { cameraGranted.value = true}) {
                    Icon(imageVector = Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                } },
                    dismissButton = { IconButton(onClick = { cameraGranted.value = false }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary )
                    }},
                    title = { Text(text = "Kamera")},
                    icon = { Icon(imageVector = ImageVector.vectorResource(R.drawable.photo_camera), contentDescription = null)},
                    text = { Text(text = "Kamerayı kullanmak ve gönderi paylaşabilmek için erişim izni vermeniz gerekmektedir")})
            */
/*
            if (!cameraGranted.value){
                AlertDialog(onDismissRequest = { !cameraGranted.value }) {
                    if (!cameraGranted.value){
                        requestPermissionCameraLauncher.launch(Manifest.permission.CAMERA)
                    }
                    else{
                        cameraGranted.value
                    }

                }
            }else {

            }

*/
                Box(modifier = Modifier.fillMaxSize()){
                    CameraPreview(
                        controller = controller,
                    )
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd),
                        onClick = {
                                controller.cameraSelector =
                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {

                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else {
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    }
                                  },
                        ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            bitmap = ImageBitmap.imageResource(R.drawable.cameraswitch),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )

                    }
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 30.dp)
                            .size(50.dp),
                        onClick = {
                            controller.takePicture(
                                ContextCompat.getMainExecutor(context),
                                object : OnImageCapturedCallback() {
                                    override fun onCaptureSuccess(image: ImageProxy) {
                                        super.onCaptureSuccess(image)
                                        onPhotoTaken(image.toBitmap())
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        super.onError(exception)
                                        println(exception.message)
                                    }
                                }
                            )
                        },
                    ) {

                        Icon(modifier = Modifier
                            .size(50.dp)
                            .clickable {

                            }
                           ,imageVector = ImageVector.vectorResource(R.drawable.camera_capture),
                            contentDescription = null,
                            tint = MaterialTheme
                                .colorScheme
                                .secondary)

                    }
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 30.dp)
                            .size(50.dp),
                        onClick = {

                        },
                    ) {
                        Icon(modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                val myVersion = Build.VERSION.SDK_INT

                                if (myVersion >= Build.VERSION_CODES.S) {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.READ_MEDIA_IMAGES
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        pickPhotoLauncher.launch("image/*")
                                    } else {
                                        multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
                                    }
                                } else {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.READ_EXTERNAL_STORAGE
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        pickPhotoLauncher.launch("image/*")
                                    } else {
                                        multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                                    }
                                }

                            },
                            imageVector = ImageVector.vectorResource(R.drawable.add_a_photo),
                            contentDescription = null,
                            tint = MaterialTheme
                                .colorScheme
                                .secondary)

                    }
                }

            }
        }
