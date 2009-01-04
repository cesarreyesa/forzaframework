package org.forzaframework.appgen;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Cesar Reyes
 * Date: 28/12/2006
 * Time: 04:16:00 PM
 * Description:
 */
public class Project implements Serializable {

    private String projectName;
    private String templatesPath = "/IdeaProjects/appgen/templates";
    private String srcRoot;
    private String webRoot;
    private String daoRoot;
    private String serviceRoot;
    private String controllerRoot;
    private List<Module> modules = new ArrayList<Module>();

    public Project(String projectName) {
        this.projectName = projectName;
        this.webRoot = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public void addModule(Module module) {
        this.modules.add(module);
    }

    public String getTemplatesPath() {
        return templatesPath;
    }

    public void setTemplatesPath(String templatesPath) {
        this.templatesPath = templatesPath;
    }

    public String getSrcRoot() {
        return srcRoot;
    }

    public void setSrcRoot(String srcRoot) {
        this.srcRoot = srcRoot;
    }

    public String getWebRoot() {
        return webRoot;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    public String getDaoRoot() {
        return daoRoot;
    }

    public void setDaoRoot(String daoRoot) {
        this.daoRoot = daoRoot;
    }

    public String getServiceRoot() {
        return serviceRoot;
    }

    public void setServiceRoot(String serviceRoot) {
        this.serviceRoot = serviceRoot;
    }

    public String getControllerRoot() {
        return controllerRoot;
    }

    public void setControllerRoot(String controllerRoot) {
        this.controllerRoot = controllerRoot;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project that = (Project) o;

        if (srcRoot != null ? !srcRoot.equals(that.srcRoot) : that.srcRoot != null) return false;
        if (webRoot != null ? !webRoot.equals(that.webRoot) : that.webRoot != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (srcRoot != null ? srcRoot.hashCode() : 0);
        result = 31 * result + (webRoot != null ? webRoot.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.getClass().toString())
                .toString();
    }
}
