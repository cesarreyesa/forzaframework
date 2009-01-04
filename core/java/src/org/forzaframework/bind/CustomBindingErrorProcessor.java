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
