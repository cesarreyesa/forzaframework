package org.forzaframework.web.servlet.view;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 16:17:36
 */
public class CsvView extends ModelAndView {

    public CsvView() {
        super(createView(""));
    }

    public CsvView(String text) {
        super(createView(text));
    }

    public CsvView(String[] lines) {
        super(createView(lines));
    }

    private static AbstractView createView(final String text) {
        return new AbstractView() {
            protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text/csv");
                response.getWriter().write(text);
            }
        };
    }

    private static AbstractView createView(final String[] lines) {
        return new AbstractView() {
            protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text/csv");
                for(String line : lines){
                    response.getWriter().write(line + "\n");
                }
            }
        };
    }

}
