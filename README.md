# Spring-Batch-CSV-File-ToCSV-File

Creating a read/process/write:<br />
read data somewhere, processed in it some way, and finally, saved somewhere else. we will use Spring Batch to read a CSV File and write ton onther CSV File:<br />
1- creat a java model domain class, defin its properties.<br />
2- creat your configure class.<br />
3- define all the beans in the configaratin class that we will use in our process.<br />
4- FlatFileItemReader object, which will read a CSV file. Each line of CVS is processed by LineMapper which takes a line and returns an object.<br />
5- converts a line to Fieldset (using DelimitedLineTokenizer);<br />
6- save each field in an object by using BeanWrapperFieldSetMapper).<br />
7- mapping lines (strings) to domain object Student:<br />
8- DelimitedLineAggregator: converts an object into a delimited list of strings.<br />
9- creat a field extractor for a java bean by BeanWrapperFieldExtractor<Student>. <br />


