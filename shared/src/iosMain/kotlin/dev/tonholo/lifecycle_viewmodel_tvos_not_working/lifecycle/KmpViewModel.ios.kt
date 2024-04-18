package dev.tonholo.lifecycle_viewmodel_tvos_not_working.lifecycle

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual typealias KmpViewModel = androidx.lifecycle.ViewModel

actual val KmpViewModel.viewModelScope: CoroutineScope
    get() = viewModelScope
