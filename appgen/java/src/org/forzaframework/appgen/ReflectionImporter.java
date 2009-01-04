package org.forzaframework.appgen;


/**
 * User: cesarreyes
 * Date: 24-dic-2006
 * Time: 18:20:52
 * Description:
 */
public class ReflectionImporter {
    private Project project;

    public ReflectionImporter() {

    }

    public ReflectionImporter(Project project) {
        this.project = project;
    }

    public void start() throws Exception {
//        List<Class> classes = ReflectionUtility.getClasses(project.getSrcRoot() + project.getModelPath(), project.getPackageToGenerate());
//        for(Class clazz : classes){
//            if(clazz.getAnnotation(Entity.class) != null){
//                IOMController.addClass(createClass(clazz));
//            }
//        }
    }

//    public IOMClass createClass(Object data) throws Exception {
//        Class clazz = (Class) data;
//        IOMClass cl = new IOMClass();
//        cl.setName(clazz.getSimpleName());
//        cl.setPackageName(clazz.getPackage().getName());
//        for(Method method : clazz.getMethods()){
//            if(method.isAnnotationPresent(Id.class)){
//                cl.setId(createAttribute(method));
//            }
//            else if(method.isAnnotationPresent(Column.class)){
//                cl.addAttribute(createAttribute(method));
//            }
//        }
//        return cl;
//    }
//
//    public IOMAttribute createAttribute(Object data) throws Exception {
//        Method method = (Method) data;
//        IOMAttribute attr = new IOMAttribute();
//        String name = method.getName();
//        if(name.startsWith("get")){
//            attr.setName(name.substring(3));
//
//        }else{
//            attr.setName(name.substring(2));
//        }
//        attr.setType(method.getReturnType().getSimpleName());
//        for(Annotation annotation : method.getAnnotations()){
//            if(annotation instanceof NotBlank){
//                attr.addValidation("not-blank");
//            }
//        }
//        return attr;
//    }
//
//    public IOMAssociation createAssociation(Object data) throws Exception {
//        return null;
//    }
//
//    public IOMRole createRole(Object data) throws Exception {
//        return null;
//    }
}
