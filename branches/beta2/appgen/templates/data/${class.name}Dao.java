package ${class.module.daoPackage};

import java.util.List;

import ${class.packageName}.${class.name};
import com.nopalsoft.dao.Dao;

public interface ${class.name}Dao extends Dao {

    /**
     * Retrieves all of the ${class.name.toPluralFirstToLower()}
     * @return list of ${class.name.toPluralFirstToLower()}
     */
    public List<${class.name}> get${class.name.toPlural()}();

    /**
     * Retrieves all of the ${class.name.toLowerCase()} filtered by a query string
     * @return list of ${class.name.toPluralFirstToLower()}
     */
    public List<${class.name}> get${class.name.toPlural()}(String query);

    /**
     * Retrieves all of the ${class.name.toLowerCase()} filtered by a criteria
     * @return list of ${class.name.toPluralFirstToLower()}
     */
    public List<${class.name}> get${class.name.toPlural()}(String field, String criteria);

    /**
     * Gets ${class.name.toLowerCase()}'s information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     * 
     * @param ${class.id.name.toLowerCase()} the ${class.name.toLowerCase()}'s ${class.id.name.toLowerCase()}
     * @return ${class.name.toLowerCase()} populated ${class.name.toLowerCase()} object
     */
    public ${class.name} get${class.name}(final ${class.id.type} ${class.id.name.toLowerCase()});

    /**
     * Saves a ${class.name.toLowerCase()}'s information
     * @param ${class.name.toLowerCase()} the object to be saved
     */	
    public void save${class.name}(${class.name} ${class.name.toLowerCase()});

	/**
     * Removes a ${class.name.toLowerCase()} from the database by ${class.id.name.toLowerCase()}
     * @param ${class.id.name.toLowerCase()} the ${class.name.toLowerCase()}'s ${class.id.name.toLowerCase()}
     */
    public void remove${class.name}(final ${class.id.type} ${class.id.name.toLowerCase()});
}

