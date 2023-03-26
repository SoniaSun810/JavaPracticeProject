# README

Open IntelliJ, cd ../Lab2-Code \
Compile the program and run it with following command:
```
javac *.java   
java -Duser.language=en SystemMain
java SystemMain Students.txt Courses.txt
```

### Main tasks for Lab2
A. Augment the system to support logging by adding a new component. All output that goes to the
screen should also be written to a log file. (Note: Do not put this functionality inside of the
ClientOutput component.) \
B. Suppose we want to know when a course becomes overbooked. Add new capability to the
system so that it announces when a class is overbooked. Note that it does not need to prohibit
registrations for overbooked classes, but simply announce that fact. For the purposes of this
assignment, consider that a class is overbooked when more than three students are registered.
(“30” is more realistic, but “3” makes testing easier.) \
C. Extract the course-conflict checking from the RegisterStudentHandler and put it into its own
component. The observable system behavior should not change. (By "observable" we mean to
the user of the system, not to someone viewing the architecture or the source code.)

## Example
```aidl
Enter your choice and press return >> 
6

Enter student ID and press return >> 
G00123456 
G00123432 
G45643133 
G01234567


Enter course ID and press return >> 
CS112

Enter course section and press return >> 
a
```