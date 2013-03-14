package ${class.parentPackageName}.webapp.action.export;

import ${class.parentPackageName}.model.${class.name};
#if(${parent})
import ${class.parentPackageName}.service.${parent.name}Manager;
import ${class.parentPackageName}.model.${parent.name};
#else
import ${class.parentPackageName}.service.${class.name}Manager;
#end
import com.nopalsoft.webapp.PageInfo;
import com.nopalsoft.util.CollectionUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ${class.name.toPlural()}XmlView extends AbstractView {

#if(${parent})
    private ${parent.name}Manager ${parent.name.toLowerCase()}Manager;
    
    public void set${parent.name}Manager(${parent.name}Manager ${parent.name.toLowerCase()}Manager) {
        this.${parent.name.toLowerCase()}Manager = ${parent.name.toLowerCase()}Manager;
    }
#else
    private ${class.name}Manager ${class.name.toLowerCase()}Manager;

    public void set${class.name}Manager(${class.name}Manager ${class.name.toLowerCase()}Manager) {
        this.${class.name.toLowerCase()}Manager = ${class.name.toLowerCase()}Manager;
    }
#end

    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("items");
        doc.setRootElement(root);

        PageInfo pageInfo = new PageInfo();
        BeanUtils.populate(pageInfo, request.getParameterMap());

#if(${parent})
        ${parent.name} ${parent.name.toLowerCase()} = ${parent.name.toLowerCase()}Manager.get${parent.name}(request.getParameter("${parent.name.toLowerCase()}"));
        List<${class.name}> list = CollectionUtils.convertSetToList(${parent.name.toLowerCase()}.get${class.name.toPlural()}());
#else
        List<${class.name}> list = ${class.name.toLowerCase()}Manager.get${class.name.toPlural()}(request.getParameter("query"));
#end
        List<${class.name}> ${class.name.toPluralFirstToLower()} = CollectionUtils.paginate(list, pageInfo);

        for(${class.name} ${class.name.toLowerCase()} : ${class.name.toPluralFirstToLower()}){
            Element el = root.addElement("item");
            el.addElement("${class.id.name.toLowerCase()}").addText(${class.name.toLowerCase()}.get${class.id.name}().toString());
#foreach( $att in ${class.attributes})
#if( ${att.type} == "String")
            el.addElement("${att.name.toLowerCase()}").addText(${class.name.toLowerCase()}.get${att.name}() == null ? "" : ${class.name.toLowerCase()}.get${att.name}());
#elseif( ${att.type} == "Boolean")
            el.addElement("${att.name.toLowerCase()}").addText(${class.name.toLowerCase()}.is${att.name}() == null ? "" : ${class.name.toLowerCase()}.is${att.name}().toString());
#else
            el.addElement("${att.name.toLowerCase()}").addText(${class.name.toLowerCase()}.get${att.name}() == null ? "" : ${class.name.toLowerCase()}.get${att.name}().toString());
#end            
#end
        }
        root.addElement("totalCount").addText(String.valueOf(list.size()));

        response.setContentType("text/xml");

        response.getWriter().write(doc.asXML());
    }
}
