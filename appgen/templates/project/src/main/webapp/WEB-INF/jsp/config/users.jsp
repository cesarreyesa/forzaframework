<%@ include file="/common/taglibs.jsp" %>

<%--
  ~ Copyright 2006-2009 the original author or authors.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~       http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<n:grid id="users-grid" titleKey="user.list.title" url="${ctx}/config/users/xml" enableFilter="true" replacePanel="System.getCenter()" border="true"
        autoHeight="true"
        onRowDblClick="function(grid){
            var url = '${ctx}/config/users/xml?username=' + grid.getSelectionModel().getSelected().get('username') + '&id=' + grid.getSelectionModel().getSelected().id;
            Ext.getCmp('user-window').show();
            Ext.getCmp('user-form').form.load({url:url});
        }">

    <n:toolbar type="top">
        <n:button textKey="user.new" handler="function(){
            Ext.getCmp('user-window').show();
            Ext.getCmp('user-form').form.reset();
        }" />
        <n:button text="Permisos" handler="function(){
            if(Ext.getCmp('users-grid').selModel.getSelected()){
                Ext.getCmp('user-permissions').show();
                Ext.getCmp('roles-assigned').store.reload({url:'${ctx}/config/userActions.html?m=getRolesForUser&id=' + Ext.getCmp('users-grid').selModel.getSelected().id});
                Ext.getCmp('roles-not-assigned').store.reload({url:'${ctx}/config/userActions.html?m=getAvailableRolesForUser&id=' + Ext.getCmp('users-grid').selModel.getSelected().id});
            }
        }" />
        <n:button textKey="button.delete" handler="function(){
            if(grid.getSelectionModel().getSelected()){
                doDelete(grid.getSelectionModel().getSelected().id, '${ctx}/config/userForm.html', function(){
                    Ext.getCmp('users-grid').store.reload();
                });
            }
        }" />
    </n:toolbar>

    <n:column field="id" hidden="true"/>
    <n:column field="username" titleKey="user.username"/>
    <n:column field="firstName" titleKey="user.firstName"/>
    <n:column field="lastName" titleKey="user.lastName"/>
    <n:column field="email" titleKey="user.email"/>
    <n:column field="preferredLocale" titleKey="user.preferredLocale"/>

    <n:column field="enabled" hidden="true"/>
    <n:column field="accountExpired" hidden="true"/>
    <n:column field="accountLocked" hidden="true"/>
    <n:column field="credentialsExpired" hidden="true"/>
</n:grid>

<n:window id="user-window" title="User" width="550" height="350" show="false">

    <n:button textKey="button.save" handler="function(){
        Ext.getCmp('user-form').form.submit({success:function(){
            Ext.getCmp('users-grid').store.reload();
            Ext.getCmp('user-window').hide();        
        }});
    }"/>
    <n:button textKey="button.cancel" handler="function(){
        Ext.getCmp('user-window').hide();
    }"/>

    <f:form id="user-form" url="${ctx}/config/user/form" labelWidth="120">

        <f:hidden field="id"/>
        <f:hidden field="mode" value="save"/>
        <n:tabPanel anchor="120">

            <n:tab title="General" style="padding:5px;" autoHeight="true" autoScroll="true">
                <f:input field="username" titleKey="user.username"/>
                <f:input field="firstName" titleKey="user.firstName"/>
                <f:input field="lastName" titleKey="user.lastName"/>
                <f:input field="password" titleKey="user.password" inputType="password"/>
                <f:input field="email" titleKey="user.email"/>
                <f:input field="preferredLocale" titleKey="user.preferredLocale"/>
            </n:tab>

            <n:tab title="Cuenta" autoHeight="true" autoScroll="true">
                <f:checkbox field="enabled" titleKey="user.enabled" value="true"/>
                <f:checkbox field="accountExpired" titleKey="user.accountExpired" value="true"/>
                <f:checkbox field="accountLocked" titleKey="user.accountLocked" value="true"/>
                <f:checkbox field="credentialsExpired" titleKey="user.credentialsExpired" value="true"/>
            </n:tab>

        </n:tabPanel>
    </f:form>

</n:window>

<n:window id="user-permissions" title="Permissions" width="550" height="350" show="false">

    <n:button textKey="button.close" handler="function(){
        Ext.getCmp('user-permissions').hide();
    }"/>

    <n:panel layout="border" border="false">

        <n:grid id="roles-assigned" region="west" width="50%" loadOnStart="false" enablePagination="false" autoSizeColumns="true"
                ddGroup="RolesAssigned" enableDragDrop="true" enableDrop="true" dropGroup="Roles"
                onDrop="function(dd, e, data){
                    if(data.grid.id == 'roles-not-assigned'){
                        var ids = '';
                        for(var i=0; i<data.selections.length; i++){
                            ids += data.selections[i].get('name') + ',';
                        }
                        ids = ids.substr(0, ids.length - 1);
                        var url = '${ctx}/config/userActions.html?m=addRoleToUser';
                        Nopal.Ajax.execute(url, {username: Ext.getCmp('users-grid').selModel.getSelected().get('username'), role: ids}, function(){
                            Ext.getCmp('roles-assigned').store.reload();
                            Ext.getCmp('roles-not-assigned').store.reload();
                        });
                    }
                }">
            <n:column field="name" title="Assigned Roles" width="200"/>
        </n:grid>
        <n:grid id="roles-not-assigned" region="center" loadOnStart="false" enablePagination="false" autoSizeColumns="true"
                ddGroup="Roles" enableDragDrop="true" enableDrop="true" dropGroup="RolesAssigned"
                onDrop="function(dd, e, data){
                    if(data.grid.id == 'roles-assigned'){
                        var ids = '';
                        for(var i=0; i<data.selections.length; i++){
                            ids += data.selections[i].get('name') + ',';
                        }
                        ids = ids.substr(0, ids.length - 1);
                        var url = '${ctx}/config/userActions.html?m=removeRoleToUser';
                        Nopal.Ajax.execute(url, {username: Ext.getCmp('users-grid').selModel.getSelected().get('username'), role: ids}, function(){
                            Ext.getCmp('roles-assigned').store.reload();
                            Ext.getCmp('roles-not-assigned').store.reload();
                        });
                    }
                }">
            <n:column field="name" title="Roles Not Assigned" width="200"/>
        </n:grid>

    </n:panel>
</n:window>