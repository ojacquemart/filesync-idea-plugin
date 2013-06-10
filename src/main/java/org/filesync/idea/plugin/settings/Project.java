package org.filesync.idea.plugin.settings;

public class Project {
    private String source;
    private String target;

    public Project(String source, String target) {
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
        project.source = "???";

        return project;
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

        if (!source.equals(project.source)) return false;
        if (target != null ? !target.equals(project.target) : project.target != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + (target != null ? target.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return source;
    }

}