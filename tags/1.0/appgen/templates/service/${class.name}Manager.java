package ${class.module.servicePackage};

import java.util.List;

import ${class.packageName}.${class.name};
import ${class.module.daoPackage}.${class.name}Dao;
import com.nopalsoft.service.Manager;

public interface ${class.name}Manager extends Manager {

    /**
     * Setter for DAO, convenient for unit testing
     */
    public void set${class.name}Dao(${class.name}Dao ${class.name.toLowerCase()}Dao);

    /**
     * Retrieves all of the assets
     * @return list of ${class.name.toPluralFirstToLower()}
     */
    public List<${class.name}> get${class.name.toPlural()}();

    /**
     * Retrieves all of the assets filtered by a query string
     * @return list of ${class.name.toPluralFirstToLower()}
     */
    public List<${class.name}> get${class.name.toPlural()}(String query);

    /**
     * Retrieves all of the assets filtered by a criteria
     * @return list of ${class.name.toPluralFirstToLower()}
     */
    public List<${class.name}> get${class.name.toPlural()}(String field, String criteria);

    /**
     * Gets asset's information based on id.
     * @param id the ${class.name.toLowerCase()}'s id
     * @return asset populated asset object
     */
    public ${class.name} get${class.name}(final String id);

    /**
     * Saves a ${class.name.toLowerCase()}'s information
     * @param ${class.name.toLowerCase()} the object to be saved
     */
    public void save${class.name}(${class.name} ${class.name.toLowerCase()});

    /**
     * Removes a ${class.name.toLowerCase()} from the database by id
     * @param id the ${class.name.toLowerCase()}'s id
     */
    public void remove${class.name}(final String id);

}

