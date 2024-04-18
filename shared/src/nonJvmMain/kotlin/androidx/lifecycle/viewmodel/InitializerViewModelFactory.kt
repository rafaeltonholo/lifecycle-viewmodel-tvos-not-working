/*
 * Copyright 2024 The Android Open Source Project
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
package androidx.lifecycle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.internal.ViewModelProviders
import androidx.lifecycle.viewmodel.internal.canonicalName
import kotlin.reflect.KClass

@DslMarker
annotation class ViewModelFactoryDsl

/**
 * Creates an [InitializerViewModelFactory] with the initializers provided in the builder.
 */
inline fun viewModelFactory(
    builder: InitializerViewModelFactoryBuilder.() -> Unit
): ViewModelProvider.Factory = InitializerViewModelFactoryBuilder().apply(builder).build()

/**
 * DSL for constructing a new [ViewModelProvider.Factory]
 */
@ViewModelFactoryDsl
class InitializerViewModelFactoryBuilder {

    private val initializers = mutableMapOf<KClass<*>, ViewModelInitializer<*>>()

    /**
     * Associates the specified [initializer] with the given [ViewModel] class.
     *
     * @param clazz [ViewModel] class with which the specified [initializer] is to be associated.
     * @param initializer factory lambda to be associated with the specified [ViewModel] class.
     */
    @Suppress("SetterReturnsThis", "MissingGetterMatchingBuilder")
    fun <T : ViewModel> addInitializer(
        clazz: KClass<T>,
        initializer: CreationExtras.() -> T,
    ) {
        require(clazz !in initializers) {
            "A `initializer` with the same `clazz` has already been added: ${clazz.canonicalName}."
        }
        initializers[clazz] = ViewModelInitializer(clazz, initializer)
    }

    /**
     * Returns an instance of [ViewModelProvider.Factory] created from the initializers set on this
     * builder.
     */
    fun build(): ViewModelProvider.Factory =
        ViewModelProviders.createInitializerFactory(initializers.values)
}

/**
 * Add an initializer to the [InitializerViewModelFactoryBuilder]
 */
inline fun <reified VM : ViewModel> InitializerViewModelFactoryBuilder.initializer(
    noinline initializer: CreationExtras.() -> VM
) {
    addInitializer(VM::class, initializer)
}