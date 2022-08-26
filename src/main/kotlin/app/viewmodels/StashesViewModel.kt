package app.viewmodels

import app.git.RefreshType
import app.git.TabState
import app.git.stash.ApplyStashUseCase
import app.git.stash.DeleteStashUseCase
import app.git.stash.GetStashListUseCase
import app.git.stash.PopStashUseCase
import app.ui.SelectedItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import javax.inject.Inject

class StashesViewModel @Inject constructor(
    private val getStashListUseCase: GetStashListUseCase,
    private val applyStashUseCase: ApplyStashUseCase,
    private val popStashUseCase: PopStashUseCase,
    private val deleteStashUseCase: DeleteStashUseCase,
    private val tabState: TabState,
) : ExpandableViewModel(true) {
    private val _stashStatus = MutableStateFlow<StashStatus>(StashStatus.Loaded(listOf()))

    val stashStatus: StateFlow<StashStatus>
        get() = _stashStatus

    suspend fun loadStashes(git: Git) {
        _stashStatus.value = StashStatus.Loading
        val stashList = getStashListUseCase(git)
        _stashStatus.value = StashStatus.Loaded(stashList.toList())
    }

    suspend fun refresh(git: Git) {
        loadStashes(git)
    }

    fun applyStash(stashInfo: RevCommit) = tabState.safeProcessing(
        refreshType = RefreshType.UNCOMMITED_CHANGES_AND_LOG,
        refreshEvenIfCrashes = true,
    ) { git ->
        applyStashUseCase(git, stashInfo)
    }

    fun popStash(stash: RevCommit) = tabState.safeProcessing(
        refreshType = RefreshType.UNCOMMITED_CHANGES_AND_LOG,
        refreshEvenIfCrashes = true,
    ) { git ->
        popStashUseCase(git, stash)

        stashDropped(stash)
    }

    fun deleteStash(stash: RevCommit) = tabState.safeProcessing(
        refreshType = RefreshType.STASHES,
    ) { git ->
        deleteStashUseCase(git, stash)
        stashDropped(stash)
    }

    fun selectTab(stash: RevCommit) {
        tabState.newSelectedStash(stash)
    }

    private fun stashDropped(stash: RevCommit) {
        val selectedValue = tabState.selectedItem.value
        if (
            selectedValue is SelectedItem.Stash &&
            selectedValue.revCommit.name == stash.name
        ) {
            tabState.noneSelected()
        }
    }
}


sealed class StashStatus {
    object Loading : StashStatus()
    data class Loaded(val stashes: List<RevCommit>) : StashStatus()
}