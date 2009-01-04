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
