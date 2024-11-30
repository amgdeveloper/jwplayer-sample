package com.jwplayer.compose.ui.jw

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jwplayer.compose.viewmodel.VideoViewModel

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun Video(
) {
  val viewModel: VideoViewModel = viewModel()

  val lifecycleOwner = remember { CustomLifecycleOwner() }

  LaunchedEffect(Unit) {
    lifecycleOwner.moveToState(Lifecycle.State.STARTED) // Move to STARTED
  }

  DisposableEffect(Unit) {
    onDispose {
      Log.e("TAG","Video Composable onDispose")
      lifecycleOwner.moveToState(Lifecycle.State.DESTROYED) // Move to DESTROYED
    }
  }

  VideoPlayer(
    modifier = Modifier.fillMaxSize(),
    videoState = viewModel.uiState.collectAsState().value,
    lifecycleOwner = lifecycleOwner,
    handleEvent = viewModel::handleEvent
  )
}

class CustomLifecycleOwner : LifecycleOwner {
  private val lifecycleRegistry = LifecycleRegistry(this)

  init {
    lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
  }


  override val lifecycle: Lifecycle
    get() = lifecycleRegistry


  fun moveToState(state: Lifecycle.State) {
    lifecycleRegistry.currentState = state
  }

}
