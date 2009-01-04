package org.forzaframework.web.servlet.view;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 12:34:21
 */

public class TextView extends ModelAndView {

    public TextView() {
        super(createView(""));
    }

    public TextView(String text) {
        super(createView(text));
    }

    private static AbstractView createView(final String text) {
        return new AbstractView() {
            protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text");
                response.getWriter().write(text);
            }
        };
    }

}
