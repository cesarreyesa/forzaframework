package org.forzaframework.web.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.Authentication;
import org.springframework.context.i18n.LocaleContextHolder;
import org.forzaframework.security.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Locale;

/**
 * @author cesarreyes
 *         Date: 15-ago-2008
 *         Time: 17:11:50
 */
/**
 * Filter to wrap request with a request including user preferred locale.
 */
public class LocaleFilter extends OncePerRequestFilter {
    private static final String PREFERRED_LOCALE = "es";

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain)
            throws IOException, ServletException {


        String locale = null;

        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();

        if((auth != null) && (auth.getPrincipal() instanceof User)){
            User user = (User) auth.getPrincipal();
            locale = user.getPreferredLocale();
        }

        Locale preferredLocale = null;

        if (locale != null) {
            preferredLocale = new Locale(locale);
        }

        HttpSession session = request.getSession(false);

        if (session != null) {
            if (preferredLocale == null) {
                preferredLocale = (Locale) session.getAttribute(PREFERRED_LOCALE);
            } else {
                session.setAttribute(PREFERRED_LOCALE, preferredLocale);
                Config.set(session, Config.FMT_LOCALE, preferredLocale);
            }

            if (preferredLocale != null && !(request instanceof LocaleRequestWrapper)) {
                request = new LocaleRequestWrapper(request, preferredLocale);
                LocaleContextHolder.setLocale(preferredLocale);
            }
        }

        chain.doFilter(request, response);

        // Reset thread-bound LocaleContext.
        LocaleContextHolder.setLocaleContext(null);
    }
}
