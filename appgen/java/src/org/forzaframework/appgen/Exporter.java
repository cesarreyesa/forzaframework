package org.forzaframework.appgen;

public interface Exporter {
    public void initialize() throws Exception;

    public void startClass(Entity entity) throws Exception;

    public void endClass(Entity entity) throws Exception;

    public void finalize() throws Exception;

    public Project getProject();

    public void startAssociation(Association attribute);

    public void endAssociation(Association attribute);

    void generate() throws Exception;
}