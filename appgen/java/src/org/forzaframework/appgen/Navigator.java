package org.forzaframework.appgen;


public class Navigator {

    private Exporter exporter = null;

    public Navigator(Exporter exporter) {
        this.exporter = exporter;
    }

    public void start() throws Exception {

        exporter.initialize();

        // Entity navigation
        Project project = exporter.getProject();
        for (Module module : project.getModules()) {
            for (Entity entity : module.getEntities()) {
                if (!entity.isEnabled()) continue;

                exporter.startClass(entity);

                // Attribute navigation
                for (Attribute attribute : entity.getAttributes()) {
                    if (attribute instanceof Association) {
                        exporter.startAssociation((Association) attribute);
                        exporter.endAssociation((Association) attribute);
                    }
                }

//            // Associtaion Navigation
//            for (IOMAssociation association : cl.getMyAssociations()) {
//                exporter.startAssociation(association, cl);
//                exporter.endAssociation(association, cl);
//            }
//
                exporter.endClass(entity);
            }
        }

        exporter.finalize();
    }

}