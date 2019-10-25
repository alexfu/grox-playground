package reusables

import com.groupon.grox.Action

interface PageableState<STATE : PageableState<STATE>> {
    val pagingLoadState: LoadState
    val page: Int
    val take: Int

    fun update(page: Int = this.page, take: Int = this.take, pagingLoadState: LoadState = this.pagingLoadState): STATE
}

class AdvancePageAction<STATE : PageableState<STATE>> : Action<STATE> {
    override fun newState(oldState: STATE): STATE {
        return oldState.update(page = oldState.page + 1)
    }
}

class SetPagingLoadStateAction<STATE : PageableState<STATE>>(private val loadState: LoadState) : Action<STATE> {
    override fun newState(oldState: STATE): STATE {
        return oldState.update(pagingLoadState = loadState)
    }
}