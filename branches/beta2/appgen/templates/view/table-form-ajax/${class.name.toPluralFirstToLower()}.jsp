<%@ include file="/common/taglibs.jsp" %>
#set($blank = "")
<script type="text/javascript">

    ${class.module.jsNamespace}.${class.name} = function(){
        var grid, formPanel, editPanel, searchPanel;
        var refreshPage = function(){
            grid.getDataSource().load();
        };
        var edit = function(){
            editPanel.show();
            if (grid.getSelectionModel().getSelected()) {
                Ext.get('${class.name.toLowerCase()}Edit').show();
                updatePanel(editPanel.id, '<c:url value="/${class.module.webFolder}${class.name.toLowerCase()}Form.html"/>?id=' + grid.getSelectionModel().getSelected().id);
            }
        }
        return {
            init : function(){
                formPanel = new YAHOO.widget.Module('${class.name.toLowerCase()}FormPanel');
                editPanel = new YAHOO.widget.Module('${class.name.toLowerCase()}EditPanel');
                searchPanel = new YAHOO.widget.Module('${class.name.toLowerCase()}SearchPanel');
//                var keyMap = new Ext.KeyMap(formPanel, [{ key: [10,13], fn: this.saveAdd, scope: formPanel },
//                                                          { key: 27, fn: formPanel.hide, scope: formPanel }], "keyup");
            },
            setGrid : function(_grid) { grid = _grid; },
            showSearch: function(){
                Ext.get(searchPanel.id).toggle();
            },
            add : function() {
                updatePanel(formPanel.id, '<c:url value="/${class.module.webFolder}${class.name.toLowerCase()}Form.html"/>#if( ${parent})?${parent.name.toLowerCase()}=$${blank}{param.${parent.name.toLowerCase()}}#end', function(){
#foreach( $att in ${class.attributes})
#if($velocityCount == 1)
                    Ext.get('${class.name.toLowerCase()}.${att.name.toLowerCase()}').focus();
#end
#end
                });
            },
            saveAdd : function() {
                doAjaxSubmit('validate', $('${class.name.toLowerCase()}Form'), $('${class.name.toLowerCase()}-save-btn'), null, function(){
                    Ext.get('${class.name.toLowerCase()}Edit').hide();
                    formPanel.hide();
                    grid.getDataSource().load();                    
                });
            },
            saveEdit : function() {
                doAjaxSubmit('validate', $('${class.name.toLowerCase()}FormEdit'), $('${class.name.toLowerCase()}-save-btn'), null, function(){
                    grid.getDataSource().load();
                });
            },
            delete${class.name} : function() {
                if(grid.getSelectionModel().getSelected()){
                    deleteObject(grid.getSelectionModel().getSelected().id, '<c:url value="/${class.module.webFolder}${class.name.toLowerCase()}Form.html"/>', refreshPage);
                }
            },
            handleSelection : function(){
                edit();
            },
            closeForm : function() {
                formPanel.hide();
            }
        };
    }();

    Ext.onReady(${class.module.jsNamespace}.${class.name}.init, ${class.module.jsNamespace}.${class.name}, true);;

</script>
<n:collapsablePanel id="${class.name.toPluralFirstToLower()}" titleKey="${class.name.toLowerCase()}.list.title">
    <n:toolbar>
        <n:toolbarButton textKey="${class.name.toLowerCase()}.new" className="x-btn-text-icon add-btn" handler="${class.module.jsNamespace}.${class.name}.add" />
        <n:toolbarButton textKey="button.delete" className="x-btn-text-icon delete-btn" handler="${class.module.jsNamespace}.${class.name}.delete${class.name}" />
        <n:toolbarSeparator />
        <n:toolbarButton textKey="button.search" className="x-btn-text-icon search-btn" handler="${class.module.jsNamespace}.${class.name}.showSearch" />
    </n:toolbar>
    <div id="${class.name.toLowerCase()}SearchPanel" style="display:none;">
        <div class="hd"></div>
        <div class="bd">
            <select id="field" name="field">
#foreach( $att in ${class.attributes})
                <option value="${att.name.toLowerCase()}"><fmt:message key="${class.name.toLowerCase()}.${att.name.toLowerCase()}"/></option>
#end
            </select>
            &nbsp;
            <input type="text" id="criteria" name="criteria" size="15" value="${param.criteria}" onkeypress="_setBasicSearch();" >
            <a href="javascript:basicSearch();"><img align="middle" src="<c:url value='/images/app_icons/search_16.gif'/>" alt="<fmt:message key="button.search" />" title="<fmt:message key="button.search"/>"></a>
        </div>
        <div class="ft"></div>
    </div>
    <div id="${class.name.toLowerCase()}FormPanel" style="display:none;">
        <div id="${class.name.toLowerCase()}FormPanel-bd" class="bd"></div>
        <div class="ft form-buttons">
            <a class="save-btn" href="#" id="${class.name.toLowerCase()}-save-btn" onclick="${class.module.jsNamespace}.${class.name}.saveAdd();"><fmt:message key="button.save" /></a>
            <a class="cancel-btn" href="#" onclick="${class.module.jsNamespace}.${class.name}.closeForm();"><fmt:message key="button.cancel" /></a>
        </div>
    </div>
    <n:grid id="${class.name.toLowerCase()}" jsNamespace="${class.module.jsNamespace}.${class.name}" url="${ctx}/${class.module.webFolder}${class.name.toPluralFirstToLower()}List.html?format=Xml#if( ${parent})&${parent.name.toLowerCase()}=$${blank}{param.${parent.name.toLowerCase()}}#end">
#foreach( $att in ${class.attributes})
        <n:column titleKey="${class.name.toLowerCase()}.${att.name.toLowerCase()}" field="${att.name.toLowerCase()}" />
#end
    </n:grid>
</n:collapsablePanel>
<n:collapsablePanel id="${class.name.toLowerCase()}Edit" titleKey="${class.name.toLowerCase()}.edit.title" collapsed="true" visible="false">
    <n:toolbar>
        <n:toolbarButton textKey="button.save" className="x-btn-text-icon save-btn" handler="${class.module.jsNamespace}.${class.name}.saveEdit" />
    </n:toolbar>
    <div id="${class.name.toLowerCase()}EditPanel" style="display:none;">
        <div id="${class.name.toLowerCase()}EditPanel-bd" class="bd"></div>
    </div>
</n:collapsablePanel>
