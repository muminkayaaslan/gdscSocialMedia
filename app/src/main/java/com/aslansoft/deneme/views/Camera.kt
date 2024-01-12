
package com.aslansoft.deneme.views

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraState
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.aslansoft.deneme.CameraPreview
import com.aslansoft.deneme.R
import com.aslansoft.deneme.classes.MainViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


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
    val viewModel = viewModel<MainViewModel>()
    val bitmaps by viewModel.bitmaps.collectAsState()
    val scope = rememberCoroutineScope()
    Surface(Modifier.fillMaxSize()) {


                CameraPreview(
                    controller = controller,
                )


            Box(modifier = Modifier.fillMaxSize()){
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
                        scope.launch {
                            controller.cameraControl.let {cameraState ->

                            }
                            controller.cameraSelector =
                                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {

                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else {
                                    CameraSelector.DEFAULT_BACK_CAMERA
                                }
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
                    Icon(modifier = Modifier.size(50.dp),imageVector = ImageVector.vectorResource(R.drawable.camera_capture),
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
                            /*
                            PhotoBotttomSheetContent(
                                bitmaps = bitmaps
                            )*/
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


@Composable
fun PhotoBotttomSheetContent(
    bitmaps: List<Bitmap>,
    modifier: Modifier = Modifier
) {
    if (bitmaps.isEmpty()){
        Box(modifier = modifier.padding(16.dp),
            contentAlignment = Alignment.Center){
            Text(text = "Henüz bir fotoğraf eklemediniz")
        }
    }else{
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
            contentPadding = PaddingValues(16.dp),
            modifier = modifier
        ){
            items(bitmaps.size){index ->
                val bitmapData = bitmaps[index]
                Image(modifier = Modifier.clip(RoundedCornerShape(10.dp)),
                    bitmap = bitmapData.asImageBitmap(),
                    contentDescription = null )
            }
        }
    }

}
