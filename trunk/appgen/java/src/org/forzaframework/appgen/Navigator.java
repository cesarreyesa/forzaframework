/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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