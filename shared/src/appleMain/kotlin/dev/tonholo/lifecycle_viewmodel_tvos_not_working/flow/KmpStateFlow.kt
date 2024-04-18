package dev.tonholo.lifecycle_viewmodel_tvos_not_working.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.experimental.ExperimentalObjCName

fun interface Closable {
    fun close()
}

actual class KmpStateFlow<out T : Any> actual constructor(
    wrapper: StateFlow<T>,
) : Flow<T> by wrapper {
    @OptIn(ExperimentalObjCName::class)
    fun observe(
        @ObjCName("_")
        block: (T) -> Unit,
    ): Closable {
        val job = Job()

        onEach(block)
            .launchIn(CoroutineScope(Dispatchers.Main + job))

        return Closable {
            job.cancel()
        }
    }
}

