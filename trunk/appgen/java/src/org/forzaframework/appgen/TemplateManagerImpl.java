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


import java.util.ArrayList;
import java.util.List;

/**
 * User: Cesar Reyes
 * Date: 28/12/2006
 * Time: 04:51:07 PM
 * Description:
 */
public class TemplateManagerImpl implements TemplateManager {

    public List<Template> getTemplates() {
        List<Template> templates = new ArrayList<Template>();

        Template template;
// Data
        templates.add(new Template("data/", "${class.name}Dao.java", "${daoRoot}", false, TemplateType.DATA));
        templates.add(new Template("data/", "${class.name}DaoHibernate.java", "${daoRoot}hibernate/", false, TemplateType.DATA));
        templates.add(new Template("data/", "app-ctx-hibernate.xml", "${webRoot}/WEB-INF/app-ctx-hibernate.xml", true, "<!-- Add new Daos here -->", TemplateType.DATA));
        template = new Template("data/", "hibernate-mappings.xml", "${webRoot}/WEB-INF/app-ctx-hibernate.xml", true, "<!-- Add additional mappings here -->", TemplateType.DATA);
        template.setTemplateStart("<!--${class.name}-START-->");
        template.setTemplateEnd("<!--${class.name}-END-->");
        templates.add(template);

// Service
        templates.add(new Template("service/", "${class.name}Manager.java", "${serviceRoot}", false, TemplateType.SERVICE));
        templates.add(new Template("service/", "${class.name}ManagerImpl.java", "${serviceRoot}impl/", false, TemplateType.SERVICE));
        templates.add(new Template("service/", "app-ctx-service.xml", "${webRoot}/WEB-INF/app-ctx-service.xml", true, "<!-- Add new Managers here -->", TemplateType.SERVICE));
        template = new Template("service/", "validation.xml", "${webRoot}/WEB-INF/validation.xml", true, "<!-- Add additional validations here -->", TemplateType.SERVICE);
        template.setTemplateStart("<!--${class.name}-START-->");
        template.setTemplateEnd("<!--${class.name}-END-->");
        templates.add(template);
        template = new Template("service/", "ApplicationResources.properties", "${webRoot}/WEB-INF/classes/ApplicationResources.properties", true, "# -- Add new Translations here", TemplateType.SERVICE);
        template.setTemplateStart("# -- ${class.name}-START");
        template.setTemplateEnd("# -- ${class.name}-END");
        templates.add(template);

// View
        template = new Template("view/", "${class.name}FormController.java", "${controllerRoot}", false, TemplateType.VIEW);
        template.setLayoutStyles(getAllLayoutStyles());
        templates.add(template);

        template = new Template("view/", "${class.name.toPlural()}Controller.java", "${controllerRoot}", false, TemplateType.VIEW);
        template.setLayoutStyles(getAllLayoutStyles());
        templates.add(template);

        template = new Template("view/", "${class.name.toPlural()}ListController.java", "${controllerRoot}", false, TemplateType.VIEW);
        template.setLayoutStyles(getAllLayoutStyles());
        templates.add(template);

        template = new Template("view/", "${class.name.toPlural()}XmlView.java", "${controllerRoot}export/", false, TemplateType.VIEW);
        template.setLayoutStyles(getAllLayoutStyles());
        templates.add(template);

        template = new Template("view/", "app-ctx-controllers.xml", "${webRoot}/WEB-INF/app-ctx-controllers-${module.name}.xml", true, "<!-- Add additional controller beans here -->", TemplateType.VIEW);
        template.setLayoutStyles(getAllLayoutStyles());
        templates.add(template);

//        table form ajax
        template = new Template("view/table-form-ajax/", "${class.name.toLowerCase()}Form.jsp", "${webRoot}WEB-INF/pages/", false, TemplateType.VIEW);
        template.addLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        templates.add(template);

        template = new Template("view/table-form-ajax/", "${class.name.toPluralFirstToLower()}.jsp", "${webRoot}WEB-INF/pages/", false, TemplateType.VIEW);
        template.addLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        templates.add(template);
//        table form ajax

//        table form
        template = new Template("view/table-form/", "${class.name.toLowerCase()}Form.jsp", "${webRoot}WEB-INF/pages/", false, TemplateType.VIEW);
        template.addLayoutStyle(LayoutStyle.TABLE_FORM);
        templates.add(template);

        template = new Template("view/table-form/", "${class.name.toPluralFirstToLower()}.jsp", "${webRoot}WEB-INF/pages/", false, TemplateType.VIEW);
        template.addLayoutStyle(LayoutStyle.TABLE_FORM);
        templates.add(template);
//        table form

//        child table form
        template = new Template("view/child-table-form/", "${class.name.toLowerCase()}Form.jsp", "${webRoot}WEB-INF/pages/", false, TemplateType.VIEW);
        template.addLayoutStyle(LayoutStyle.CHILD_TABLE_FORM);
        templates.add(template);

        template = new Template("view/child-table-form/", "${class.name.toPluralFirstToLower()}.jsp", "${webRoot}WEB-INF/pages/", false, TemplateType.VIEW);
        template.addLayoutStyle(LayoutStyle.CHILD_TABLE_FORM);
        templates.add(template);
//        child table form

        template = new Template("view/", "url-mappings.xml", "${webRoot}/WEB-INF/app-ctx-controllers-${module.name}.xml", true, "<!-- Add additional URL mappings here -->", TemplateType.VIEW);
        template.setTemplateStart("<!--${class.name}-URL-START-->");
        template.setTemplateEnd("<!--${class.name}-URL-END-->");
        template.setLayoutStyles(getAllLayoutStyles());
        templates.add(template);

        template = new Template("view/", "views.xml", "${webRoot}/WEB-INF/views.xml", true, "<!-- Add new Views here -->", TemplateType.VIEW);
        template.setLayoutStyles(getAllLayoutStyles());
        templates.add(template);

        template = new Template("view/", "menu.jsp", "${webRoot}/WEB-INF/pages/menu.jsp", true, "<!-- Add additional menu links here -->", TemplateType.VIEW);
        template.setTemplateStart("<!--${class.name}-START-->");
        template.setTemplateEnd("<!--${class.name}-END-->");
        template.addLayoutStyle(LayoutStyle.FORM);
        template.addLayoutStyle(LayoutStyle.PARENT_SHUTTLE);
        template.addLayoutStyle(LayoutStyle.SELECT_FORM);
        template.addLayoutStyle(LayoutStyle.TABLE);
        template.addLayoutStyle(LayoutStyle.TABLE_FORM);
        template.addLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        template.addLayoutStyle(LayoutStyle.TREE);
        template.addLayoutStyle(LayoutStyle.TREE_FORM);
        templates.add(template);

        return templates;
    }

    private List<LayoutStyle> getAllLayoutStyles() {
        List<LayoutStyle> list = new ArrayList<LayoutStyle>();
        list.add(LayoutStyle.FORM);
        list.add(LayoutStyle.PARENT_SHUTTLE);
        list.add(LayoutStyle.SELECT_FORM);
        list.add(LayoutStyle.TABLE);
        list.add(LayoutStyle.TABLE_FORM);
        list.add(LayoutStyle.TABLE_FORM_AJAX);
        list.add(LayoutStyle.TREE);
        list.add(LayoutStyle.TREE_FORM);
        list.add(LayoutStyle.CHILD_TABLE);
        list.add(LayoutStyle.CHILD_TABLE_FORM);
        return list;
    }
}
