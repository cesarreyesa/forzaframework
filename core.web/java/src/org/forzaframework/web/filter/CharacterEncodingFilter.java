package org.forzaframework.web.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author cesarreyes
 *         Date: 15-ago-2008
 *         Time: 17:09:59
 */
public class CharacterEncodingFilter extends OncePerRequestFilter {

	private String encoding;

	private boolean forceEncoding;

	/**
	 * Set the encoding to use for requests. This encoding will be
	 * passed into a ServletRequest.setCharacterEncoding call.
	 * <p>Whether this encoding will override existing request
	 * encodings depends on the "forceEncoding" flag.
	 * @see #setForceEncoding
	 * @see javax.servlet.ServletRequest#setCharacterEncoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Set whether the encoding of this filter should override existing
	 * request encodings. Default is "false", i.e. do not modify encoding
	 * if ServletRequest.getCharacterEncoding returns a non-null value.
	 * @see #setEncoding
	 * @see javax.servlet.ServletRequest#getCharacterEncoding
	 */
	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
	}

	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (this.forceEncoding) {
			request.setCharacterEncoding(this.encoding);
            response.setCharacterEncoding(this.encoding);
        }
//		if (this.forceEncoding || request.getCharacterEncoding() == null) {
//			request.setCharacterEncoding(this.encoding);
//		}
		filterChain.doFilter(request, response);
	}

}
