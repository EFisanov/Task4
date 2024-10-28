package org.example;

import org.example.model.Class;
import org.example.model.Student;
import org.example.model.Subject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyFormatManager {
    public void serialisationToMyFormat(String fileName, Class clazz) throws IOException {
        if (clazz != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            builder.append("head");
            builder.append(":class=");

            if (clazz.getName() == null) {
                builder.append(buildSimpleObject("name", ""));
            } else {
                builder.append(buildSimpleObject("name", clazz.getName()));
            }

            builder.append(buildList("students", clazz.getStudents()));
            builder.append(buildList("subjects", clazz.getSubjects()));
            builder.append("]");
            saveToFile(fileName, builder.toString());
        }
    }

    private String buildSimpleObject(String name, Object object) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(name);
        if (object instanceof String) {
            if (object.equals("")) {
                builder.append(":string=");
                builder.append("'");
                builder.append("NULL");
                builder.append("'");
            } else {
                builder.append(":string=");
                builder.append("'");
                builder.append(object);
                builder.append("'");
            }
        } else if (object instanceof Integer) {
            if ((Integer) object == -1) {
                builder.append(":integer=");
                builder.append("'");
                builder.append("NULL");
                builder.append("'");
            } else {
                builder.append(":integer=");
                builder.append("'");
                builder.append(object);
                builder.append("'");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private String buildComplexObject(String name, Object object) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(name);
        if (object instanceof Student) {
            builder.append(":student=");

            if (((Student) object).getName() == null) {
                builder.append(buildSimpleObject("name", ""));
            } else {
                builder.append(buildSimpleObject("name", ((Student) object).getName()));
            }

            if (((Student) object).getSurName() == null) {
                builder.append(buildSimpleObject("surName", ""));
            } else {
                builder.append(buildSimpleObject("surName", ((Student) object).getSurName()));
            }

            if (((Student) object).getAge() == null) {
                builder.append(buildSimpleObject("age", Integer.valueOf(-1)));
            } else {
                builder.append(buildSimpleObject("age", ((Student) object).getAge()));
            }
        } else if (object instanceof Subject) {
            builder.append(":subject=");

            if (((Subject) object).getName() == null) {
                builder.append(buildSimpleObject("name", ""));
            } else {
                builder.append(buildSimpleObject("name", ((Subject) object).getName()));
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private String buildList(String name, Object object) {
        StringBuilder builder = new StringBuilder();
        if (object instanceof ArrayList<?>) {
            ArrayList<?> list = (ArrayList<?>) object;
            builder.append("[");
            builder.append(name);
            builder.append(":list=");
            builder.append("<");
            for (int i = 0; i < list.size(); i++) {
                builder.append(buildComplexObject(String.valueOf(i), list.get(i)));
            }
            builder.append(">");
            builder.append("]");
        }
        return builder.toString();
    }

    private void saveToFile(String name, String text) throws IOException {
        if (text != null) {
            FileWriter writer = new FileWriter(name, false);
            writer.write(text);
            writer.close();
        }
    }

    public static String loadRecords(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        return reader.readLine();
    }

    private Student parseStudentFromString(String studentAsString) {
        Student student = new Student();
        do {
            if (studentAsString.substring(studentAsString.indexOf("[") + 1, studentAsString.indexOf(":")).equals("name")) {
                if (studentAsString.substring(studentAsString.indexOf("='") + 2, studentAsString.indexOf("']")).equals("NULL")) {
                    student.setName(null);
                } else {
                    student.setName(studentAsString.substring(studentAsString.indexOf("='") + 2, studentAsString.indexOf("']")));
                }
                studentAsString = studentAsString.replace(studentAsString.substring(studentAsString.indexOf('['), studentAsString.indexOf(']') + 1), "");
            } else if (studentAsString.substring(studentAsString.indexOf("[") + 1, studentAsString.indexOf(":")).equals("surName")) {
                if (studentAsString.substring(studentAsString.indexOf("='") + 2, studentAsString.indexOf("']")).equals("NULL")) {
                    student.setName(null);
                } else {
                    student.setSurName(studentAsString.substring(studentAsString.indexOf("='") + 2, studentAsString.indexOf("']")));
                }
                studentAsString = studentAsString.replace(studentAsString.substring(studentAsString.indexOf('['), studentAsString.indexOf(']') + 1), "");
            } else if (studentAsString.substring(studentAsString.indexOf("[") + 1, studentAsString.indexOf(":")).equals("age")) {
                if (studentAsString.substring(studentAsString.indexOf("='") + 2, studentAsString.indexOf("']")).equals("NULL")) {
                    student.setAge(null);
                } else {
                    student.setAge(Integer.parseInt(studentAsString.substring(studentAsString.indexOf("='") + 2, studentAsString.indexOf("']"))));
                }
                studentAsString = studentAsString.replace(studentAsString.substring(studentAsString.indexOf('['), studentAsString.indexOf(']') + 1), "");
            }
        } while (studentAsString.isEmpty() == false);

        return student;
    }

    public Class parseFromMyFormat(String fileName) throws IOException {
        Class newClazz = new Class();
        String source = loadRecords(fileName);
        String classValue = source.substring(source.indexOf('=') + 1, source.length() - 1);
        List<String> objects = new ArrayList<>();
        do {
            if (classValue.substring(classValue.indexOf(':') + 1, classValue.indexOf('=')).equals("string")) {
                String string = classValue.substring(classValue.indexOf('['), classValue.indexOf(']') + 1);
                objects.add(string);
                classValue = classValue.replace(string, "");
            } else if (classValue.substring(classValue.indexOf(':') + 1, classValue.indexOf('=')).equals("list")) {
                String string = classValue.substring(classValue.indexOf('['), classValue.indexOf(">") + 2);
                objects.add(string);
                classValue = classValue.replace(string, "");
            }
        } while (classValue.isEmpty() == false);

        for (String object : objects) {
            if (object.substring(object.indexOf(':') + 1, object.indexOf('=')).equals("string")) {
                String string = object.substring(object.indexOf("='") + 2, object.indexOf("']"));
                if (string.equals("NULL")) {
                    newClazz.setName(null);
                } else {
                    newClazz.setName(string);
                }
            } else if (object.substring(object.indexOf(':') + 1, object.indexOf('=')).equals("list")) {
                String listAsString = object.substring(object.indexOf('<') + 1, object.indexOf('>'));
                do {
                    if (listAsString.substring(listAsString.indexOf(':') + 1, listAsString.indexOf('=')).equals("student")) {
                        String studentAsString = listAsString.substring(listAsString.indexOf("=") + 1, listAsString.indexOf("]]") + 1);
                        newClazz.addStudent(parseStudentFromString(studentAsString));
                        listAsString = listAsString.replace(listAsString.substring(object.indexOf("["), listAsString.indexOf("]]") + 2), "");
                    } else if (listAsString.substring(listAsString.indexOf(':') + 1, listAsString.indexOf('=')).equals("subject")) {
                        String subjectName = listAsString.substring(listAsString.indexOf("='") + 2, listAsString.indexOf("']"));
                        newClazz.addSubject(new Subject(subjectName));
                        listAsString = listAsString.replace(listAsString.substring(object.indexOf("["), listAsString.indexOf("]]") + 2), "");
                    }
                } while (listAsString.isEmpty() == false);
            }
        }
        return newClazz;
    }
}
