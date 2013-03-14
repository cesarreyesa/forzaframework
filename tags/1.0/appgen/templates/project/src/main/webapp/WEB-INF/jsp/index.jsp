<%@ include file="/common/taglibs.jsp" %>

<%--<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title><fmt:message key="webapp.name"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <link href="${res}/css/ext-all.css" rel="stylesheet">

    <script type="text/javascript" src="${ctx}/static/js/global.js"></script>
    <script type="text/javascript" src="${cdn}/js/ext-base.js"></script>
    <script type="text/javascript" src="${cdn}/js/ext-all.js"></script>
    <%--<script type="text/javascript" src="${cdn}/js/ext-all-debug.js"></script>--%>
    <script type="text/javascript">
        Ext.BLANK_IMAGE_URL="${ctx}/static/images/s.gif";
    </script>

    <script type="text/javascript" src="${cdn}/js/next.js"></script>
    <script type="text/javascript" src="${cdn}/js/ux.js"></script>
</head>

<body>

<n:viewport layout="fit">
    <n:panel layout="border" titleKey="webapp.name">
        <n:toolbar type="top">
            <n:button text="Home" handler=""/>
            <n:menu text="System">
                <n:button text="Usuarios" handler="System.getCenter().load('${ctx}/config/users');"/>
            </n:menu>

            <n:separator alignToRight="true"/>

            <c:if test="${pageContext.request.remoteUser != null}">
                <%--@elvariable id="user_name" type="java.lang.String"--%>
                <n:menu text="${pageContext.request.remoteUser}" iconCls="user-btn">
                    <n:button text="Logout" handler="function(){ document.location.href = '${ctx}/logout'; }"/>
                </n:menu>
            </c:if>
        </n:toolbar>
    	<n:toolbar type="bottom">
            <n:text id="status-bar" text="" type="tbstatusbar"/>
            <n:text id="loader" text="<div id=\"dc\"/>" type="tbtext"/>
    		<n:separator alignToRight="true"/>
    		<n:text text="Version 1.0 &middot; Copyright &copy; 2008 &middot; Nopalsoft"/>
    	</n:toolbar>
	    <n:panel id="center" layout="card" margins="5 0 5 0" region="center" border="false">
	        <n:panel layout="fit" border="true">

	        </n:panel>
	    </n:panel>
    </n:panel>
</n:viewport>

</body>

</html>