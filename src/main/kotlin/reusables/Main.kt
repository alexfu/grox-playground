package reusables

import com.groupon.grox.Action
import com.groupon.grox.Store
import com.groupon.grox.rxjava2.RxStores
import io.reactivex.Observable

fun main() {
    val store = TestStore()

    store.states()
        .distinctUntilChanged()
        .subscribe { println("State Updated = $it") }

    store.dispatch(SetLoadStateAction(LoadState.InProgress))
    store.dispatch(
        CompositeAction(
            UpdateNameAction(name = "Alex"),
            SetLoadStateAction(LoadState.Success)
        )
    )
    store.dispatch(SetPagingLoadStateAction(LoadState.InProgress))
    store.dispatch(
        CompositeAction(
            SetPagingLoadStateAction(LoadState.Success),
            AdvancePageAction()
        )
    )
}

// Actions

class UpdateNameAction(private val name: String) : Action<TestState> {
    override fun newState(oldState: TestState): TestState {
        return oldState.copy(name = name)
    }
}

class CompositeAction<STATE>(private vararg val actions: Action<STATE>) : Action<STATE> {
    override fun newState(oldState: STATE): STATE {
        return actions.fold(oldState) { previousState, action ->
            action.newState(previousState)
        }
    }
}

// Store / State

class TestStore : Store<TestState>(TestState())

data class TestState(
    val name: String = "",
    override val loadState: LoadState = LoadState.Init,
    override val pagingLoadState: LoadState = LoadState.Init,
    override val page: Int = 0,
    override val take: Int = 10
) : LoadableState<TestState>, PageableState<TestState> {
    override fun update(loadState: LoadState): TestState {
        return copy(loadState = loadState)
    }

    override fun update(page: Int, take: Int, pagingLoadState: LoadState): TestState {
        return copy(page = page, take = take, pagingLoadState = pagingLoadState)
    }
}

// Extensions

fun <STATE> Store<STATE>.states(): Observable<STATE> {
    return RxStores.states(this)
}