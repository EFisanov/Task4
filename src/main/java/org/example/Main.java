package org.example;

import com.example.school.ClassXmlDocument;
import com.example.school.StudentXml;
import com.example.school.SubjectXml;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.example.model.Class;
import org.example.model.Student;
import org.example.model.Subject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    private final static String FILE_NAME_JSON = "src/main/resources/jsonVersion.json";
    private final static String FILE_NAME_XML = "src/main/resources/xmlVersion.xml";
    private final static String FILE_NAME_MYF = "src/main/resources/myfVersion.myf";

    public static void main(String[] args) throws IOException, XmlException {
        MyFormatManager myFormatManager = new MyFormatManager();
        myFormatManager.serialisationToMyFormat(FILE_NAME_MYF, prepareData());
        Class actualClazz2 = myFormatManager.parseFromMyFormat(FILE_NAME_MYF);
        System.out.println("\n *** MYF version ***\n " + actualClazz2);

        serialisationToJson(FILE_NAME_JSON, prepareData());
        Class actualClazz = deserialisationFromJson(FILE_NAME_JSON);
        System.out.println("\n *** JSON version ***\n " + actualClazz);

        serialisationToXml(FILE_NAME_XML, prepareXmlData());
        ClassXmlDocument classXmlDocument = deserialisationFromXml(FILE_NAME_XML);
        System.out.println("\n *** XML version ***\n " + classXmlDocument.getClassXml());
    }

    public static Class prepareData() {
        Class sourceClazz = new Class("10А");

        Student studentOne = new Student("Иван", "Иванов", 16);
        Student studentTwo = new Student("Пётр", "Петров", 17);
        Student studentThree = new Student("Сидор", "Сидоров", 16);

        Subject subjectOne = new Subject("Математика");
        Subject subjectTwo = new Subject("Русский язык");
        Subject subjectThree = new Subject("Физика");

        sourceClazz.addStudent(studentOne);
        sourceClazz.addStudent(studentTwo);
        sourceClazz.addStudent(studentThree);
        sourceClazz.addSubject(subjectOne);
        sourceClazz.addSubject(subjectTwo);
        sourceClazz.addSubject(subjectThree);

        return sourceClazz;
    }

    public static void serialisationToJson(String fileName, Class clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(fileName), clazz);
    }

    public static Class deserialisationFromJson(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(fileName), Class.class);
    }

    public static ClassXmlDocument prepareXmlData() {
        Class sourceClazz = prepareData();
        ClassXmlDocument classDocument = ClassXmlDocument.Factory.newInstance();
        ClassXmlDocument.ClassXml classXml = classDocument.addNewClassXml();
        classXml.setName(sourceClazz.getName());

        StudentXml studentXmlOne = classXml.addNewStudents();
        studentXmlOne.setName(sourceClazz.getStudents().get(0).getName());
        studentXmlOne.setSurName(sourceClazz.getStudents().get(0).getSurName());
        if (sourceClazz.getStudents().get(0).getAge() != null) {
            studentXmlOne.setAge(sourceClazz.getStudents().get(0).getAge());
        }

        StudentXml studentXmlTwo = classXml.addNewStudents();
        studentXmlTwo.setName(sourceClazz.getStudents().get(1).getName());
        studentXmlTwo.setSurName(sourceClazz.getStudents().get(1).getSurName());
        if (sourceClazz.getStudents().get(1).getAge() != null) {
            studentXmlTwo.setAge(sourceClazz.getStudents().get(1).getAge());
        }

        StudentXml studentXmlThree = classXml.addNewStudents();
        studentXmlThree.setName(sourceClazz.getStudents().get(2).getName());
        studentXmlThree.setSurName(sourceClazz.getStudents().get(2).getSurName());
        if (sourceClazz.getStudents().get(2).getAge() != null) {
            studentXmlThree.setAge(sourceClazz.getStudents().get(2).getAge());
        }

        SubjectXml subjectXmlOne = classXml.addNewSubjects();
        subjectXmlOne.setName(sourceClazz.getSubjects().get(0).getName());

        SubjectXml subjectXmlTwo = classXml.addNewSubjects();
        subjectXmlTwo.setName(sourceClazz.getSubjects().get(1).getName());

        SubjectXml subjectXmlThree = classXml.addNewSubjects();
        subjectXmlThree.setName(sourceClazz.getSubjects().get(2).getName());

        return classDocument;
    }

    public static void serialisationToXml(String fileName, ClassXmlDocument classDocument) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            classDocument.save(fos, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ClassXmlDocument deserialisationFromXml(String fileName) throws XmlException, IOException {
        File xmlFile = new File(fileName);
        return ClassXmlDocument.Factory.parse(xmlFile);
    }
}