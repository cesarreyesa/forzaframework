package ${root.package}.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: cesarreyes
 * Date: 26-may-2009
 * Time: 23:27:27
 */

@Controller
public class MainController {

    @RequestMapping("/index")
    public void index(){
        
    }
}
