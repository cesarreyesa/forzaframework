package org.forzaframework.layout;

import java.util.List;

/**
 * @author cesarreyes
 *         Date: 09-sep-2008
 *         Time: 15:56:18
 */
public interface FileDefinitionManager {

    List<FileDefinition> getFileDefinitionsByEntityCode(String code);

}
