package app.git.stash

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.eclipse.jgit.api.Git
import javax.inject.Inject

class StashChangesUseCase @Inject constructor() {
    suspend operator fun invoke(git: Git, message: String?): Unit = withContext(Dispatchers.IO) {
        git
            .stashCreate()
            .setIncludeUntracked(true)
            .apply {
                if (message != null)
                    setWorkingDirectoryMessage(message)
            }
            .call()
    }
}