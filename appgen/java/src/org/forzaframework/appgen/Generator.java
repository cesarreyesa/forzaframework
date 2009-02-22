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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.forzaframework.appgen.velocity.VelocityExporter;

public class Generator {


    public static void main(String args[]) throws Exception {
        if (args.length != 2) {
            System.out.println("Sintax: Generator <importer> <exporter>");
            System.exit(1);
        }
        //TODO: ver como hacerle para que genere las clases embeded
        //TODO: Debe generar los paquetes y directorios necesarios, lo cual todavia no hace
        //TODO: URGENTE: Debe de aceptar que un atributo sea una lista de valores, y que de los valores para poder crear un combo ahi.
        //TODO: Layout de GRID
// TODO:       Templates $class.name.toPlural()XmlView.java corregir la parte de la validacion de los nulos en el xml
//        Project project = getMonoProject();
//        Project project = getQuetzalProject();
//        Project project = getIssuesProject();
//        Project project = getArnomProject();
        Project project = getCaramelProject();

        XStream xstream = new XStream(new DomDriver());
        xstream.alias("project", Project.class);
        xstream.alias("module", Module.class);
        xstream.alias("entity", Entity.class);
        xstream.alias("attribute", Attribute.class);
        xstream.alias("association", Association.class);
        xstream.alias("name", Name.class);

//        FileWriter writer = new FileWriter("/IdeaProjects/appgen/projects/arnom.xml");
//        writer.write(xstream.toXML(project));
//        writer.close();
//        FileReader reader = new FileReader("/IdeaProjects/appgen/projects/arnom.xml");
//        Project proj = (Project) xstream.fromXML(reader);
//        reader.close();

        Exporter exporter = new VelocityExporter(project);

        exporter.initialize();
        exporter.generate();
//        Navigator navigator = new Navigator(exporter);
//        navigator.start();

    }

