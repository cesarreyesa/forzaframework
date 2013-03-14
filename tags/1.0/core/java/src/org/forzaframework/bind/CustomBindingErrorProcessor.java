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

package org.forzaframework.bind;

import org.springframework.validation.DefaultBindingErrorProcessor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.beans.PropertyAccessException;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 9:16:45
 */
public class CustomBindingErrorProcessor extends DefaultBindingErrorProcessor {

    /**
     * Busca en la causa de la excepcion para mostrar un mejor mensaje de error.
     *
     * @param ex exception
     * @param bindingResult Binding Result
     */
    public void processPropertyAccessException(PropertyAccessException ex, BindingResult bindingResult) {
		// Create field error with the exceptions's code, e.g. "typeMismatch".
		String field = ex.getPropertyChangeEvent().getPropertyName();
		Object value = ex.getPropertyChangeEvent().getNewValue();
		String[] codes = bindingResult.resolveMessageCodes(ex.getErrorCode(), field);
		Object[] arguments = getArgumentsForBindError(bindingResult.getObjectName(), field);
        String message;
        if(ex.getMessage().startsWith("Failed to convert property")){
            message = ex.getCause() != null ? ex.getCause().getLocalizedMessage() : ex.getLocalizedMessage();
        }
        else {
            message = ex.getLocalizedMessage();
        }

        bindingResult.addError(new FieldError(
				bindingResult.getObjectName(), field, value, true,
				codes, arguments, message));
	}

}
