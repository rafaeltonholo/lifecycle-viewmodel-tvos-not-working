/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:OptIn(ExperimentalStdlibApi::class)

package androidx.lifecycle

import androidx.annotation.MainThread
import androidx.lifecycle.viewmodel.internal.SynchronizedObject
import androidx.lifecycle.viewmodel.internal.VIEW_MODEL_SCOPE_KEY
import androidx.lifecycle.viewmodel.internal.ViewModelImpl
import androidx.lifecycle.viewmodel.internal.createViewModelScope
import androidx.lifecycle.viewmodel.internal.synchronized
import kotlinx.coroutines.CoroutineScope

abstract class ViewModel {

    private val impl: ViewModelImpl

    constructor() {
        impl = ViewModelImpl()
    }

    constructor(viewModelScope: CoroutineScope) {
        impl = ViewModelImpl(viewModelScope)
    }

    constructor(vararg closeables: AutoCloseable) {
        impl = ViewModelImpl(*closeables)
    }

    constructor(viewModelScope: CoroutineScope, vararg closeables: AutoCloseable) {
        impl = ViewModelImpl(viewModelScope, *closeables)
    }

    protected open fun onCleared() {}

    @MainThread
    internal fun clear() {
        impl.clear()
        onCleared()
    }

    fun addCloseable(key: String, closeable: AutoCloseable) {
        impl.addCloseable(key, closeable)
    }

    open fun addCloseable(closeable: AutoCloseable) {
        impl.addCloseable(closeable)
    }

    fun <T : AutoCloseable> getCloseable(key: String): T? = impl.getCloseable(key)
}

/**
 * The [CoroutineScope] associated with this [ViewModel].
 *
 * The [CoroutineScope.coroutineContext] is configured with:
 * - [SupervisorJob]: ensures children jobs can fail independently of each other.
 * - [MainCoroutineDispatcher.immediate]: executes jobs immediately on the main (UI) thread. If
 *  the [Dispatchers.Main] is not available on the current platform (e.g., Linux), we fallback
 *  to an [EmptyCoroutineContext].
 *
 * This scope is automatically cancelled when the [ViewModel] is cleared, and can be replaced by
 * using the [ViewModel] constructor overload that takes in a `viewModelScope: CoroutineScope`.
 *
 * For background execution, use [kotlinx.coroutines.withContext] to switch to appropriate
 * dispatchers (e.g., [kotlinx.coroutines.IO]).
 *
 * @see ViewModel.onCleared
 */
public val ViewModel.viewModelScope: CoroutineScope
    get() = synchronized(VIEW_MODEL_SCOPE_LOCK) {
        getCloseable(VIEW_MODEL_SCOPE_KEY)
            ?: createViewModelScope().also { scope -> addCloseable(VIEW_MODEL_SCOPE_KEY, scope) }
    }

private val VIEW_MODEL_SCOPE_LOCK = SynchronizedObject()
