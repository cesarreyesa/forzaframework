package org.forzaframework.web.servlet.view;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 12:32:22
 */

public class XmlView extends ModelAndView {

    public XmlView() {
        super(createView(""));
    }

    public XmlView(String xml) {
        super(createView(xml));
    }

    private static AbstractView createView(final String xml) {
        return new AbstractView() {
            protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text/xml");
                response.getWriter().write(xml);
            }
        };
    }
}