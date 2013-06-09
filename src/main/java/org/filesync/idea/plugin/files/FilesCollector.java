package org.filesync.idea.plugin.files;

import com.intellij.history.LocalHistory;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FilesCollector implements BulkFileListener {

    public void before(@NotNull List<? extends VFileEvent> vFileEvents) {
        for (VFileEvent event : vFileEvents) {
            final Object requestor = event.getRequestor();
            if (requestor instanceof FileDocumentManager ||
                    requestor instanceof PsiManager ||
                    requestor == LocalHistory.VFS_EVENT_REQUESTOR) {
                FilesSynchronyzer.sync();
            }
        }

    }

    public void after(@NotNull List<? extends VFileEvent> vFileEvents) {
    }

}
