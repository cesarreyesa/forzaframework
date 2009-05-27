<%@ include file="/common/taglibs.jsp" %>

<c:set var="formId" value="${class.name.toLowerCase()}Form" />
#set($blank = "")
<c:if test="$${blank}{not empty param.id${blank}}">
    <c:set var="formId" value="${class.name.toLowerCase()}FormEdit" />
</c:if>
<form:form commandName="${class.name.toLowerCase()}" id="${formId}" method="post" action="${ctx}/${class.module.webFolder}${class.name.toLowerCase()}Form.html" onsubmit="return false;" >
    <input type="hidden" name="mode" value="save"/>
    <form:hidden path="${class.id.name.toLowerCase()}" />
#if( ${parent} )
    <form:hidden path="${parent.name.toLowerCase()}" />
#end

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
#if( !${parent} || !${association.name} == ${parent.name} )
#if( ${association.lovLayoutStyle.toString()} == "DropdownList")
        <label><fmt:message key="${class.name.toLowerCase()}.${association.entity.name.toLowerCase()}" /></label>
        <form:select id="${class.name.toLowerCase()}.${association.entity.name.toLowerCase()}" path="${association.entity.name.toLowerCase()}">
            <form:option value=""/>
            <form:options items="$${blank}{${association.entity.name.toPluralFirstToLower()}}" itemLabel="name" itemValue="id" />
        </form:select>
        <div id="error.${class.name.toLowerCase()}.${association.name.toLowerCase()}"></div>
        <br />

#end
#if( ${association.lovLayoutStyle.toString()} == "AutoComplete")
        <label><fmt:message key="${class.name.toLowerCase()}.${association.entity.name.toLowerCase()}" /></label>
        <n:autoComplete id="${class.name.toLowerCase()}.${association.entity.name.toLowerCase()}" path="${association.entity.name.toLowerCase()}" jsNamespace="${class.module.jsNamespace}.${association.entity.name}AC" url="${ctx}/${association.entity.module.webFolder}${association.entity.name.toPluralFirstToLower()}List.html" />
        <div id="error.${class.name.toLowerCase()}.${association.name.toLowerCase()}"></div>
        <br />

#end
#end
#end
#end

    </div>
</form:form>
