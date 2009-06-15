package ${class.module.servicePackage}.impl;

import java.util.List;

import ${class.packageName}.${class.name};
import ${class.module.daoPackage}.${class.name}Dao;
import ${class.module.servicePackage}.${class.name}Manager;
import com.nopalsoft.service.impl.BaseManager;

public class ${class.name}ManagerImpl extends BaseManager implements ${class.name}Manager {
    private ${class.name}Dao ${class.name.toLowerCase()}Dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param ${class.name.toLowerCase()}Dao
     */
    public void set${class.name}Dao(${class.name}Dao ${class.name.toLowerCase()}Dao) {
        this.${class.name.toLowerCase()}Dao = ${class.name.toLowerCase()}Dao;
    }

    /**
     * @see ${class.module.servicePackage}.${class.name}Manager#get${class.name.toPlural()}(String)
     */
    public List<${class.name}> get${class.name.toPlural()}() {
        return ${class.name.toLowerCase()}Dao.get${class.name.toPlural()}();
    }

    /**
     * @see ${class.module.servicePackage}.${class.name}Manager#get${class.name.toPlural()}(String)
     */
    public List<${class.name}> get${class.name.toPlural()}(String query) {
        return ${class.name.toLowerCase()}Dao.get${class.name.toPlural()}(query);
    }

    /**
     * @see ${class.module.servicePackage}.${class.name}Manager#get${class.name.toPlural()}(String field, String criteria)
     */
    public List<${class.name}> get${class.name.toPlural()}(String field, String criteria){
        return ${class.name.toLowerCase()}Dao.get${class.name.toPlural()}(field, criteria);
    }

    /**
     * @see ${class.module.servicePackage}.${class.name}Manager#get${class.name}(String ${class.id.name.toLowerCase()})
     */
    public ${class.name} get${class.name}(final String ${class.id.name.toLowerCase()}) {
        return ${class.name.toLowerCase()}Dao.get${class.name}(new ${class.id.type}(${class.id.name.toLowerCase()}));
    }

    /**
     * @see ${class.module.servicePackage}.${class.name}Manager#save${class.name}(${class.name} ${class.name.toLowerCase()})
     */
    public void save${class.name}(${class.name} ${class.name.toLowerCase()}) {
        ${class.name.toLowerCase()}Dao.save${class.name}(${class.name.toLowerCase()});
    }

    /**
     * @see ${class.module.servicePackage}.${class.name}Manager#remove${class.name}(String ${class.id.name.toLowerCase()})
     */
    public void remove${class.name}(final String ${class.id.name.toLowerCase()}) {
        ${class.name.toLowerCase()}Dao.remove${class.name}(new ${class.id.type}(${class.id.name.toLowerCase()}));
    }
}
