package daham.app.config;

import daham.app.model.Student;
import daham.app.processor.StudentProcesser;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@ComponentScan("daham.app")
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    // method returning a FlatFileItemReader object, which will read a CSV file
    @Bean
    public FlatFileItemReader<Student> csvFileReader() {
        FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
        reader.setResource(new FileSystemResource("/Users/m-store/Desktop/New Spring-Thymleaf/Security/SpringBatchCsvToCSV/Resource/student.csv"));
        reader.setLineMapper(lineMapper());
        return reader;
    }

    @Bean
    public DefaultLineMapper<Student> lineMapper() {
        //Each line is processed by LineMapper
        //takes a line and returns an object.
        DefaultLineMapper<Student> lineMapper = new DefaultLineMapper<>();
        //converts a line to Fieldset (using DelimitedLineTokenizer)
        lineMapper.setLineTokenizer(tokenizer());
        lineMapper.setFieldSetMapper(fieldSetMapper());
        return lineMapper;
    }
    @Bean
    public BeanWrapperFieldSetMapper<Student> fieldSetMapper() {
        //save each field in an object
        // BeanWrapperFieldSetMapper).
        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Student.class);
        return fieldSetMapper; }

    @Bean
    public DelimitedLineTokenizer tokenizer() {
        //converts a line to Fieldset (using DelimitedLineTokenizer)
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        // mapping lines (strings) to domain object Student:
        tokenizer.setNames(new String[]{"id","firstName", "lastName", "email"});
        return tokenizer;
    }
    @Bean
    public StudentProcesser studentProcesser(){
        StudentProcesser studentProcesser = new StudentProcesser();
        return studentProcesser;
    }

    @Bean
    public FlatFileItemWriter<Student> writer(){
        FlatFileItemWriter<Student> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("/Users/m-store/Desktop/New Spring-Thymleaf/Security/SpringBatchCsvToCSV/Resource/out_new.csv"));

        // DelimitedLineAggregator: converts an object into a delimited list of strings
        DelimitedLineAggregator<Student> lineAggregator = new DelimitedLineAggregator<Student>();

        //This is a field extractor for a java bean.
        BeanWrapperFieldExtractor<Student> fieldExtractor = new
                BeanWrapperFieldExtractor<Student>();
        fieldExtractor.setNames(new String[]{"id","firstName", "lastName", "email"});
        lineAggregator.setFieldExtractor(fieldExtractor);
        writer.setLineAggregator(lineAggregator);
        return writer;
    }
    @Bean
    public Step excuteStudentStep() {
        return steps.get("excuteStudentStep").<Student, Student>chunk(2)
                .reader(csvFileReader())
                .processor(studentProcesser())
                .writer(writer()).build();
    }
    @Bean
    public Job processStudentJob(){
        return jobs.get("processStudentJob")
                .flow(excuteStudentStep())
                .end().build();
    }
}