    private static Project getArnomProject() throws Exception {
        Project project = new Project("arnom");
        project.setSrcRoot("/IdeaProjects/arnom/");
        project.setWebRoot("arnom/");
        project.setDaoRoot("src/dao/");
        project.setServiceRoot("src/service/");
        project.setControllerRoot("src/web/");

        Module hr = new Module("hr", "com.argos.arnom.hr.model", "com.argos.arnom.hr.dao", "com.argos.arnom.hr.service", "com.argos.arnom.hr.webapp.action", "hr/");
        hr.setJsNamespace("Argos.Arnom.Hr");

        Entity leaveType = new Entity(new Name("LeaveType"), hr, false);
        leaveType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        leaveType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        leaveType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        leaveType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        hr.addEntity(leaveType);

        Entity movementType = new Entity(new Name("MovementType"), hr, false);
        movementType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        movementType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        movementType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        movementType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        hr.addEntity(movementType);

        Entity vacationType = new Entity(new Name("VacationType"), hr, false);
        vacationType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        vacationType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        vacationType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        vacationType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        hr.addEntity(vacationType);

        Entity evaluation = new Entity(new Name("Evaluation"), hr, false);
        evaluation.setLayoutStyle(LayoutStyle.TABLE_FORM);
        evaluation.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        evaluation.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        evaluation.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        hr.addEntity(evaluation);

        Entity questionCategory = new Entity(new Name("QuestionCategory"), hr, false);
        questionCategory.setLayoutStyle(LayoutStyle.TABLE);
        questionCategory.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        questionCategory.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        questionCategory.addAttribute(new Association(new Name("Evaluation"), evaluation, "ManyToOne"));

        evaluation.addAttribute(new Association(new Name("QuestionCategories"), questionCategory, "OneToMany", LayoutStyle.CHILD_TABLE_FORM));
        hr.addEntity(questionCategory);

        Entity question = new Entity(new Name("Question"), hr, false);
        question.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        question.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        question.addAttribute(new Attribute(new Name("Question"), "String", "not-blank"));
        question.addAttribute(new Attribute(new Name("QuestionType"), "String", "not-blank"));
        hr.addEntity(question);

        Entity employeeEvaluation = new Entity(new Name("EmployeeEvaluation"), hr, false);
        employeeEvaluation.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        employeeEvaluation.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        hr.addEntity(employeeEvaluation);

        Entity questionAnswer = new Entity(new Name("QuestionAnswer"), hr, false);
        questionAnswer.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        questionAnswer.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        hr.addEntity(questionAnswer);

        Entity relationship = new Entity(new Name("Relationship"), hr, false);
        relationship.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        relationship.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        relationship.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        relationship.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        hr.addEntity(relationship);

        project.addModule(hr);

        Module root = new Module("", "com.argos.arnom.model", "com.argos.arnom.dao", "com.argos.arnom.service", "com.argos.arnom.webapp.action", "");
        root.setJsNamespace("Argos.Arnom");

        Entity unit = new Entity(new Name("Unit"), root, false);
        unit.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        unit.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        unit.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        unit.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        root.addEntity(unit);

        project.addModule(root);

        Module config = new Module("config", "com.argos.arnom.config.model", "com.argos.arnom.config.dao", "com.argos.arnom.config.service", "com.argos.arnom.config.webapp.action", "config/");
        config.setJsNamespace("Argos.Arnom.Config");

        Entity fileDefinition = new Entity(new Name("FileDefinition"), config, true);
        fileDefinition.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        fileDefinition.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        fileDefinition.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        fileDefinition.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        fileDefinition.addAttribute(new Attribute(new Name("Entity"), "String", "not-blank"));
        fileDefinition.addAttribute(new Attribute(new Name("Format"), "String"));
        fileDefinition.addAttribute(new Attribute(new Name("Delimiter"), "String"));
        config.addEntity(fileDefinition);

        Entity user = new Entity(new Name("User"), config, false);
        user.setGenerateDataLayer(false);
        user.setGenerateServiceLayer(false);
        user.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        user.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        user.addAttribute(new Attribute(new Name("Username"), "String", "not-blank"));
        user.addAttribute(new Attribute(new Name("Password"), "String", "not-blank"));
        user.addAttribute(new Attribute(new Name("FirstName"), "String", "not-blank"));
        user.addAttribute(new Attribute(new Name("LastName"), "String", "not-blank"));
        user.addAttribute(new Attribute(new Name("Email"), "String", "not-blank"));
        user.addAttribute(new Attribute(new Name("PreferredLocale"), "String", "not-blank"));
        user.addAttribute(new Attribute(new Name("Enabled"), "Boolean"));
        user.addAttribute(new Attribute(new Name("AccountExpired"), "Boolean"));
        user.addAttribute(new Attribute(new Name("AccountLocked"), "Boolean"));
        user.addAttribute(new Attribute(new Name("CredentialsExpired"), "Boolean"));
        config.addEntity(user);

        project.addModule(config);

        Module payroll = new Module("payroll", "com.argos.arnom.payroll.model", "com.argos.arnom.payroll.dao", "com.argos.arnom.payroll.service", "com.argos.arnom.payroll.webapp.action", "payroll/");
        payroll.setJsNamespace("Argos.Arnom.Payroll");

        Entity employeeType = new Entity(new Name("EmployeeType"), payroll, false);
        employeeType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        employeeType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        employeeType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        employeeType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        payroll.addEntity(employeeType);

        Entity journalType = new Entity(new Name("JournalType"), payroll, false);
        journalType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        journalType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        journalType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        journalType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        payroll.addEntity(journalType);

        Entity pensionType = new Entity(new Name("PensionType"), payroll, false);
        pensionType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        pensionType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        pensionType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        pensionType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        payroll.addEntity(pensionType);

        Entity salaryType = new Entity(new Name("SalaryType"), payroll, false);
        salaryType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        salaryType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        salaryType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        salaryType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        payroll.addEntity(salaryType);

        Entity maritalStatus = new Entity(new Name("MaritalStatus"), payroll, false);
        maritalStatus.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        maritalStatus.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        maritalStatus.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        maritalStatus.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        payroll.addEntity(maritalStatus);

        Entity paymentMethod = new Entity(new Name("PaymentMethod"), payroll, false);
        paymentMethod.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        paymentMethod.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        paymentMethod.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        paymentMethod.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        payroll.addEntity(paymentMethod);

        Entity payrollType = new Entity(new Name("PayrollType"), payroll, false);
        payrollType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        payrollType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        payrollType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        payrollType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        payroll.addEntity(payrollType);

        Entity bank = new Entity(new Name("Bank"), payroll, false);
        bank.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        bank.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        bank.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        bank.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        payroll.addEntity(bank);

        Entity bankAccountType = new Entity(new Name("BankAccountType"), payroll, false);
        bankAccountType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        bankAccountType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        bankAccountType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        bankAccountType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        payroll.addEntity(bankAccountType);

        project.addModule(payroll);

        return project;
    }

