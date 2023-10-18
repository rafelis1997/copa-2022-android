package me.dio.copa.catar.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import me.dio.copa.catar.core.BaseViewModel
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.usecase.DisableNotificationUseCase
import me.dio.copa.catar.domain.usecase.EnableNotificationUseCase
import me.dio.copa.catar.domain.usecase.GetMatchesUseCase
import me.dio.copa.catar.remote.NotFoundException
import me.dio.copa.catar.remote.UnexpectedException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val enableNotificationUseCase: EnableNotificationUseCase,
    private val disableNotificationUseCase: DisableNotificationUseCase,
) : BaseViewModel<MainUiState, MainUiAction>(MainUiState()) {

    init {
        fetchMatches()
    }

    private fun fetchMatches() = viewModelScope.launch {
        getMatchesUseCase()
            .flowOn(Dispatchers.Main)
            .catch {
                when(it) {
                    is NotFoundException ->
                        sendAction(MainUiAction.MatchesNotFound(it.message ?: "Erro sem mensagem"))
                    is UnexpectedException ->
                        sendAction(MainUiAction.Unexpected)
                }
            }.collect {
                setState {
                    copy(matches = it)
                }
            }
    }
}

data class MainUiState(
    val matches: List<MatchDomain> = emptyList()
)

sealed class MainUiAction {
    object Unexpected: MainUiAction()
    data class MatchesNotFound(val message: String) : MainUiAction()
}