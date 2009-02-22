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