    private static Project getQuetzalProject() throws Exception {

        Project project = new Project("quetzal");
        project.setSrcRoot("/IdeaProjects/quetzal/");
        project.setWebRoot("quetzal/");
        project.setDaoRoot("src/dao/");
        project.setServiceRoot("src/service/");
        project.setControllerRoot("src/web/");

        Module admin = new Module("admin", "com.nopalsoft.quetzal.admin.model", "com.nopalsoft.quetzal.admin.dao", "com.nopalsoft.quetzal.admin.service", "com.nopalsoft.quetzal.admin.webapp.action", "admin/");
        admin.setJsNamespace("Nopal.Quetzal.Admin");

        Entity documentDefinition = new Entity(new Name("DocumentDefinition"), admin, false);
        documentDefinition.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        documentDefinition.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        documentDefinition.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        documentDefinition.addAttribute(new Attribute(new Name("ReminderDays"), "Integer", "not-null"));
        documentDefinition.addAttribute(new Attribute(new Name("DocumentType"), "String", "not-blank"));
        admin.addEntity(documentDefinition);

        Entity car = new Entity(new Name("Car"), admin, false);
        car.setLayoutStyle(LayoutStyle.TABLE_FORM);
        car.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        car.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        car.addAttribute(new Attribute(new Name("Capacity"), "Integer", "not-null"));
        car.addAttribute(new Attribute(new Name("Status"), "String", "not-blank"));

        Entity carDocument = new Entity(new Name("CarDocument"), admin, false);
        carDocument.setLayoutStyle(LayoutStyle.TABLE);
        carDocument.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        carDocument.addAttribute(new Attribute(new Name("StartDate"), "Date"));
        carDocument.addAttribute(new Attribute(new Name("EndDate"), "Date"));
        carDocument.addAttribute(new Association(new Name("DocumentDefinition"), documentDefinition, "ManyToOne"));
        admin.addEntity(carDocument);

        car.addAttribute(new Association(new Name("CarDocuments"), carDocument, "OneToMany", LayoutStyle.CHILD_TABLE_FORM));

        admin.addEntity(car);

        Entity customer = new Entity(new Name("Customer"), admin, true);
        customer.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);

        project.addModule(admin);

        Module config = new Module("config", "com.nopalsoft.quetzal.config.model", "com.nopalsoft.quetzal.config.dao", "com.nopalsoft.quetzal.config.service", "com.nopalsoft.quetzal.config.webapp.action", "config/");
        config.setJsNamespace("Nopal.Quetzal.Config");


