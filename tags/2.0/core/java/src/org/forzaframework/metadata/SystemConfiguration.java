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

package org.forzaframework.metadata;

import java.util.List;
import java.util.ArrayList;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 15:39:39
 */

public class SystemConfiguration {

    private List<SystemEntity> systemEntities = new ArrayList<SystemEntity>();
    private Boolean enableExternalSystems = false;
    private List<ExternalSystem> externalSystems  = new ArrayList<ExternalSystem>();

    public List<SystemEntity> getSystemEntities() {
        return systemEntities;
    }

    public void setSystemEntities(List<SystemEntity> systemEntities) {
        this.systemEntities = systemEntities;
    }

    public Boolean getEnableExternalSystems() {
        return enableExternalSystems;
    }

    public void setEnableExternalSystems(Boolean enableExternalSystems) {
        this.enableExternalSystems = enableExternalSystems;
    }

    public List<ExternalSystem> getExternalSystems() {
        return externalSystems;
    }

    public void setExternalSystems(List<ExternalSystem> externalSystems) {
        this.externalSystems = externalSystems;
    }

    public ExternalSystem getExternalSystem(String code){
        for(ExternalSystem system : externalSystems){
            if(system.getCode().equals(code)){
                return system;
            }
        }
        return null;
    }

    public SystemEntity getSystemEntity(String code){
        for(SystemEntity entity : systemEntities){
            if(entity.getCode().equals(code)){
                return entity;
            }
        }
        return null;
    }

    public SystemEntity getSystemEntity(Class clazz){
        for(SystemEntity entity : systemEntities){
            if(clazz.equals(entity.getType())){
                return entity;
            }
        }
        return null;
    }

    public List<SystemEntity> getDynamicEntities(){
        List<SystemEntity> dynamicEntityies = new ArrayList<SystemEntity>();
        for(SystemEntity entity : systemEntities){
            if(entity.getEntityType() != null && entity.getEntityType().equals("dynamic")){
                dynamicEntityies.add(entity);
            }
        }
        return dynamicEntityies;
    }
}
