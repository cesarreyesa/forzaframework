<%--child table form--%>
<%@ include file="/common/taglibs.jsp" %>

<c:set var="formId" value="${class.name.toLowerCase()}Form" />
#set($blank = "")
<c:if test="$${blank}{not empty param.id${blank}}">
    <c:set var="formId" value="${class.name.toLowerCase()}FormEdit" />
#foreach( $association in ${class.associations})
#if( ${association.multiplicity} == "OneToMany")
    <script type="text/javascript">
        updatePanel("${association.entity.name.toPluralFirstToLower()}Panel", '<c:url value="/${class.module.webFolder}${association.entity.name.toPluralFirstToLower()}.html"/>?${class.name.toLowerCase()}=' + ${param.id});
    </script>
#end
#end
</c:if>
<n:collapsablePanel id="${class.name.toLowerCase()}" titleKey="${class.name.toLowerCase()}.detail.title">
    <form:form commandName="${class.name.toLowerCase()}" id="${formId}" method="post" action="${ctx}/${class.module.webFolder}${class.name.toLowerCase()}Form.html" onsubmit="return false;" >
        <input type="hidden" name="mode" value="save"/>
        <form:hidden path="${class.id.name.toLowerCase()}" />
        <form:hidden path="${parent.name.toLowerCase()}" />

        <div class="form">
            <div id="error.${class.name.toLowerCase()}"></div>

#foreach( $att in ${class.attributes})
            <label><fmt:message key="${class.name.toLowerCase()}.${att.name.toLowerCase()}" /></label>
#if( ${att.type} == "String" || ${att.type} == "Integer")
            <form:input id="${class.name.toLowerCase()}.${att.name.toLowerCase()}" path="${att.name.toLowerCase()}" />
            <div id="error.${class.name.toLowerCase()}.${att.name.toLowerCase()}"></div>
#elseif( ${att.type} == "Date")
            <form:input id="${class.name.toLowerCase()}.${att.name.toLowerCase()}" path="${att.name.toLowerCase()}" />
            <img id="${class.name.toLowerCase()}.${att.name.toLowerCase()}Cal" src="<c:url value='/images/app_icons/calendar_16.gif' />" alt="Open Calendar" class="icon">
            <div id="error.${class.name.toLowerCase()}.${att.name.toLowerCase()}"></div>
            <script type="text/javascript">Calendar.setup({ inputField : "${class.name.toLowerCase()}.${att.name.toLowerCase()}", button : "${class.name.toLowerCase()}.${att.name.toLowerCase()}Cal" });</script>
#elseif( ${att.type} == "Boolean")
            <form:checkbox id="${class.name.toLowerCase()}.${att.name.toLowerCase()}" path="${att.name.toLowerCase()}" />
            <div id="error.${class.name.toLowerCase()}.${att.name.toLowerCase()}"></div>
#end
            <br />

#end

#foreach( $association in ${class.associations})
#if( ${association.multiplicity} == "ManyToOne")
#if( !${association.name} == ${parent.name} )
            <label><fmt:message key="${class.name.toLowerCase()}.${association.entity.name.toLowerCase()}" /></label>
            <form:select id="${class.name.toLowerCase()}.${association.entity.name.toLowerCase()}" path="${association.entity.name.toLowerCase()}">
                <form:option value=""/>
                <form:options items="$${blank}{${association.entity.name.toPluralFirstToLower()}}" itemLabel="name" itemValue="id" />
            </form:select>
            <br />
#end
#end
#end
        </div>

#if( ${class.hasOneToManyAssociations()} )
        <c:if test="$${blank}{not empty param.id}">
            <div class="ft form-buttons">
                <a class="save-btn" href="#" id="evaluation-save-btn" onclick="${class.module.jsNamespace}.${class.name}.saveAdd();"><fmt:message key="button.save" /></a>
                <a class="cancel-btn" href="#" onclick="${class.module.jsNamespace}.${class.name}.closeForm();"><fmt:message key="button.cancel" /></a>
            </div
        </c:if>
#end

        </div>
    </form:form>
</n:collapsablePanel>

#if( ${class.hasOneToManyAssociations()} )
#foreach( $association in ${class.associations})
#if( ${association.multiplicity} == "OneToMany")
<c:if test="$${blank}{not empty param.id}">
    <div id="${association.entity.name.toPluralFirstToLower()}Panel">
        <div id="${association.entity.name.toPluralFirstToLower()}Panel-bd"></div>
    </div>
</c:if>
#end
#end
#end