        Entity country = new Entity(new Name("Country"), config, false);
        country.setLayoutStyle(LayoutStyle.TABLE_FORM);
        country.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        country.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        country.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));

        Entity state = new Entity(new Name("State"), config, false);
        state.setLayoutStyle(LayoutStyle.TABLE_FORM);
        state.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        state.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        state.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        state.addAttribute(new Association(new Name("Country"), country, "ManyToOne"));

        Entity city = new Entity(new Name("City"), config, false);
        city.setLayoutStyle(LayoutStyle.TABLE_FORM);
        city.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        city.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        city.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        city.addAttribute(new Association(new Name("State"), state, "ManyToOne"));
        config.addEntity(city);

        state.addAttribute(new Association(new Name("Cities"), city, "OneToMany", LayoutStyle.TABLE_FORM_AJAX));
        config.addEntity(state);

        country.addAttribute(new Association(new Name("States"), state, "OneToMany", LayoutStyle.CHILD_TABLE_FORM));
        config.addEntity(country);

        project.addModule(config);

        Module booking = new Module("booking", "com.nopalsoft.quetzal.booking.model", "com.nopalsoft.quetzal.booking.dao", "com.nopalsoft.quetzal.booking.service", "com.nopalsoft.quetzal.booking.webapp.action", "booking/");
        booking.setJsNamespace("Nopal.Quetzal.Booking");

        Entity reservation = new Entity(new Name("Reservation"), booking, false);
        reservation.setLayoutStyle(LayoutStyle.TABLE_FORM);
        reservation.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        reservation.addAttribute(new Attribute(new Name("Service"), "String"));
        reservation.addAttribute(new Attribute(new Name("Date"), "Date"));
        reservation.addAttribute(new Attribute(new Name("PersonName"), "String"));
        reservation.addAttribute(new Attribute(new Name("Pax"), "String"));
        reservation.addAttribute(new Attribute(new Name("FlightNumber"), "String"));
        reservation.addAttribute(new Attribute(new Name("FlightTime"), "Date"));
        reservation.addAttribute(new Attribute(new Name("StartPlace"), "String"));
        reservation.addAttribute(new Attribute(new Name("DestinationPlace"), "String"));
        booking.addEntity(reservation);

        project.addModule(booking);

        return project;

    }

    private static Project getMonoProject() throws Exception {

        Project project = new Project("mono");
        project.setSrcRoot("/IdeaProjects/mono/");
        project.setWebRoot("mono/");
        project.setDaoRoot("src/dao/");
        project.setServiceRoot("src/service/");
        project.setControllerRoot("src/web/");

        Module admin = new Module("admin", "com.nopalsoft.mono.admin.model", "com.nopalsoft.mono.admin.dao", "com.nopalsoft.mono.admin.service", "com.nopalsoft.mono.admin.webapp.action", "admin/");
        admin.setJsNamespace("Nopal.Mono.Admin");

        Entity assetCategory = new Entity(new Name("AssetCategory"), admin, false);
        assetCategory.setLayoutStyle(LayoutStyle.TABLE_FORM);
        assetCategory.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        assetCategory.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        assetCategory.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));

        Entity asset = new Entity(new Name("Asset"), admin, false);
        asset.setLayoutStyle(LayoutStyle.TABLE_FORM);
        asset.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        asset.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        asset.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        asset.addAttribute(new Association(new Name("AssetCategory"), assetCategory, "ManyToOne"));
        admin.addEntity(asset);

        assetCategory.addAttribute(new Association(new Name("Assets"), asset, "OneToMany", LayoutStyle.TABLE_FORM_AJAX));
        admin.addEntity(assetCategory);

        Entity assetType = new Entity(new Name("AssetType"), admin, false);
        assetType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        assetType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        assetType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        assetType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        admin.addEntity(assetType);

        Entity service = new Entity(new Name("Service"), admin, false);
        service.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        service.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        service.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        service.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        admin.addEntity(service);

        Entity priority = new Entity(new Name("Priority"), admin, false);
        priority.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        priority.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        priority.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        priority.addAttribute(new Attribute(new Name("Priority"), "Integer", "not-null"));
        admin.addEntity(priority);

        Entity customer = new Entity(new Name("Customer"), admin, false);
        customer.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        customer.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        customer.addAttribute(new Attribute(new Name("FirstName"), "String", "not-blank"));
        customer.addAttribute(new Attribute(new Name("LastName"), "String", "not-blank"));
        customer.addAttribute(new Attribute(new Name("SecondLastName"), "String"));
        customer.addAttribute(new Attribute(new Name("FiscalName"), "String"));
        customer.addAttribute(new Attribute(new Name("Rfc"), "String"));
        admin.addEntity(customer);

        project.addModule(admin);

