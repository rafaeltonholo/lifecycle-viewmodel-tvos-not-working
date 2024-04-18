package dev.tonholo.lifecycle_viewmodel_tvos_not_working.lifecycle

import kotlinx.coroutines.CoroutineScope

expect abstract class KmpViewModel()

expect val KmpViewModel.viewModelScope: CoroutineScope
