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

package org.forzaframework.web.servlet;

import org.forzaframework.layout.FileDefinitionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.ModelMap;
import org.forzaframework.metadata.SystemConfiguration;

/**
 * @author cesarreyes
 *         Date: 19-ago-2008
 *         Time: 13:11:01
 */
@Controller
@RequestMapping("/entityList.html")
public class EntityListController extends BaseController {

    private SystemConfiguration systemConfiguration;
    private FileDefinitionManager fileDefinitionManager;
    private String view = "/entityList";

    public void setSystemConfiguration(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    public void setFileDefinitionManager(FileDefinitionManager fileDefinitionManager) {
        this.fileDefinitionManager = fileDefinitionManager;
    }

    public void setView(String view) {
        this.view = view;
    }

    @RequestMapping(method = RequestMethod.GET)
    @SuppressWarnings(value = "unchecked")
    public String processSubmit(@RequestParam("e") String entityName, @RequestParam(value = "disableExternalSystems", required = false) Boolean disableExternalSystems,
                                ModelMap model) throws Exception {
        model.addAttribute("entity", systemConfiguration.getSystemEntity(entityName));
        if (disableExternalSystems == null) disableExternalSystems = false;

        systemConfiguration.setEnableExternalSystems(!disableExternalSystems);
        if(systemConfiguration.getEnableExternalSystems()){
            model.addAttribute("externalSystems", systemConfiguration.getExternalSystems());
        }

        if(fileDefinitionManager!= null){
            model.put("fileDefinitions", fileDefinitionManager.getFileDefinitionsByEntityCode(entityName));
        }
        model.addAttribute("enableExternalSystems", systemConfiguration.getEnableExternalSystems());

        return view;
    }
}