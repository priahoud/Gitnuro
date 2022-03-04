package app.extensions

import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import app.theme.addFile
import app.theme.modifyFile
import org.eclipse.jgit.diff.DiffEntry

val DiffEntry.parentDirectoryPath: String
    get() {
        val path = if (this.changeType == DiffEntry.ChangeType.DELETE) {
            this.oldPath
        } else
            this.newPath

        val pathSplit = path.split("/").toMutableList()
        pathSplit.removeLast()

        val directoryPath = pathSplit.joinToString("/")

        return if (directoryPath.isEmpty())
            ""
        else
            "${directoryPath}/"
    }

val DiffEntry.fileName: String
    get() {
        val path = if (this.changeType == DiffEntry.ChangeType.DELETE) {
            this.oldPath
        } else
            this.newPath

        val pathSplit = path.split("/")

        return pathSplit.lastOrNull() ?: ""
    }

val DiffEntry.filePath: String
    get() {
        val path = if (this.changeType == DiffEntry.ChangeType.DELETE) {
            this.oldPath
        } else
            this.newPath

        return path
    }

val DiffEntry.icon: ImageVector
    get() {
        return when (this.changeType) {
            DiffEntry.ChangeType.ADD -> Icons.Default.Add
            DiffEntry.ChangeType.MODIFY -> Icons.Default.Edit
            DiffEntry.ChangeType.DELETE -> Icons.Default.Delete
            DiffEntry.ChangeType.COPY -> Icons.Default.Add
            DiffEntry.ChangeType.RENAME -> Icons.Default.Refresh
            else -> throw NotImplementedError("Unexpected ChangeType")
        }
    }

val DiffEntry.iconColor: Color
    @Composable
    get() {
        return when (this.changeType) {
            DiffEntry.ChangeType.ADD -> MaterialTheme.colors.addFile
            DiffEntry.ChangeType.MODIFY -> MaterialTheme.colors.modifyFile
            DiffEntry.ChangeType.DELETE -> MaterialTheme.colors.error
            DiffEntry.ChangeType.COPY -> MaterialTheme.colors.addFile
            DiffEntry.ChangeType.RENAME -> MaterialTheme.colors.modifyFile
            else -> throw NotImplementedError("Unexpected ChangeType")
        }
    }