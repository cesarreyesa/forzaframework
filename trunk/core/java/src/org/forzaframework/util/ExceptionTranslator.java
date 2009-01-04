/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.forzaframework.util;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.apache.commons.lang.StringUtils;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.Locale;

/**
 * @author cesarreyes
 *         Date: 12-ago-2008
 *         Time: 15:53:22
 */
public class ExceptionTranslator {

    public static String DUPLICATE_KEY_MESSAGE = "ERROR: duplicate key violates unique constraint";
    public static String FOREING_KEY_MESSAGE = "violates foreign key constraint";


    public static String translate(Exception e){
        return translate(e, null);
    }

    public static String translate(Exception e, Locale locale){

        if(e instanceof DataIntegrityViolationException){
            if(e.getCause() != null){
                if(e.getCause() instanceof BatchUpdateException){
                	return translate(((BatchUpdateException) e.getCause()), locale);
                }
//                else if(e.getCause() instanceof ConstraintViolationException){
//                	ConstraintViolationException cause = (ConstraintViolationException) e.getCause();
//                    if(cause.getCause() instanceof BatchUpdateException){
//                    	return translate(((BatchUpdateException) cause.getCause()), locale);
//                    }
//                    else{
//                        return "El registro ya existe o existe un id o codigo repetido. [constraintName:" + ((ConstraintViolationException) e.getCause()).getConstraintName() + "]\nInner exception:" + ((ConstraintViolationException) e.getCause()).getCause().getMessage();
//                    }
//                }
            }
        }
        else if(e instanceof DeadlockLoserDataAccessException){
            return "Error de concurrencia. Intente de nuevo por favor.";
        }
        else if(e instanceof NullPointerException){
            return "Null pointer exception";
        }
        else if(e instanceof IllegalStateException && e.getCause() != null){
        	return e.getCause().getMessage();
        }
        else if(e instanceof ObjectRetrievalFailureException){
            ObjectRetrievalFailureException orfe = (ObjectRetrievalFailureException) e;
            return "No se encontro la entidad de tipo [" + orfe.getPersistentClassName() + "] con el identificador [" + orfe.getIdentifier() + "]";
        }
        return e.getMessage();
    }

    private static String translate(BatchUpdateException e, Locale locale){

    	if(e.getNextException() != null){
            SQLException nextException = e.getNextException();
            String message = nextException.getMessage();

            if(message.startsWith(DUPLICATE_KEY_MESSAGE)){
//"ERROR: duplicate key violates unique constraint \"key\""
                String key = getKey(message, DUPLICATE_KEY_MESSAGE);
                return key;

            }else if(StringUtils.contains(message, FOREING_KEY_MESSAGE)){
//ERROR: update or delete on "unit" violates foreign key constraint "fk990d15446e76ce6e" on "service_order"
                return message;

            }
            return message;
        }
        else{
            return e.getMessage();
        }
    }

    private static String getKey(String message, String extractFrom){
        String s = StringUtils.substringAfter(message, extractFrom);
        s = StringUtils.replaceChars(s, '\"', ' ');
        return "error." + s.trim();
    }
}
