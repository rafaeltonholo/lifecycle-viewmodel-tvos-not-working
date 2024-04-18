package dev.tonholo.lifecycle_viewmodel_tvos_not_working.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

actual class KmpStateFlow<out T : Any> actual constructor(private val wrapper: StateFlow<T>) : Flow<T> by wrapper {
    fun asStateFlow(): StateFlow<T> = wrapper
}
