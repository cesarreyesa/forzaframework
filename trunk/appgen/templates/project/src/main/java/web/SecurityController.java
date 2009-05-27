package ${root.package}.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;
import org.forzaframework.security.service.UserManager;
import org.forzaframework.security.User;
import org.forzaframework.security.Role;
import org.forzaframework.core.persistance.EntityManager;
import org.forzaframework.core.persistance.Restrictions;
import org.forzaframework.core.persistance.Criteria;
import org.forzaframework.util.XmlUtils;
import org.forzaframework.util.CollectionUtils;
import org.forzaframework.util.ExceptionTranslator;
import org.forzaframework.web.servlet.view.XmlView;
import org.forzaframework.web.servlet.view.TextView;
import org.forzaframework.web.servlet.BaseController;
import org.forzaframework.validation.Information;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * User: cesarreyes
 * Date: 26-may-2009
 * Time: 23:02:45
 */

@Controller
public class SecurityController extends BaseController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserManager userManager;


    @RequestMapping("/login")
    public void login(){
    }

    @RequestMapping("/logout")
    public void logout(){        
    }

    @RequestMapping("/config/users")
    public void users(){
    }

    @RequestMapping("/config/users/xml")
    public ModelAndView userList(@RequestParam(value = "format", required = false) String format, WebRequest request){
        Document doc = DocumentHelper.createDocument();

        String id = request.getParameter("username");

        if(StringUtils.isNotBlank(id)){
            User user = userManager.getUser(id);
            Element element = user.toXml();
            Element root = doc.addElement("response");
            root.addAttribute("success", "true");
            root.add(element);
            doc.setRootElement(root);

        }else if(request.getParameter("username") != null){
            XmlUtils.buildEmptyListDocument(doc);
        }else{
            String query = request.getParameter("query") == null ? "" : request.getParameter("query");
            Criteria criteria = new Criteria();
            if(StringUtils.isNotBlank(query)){
                criteria.add(Restrictions.like("username", query));
            }
            List<User> list = entityManager.find(User.class, criteria);
            List<User> users = CollectionUtils.paginate(list, request.getParameterMap());
            doc = XmlUtils.buildDocument(users, list.size());
        }
        return new XmlView(doc.asXML());
    }

    @RequestMapping(value = "/config/user/form", method = RequestMethod.GET)
    public String setupForm(@RequestParam(value = "id", required = false) Long id, @RequestParam("username") String username, ModelMap model) {
        User user;

        if (username != null && id != null) {
            user = userManager.getUser(username);
        } else {
            user = new User();
        }

        model.addAttribute(user);

        return "/config/userForm";
    }

    @RequestMapping(value = "/config/user/form", method = RequestMethod.POST)
    public ModelAndView processSubmit(@ModelAttribute User user, BindingResult result, @RequestParam("mode") String mode){
        if(result.hasErrors()) return processErrors(result);

        Information info = new Information();
        try{
            if(mode != null && mode.equals("delete")){
                userManager.removeUser(user.getUsername());
            }else{
            	if(user.getId() != null){
                    userManager.saveUser(user);
            	}else{
                    Map model = new HashMap();
//                    model.put("baseUrl", configuration.getBaseUrl());
                    userManager.newUser(user, model);
            	}
            }
        }catch(Exception ex){
            info.addError(ExceptionTranslator.translate(ex));
        }
        return new XmlView(info.toXml());
    }

    @RequestMapping(value = "/config/userActions.html", params = "m=getRolesForUser")
    public ModelAndView getRolesForUser(@RequestParam("id") Long id){
        User user = (User) entityManager.get(User.class, id);
        List roles = CollectionUtils.convertSetToList(user.getRoles());
        return new XmlView(XmlUtils.buildDocument(roles).asXML());
    }

    @RequestMapping(value = "/config/userActions.html", params = "m=getAvailableRolesForUser")
    public ModelAndView getAvailableRolesForUser(@RequestParam("id") Long id){
        List roles = entityManager.find("from Role role where role.name not in (select r.name from User u join u.roles r where u.id = ?)", id);
        return new XmlView(XmlUtils.buildDocument(roles).asXML());
    }

    @RequestMapping(value = "/config/userActions.html", params = "m=addRoleToUser")
    public ModelAndView addRoleToUser(@RequestParam("username") String username, @RequestParam("role") String roleName) throws Exception{
        Information info = new Information();
        try {
            User user = userManager.getUser(username);
            Role role = userManager.getRole(roleName);
            user.addRole(role);
            userManager.saveUser(user);
        } catch (Exception ex) {
            info.addError(ExceptionTranslator.translate(ex));
        }
        return new TextView(info.toJSONString());
    }

    @RequestMapping(value = "/config/userActions.html", params = "m=removeRoleToUser")
    public ModelAndView removeRoleToUser(@RequestParam("username") String username, @RequestParam("role") String roleName) throws Exception{
        Information info = new Information();
        try {
            User user = userManager.getUser(username);
            Role role = userManager.getRole(roleName);
            user.getRoles().remove(role);
            userManager.saveUser(user);
        } catch (Exception ex) {
            info.addError(ExceptionTranslator.translate(ex));
        }
        return new TextView(info.toJSONString());
    }

}
