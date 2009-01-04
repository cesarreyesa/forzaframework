package org.forzaframework.appgen;

import java.util.ArrayList;
import java.util.List;

/**
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 18:25:18
 * Description:
 */
public class Module {

    private String name;
    private String packageName;
    private String daoPackage;
    private String servicePackage;
    private String controllerPackage;
    private String webFolder;
    private String jsNamespace;
    private List<Entity> entities = new ArrayList<Entity>();

    public Module() {
    }

    public Module(String name, String packageName, String webFolder) {
        this.name = name;
        this.packageName = packageName;
        this.webFolder = webFolder;
    }

    public Module(String name, String packageName, String daoPackage, String servicePackage, String controllerPackage, String webFolder) {
        this.name = name;
        this.packageName = packageName;
        this.daoPackage = daoPackage;
        this.servicePackage = servicePackage;
        this.controllerPackage = controllerPackage;
        this.webFolder = webFolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getParentPackageName() {
        String packageName = getPackageName();
        if (packageName != null)
            return packageName.substring(0, packageName.lastIndexOf("."));
        else
            return "";
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackage = daoPackage;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public String getWebFolder() {
        return webFolder;
    }

    public void setWebFolder(String webFolder) {
        this.webFolder = webFolder;
    }

    public String getJsNamespace() {
        return jsNamespace;
    }

    public void setJsNamespace(String jsNamespace) {
        this.jsNamespace = jsNamespace;
    }
}
