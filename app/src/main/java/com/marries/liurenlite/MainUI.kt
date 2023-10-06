package com.marries.liurenlite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ViewState {
    UP, UPPING, MHD, MHDING, DOWN, DOWNING
}

@Preview(showBackground = true)
@Composable
fun MainUI(
    gua: String = "速喜",
    guaMHD: String = "赤口 流连 小吉",
    calender: String = "四月初一酉时",
    viewState: MutableState<ViewState> = mutableStateOf(ViewState.DOWN)
) {
    var viewState by remember { viewState }

    val mhding by animateFloatAsState(
        if (viewState == ViewState.MHDING || viewState == ViewState.MHD) 1f else 0f,
        tween(600),
        label = "MHDING"
    ) {
        if (it == 1.0f) {
            viewState = ViewState.MHD
        }
    }

    val upping by animateFloatAsState(
        if (viewState == ViewState.UPPING || viewState == ViewState.UP) 1f else 0f,
        tween(600),
        label = "UPPING"
    ) {
        if (it == 1.0f) {
            viewState = ViewState.UP
        }
    }

    val downing by animateFloatAsState(
        if (viewState == ViewState.DOWNING || viewState == ViewState.DOWN) 1f else 0f,
        tween(600),
        label = "DOWNING"
    ) {
        if (it == 1.0f) {
            viewState = ViewState.DOWN
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .clickable {
                when (viewState) {
                    ViewState.UP -> viewState = ViewState.DOWNING
                    ViewState.MHD -> viewState = ViewState.UPPING
                    ViewState.DOWN -> viewState = ViewState.MHDING
                    ViewState.UPPING, ViewState.MHDING, ViewState.DOWNING -> Unit
                }
            }, Alignment.Center) {
        Text(
            gua,
            Modifier
                .width(100.dp)
                .height(300.dp)
                .padding(top = 24.dp)
                .offset((upping * 200).dp, (upping * -200).dp),
            fontSize = 100.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.hongleixingshu))
        )
        if (viewState == ViewState.UPPING || viewState == ViewState.UP || viewState == ViewState.DOWNING) {
            Text(
                calender,
                Modifier
                    .width(140.dp)
                    .padding(start = 120.dp)
                    .alpha(upping),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.hongleixingshu))
            )
        }
        if (viewState == ViewState.MHD || viewState == ViewState.MHDING) {
            Text(
                guaMHD,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(mhding),
                fontFamily = FontFamily(Font(R.font.hongleixingshu))
            )
        }
    }
}