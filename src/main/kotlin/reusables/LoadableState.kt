package reusables

import com.groupon.grox.Action

interface LoadableState<STATE: LoadableState<STATE>> {
    val loadState: LoadState
    fun update(loadState: LoadState): STATE

    val isSuccess: Boolean
        get() = loadState == LoadState.Success
}

enum class LoadState {
    Init, InProgress, Success
}

class SetLoadStateAction<STATE : LoadableState<STATE>>(private val loadState: LoadState): Action<STATE> {
    override fun newState(oldState: STATE): STATE {
        return oldState.update(loadState)
    }
}