package ${class.parentPackageName}.webapp.action;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ${class.name.toPlural()}ListController implements Controller {

   public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
       String format = "Xml";
       if (request.getParameter("format") != null) {
           format = request.getParameter("format");
       }
       return new ModelAndView("${class.name.toPluralFirstToLower()}" + format);
   }
}
