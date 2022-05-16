/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.wearable.composeforwearos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import com.example.android.wearable.composeforwearos.data.HomeRepository
import com.example.android.wearable.composeforwearos.theme.WearAppTheme
import com.example.grpctest.R
//import com.sdu.kangaroo.R


/**
 * This code lab is meant to help existing Compose developers get up to speed quickly on
 * Compose for Wear OS.
 *
 * The code lab walks through a majority of the simple composables for Wear OS (both similar to
 * existing mobile composables and new composables).
 *
 * It also covers more advanced composables like [ScalingLazyColumn] (Wear OS's version of
 * [LazyColumn]) and the Wear OS version of [Scaffold].
 *
 * Check out [this link](https://android-developers.googleblog.com/2021/10/compose-for-wear-os-now-in-developer.html)
 * for more information on Compose for Wear OS.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeRepository = (application as BaseApplication).homeRepository
        setContent {
            WearApp(homeRepository = homeRepository)
        }
    }
}

@Composable
@OptIn(ExperimentalWearMaterialApi::class)
fun WearApp(homeRepository: HomeRepository) {
    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModel.provideFactory(homeRepository)
    )
    val mainViewState by mainViewModel.mainViewState.observeAsState()

    WearAppTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(20.dp))
            Text(text = mainViewModel.getVideoModel().title)
            Spacer(modifier = Modifier.size(10.dp))
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 30.sp)) {
                    append(mainViewState!!.score.toString())
                }
                append(" 分")
            })
            Spacer(modifier = Modifier.size(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val buttonColor = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = Color.Transparent
                )
                val buttonContentModifier = Modifier.size(22.dp)

                Button(
                    onClick = { mainViewModel.last() },
                    colors = buttonColor
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.last),
                        contentDescription = "上一段",
                        modifier =buttonContentModifier
                    )
                }
                if (mainViewState!!.isPaused) {
                    Button(onClick = { mainViewModel.pause() }, colors = buttonColor) {
                        Image(
                            painter = painterResource(id = R.drawable.pause),
                            contentDescription = "暂停",
                            modifier = Modifier.size(75.dp)
                        )
                    }
                } else {
                    Button(onClick = { mainViewModel.start() }, colors = buttonColor) {
                        Image(
                            painter = painterResource(id = R.drawable.play_button),
                            contentDescription = "播放",
                            modifier = Modifier.size(75.dp)
                        )
                    }
                }
                Button(onClick = { mainViewModel.next() }, colors = buttonColor) {
                    Image(
                        painter = painterResource(id = R.drawable.next),
                        contentDescription = "下一段",
                        modifier = buttonContentModifier
                    )
                }
            }
        }

        // TODO (End): Create a Scaffold (Wear Version)

    }
}
/*

// Note: Preview in Android Studio doesn't support the round view yet (coming soon).
@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun WearAppPreview() {
    WearApp()
}
*/
