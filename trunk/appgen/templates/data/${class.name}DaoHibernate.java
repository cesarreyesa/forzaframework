package ${class.module.daoPackage}.hibernate;

import ${class.packageName}.${class.name};
import ${class.module.daoPackage}.${class.name}Dao;
import com.nopalsoft.dao.hibernate.BaseDaoHibernate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.List;

public class ${class.name}DaoHibernate extends BaseDaoHibernate implements ${class.name}Dao {

    /**
     * @see ${class.module.daoPackage}.${class.name}Dao#get${class.name.toPlural()}()
     */
    public List<${class.name}> get${class.name.toPlural()}() {
        return getHibernateTemplate().find("from ${class.name}");
    }

    /**
     * @see ${class.module.daoPackage}.${class.name}Dao#get${class.name.toPlural()}(String)
     */
    public List<${class.name}> get${class.name.toPlural()}(String query) {
        DetachedCriteria crit = DetachedCriteria.forClass(${class.name}.class);

        Disjunction disjunction = Restrictions.disjunction();

        if(StringUtils.isNotBlank(query)) {
#foreach( $att in ${class.attributes})
#if( ${att.type} == "String")
#if( ${att.name.toString()} == "Code" || ${att.name.toString()} == "Name" || ${att.name.toString()} == "Description")
            disjunction.add(Restrictions.like("${att.name.toLowerCase()}", query, MatchMode.ANYWHERE).ignoreCase());
#end
#end
#end
        }
        crit.add(disjunction);
        log.debug("disjunction:" + disjunction.toString());
        return getHibernateTemplate().findByCriteria(crit);
    }

    /**
     * @see ${class.module.daoPackage}.${class.name}Dao#get${class.name.toPlural()}(String field, String criteria)
     */
    public List<${class.name}> get${class.name.toPlural()}(String field, String criteria){
        DetachedCriteria crit = DetachedCriteria.forClass(${class.name}.class);

        crit.add(Restrictions.like(field, criteria).ignoreCase());

        return getHibernateTemplate().findByCriteria(crit);
    }

    /**
     * @see ${class.module.daoPackage}.${class.name}Dao#get${class.name}(${class.id.type} ${class.id.name.toLowerCase()})
     */
    public ${class.name} get${class.name}(final ${class.id.type} ${class.id.name.toLowerCase()}) {
        ${class.name} ${class.name.toLowerCase()} = (${class.name}) getHibernateTemplate().get(${class.name}.class, ${class.id.name.toLowerCase()});
        
        if (${class.name.toLowerCase()} == null) {
            log.warn("uh oh, ${class.name.toLowerCase()} with ${class.id.name.toLowerCase()} '" + ${class.id.name.toLowerCase()} + "' not found...");
            throw new ObjectRetrievalFailureException(${class.name}.class, ${class.id.name.toLowerCase()});
        }

        return ${class.name.toLowerCase()};
    }

    /**
     * @see ${class.module.daoPackage}.${class.name}Dao#save${class.name}(${class.name} ${class.name.toLowerCase()})
     */    
    public void save${class.name}(final ${class.name} ${class.name.toLowerCase()}) {
        getHibernateTemplate().saveOrUpdate(${class.name.toLowerCase()});
    }

    /**
     * @see ${class.module.daoPackage}.${class.name}Dao#remove${class.name}(${class.id.type} ${class.id.name.toLowerCase()})
     */
    public void remove${class.name}(final ${class.id.type} ${class.id.name.toLowerCase()}) {
        getHibernateTemplate().delete(get${class.name}(${class.id.name.toLowerCase()}));
    }
}
