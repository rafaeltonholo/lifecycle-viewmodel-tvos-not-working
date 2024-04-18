package dev.tonholo.lifecycle_viewmodel_tvos_not_working

import dev.tonholo.lifecycle_viewmodel_tvos_not_working.flow.KmpStateFlow
import dev.tonholo.lifecycle_viewmodel_tvos_not_working.flow.kmp
import dev.tonholo.lifecycle_viewmodel_tvos_not_working.lifecycle.KmpViewModel
import dev.tonholo.lifecycle_viewmodel_tvos_not_working.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration.Companion.milliseconds

sealed class UiState {
    data object Loading : UiState()
    data class Success(val text: String) : UiState()
}

class MyViewModel : KmpViewModel() {
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: KmpStateFlow<UiState> = _state.asStateFlow().kmp

    init {
        viewModelScope.launch {
            delay(Random.nextInt(1000..5000).milliseconds)
            _state.value = UiState.Success(text = "That was a success!")
        }
        println("init called")
    }

}
