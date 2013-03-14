package ${class.parentPackageName}.webapp.action;

import ${class.parentPackageName}.model.${class.name};
import ${class.parentPackageName}.service.${class.name}Manager;
import com.nopalsoft.xt.action.SuccessAction;
import com.nopalsoft.webapp.action.BaseFormController;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.xt.ajax.AjaxAction;
#foreach( $association in ${class.getManyToOneAssociations()})
import ${association.entity.module.packageName}.${association.entity.name};
#end
#if( ${parent})
import ${parent.module.packageName}.${parent.name};
#end

#if( ${class.hasManyToOneAssociations()} )
import org.springframework.web.bind.ServletRequestDataBinder;
import com.nopalsoft.webapp.CustomClassEditor;
#end
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ${class.name}FormController extends BaseFormController {

    private ${class.name}Manager ${class.name.toLowerCase()}Manager = null;

    public ${class.name}FormController() {
        setCommandName("${class.name.toLowerCase()}");
        setCommandClass(${class.name}.class);
    }

    public void set${class.name}Manager(${class.name}Manager ${class.name.toLowerCase()}Manager) {
        this.${class.name.toLowerCase()}Manager = ${class.name.toLowerCase()}Manager;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        ${class.name} ${class.name.toLowerCase()};

        if (!StringUtils.isEmpty(id)) {
            ${class.name.toLowerCase()} = ${class.name.toLowerCase()}Manager.get${class.name}(id);
        } else {
            ${class.name.toLowerCase()} = new ${class.name}();

#if( ${parent})
            String ${parent.name.toLowerCase()} = request.getParameter("${parent.name.toLowerCase()}");
            if(StringUtils.isNotBlank(${parent.name.toLowerCase()})) {
                ${class.name.toLowerCase()}.set${parent.name}(new ${parent.name}(Long.valueOf(${parent.name.toLowerCase()})));
            }
#end
        }

        return ${class.name.toLowerCase()};
    }
#if( ${class.hasManyToOneAssociations()} )

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
#foreach( $association in ${class.getManyToOneAssociations()})
        binder.registerCustomEditor(${association.entity.name}.class, new CustomClassEditor<${association.entity.name}>(${association.entity.name}.class));
#end

        super.initBinder(request, binder);
    }

#end
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        ${class.name} ${class.name.toLowerCase()} = (${class.name}) command;
        boolean isNew = (${class.name.toLowerCase()}.getId() == null);
        Locale locale = request.getLocale();
        List<AjaxAction> actions = new ArrayList<AjaxAction>();

        if (request.getParameter("mode").equals("delete")) {
            try {
                ${class.name.toLowerCase()}Manager.remove${class.name}(${class.name.toLowerCase()}.getId().toString());
            } catch (Exception e) {
                errors.reject("error.${class.name.toLowerCase()}", e.getMessage());
                return this.showForm(request, response, errors);
            }

            saveMessage(actions, getText("${class.name.toLowerCase()}.deleted", locale));
            actions.add(new SuccessAction());

        } else if (request.getParameter("mode").equals("save")) {
            try {
                ${class.name.toLowerCase()}Manager.save${class.name}(${class.name.toLowerCase()});
            } catch (Exception e) {
                errors.reject("error.${class.name.toLowerCase()}", e.getMessage());
                return this.showForm(request, response, errors);
            }

            String key = (isNew) ? "${class.name.toLowerCase()}.added" : "${class.name.toLowerCase()}.updated";

            saveMessage(actions, getText(key, locale));
            actions.add(new SuccessAction());
        }

        return new ModelAndView("ajax-action", "ajax-actions", actions);
    }
}
