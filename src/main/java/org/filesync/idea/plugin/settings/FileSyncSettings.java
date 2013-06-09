package org.filesync.idea.plugin.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@State(
        name = "ConfigPersistentService",
        storages = {
            @Storage(file = StoragePathMacros.APP_CONFIG + "/filesync_settings.xml")
        }
)
public class FileSyncSettings implements PersistentStateComponent<Element> {

    private static final Logger LOGGER = Logger.getInstance(FileSyncSettings.class);

    private static final String ELEMENT_ROOT = "ROOT";
    private static final String ELEMENT_PROJECTS = "PROJECTS";
    private static final String ELEMENT_PROJECT = "PROJECT";
    private static final String ELEMENT_PRJ_NAME = "NAME";
    private static final String ELEMENT_PRJ_SOURCE = "SOURCE";
    private static final String ELEMENT_PRJ_TARGET = "TARGET";

    private List<Project> projects = new ArrayList<Project>();

    public static FileSyncSettings getInstance(){
        return ServiceManager.getService(FileSyncSettings.class);
    }

    public Element getState() {
        LOGGER.info("Getting state");

        final Element eltRoot = new Element(ELEMENT_ROOT);

        try {
            Element eltDirectories = new Element(ELEMENT_PROJECTS);
            for (Project directory : projects) {
                if (directory.getName() != null) {
                    LOGGER.info("Adding " + directory);

                    Element eltDir = new Element(ELEMENT_PROJECT);
                    eltDir.setAttribute(ELEMENT_PRJ_NAME, directory.getName());
                    eltDir.setAttribute(ELEMENT_PRJ_SOURCE, directory.getSource());
                    eltDir.setAttribute(ELEMENT_PRJ_TARGET, directory.getTarget());

                    eltDirectories.addContent(eltDir);
                }
            }
            eltRoot.addContent(eltDirectories);
        } catch (Exception e) {
            LOGGER.error("Error during getting state...", e);
        }

        return eltRoot;
    }

    public void loadState(@NotNull Element element) {
        try {
            LOGGER.info("Loading state");

            for (Element projetcts : (List<Element>) element.getChildren(ELEMENT_PROJECTS)) {
                for (Object object : projetcts.getChildren(ELEMENT_PROJECT)) {
                    Element elt = (Element) object;
                    String name = elt.getAttributeValue(ELEMENT_PRJ_NAME);
                    String source = elt.getAttributeValue(ELEMENT_PRJ_SOURCE);
                    String target = elt.getAttributeValue(ELEMENT_PRJ_TARGET);
                    addProject(name, source, target);
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("Error happened while loading github settings: " + e);
        }
    }


    private void addProject(String name, String source, String target) {
        LOGGER.info("Adding dir source " + source + " and target " + target);

        projects.add(new Project(name, source, target));
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void clear() {
        projects = new ArrayList<Project>();
    }


}