//        CONFIG
        Module config = new Module("config", "com.nopalsoft.mono.config.model", "com.nopalsoft.mono.config.dao", "com.nopalsoft.mono.config.service", "com.nopalsoft.mono.config.webapp.action", "config/");
        config.setJsNamespace("Nopal.Mono.Config");


        Entity country = new Entity(new Name("Country"), config, false);
        country.setLayoutStyle(LayoutStyle.TABLE_FORM);
        country.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        country.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        country.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));

        Entity state = new Entity(new Name("State"), config, false);
        state.setLayoutStyle(LayoutStyle.TABLE_FORM);
        state.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        state.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        state.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        state.addAttribute(new Association(new Name("Country"), country, "ManyToOne"));

        Entity city = new Entity(new Name("City"), config, false);
        city.setLayoutStyle(LayoutStyle.TABLE_FORM);
        city.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        city.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        city.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        city.addAttribute(new Association(new Name("State"), state, "ManyToOne"));
        config.addEntity(city);

        state.addAttribute(new Association(new Name("Cities"), city, "OneToMany", LayoutStyle.TABLE_FORM_AJAX));
        config.addEntity(state);

        country.addAttribute(new Association(new Name("States"), state, "OneToMany", LayoutStyle.CHILD_TABLE_FORM));
        config.addEntity(country);

        project.addModule(config);
//        CONFIG

