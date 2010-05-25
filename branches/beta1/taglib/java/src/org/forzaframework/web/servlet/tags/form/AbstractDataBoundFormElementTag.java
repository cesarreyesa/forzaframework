/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.web.servlet.tags.form;

import org.forzaframework.web.servlet.tags.BaseBodyTag;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.core.Conventions;
import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.servlet.tags.EditorAwareTag;
import org.springframework.web.servlet.tags.NestedPathTag;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.beans.PropertyEditor;

/**
 * User: Cesar Reyes
 * Date: 11/06/2007
 * Time: 05:42:16 PM
 * Description:
 */
/**
 * Base tag for all data-binding aware JSP form tags.
 *
 * <p>Provides the common {@link #setPath path} and {@link #setId id} properties.
 * Provides sub-classes with utility methods for accessing the {@link BindStatus}
 * of their bound value and also for
 * with the {@link org.springframework.web.servlet.tags.form.TagWriter}.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 */
public abstract class AbstractDataBoundFormElementTag extends BaseBodyTag implements EditorAwareTag {

    private static final String MODEL_ATTRIBUTE = "modelAttribute";
    static final String MODEL_ATTRIBUTE_VARIABLE_NAME =
            Conventions.getQualifiedAttributeName(FormTag.class, MODEL_ATTRIBUTE);
	/**
	 * Name of the exposed path variable within the scope of this tag: "nestedPath".
	 * Same value as {@link org.springframework.web.servlet.tags.NestedPathTag#NESTED_PATH_VARIABLE_NAME}.
	 */
	protected static final String NESTED_PATH_VARIABLE_NAME = NestedPathTag.NESTED_PATH_VARIABLE_NAME;


	/**
	 * The property path from the {@link org.springframework.web.servlet.tags.form.FormTag#setModelAttribute form object}.
	 */
	private String path;

	/**
	 * The {@link BindStatus} of this tag.
	 */
	private BindStatus bindStatus;


	/**
	 * Set the property path from the {@link org.springframework.web.servlet.tags.form.FormTag#setModelAttribute form object}.
	 * May be a runtime expression. Required.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get the {@link #evaluate resolved} property path for the
	 * {@link org.springframework.web.servlet.tags.form.FormTag#setModelAttribute form object}.
	 */
	protected final String getPath() throws JspException {
		String resolvedPath = (String) evaluate("path", this.path);
		return (resolvedPath != null ? resolvedPath : "");
	}

	/**
	 * Autogenerate the '<code>id</code>' attribute value for this tag.
	 * <p>The default implementation simply delegates to {@link #getName}.
	 */
	protected String autogenerateId() throws JspException {
		return getName();
	}

	/**
	 * Get the value for the HTML '<code>name</code>' attribute.
	 * <p>The default implementation simply delegates to
	 * {@link #getPropertyPath()} to use the property path as the name.
	 * For the most part this is desirable as it links with the server-side
	 * expectation for databinding. However, some subclasses may wish to change
	 * the value of the '<code>name</code>' attribute without changing the bind path.
	 * @return the value for the HTML '<code>name</code>' attribute
	 */
	protected String getName() throws JspException {
		return getPropertyPath();
	}

	/**
	 * Get the bound value.
	 * @see #getBindStatus()
	 */
	protected final Object getBoundValue() throws JspException {
        try{
            return getBindStatus().getValue();
        }catch(NotReadablePropertyException ignored){
            return null;
        }
	}

    /**
     * Get the actual value.
     * @see #getActualValue()
     */
    protected final Object getActualValue() throws JspException {
        try {
            return getBindStatus().getActualValue();
        } catch (JspException e) {
            return null;
        }
    }

	/**
	 * Get the {@link PropertyEditor}, if any, in use for value bound to this tag.
	 */
	protected PropertyEditor getPropertyEditor() throws JspException {
		return getBindStatus().getEditor();
	}

	/**
	 * Get the {@link BindStatus} for this tag.
	 */
	protected BindStatus getBindStatus() throws JspException {
		if (this.bindStatus == null) {
			// HTML escaping in tags is performed by the ValueFormatter class.
			String nestedPath = getNestedPath();
			String pathToUse = (nestedPath != null ? nestedPath + getPath() : getPath());
			this.bindStatus = new BindStatus(getRequestContext(), pathToUse, false);
		}
		return this.bindStatus;
	}

	/**
	 * Get the value of the nested path that may have been exposed by the
	 * {@link NestedPathTag}.
	 */
	protected String getNestedPath() {
		return (String) this.pageContext.getAttribute(NESTED_PATH_VARIABLE_NAME, PageContext.REQUEST_SCOPE);
	}

	/**
	 * Build the property path for this tag, including the nested path
	 * but <i>not</i> prefixed with the name of the form attribute.
	 * @see #getNestedPath()
	 * @see #getPath()
	 */
	protected String getPropertyPath() throws JspException {
		return getBindStatus().getExpression();
	}

	public PropertyEditor getEditor() throws JspException {
		return getBindStatus().getEditor();
	}

	/**
	 * Disposes of the {@link BindStatus} instance.
	 */
	public void doFinally() {
		this.bindStatus = null;
	}

    protected Object evaluate(String attributeName, Object value) throws JspException {
        if (value instanceof String) {
            return ExpressionEvaluationUtils.evaluate(attributeName, (String) value, this.pageContext);
        }
        else {
            return value;
        }
    }

    public String getCommandName() {
        return (String) this.pageContext.getAttribute(MODEL_ATTRIBUTE_VARIABLE_NAME, PageContext.REQUEST_SCOPE);
    }

}
