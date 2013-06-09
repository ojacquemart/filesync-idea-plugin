package org.filesync.idea.plugin;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.messages.MessageBusConnection;
import org.filesync.idea.plugin.files.FilesCollector;
import org.jetbrains.annotations.NotNull;

public class FileSyncPlugin implements ProjectComponent, ApplicationComponent {

    private static final String PLUGIN_NAME = "FileSyncPlugin";
    public static final String PLUGIN_DISPLAY_NAME = "File Synchronization";

    private FilesCollector collector;
    private MessageBusConnection connection;
    private Project project;

    public FileSyncPlugin(Project project) {
        this.project = project;
        this.connection = ApplicationManager.getApplication().getMessageBus().connect();
        this.collector = new FilesCollector();
    }

    public void initComponent() {
        connection.subscribe(VirtualFileManager.VFS_CHANGES, collector);
    }

    public void disposeComponent() {
        connection.disconnect();
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    @NotNull
    public String getComponentName() {
        return PLUGIN_NAME + "Component";
    }

}
