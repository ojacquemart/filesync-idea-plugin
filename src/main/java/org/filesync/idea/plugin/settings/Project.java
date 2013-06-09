package org.filesync.idea.plugin.settings;

public class Project {
    private String name;
    private String source;
    private String target;

    public Project(String name, String source, String target) {
        this.name = name;
        this.source = source;
        this.target = target;
    }

    public Project() {
    }

    /**
     * Returns an empty project with a foo name to force input.
     */
    public static Project empty() {
        Project project = new Project();
        project.name = "???";

        return project;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(final String target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (!name.equals(project.name)) return false;
        if (source != null ? !source.equals(project.source) : project.source != null) return false;
        if (target != null ? !target.equals(project.target) : project.target != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

}