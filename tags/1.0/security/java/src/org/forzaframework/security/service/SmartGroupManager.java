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

package org.forzaframework.security.service;

import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.forzaframework.security.SmartGroup;
import org.forzaframework.security.User;

import java.util.List;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 16:28:09
 */

public interface SmartGroupManager {

	void save(SmartGroup smartGroup);

	List getEntities(SmartGroup smartGroup, Class clazz);

    void applySmartGroup(SmartGroup smartGroup);

    List<SmartGroup> getSmartGroupsForUser(User user);

    List<User> getUsersNotInSmartGroup(SmartGroup smartGroup);

    List<User> getSmartGroupUsers(SmartGroup smartGroup);

    Boolean isInSmartGroup(ObjectIdentity objectIdentity, SmartGroup aclPermission);

    Boolean isInSmartGroup(ObjectIdentity objectIdentity, List<SmartGroup> aclPermissions);
}
