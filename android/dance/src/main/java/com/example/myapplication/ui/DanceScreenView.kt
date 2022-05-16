package com.example.myapplication.ui

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R

@Preview
@Composable
fun DanceApp() {
    MyApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val danceScreenViewModel: DanceScreenViewModel = viewModel(
                factory = DanceScreenViewModel.provideFactory(LocalContext.current)
            )
            MainView(viewModel = danceScreenViewModel)
        }
    }
}

@Composable
fun MainView(viewModel: DanceScreenViewModel) {
    val danceScreenState by viewModel.danceScreenState.observeAsState()
    if (danceScreenState!!.isLoading) {
        LoadingPage()
    } else {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {
//            viewModel.play()
            }) {
            CameraPreview(startDetectPost = viewModel::startDetectPose)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TopItem(title = "当前得分", value = danceScreenState!!.score.toString())
                TopItem(title = "当前状态", value = danceScreenState!!.status)
                TopItem(title = "练习效果", value = danceScreenState!!.effect)
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(200.dp, 200.dp)
                    .background(Color(0xffffa500), RoundedCornerShape(topStart = 200.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomItem(title = "血氧", value = "${danceScreenState!!.bloodOxygen}%")
                    Spacer(modifier = Modifier.height(20.dp))
                    BottomItem(title = "心率", value = "${danceScreenState!!.heartRate}")
                    Spacer(modifier = Modifier.height(20.dp))
                    BottomItem(title = "卡路里", value = "${danceScreenState!!.calorie}cal")
//                    BottomItem(title = "当前视频", value = "${viewModel.gift_index}")
                }
            }
            /*         Button(modifier = Modifier.size(50.dp), onClick = { viewModel.attachView() }) {
                         Text("开启播放器")
                     }
                     Button(
                         modifier = Modifier
                             .size(50.dp)
                             .align(Alignment.BottomStart),
                         onClick = { viewModel.play() }) {
                         Text("播放")
                     }*/
            ExoPlayerView(
                modifier = Modifier
                    .size(350.dp, 300.dp)
                    .align(Alignment.CenterStart),
                viewModel = viewModel
            )
        }
    }
}


@Composable
fun LoadingPage() {
    CircularProgressIndicator()
}


@Composable
fun TopItem(title: String, value: String) {
    Text(
        text = "$title: $value", color = Color.White, fontFamily = FontFamily(
            Font(R.font.dance_ui, FontWeight.Light),
            Font(R.font.dance_ui, FontWeight.Light),
            Font(R.font.dance_ui, FontWeight.Normal),
            Font(R.font.dance_ui, FontWeight.Normal, FontStyle.Italic),
            Font(R.font.dance_ui, FontWeight.Medium),
            Font(R.font.dance_ui, FontWeight.Bold),
        ),
        fontWeight = FontWeight.Light,
        style = TextStyle(fontSize = 30.sp)
    )
}

@Composable
fun BottomItem(title: String, value: String) {
    Text(
        text = "$title: $value", color = Color.White, fontFamily = FontFamily(
            Font(R.font.dance_ui, FontWeight.Light),
            Font(R.font.dance_ui, FontWeight.Light),
            Font(R.font.dance_ui, FontWeight.Normal),
            Font(R.font.dance_ui, FontWeight.Normal, FontStyle.Italic),
            Font(R.font.dance_ui, FontWeight.Medium),
            Font(R.font.dance_ui, FontWeight.Bold),
        ),
        fontWeight = FontWeight.Light,
        style = TextStyle(fontSize = 20.sp)
    )
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    startDetectPost: (PreviewView) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                this.scaleX = -1f
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                // Preview is incorrectly scaled in Compose on some devices without this
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
            startDetectPost(previewView)
            previewView
        })
}

@Composable
fun ExoPlayerView(
    modifier: Modifier = Modifier,
    viewModel: DanceScreenViewModel
) {
    AndroidView(
        modifier = modifier,
        factory = {
            val exoPlayerView = viewModel.videoGiftView
            exoPlayerView
        },
    )
}