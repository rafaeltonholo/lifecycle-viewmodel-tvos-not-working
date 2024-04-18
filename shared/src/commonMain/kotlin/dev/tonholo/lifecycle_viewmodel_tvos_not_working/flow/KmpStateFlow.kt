package dev.tonholo.lifecycle_viewmodel_tvos_not_working.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

expect class KmpStateFlow<out T : Any>(
    wrapper: StateFlow<T>,
) : Flow<T>

val <T : Any> StateFlow<T>.kmp
    get() = KmpStateFlow(this)
