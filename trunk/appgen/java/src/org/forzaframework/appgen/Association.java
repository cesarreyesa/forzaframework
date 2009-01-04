package org.forzaframework.appgen;

/**
 * User: cesarreyes
 * Date: 14-ene-2007
 * Time: 21:37:58
 * Description:
 */
public class Association extends Attribute {

    private Name name;
    private Entity entity;
    private LayoutStyle layoutStyle;
    private LovLayoutStyle lovLayoutStyle;
    private String multiplicity;

    public Association() {
    }

    public Association(Entity entity, String multiplicity) {
        this(null, entity, multiplicity);
    }

    public Association(Name name, Entity entity, String multiplicity) {
        this.name = name;
        this.entity = entity;
        this.multiplicity = multiplicity;
    }

    public Association(Name name, Entity entity, String multiplicity, LovLayoutStyle lovLayoutStyle) {
        this.name = name;
        this.entity = entity;
        this.multiplicity = multiplicity;
        this.lovLayoutStyle = lovLayoutStyle;
    }

    public Association(Name name, Entity entity, String multiplicity, LayoutStyle layoutStyle) {
        this.name = name;
        this.entity = entity;
        this.layoutStyle = layoutStyle;
        this.multiplicity = multiplicity;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public LayoutStyle getLayoutStyle() {
        return layoutStyle;
    }

    public void setLayoutStyle(LayoutStyle layoutStyle) {
        this.layoutStyle = layoutStyle;
    }

    public LovLayoutStyle getLovLayoutStyle() {
        return lovLayoutStyle;
    }

    public void setLovLayoutStyle(LovLayoutStyle lovLayoutStyle) {
        this.lovLayoutStyle = lovLayoutStyle;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }
}
