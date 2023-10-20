package me.dio.copa.catar.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.compose.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import me.dio.copa.catar.extensions.observe
import me.dio.copa.catar.notification.scheduler.extensions.NotificationMatcherWorker

@AndroidEntryPoint
@ActivityScoped
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeActions()
        setContent {
            AppTheme(useDarkTheme = true) {
                val state by viewModel.state.collectAsState()
                MainScreen(state.matches, viewModel::toggleNotification)
            }
        }
    }

    private fun observeActions() {
        viewModel.action.observe(this) { action ->
            when(action) {
                is MainUiAction.DisableNotification ->
                    NotificationMatcherWorker.cancel(applicationContext, action.match)
                is MainUiAction.EnableNotification ->
                    NotificationMatcherWorker.start(applicationContext, action.match)
                is MainUiAction.MatchesNotFound -> TODO()
                MainUiAction.Unexpected -> TODO()
            }
        }
    }
}
