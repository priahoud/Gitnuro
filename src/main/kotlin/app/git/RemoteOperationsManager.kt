package app.git

import app.credentials.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ProgressMonitor
import org.eclipse.jgit.transport.*
import java.io.File
import javax.inject.Inject

class RemoteOperationsManager @Inject constructor(
    private val sessionManager: GSessionManager
) {
    private val _cloneStatus = MutableStateFlow<CloneStatus>(CloneStatus.None)
    val cloneStatus: StateFlow<CloneStatus>
        get() = _cloneStatus

    suspend fun pull(git: Git) = withContext(Dispatchers.IO) {
        git
            .pull()
            .setTransportConfigCallback {
                if (it is SshTransport) {
                    it.sshSessionFactory = sessionManager.generateSshSessionFactory()
                } else if (it is HttpTransport) {
                    it.credentialsProvider = HttpCredentialsProvider()
                }
            }
            .setCredentialsProvider(CredentialsProvider.getDefault())
            .call()
    }

    suspend fun push(git: Git) = withContext(Dispatchers.IO) {
        val currentBranchRefSpec = git.repository.fullBranch

        git
            .push()
            .setRefSpecs(RefSpec(currentBranchRefSpec))
            .setPushTags()
            .setTransportConfigCallback {
                if (it is SshTransport) {
                    it.sshSessionFactory = sessionManager.generateSshSessionFactory()
                } else if (it is HttpTransport) {
                    it.credentialsProvider = HttpCredentialsProvider()
                }
            }
            .call()
    }

    suspend fun clone(directory: File, url: String) = withContext(Dispatchers.IO) {
        try {
            _cloneStatus.value = CloneStatus.Cloning(0)

            Git.cloneRepository()
                .setDirectory(directory)
                .setURI(url)
                .setProgressMonitor(object : ProgressMonitor {
                    override fun start(totalTasks: Int) {
                        println("ProgressMonitor Start")
                    }

                    override fun beginTask(title: String?, totalWork: Int) {
                        println("ProgressMonitor Begin task")
                    }

                    override fun update(completed: Int) {
                        println("ProgressMonitor Update $completed")
                        _cloneStatus.value = CloneStatus.Cloning(completed)
                    }

                    override fun endTask() {
                        println("ProgressMonitor End task")
                        _cloneStatus.value = CloneStatus.CheckingOut
                    }

                    override fun isCancelled(): Boolean {
                        return !isActive
                    }

                })
                .setTransportConfigCallback {
                    if (it is SshTransport) {
                        it.sshSessionFactory = sessionManager.generateSshSessionFactory()
                    } else if (it is HttpTransport) {
                        it.credentialsProvider = HttpCredentialsProvider()
                    }
                }
                .call()

            _cloneStatus.value = CloneStatus.Completed
        } catch (ex: Exception) {
            _cloneStatus.value = CloneStatus.Fail(ex.localizedMessage)
        }
    }

    fun resetCloneStatus() {
        _cloneStatus.value = CloneStatus.None
    }

}

sealed class CloneStatus {
    object None : CloneStatus()
    data class Cloning(val progress: Int) : CloneStatus()
    object CheckingOut : CloneStatus()
    data class Fail(val reason: String) : CloneStatus()
    object Completed : CloneStatus()
}