package daham.app.processor;

import daham.app.model.Student;
import org.springframework.batch.item.ItemProcessor;

public class StudentProcesser implements ItemProcessor<Student, Student> {
    @Override
    public Student process(Student student) throws Exception {
        // method modified Student object
        final String firstName = student.getFirstName().toUpperCase();
        final String lastName = student.getLastName().toUpperCase();
        final Student data = new Student(student.getId(),firstName,lastName, student.getEmail());
        return data;
    }
}
