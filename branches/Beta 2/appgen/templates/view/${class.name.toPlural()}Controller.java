package ${class.parentPackageName}.webapp.action;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ${class.name.toPlural()}Controller implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("${class.module.webFolder}${class.name.toPluralFirstToLower()}");
    }
}
