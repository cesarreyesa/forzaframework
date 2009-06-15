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

package org.forzaframework.appgen.velocity;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.forzaframework.appgen.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

public class VelocityExporter implements Exporter {

    private TemplateManager templateManager;
    private Project project;


    public VelocityExporter(Project project) {
// TODO: Cambiar por IOC
        templateManager = new TemplateManagerImpl();
        this.project = project;
    }

    public void setTemplateManager(TemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    public Project getProject() {
        return project;
    }

    private Entity currentEntity = null;

    public void initialize() throws Exception {
        Properties p = new Properties();
        p.setProperty("resource.loader", "file");
        p.setProperty("file.resource.loader.path", project.getTemplatesPath());
        Velocity.init(p);
    }

    public void generate() throws Exception{
        // Get the modules
        for (Module module : project.getModules()) {

            for (Entity entity : module.getEntities()) {
                // generate only if the entity is enabled 
                if (entity.isEnabled()) {
                    generateEntity(entity);
                }
            }
        }
    }

    private void generateEntity(Entity entity) throws Exception {
        generateEntity(entity, entity.getLayoutStyle());
    }

    private void generateEntity(Entity entity, LayoutStyle layoutStyle) throws Exception {
        generateEntity(entity, layoutStyle, null);
    }

    private void generateEntity(Entity entity, LayoutStyle layoutStyle, Entity parent) throws Exception {

        // Get the templates from the manager
        List<Template> templates = templateManager.getTemplates();

        // Generate Data and Service templates only if there is no parent, if the parent class is not null asume that these templates are already generated
        if(parent == null) {
            if(entity.getGenerateDataLayer()) generateDataTemplates(templates, entity);
            if(entity.getGenerateServiceLayer()) generateServiceTemplates(templates, entity);
        }

        if(entity.getGenerateViewLayer()) {
            generateViewTemplates(templates, entity, layoutStyle, parent);

            for(Association association : entity.getAssociations()){
                // TODO: Change for a property that enable the association generation, instead of checking the layoutStyle
                if(association.getLayoutStyle() != null) {
                    generateEntity(association.getEntity(), association.getLayoutStyle(), entity);
                }
            }
        }
        System.out.println("Entity " + entity.getName() + " generated!");
    }

    private void generateDataTemplates(List<Template> templates, Entity entity) throws Exception {
        for(Template template : templates){
            if(template.getType().equals(TemplateType.DATA)) {
                generateTemplate(entity, template);
            }
        }
    }

    private void generateServiceTemplates(List<Template> templates, Entity entity) throws Exception {
        for(Template template : templates){
            if(template.getType().equals(TemplateType.SERVICE)) {
                generateTemplate(entity, template);
            }
        }
    }

    private void generateViewTemplates(List<Template> templates, Entity entity, LayoutStyle layoutStyle, Entity parent) throws Exception {
        for(Template template : templates){
            if(template.getType().equals(TemplateType.VIEW)) {
                // Generate only if the template supports the Entity layoutStyle
                if(template.supports(layoutStyle)){
                    generateTemplate(entity, template, parent);
                }
            }
        }
    }

    private void generateTemplate(Entity entity, Template template) throws Exception {
        generateTemplate(entity, template, null);
    }

    private void generateTemplate(Entity entity, Template template, Entity parent) throws Exception {

        VelocityContext context = new VelocityContext();
        context.put("class", entity);
        if(parent != null) {
            context.put("parent", parent);
        }

        org.apache.velocity.Template velocityTemplate = Velocity.getTemplate(template.getPath() + template.getFileName());

        if(!template.willMerge()){
                String fileName = project.getSrcRoot() + translatePath(template.getDestinationPath(), entity) + replaceTemplateName(template.getFileName(), entity);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            velocityTemplate.merge(context, writer);
            writer.flush();
            writer.close();
        }
        else{
            String destinationPath = template.getDestinationPath();
            if(destinationPath.contains("${module.name}")){
                if(StringUtils.isNotBlank(entity.getModule().getName())){
                    destinationPath = destinationPath.replace("${module.name}", entity.getModule().getName());
                }else{
                    destinationPath = destinationPath.replace("-${module.name}", "");
                }
            }
            String fileName = project.getSrcRoot() + translatePath(destinationPath);
            StringWriter writer = new StringWriter();
            velocityTemplate.merge(context, writer);

            String fileContent = FileUtils.readFileToString(new File(fileName), null);
            StringBuffer contents = new StringBuffer();

            fileContent = fileContent.replace(replaceTemplateName(template.getTemplateStart(), entity), template.getTemplateStartReplaceText());
            fileContent = fileContent.replace(replaceTemplateName(template.getTemplateEnd(), entity), template.getTemplateEndReplaceText());
            fileContent = fileContent.replaceFirst(template.getMergeDeleteText(), "");
            fileContent = fileContent.replace(template.getMergeReplaceText(), writer.toString());

            contents.append(fileContent);
            FileUtils.writeStringToFile(new File(fileName), contents.toString(), null);
        }
    }

    private String translatePath(String destinationPath) {
        if(destinationPath.contains("${webRoot}")){
            return destinationPath.replace("${webRoot}", project.getWebRoot());
        }
        else if(destinationPath.contains("${daoRoot}")){
            return destinationPath.replace("${daoRoot}", project.getDaoRoot());
        }
        else if(destinationPath.contains("${controllerRoot}")){
            return destinationPath.replace("${controllerRoot}", project.getControllerRoot());
        }
        else if(destinationPath.contains("${serviceRoot}")){
            return destinationPath.replace("${serviceRoot}", project.getServiceRoot());
        }
        return destinationPath;
    }

    private String translatePath(String destinationPath, Entity entity) {
        if(destinationPath.contains("${webRoot}")){
            return destinationPath.replace("${webRoot}", project.getWebRoot()) + entity.getModule().getWebFolder();
        }
        else if(destinationPath.contains("${daoRoot}")){
            return destinationPath.replace("${daoRoot}", project.getDaoRoot() + GeneratorUtility.packageToPath(entity.getModule().getDaoPackage()));
        }
        else if(destinationPath.contains("${serviceRoot}")){
            return destinationPath.replace("${serviceRoot}", project.getServiceRoot() + GeneratorUtility.packageToPath(entity.getModule().getServicePackage()));
        }
        else if(destinationPath.contains("${controllerRoot}")){
            return destinationPath.replace("${controllerRoot}", project.getControllerRoot() + GeneratorUtility.packageToPath(entity.getModule().getControllerPackage()));
        }
        return destinationPath;
    }

    public String replaceTemplateName(String fileName, Entity entity){
        if(fileName.contains("${class.name}")){
            return fileName.replace("${class.name}", entity.getName().toString());
        }
        else if(fileName.contains("${class.name.toPlural()}")){
            return fileName.replace("${class.name.toPlural()}", entity.getName().toPlural());
        }
        else if(fileName.contains("${class.name.toPluralFirstToLower()}")){
            return fileName.replace("${class.name.toPluralFirstToLower()}", entity.getName().toPluralFirstToLower());
        }
        else if(fileName.contains("${class.name.toLowerCase()}")){
            return fileName.replace("${class.name.toLowerCase()}", entity.getName().toLowerCase());
        }
        return fileName;
    }

    public void startClass(Entity entity) throws Exception {
        currentEntity = entity;
    }

    public void endClass(Entity entity) throws Exception {
        generateEntity2(entity);
    }

    private void generateEntity2(Entity entity) throws Exception{
        VelocityContext context = new VelocityContext();
        context.put("class", currentEntity);
        context.put("config", project);

        List<Template> templates = templateManager.getTemplates();

        for(Template template : templates){
            if(template.getType().equals(TemplateType.VIEW)){
                if(!template.supports(entity.getLayoutStyle())){
                    continue;
                }
            }

            org.apache.velocity.Template velocityTemplate = Velocity.getTemplate(template.getPath() + template.getFileName());

            if(!template.willMerge()){
                String fileName = project.getSrcRoot() + translatePath(template.getDestinationPath(), entity) + replaceTemplateName(template.getFileName(), entity);
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

                velocityTemplate.merge(context, writer);
                writer.flush();
                writer.close();
            }
            else{
                String destinationPath = template.getDestinationPath();
                if(destinationPath.contains("${module.name}")){
                    destinationPath = destinationPath.replace("${module.name}", entity.getModule().getName());
                }
                String fileName = project.getSrcRoot() + translatePath(destinationPath);
                StringWriter writer = new StringWriter();
                velocityTemplate.merge(context, writer);

                String fileContent = FileUtils.readFileToString(new File(fileName), null);
                StringBuffer contents = new StringBuffer();

                fileContent = fileContent.replace(replaceTemplateName(template.getTemplateStart(), entity), template.getTemplateStartReplaceText());
                fileContent = fileContent.replace(replaceTemplateName(template.getTemplateEnd(), entity), template.getTemplateEndReplaceText());
                fileContent = fileContent.replaceFirst(template.getMergeDeleteText(), "");
                fileContent = fileContent.replace(template.getMergeReplaceText(), writer.toString());

                contents.append(fileContent);
                FileUtils.writeStringToFile(new File(fileName), contents.toString(), null);
            }
        }
        System.out.println("Entity " + entity.getName() + " generated!");

    }

    public void startAssociation(Association attribute) {

    }

    public void endAssociation(Association attribute) {
    }

    public void finalize() throws Exception {
    }

}