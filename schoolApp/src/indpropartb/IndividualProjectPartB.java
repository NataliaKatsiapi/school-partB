package indpropartb;

import daos.AssignmentDao;
import daos.AssignmentsPerCourseDao;
import daos.CourseDao;
import daos.StudentDao;
import daos.StudentsPerCourseDao;
import daos.TrainerDao;
import daos.TrainersPerCourseDao;
import daos.UserDao;
import entities.Assignment;
import entities.AssignmentsPerCourse;
import entities.Course;
import entities.Student;
import entities.StudentsPerCourse;
import entities.Trainer;
import entities.TrainersPerCourse;
import entities.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class IndividualProjectPartB {

    private static StudentDao sdao = new StudentDao();
    private static TrainerDao tdao = new TrainerDao();
    private static CourseDao cdao = new CourseDao();
    private static AssignmentDao adao = new AssignmentDao();
    private static StudentsPerCourseDao spcdao = new StudentsPerCourseDao();
    private static TrainersPerCourseDao tpcdao = new TrainersPerCourseDao();
    private static AssignmentsPerCourseDao apcdao = new AssignmentsPerCourseDao();
    private static UserDao udao = new UserDao();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to Private School Application");
        if (logIn(input) == false) { //username: admin   password: 1234
            return;
        }

        for (;;) {
            System.out.println("Select: ");
            System.out.println("-1- for seeing stored information");
            System.out.println("-2- for inserting new data");
            System.out.println("-0- for exiting the application");
            int choice = checkInt(input);
            switch (choice) {
                case 1:
                    printingMenu(input);
                    break;
                case 2:
                    insertingMenu(input);
                    break;
                case 0:
                    input.close();
                    return;
                default:
                    System.out.println("There is no such choice. Try again.");
                    break;
            }
        }
    }

    private static int checkInt(Scanner in) {
        int x;
        for (;;) {
            try {
                x = in.nextInt();
                break;
            } catch (InputMismatchException ex) {
                System.out.println("You should enter an ineger");
                in.nextLine();
            }
        }
        return x;
    }

    private static double checkDouble(Scanner in) {
        double x;
        for (;;) {
            try {
                x = in.nextDouble();
                break;
            } catch (InputMismatchException ex) {
                System.out.println("You should enter a number");
                in.nextLine();
            }
        }
        return x;
    }

    private static String checkString(Scanner in) {
        String x;
        for (;;) {
            x = in.nextLine();
            if (x.equals("")) {
                System.out.println("This field is required.");
                continue;
            }
            return x;
        }
    }

    private static void printingMenu(Scanner in) {
        boolean cont = true;
        while (cont) {
            System.out.println("Select:");
            System.out.println("1 to show all students");
            System.out.println("2 to show all trainers");
            System.out.println("3 to show all assignments");
            System.out.println("4 to show all courses");
            System.out.println("5 to show all students per course");
            System.out.println("6 to show all trainers per course");
            System.out.println("7 to show all assignments per course");
            System.out.println("8 to show all assignments per course per student");
            System.out.println("9 to show students that belong to more than one courses");
            System.out.println("0 to return to main menu");
            int choice = checkInt(in);
            switch (choice) {
                case 1:
                    Student.printAllStudents();
                    break;

                case 2:
                    Trainer.printAllTrainers();
                    break;

                case 3:
                    Assignment.printAllAssignments();
                    break;

                case 4:
                    Course.printAllCourses();
                    break;

                case 5:
                    List<StudentsPerCourse> studentsPerCourses = spcdao.getStudentsPerCourses();
                    for (StudentsPerCourse spc : studentsPerCourses) {
                        System.out.println(spc.getCourse() + ": ");
                        for (Student s : spc.getStudents()) {
                            System.out.println(s);
                        }
                        System.out.println();
                    }
                    break;

                case 6:
                    List<TrainersPerCourse> trainersPerCourses = tpcdao.getTrainersPerCourses();
                    for (TrainersPerCourse tpc : trainersPerCourses) {
                        System.out.println(tpc.getCourse() + ": ");
                        for (Trainer t : tpc.getTrainers()) {
                            System.out.println(t);
                        }
                    }
                    break;

                case 7:
                    List<AssignmentsPerCourse> assignmentsPerCourses = apcdao.getAssignmentsPerCourses();
                    for (AssignmentsPerCourse apc : assignmentsPerCourses) {
                        System.out.println(apc.getCourse() + ": ");
                        for (Assignment a : apc.getAssignments()) {
                            System.out.println(a);
                        }
                        System.out.println();
                    }
                    break;

                //Printin the assignments of a specific student per course
                case 8:
                    Student s1;
                    for (;;) {
                        System.out.println("Registered students are: ");
                        Student.printAllStudents();
                        System.out.print("Choose the id of the student: ");
                        int sid = checkInt(in);
                        s1 = sdao.getStudentById(sid);
                        if (s1 == null) {
                            System.out.println("Student was not found");
                            continue;
                        }
                        break;
                    }
                    assignmentsPerCourses = apcdao.getAssignmentsPerCoursesByStudent(s1);
                    for (AssignmentsPerCourse apc : assignmentsPerCourses) {
                        System.out.println(apc);
                    }
                    break;

                case 9:
                    List<Student> stWithMult = spcdao.getStudentsWithMultipleCourses();
                    for (Student s : stWithMult) {
                        System.out.println(s);
                    }
                    break;

                case 0:
                    System.out.println("Returning to main menu...\n");
                    cont = false;
                    break;
            }
        }
    }

    private static void insertingMenu(Scanner in) {
        System.out.println("Select:");
        System.out.println("1 to insert a new student");
        System.out.println("2 to insert a new trainer");
        System.out.println("3 to insert a new assignment");
        System.out.println("4 to insert a new course");
        System.out.println("5 to insert a student into a course");
        System.out.println("6 to insert a trainer into a course");
        System.out.println("7 to insert a student's marks for an assignment");
        System.out.println("0 to return to main menu");
        int choice = checkInt(in);
        switch (choice) {
            case 1:
                in.nextLine();
                System.out.println("Insert the following attributes: ");
                System.out.print("First Name: ");
                String firstName = checkString(in);
                System.out.print("Last Name: ");
                String lastName = checkString(in);
                System.out.print("Tuition Fees: ");
                Double tuitionFees = checkDouble(in);
                System.out.print("Date of Birth(dd/MM/yyyy): ");
                LocalDate dob = checkDate(in);
                sdao.insertStudent(new Student(firstName, lastName, tuitionFees, dob));
                break;

            case 2:
                in.nextLine();
                System.out.println("Insert the following attributes: ");
                System.out.print("First Name: ");
                String fname = checkString(in);
                System.out.print("Last Name: ");
                String lname = checkString(in);
                System.out.print("Subject: ");
                String subject = checkString(in);
                tdao.insertTrainer(new Trainer(fname, lname, subject));
                break;

            case 3:
                in.nextLine();
                System.out.println("Insert the following attributes: ");
                System.out.print("Title: ");
                String title = checkString(in);
                System.out.print("Description: ");
                String descr = checkString(in);
                System.out.println("Course id: ");
                Course c;
                for (;;) {
                    System.out.println("Available courses are: ");
                    Course.printAllCourses();
                    int cid = checkInt(in);
                    c = cdao.getCourseById(cid);
                    if (c == null) {
                        System.out.println("Course was not found");
                        continue;
                    }
                    break;
                }
                LocalDate subDate;
                for (;;) {
                    System.out.print("Submission Date: ");
                    subDate = checkDate(in);
                    if (subDate.isAfter(c.getEndDate())) {
                        System.out.println("Submission date is after the end date of the course. Proceed(Y/N)?");
                        in.nextLine();
                        String ans = checkString(in);
                        if (ans.toLowerCase().equals("y")) {
                            break;
                        }
                    } else if (subDate.isBefore(c.getStartDate())) {
                        System.out.println("Submission date is before the start date of the course. Proceed(Y/N)?");
                        in.nextLine();
                        String ans = checkString(in);
                        if (ans.toLowerCase().equals("y")) {
                            break;
                        }
                    }
                }
                adao.insertAssignment(new Assignment(title, descr, subDate, c));
                break;

            case 4:
                in.nextLine();
                System.out.println("Insert the following attributes: ");
                System.out.print("Title: ");
                title = checkString(in);
                System.out.print("Stream: ");
                String stream = checkString(in);
                System.out.print("Start Date: ");
                LocalDate sdate = checkDate(in);
                LocalDate edate;
                for (;;) {
                    System.out.print("End Date: ");
                    edate = checkDate(in);
                    if (edate.isBefore(sdate)) {
                        System.out.println("Please enter an end date that is after start date(" + sdate + ")");
                        continue;
                    }
                    break;
                }
                System.out.print("Type(Part/Full): ");
                String type;
                in.nextLine();
                for (;;) {
                    type = checkString(in);
                    if (!(type.equalsIgnoreCase("full") || type.equalsIgnoreCase("part"))) {
                        System.out.println("Course type should be either \"part\" or \"full\" ");
                        continue;
                    }
                    break;
                }
                cdao.insertCourse(new Course(title, stream, sdate, edate, type));

            case 5:
                in.nextLine();
                Student s;
                for (;;) {
                    System.out.println("Registered students are: ");
                    Student.printAllStudents();
                    System.out.print("Choose the id of the student that you want to add: ");
                    int sid = checkInt(in);
                    s = sdao.getStudentById(sid);
                    if (s == null) {
                        System.out.println("Student was not found");
                        continue;
                    }
                    break;
                }
                Course course1;
                for (;;) {
                    System.out.println("Available courses are: ");
                    Course.printAllCourses();
                    System.out.print("Choose the id of the course: ");
                    int cid = checkInt(in);
                    course1 = cdao.getCourseById(cid);
                    if (course1 == null) {
                        System.out.println("Course was not found");
                        continue;
                    }
                    break;
                }
                spcdao.insertStudentCourse(s, course1);
                break;

            case 6:
                in.nextLine();
                Trainer t;
                for (;;) {
                    System.out.println("Registered trainers are: ");
                    Student.printAllStudents();
                    System.out.print("Choose the id of the trainer that you want to add: ");
                    int tid = checkInt(in);
                    t = tdao.getTrainerById(tid);
                    if (t == null) {
                        System.out.println("Student was not found");
                        continue;
                    }
                    break;
                }
                for (;;) {
                    System.out.println("Available courses are: ");
                    Course.printAllCourses();
                    System.out.print("Choose the id of the course: ");
                    int cid = checkInt(in);
                    course1 = cdao.getCourseById(cid);
                    if (course1 == null) {
                        System.out.println("Course was not found");
                        continue;
                    }
                    break;
                }
                tpcdao.insertTrainerCourse(t, course1);
                break;

            case 7:
                Assignment a ;

              
                for (;;) {
                    System.out.println("Registered students are: ");
                    Student.printAllStudents();
                    System.out.print("Choose the id of the student: ");
                    int sid = checkInt(in);
                    s = sdao.getStudentById(sid);
                    if (s == null) {
                        System.out.println("Student was not found");
                        continue;
                    }
                    break;
                }
                for (;;) {
                    List<AssignmentsPerCourse> assignmentsPerCourses = apcdao.getAssignmentsPerCoursesByStudent(s);
                    for (AssignmentsPerCourse apc : assignmentsPerCourses) {
                        System.out.println(apc);
                    }
                    System.out.print("Choose the id of the assignment: ");
                    int aid = checkInt(in);
                    a = adao.getAssignmentById(aid);
                    if (a == null) {
                        System.out.println("Assignment was not found");
                        continue;
                    }
                    break;
                }
                System.out.println(a.getId());
                System.out.print("Oral Mark: ");
                Double oralMark = checkDouble(in);
                System.out.print("Total Mark: ");
                Double totalMark = checkDouble(in);
                a.setOralMark(oralMark);
                a.setTotalMark(totalMark);
                adao.insertStudentAssignment(s, a);
                break;
            case 0:
                return;
        }
    }

    public static LocalDate checkDate(Scanner sc) {
        LocalDate date;
        for (;;) {
            try {
                String dateAsString = sc.next();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                date = LocalDate.parse(dateAsString, formatter);
                return date;
            } catch (DateTimeParseException dateTimeParseException) {
                System.out.println("Date does not exist. Enter a valid date in form dd/MM/yyyy");
            }
        }
    }

    private static boolean logIn(Scanner in) {
        for (int i = 1; i <= 3; i++) {
            System.out.print("Username: ");
            String username = checkString(in);
            System.out.print("Password: ");
            String password = checkString(in);
            if (udao.getUser(new User(username, password))) {
                return true;
            }
            System.out.println("Wrong username or password");
            System.out.println((3 - i) + " attempts left");

        }
        return false;
    }

}