//        WO
        Module wo = new Module("wo", "com.nopalsoft.mono.wo.model", "com.nopalsoft.mono.wo.dao", "com.nopalsoft.mono.wo.service", "com.nopalsoft.mono.wo.webapp.action", "wo/");
        config.setJsNamespace("Nopal.Mono.WO");

        Entity workOrder = new Entity(new Name("WorkOrder"), wo, true);
        workOrder.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        workOrder.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        workOrder.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        workOrder.addAttribute(new Attribute(new Name("Description"), "String"));
        workOrder.addAttribute(new Attribute(new Name("DateRequested"), "Date"));
        workOrder.addAttribute(new Attribute(new Name("Status"), "String"));
        workOrder.addAttribute(new Association(new Name("Service"), service, "ManyToOne", LovLayoutStyle.AutoComplete));
        workOrder.addAttribute(new Association(new Name("Customer"), customer, "ManyToOne", LovLayoutStyle.AutoComplete));

        wo.addEntity(workOrder);


        project.addModule(wo);

        return project;

    }

    private static Project getIssuesProject() throws Exception {

        Project project = new Project("issues");
        project.setSrcRoot("/IdeaProjects/issues/");
        project.setWebRoot("issues/");
        project.setDaoRoot("src/dao/");
        project.setServiceRoot("src/service/");
        project.setControllerRoot("src/web/");

        Module root = new Module("", "com.nopalsoft.issues.model", "com.nopalsoft.issues.dao", "com.nopalsoft.issues.service", "com.nopalsoft.issues.webapp.action", "");
        root.setJsNamespace("Nopal.Issues");

        Entity issueType = new Entity(new Name("IssueType"), root, true);
        issueType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        issueType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        issueType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        issueType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        root.addEntity(issueType);

        Entity priority = new Entity(new Name("Priority"), root, true);
        priority.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        priority.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        priority.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        priority.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        priority.addAttribute(new Attribute(new Name("Priority"), "Integer"));
        root.addEntity(priority);

        Entity _project = new Entity(new Name("Project"), root, true);
        _project.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        _project.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        _project.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        _project.addAttribute(new Attribute(new Name("ForceDateSelection"), "Boolean", "not-blank"));
        root.addEntity(_project);

        Entity issueReply = new Entity(new Name("IssueReply"), root, true);
        issueReply.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        issueReply.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        issueReply.addAttribute(new Attribute(new Name("Comments"), "String", "not-blank"));
        root.addEntity(issueReply);

        Entity issue = new Entity(new Name("Issue"), root, true);
        issue.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        issue.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        issue.addAttribute(new Attribute(new Name("Title"), "String", "not-blank"));
        issue.addAttribute(new Attribute(new Name("Description"), "String", "not-blank"));
        issue.addAttribute(new Attribute(new Name("Status"), "String", "not-blank"));
        issue.addAttribute(new Attribute(new Name("Date"), "Date", "not-blank"));
        issue.addAttribute(new Attribute(new Name("StartDate"), "Date"));
        issue.addAttribute(new Attribute(new Name("EndDate"), "Date"));
        issue.addAttribute(new Attribute(new Name("CompletedPercentage"), "Boolean"));
        issue.addAttribute(new Attribute(new Name("IsArchived"), "Boolean"));
        issue.addAttribute(new Association(new Name("Project"), _project, "ManyToOne"));
        issue.addAttribute(new Association(new Name("IssueType"), issueType, "ManyToOne"));
        issue.addAttribute(new Association(new Name("Priority"), priority, "ManyToOne"));

        project.addModule(root);

        return project;

    }

    private static Project getCaramelProject() throws Exception {

        Project project = new Project("caramel");
        project.setSrcRoot("/IdeaProjects/caramel/");
        project.setWebRoot("caramel/");
        project.setDaoRoot("src/dao/");
        project.setServiceRoot("src/service/");
        project.setControllerRoot("src/web/");

        Module rs = new Module("rs", "com.nopalsoft.caramel.rs.model", "com.nopalsoft.caramel.rs.dao", "com.nopalsoft.caramel.rs.service", "com.nopalsoft.caramel.rs.webapp.action", "rs/");
        rs.setJsNamespace("Nopal.Caramel.Rs");

        Entity development = new Entity(new Name("Development"), rs, false);
        development.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        development.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        development.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        development.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        rs.addEntity(development);

        Entity buildingType = new Entity(new Name("BuildingType"), rs, false);
        buildingType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        buildingType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        buildingType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        buildingType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        rs.addEntity(buildingType);

        Entity unitType = new Entity(new Name("UnitType"), rs, false);
        unitType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        unitType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        unitType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        unitType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        rs.addEntity(unitType);

        project.addModule(rs);

        Module crm = new Module("crm", "com.nopalsoft.caramel.crm.model", "com.nopalsoft.caramel.crm.dao", "com.nopalsoft.caramel.crm.service", "com.nopalsoft.caramel.crm.webapp.action", "crm/");
        crm.setJsNamespace("Nopal.Caramel.Crm");

        Entity lead = new Entity(new Name("Lead"), crm, false);
        lead.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        lead.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        crm.addEntity(lead);

        Entity emailType = new Entity(new Name("EmailType"), crm, true);
        emailType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        emailType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        emailType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        emailType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        crm.addEntity(emailType);

        Entity phoneType = new Entity(new Name("PhoneType"), crm, true);
        phoneType.setLayoutStyle(LayoutStyle.TABLE_FORM_AJAX);
        phoneType.setId(new Attribute(new Name("Id"), "Long", "not-blank"));
        phoneType.addAttribute(new Attribute(new Name("Code"), "String", "not-blank"));
        phoneType.addAttribute(new Attribute(new Name("Name"), "String", "not-blank"));
        crm.addEntity(phoneType);

        project.addModule(crm);

        return project;

    }
}