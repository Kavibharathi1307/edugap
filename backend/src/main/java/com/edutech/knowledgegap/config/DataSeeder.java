package com.edutech.knowledgegap.config;

import com.edutech.knowledgegap.entity.*;
import com.edutech.knowledgegap.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final String DEMO_EMAIL = "student@edugap.ac.in";
    private static final String DEMO_PASSWORD = "Password@123";

    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final LearningResourceRepository resourceRepository;
    private final StudentRepository studentRepository;
    private final QuizAttemptRepository attemptRepository;
    private final LearningRecommendationRepository recommendationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(SubjectRepository subjectRepository,
                      TopicRepository topicRepository,
                      QuestionRepository questionRepository,
                      QuestionOptionRepository optionRepository,
                      LearningResourceRepository resourceRepository,
                      StudentRepository studentRepository,
                      QuizAttemptRepository attemptRepository,
                      LearningRecommendationRepository recommendationRepository,
                      PasswordEncoder passwordEncoder) {
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.resourceRepository = resourceRepository;
        this.studentRepository = studentRepository;
        this.attemptRepository = attemptRepository;
        this.recommendationRepository = recommendationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        ensureDemoStudent();

        Subject java = ensureSubject("JAVA", "Java Programming",
                "Core language fundamentals, object-oriented design, collections, and modern Java features.",
                "coffee", 1);
        Subject python = ensureSubject("PYTHON", "Python Programming",
                "Python syntax, control flow, functions, object-oriented programming, and data handling.",
                "terminal", 2);
        Subject dsa = ensureSubject("DSA", "Data Structures and Algorithms",
                "Arrays, linked lists, stacks, queues, trees, and algorithmic problem solving.",
                "account_tree", 3);
        Subject dbms = ensureSubject("DBMS", "Database Management Systems",
                "Relational modelling, SQL, normalization, indexing, and transactions.",
                "storage", 4);
        Subject networks = ensureSubject("CN", "Computer Networks",
                "Network layers, TCP/IP, routing, DNS, and core internet protocols.",
                "lan", 5);
        Subject math = ensureSubject("MATH", "Mathematics",
                "Algebra, probability, and statistics foundations for computing.",
                "calculate", 6);

        backfillLegacyData(java);
        repairResourceUrls();
        backfillRecommendationResources();

        // Seed the Java curriculum only when it is missing, even if other subjects already exist.
        if (topicRepository.countBySubjectId(java.getId()) > 0) {
            seedIfEmpty(python);
            seedIfEmpty(dsa);
            seedIfEmpty(dbms);
            seedIfEmpty(networks);
            seedIfEmpty(math);
            return;
        }

        // ==========================================
        // 1. JAVA FUNDAMENTALS
        // ==========================================
        Topic t1 = topicRepository.save(new Topic(
                "Java Fundamentals",
                "Understand the core architecture of Java: JVM, JRE, JDK, bytecode, and write your first Hello World program.",
                "1. Differentiate JVM, JRE, and JDK.\n2. Understand WORA (Write Once, Run Anywhere).\n3. Master Java program structure and compilation process.",
                "Java is a high-level, class-based, object-oriented programming language. When you compile Java source code (.java), it turns into platform-independent Bytecode (.class). The Java Virtual Machine (JVM) interprets and executes this bytecode on any operating system.",
                "Confusing JDK with JRE, omitting the public static void main method signature, or forgetting that Java is strictly case-sensitive.",
                "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, EduGap!\");\n    }\n}",
                1
        ));
        seedResource(t1, "Introduction to Java Architecture (JVM/JRE/JDK)", "ARTICLE", "https://docs.oracle.com/en/java/", "Learn how Java bytecode enables platform independence across architectures.", "BEGINNER");
        seedResource(t1, "Bytecode & Execution Walkthrough", "CODE_EXAMPLE", "https://dev.java/learn/", "Deconstructing the main method and console input/output.", "BEGINNER");

        addQuestion(t1, "What does the Java Virtual Machine (JVM) directly execute?",
                "Java Source Code (.java)", "Java Bytecode (.class)", "Native Machine Assembly", "C++ Source Code",
                "B", "The Java compiler (javac) converts .java source code into .class bytecode, which is directly executed by the JVM.", "BEGINNER");

        addQuestion(t1, "Which component is strictly required to compile Java source code?",
                "JRE only", "JVM only", "JDK (Java Development Kit)", "Java Applet Viewer",
                "C", "The JDK includes development tools like the compiler (javac) along with the JRE and JVM.", "BEGINNER");

        addQuestion(t1, "Why is Java considered platform independent?",
                "Java programs execute directly on the computer hardware without any runtime.",
                "Java Bytecode can execute on any operating system that has a compatible JVM installed.",
                "Java converts all code directly into browser-compatible JavaScript.",
                "Java does not support native pointers or memory addresses.",
                "B", "Platform independence is achieved through Bytecode: write once on any OS, and execute anywhere with a compatible JVM.", "INTERMEDIATE");

        addQuestion(t1, "What is the exact signature required for a standard Java application entry point?",
                "public void main(String[] args)",
                "public static void main(String[] args)",
                "static void main(String args)",
                "public int main(String[] args)",
                "B", "The Java runtime looks specifically for public static void main(String[] args) to start execution.", "BEGINNER");

        addQuestion(t1, "Which tool is used to debug Java applications?",
                "JDB (Java Debugger)", "JVM Inspector", "Java Profiler", "APM Tool",
                "A", "JDB (Java Debugger) is a simple command-line debugger that comes with the JDK.", "BEGINNER");

        // ==========================================
        // 2. VARIABLES AND DATA TYPES
        // ==========================================
        Topic t2 = topicRepository.save(new Topic(
                "Variables and Data Types",
                "Master Java primitive types, reference types, type casting, scope, and memory allocation in stack vs. heap.",
                "1. Identify the 8 primitive data types.\n2. Understand widening vs. narrowing casting.\n3. Recognize default values and variable scope.",
                "In Java, every variable has a declared type. Primitive types (byte, short, int, long, float, double, char, boolean) store values directly in memory. Reference types store memory addresses pointing to objects.",
                "Attempting to assign a larger primitive (like double or long) to an int without explicit type casting; uninitialized local variables.",
                "int age = 21;\ndouble gpa = 3.85;\nchar grade = 'A';\nboolean isEnrolled = true;\n\n// Explicit narrowing cast\nint roundedGpa = (int) gpa;",
                2
        ));
        seedResource(t2, "Java Primitive Types & Memory Footprint", "ARTICLE", "https://docs.oracle.com/javase/tutorial/essential/java/datatypes.html", "Detailed breakdown of byte sizes, min/max values, and IEEE floating points.", "BEGINNER");
        seedResource(t2, "Type Casting Rules & Overflow Prevention", "CHEATSHEET", "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html", "Widening vs Narrowing casting reference table.", "INTERMEDIATE");

        addQuestion(t2, "What is the size of an int primitive in Java?",
                "16 bits (2 bytes)", "32 bits (4 bytes)", "64 bits (8 bytes)", "Platform dependent",
                "B", "In Java, an int is consistently 32 bits (4 bytes) signed two's complement across all platforms.", "BEGINNER");

        addQuestion(t2, "Which line will result in a compile-time error?",
                "double d = 100;", "int x = (int) 45.67;", "int num = 10.5;", "float f = 12.3f;",
                "C", "Assigning a double literal (10.5) to an int variable requires an explicit cast: (int) 10.5.", "BEGINNER");

        addQuestion(t2, "What is the default value of an uninitialized boolean instance variable in a class?",
                "true", "false", "null", "0",
                "B", "Instance boolean fields default to false in Java. (Local variables must be explicitly initialized).", "BEGINNER");

        addQuestion(t2, "What will be printed by: byte b = (byte) 130; System.out.println(b);?",
                "130", "-126", "127", "Compilation Error",
                "B", "A Java byte ranges from -128 to 127. 130 overflows 127 and wraps around to -126 in two's complement representation.", "ADVANCED");

        addQuestion(t2, "Which of the following is a valid Java literal for a double?",
                "3.14f", "3.14d", "3.14i", "3.14l",
                "B", "The 'd' suffix denotes a double literal; 'f' denotes float, 'i' is int, and 'l' is long.", "BEGINNER");

        // ==========================================
        // 3. CONDITIONAL STATEMENTS
        // ==========================================
        Topic t3 = topicRepository.save(new Topic(
                "Conditional Statements",
                "Control application execution flow using if-else ladders, nested conditionals, ternary operator, and switch statements.",
                "1. Formulate boolean expressions with logical operators.\n2. Implement if, else-if, and nested conditions.\n3. Utilize modern switch statements and avoid fall-through bugs.",
                "Conditionals evaluate boolean expressions to choose execution paths. Java supports traditional if-else structures and switch statements with pattern matching.",
                "Using single '=' (assignment) instead of '==' (equality); forgetting 'break' in traditional switch blocks causing fall-through.",
                "int score = 82;\nString result = (score >= 75) ? \"Distinction\" : \"Pass\";\n\nswitch (score / 10) {\n    case 10, 9 -> System.out.println(\"Grade A\");\n    case 8 -> System.out.println(\"Grade B\");\n    default -> System.out.println(\"Review Needed\");\n}",
                3
        ));
        seedResource(t3, "Mastering Decision Making in Java", "ARTICLE", "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/if.html", "Control flow logic, truth tables, and short-circuit evaluation.", "BEGINNER");

        addQuestion(t3, "What is the result of true || false && false?",
                "false", "true", "Compilation error", "null",
                "B", "In Java, logical AND (&&) has higher operator precedence than logical OR (||). So (false && false) evaluates to false, and (true || false) evaluates to true.", "INTERMEDIATE");

        addQuestion(t3, "What happens if a traditional switch case does not include a 'break' statement?",
                "The program throws an IllegalArgumentException.",
                "Execution falls through to the subsequent cases until a break or end of switch is reached.",
                "The code will fail to compile.",
                "The switch statement restarts from the beginning.",
                "B", "Without break, execution continues linearly into the following cases (fall-through behavior).", "BEGINNER");

        addQuestion(t3, "Which of the following types CANNOT be used in a switch expression prior to Java 21 pattern matching?",
                "int", "String", "boolean", "enum",
                "C", "Prior to switch pattern matching, switch expressions supported byte, short, char, int, enum, and String, but never boolean.", "INTERMEDIATE");

        addQuestion(t3, "What will: int x = 5; System.out.println(x > 2 ? x < 4 ? 10 : 20 : 30); output?",
                "10", "20", "30", "Compilation error",
                "B", "Since x > 2 is true, it evaluates the inner ternary: x < 4 is false (5 < 4 is false), returning 20.", "INTERMEDIATE");

        addQuestion(t3, "Which of the following is the correct way to declare a switch variable in Java 12+?",
                "switch (int x) {}", "switch (Integer x) {}", "switch (x int) {}", "switch (int x: arr) {}",
                "A", "Java 12+ allows switch expressions with primitive types directly wrapped in classes.", "INTERMEDIATE");

        // ==========================================
        // 4. LOOPS
        // ==========================================
        Topic t4 = topicRepository.save(new Topic(
                "Loops",
                "Iterate over data structures and repeat algorithms using for loops, enhanced for-each, while, do-while, and break/continue.",
                "1. Choose between for, while, and do-while.\n2. Iterate collections using enhanced for-each.\n3. Control loop cycles with break, continue, and labeled statements.",
                "Loops execute a code block repeatedly while a condition remains true. The while loop checks conditions first; do-while executes at least once before checking.",
                "Off-by-one errors in index boundaries (e.g. i <= arr.length); infinite loops caused by missing counter increments.",
                "for (int i = 0; i < 5; i++) {\n    if (i == 2) continue;\n    System.out.println(\"Iteration: \" + i);\n}\n\nint count = 0;\ndo {\n    count++;\n} while (count < 3);",
                4
        ));
        seedResource(t4, "Looping Constructs & Performance Optimization", "ARTICLE", "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/loop.html", "Step-by-step breakdown of loop mechanics and efficiency.", "BEGINNER");

        addQuestion(t4, "How many times does the body of a do-while loop execute at minimum?",
                "0 times", "1 time", "Depends on condition", "Infinite times",
                "B", "A do-while loop evaluates its condition after executing the body, ensuring at least one execution.", "BEGINNER");

        addQuestion(t4, "What does the 'continue' keyword do inside a loop?",
                "Immediately exits the entire loop.",
                "Skips the remainder of the current iteration and jumps to the next cycle.",
                "Restarts the program from the main method.",
                "Pauses execution for 100 milliseconds.",
                "B", "The continue statement aborts the remaining statements in the active iteration and advances to the next iteration step.", "BEGINNER");

        addQuestion(t4, "What will be the output of: for (int i = 0; i < 3; i++) { if (i == 1) break; System.out.print(i); }?",
                "0", "01", "012", "Compilation error",
                "A", "When i = 0, 0 is printed. Next, i becomes 1; the condition (i == 1) triggers break, instantly exiting the loop before printing 1.", "BEGINNER");

addQuestion(t4, "What is a key limitation of the enhanced for-each loop in Java arrays?",
                "It cannot iterate backwards or directly modify element values by index.",
                "It is slower than a while loop by a factor of 10.",
                "It only works with Strings, not primitive numbers.",
                "It requires wrapping all numbers in ArrayList.",
                "A", "The enhanced for-each loop provides read-only traversal without exposed numeric indices, making reverse iteration impossible without manual counters.", "INTERMEDIATE");

        addQuestion(t4, "What will be the output of: for (int i = 0; i < 3; i++) { if (i == 1) break; System.out.print(i); }?",
                "0", "01", "012", "Compilation error",
                "A", "When i = 0, 0 is printed. Next, i becomes 1; the condition (i == 1) triggers break, instantly exiting the loop before printing 1.", "BEGINNER");

        addQuestion(t4, "Which of the following can be used to prevent infinite loop execution?",
                "A labeled break statement", "A while loop with true condition", "A for loop without increment", "A do-while loop with immediate break",
                "A", "A labeled break statement can exit enclosed loops beyond the immediate loop.", "BEGINNER");

        // ==========================================
        // 5. ARRAYS
        // ==========================================
        Topic t5 = topicRepository.save(new Topic(
                "Arrays",
                "Understand fixed-size indexed data collections, multi-dimensional arrays, ArrayIndexOutOfBoundsException, and java.util.Arrays utilities.",
                "1. Declare, instantiate, and initialize 1D and 2D arrays.\n2. Access elements by zero-based indexing.\n3. Utilize java.util.Arrays methods (sort, binarySearch, equals).",
                "An array is a fixed-size container that holds elements of a single homogeneous type. In Java, arrays are objects allocated on the heap, indexed starting from zero.",
                "Attempting to resize a Java array after instantiation; indexing arr[arr.length] instead of arr[arr.length - 1].",
                "int[] numbers = {10, 20, 30, 40};\nSystem.out.println(\"First element: \" + numbers[0]);\nSystem.out.println(\"Length: \" + numbers.length);\n\njava.util.Arrays.sort(numbers);",
                5
        ));
        seedResource(t5, "Java Arrays in Depth", "ARTICLE", "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html", "Memory layout of arrays, contiguous memory allocation, and array utilities.", "BEGINNER");

        addQuestion(t5, "What exception is thrown if you access arr[5] on an array of length 5?",
                "NullPointerException", "ArrayIndexOutOfBoundsException", "IllegalArgumentException", "IndexUnderflowException",
                "B", "Java arrays are 0-indexed; an array of length 5 has valid indices 0 through 4. Accessing index 5 throws ArrayIndexOutOfBoundsException.", "BEGINNER");

        addQuestion(t5, "What is the default value of elements in a freshly allocated int array: int[] arr = new int[4];?",
                "null", "0", "-1", "undefined",
                "B", "Numeric primitive arrays are automatically initialized with 0 for all elements upon allocation.", "BEGINNER");

        addQuestion(t5, "Which method correctly sorts a primitive array in ascending order?",
                "arr.sort()", "java.util.Arrays.sort(arr)", "Collections.sort(arr)", "System.arraySort(arr)",
                "B", "Arrays.sort() is the standard utility method for sorting primitive and object arrays in Java.", "BEGINNER");

addQuestion(t5, "Can the length of an instantiated Java array be changed dynamically?",
                "Yes, using arr.length = newSize;",
                "Yes, using arr.resize(newSize);",
                "No, Java arrays have a fixed size upon creation.",
                "Yes, if created with the dynamic keyword.",
                "C", "Array dimensions are immutable once created in memory. To support dynamic resizing, ArrayList is used.", "INTERMEDIATE");

        addQuestion(t5, "Which of the following produces a compile error when assigning to an array element?",
                "arr[0] = 42;", "arr[0] = (double) 3.14;", "arr[0] = 'A';", "arr[-1] = 9;",
                "B", "Assigning a double to an int array element requires an explicit cast in Java.", "BEGINNER");

        // ==========================================
        // 6. STRINGS
        // ==========================================
        Topic t6 = topicRepository.save(new Topic(
                "Strings",
                "Master String immutability, the String Pool, StringBuilder vs. StringBuffer, and essential manipulation methods.",
                "1. Understand String immutability and memory optimization.\n2. Differentiate '==' (reference check) and '.equals()' (value check).\n3. Use StringBuilder for efficient string concatenation.",
                "Strings in Java are immutable objects. Once created, their character sequence cannot be altered. Literal strings are cached in the String Constant Pool inside the heap.",
                "Using '==' to compare string content instead of '.equals()'; concatenating strings inside large loops using '+' instead of StringBuilder.",
                "String s1 = \"EduGap\";\nString s2 = new String(\"EduGap\");\n\n// s1 == s2 is false (different references)\n// s1.equals(s2) is true (same content)\n\nStringBuilder sb = new StringBuilder();\nsb.append(\"Java\").append(\" 21\");",
                6
        ));
        seedResource(t6, "String Constant Pool & Immutability Explained", "ARTICLE", "https://dev.java/learn/strings/", "Why Strings are immutable in Java and how heap pooling saves memory.", "BEGINNER");

        addQuestion(t6, "Why is String immutable in Java?",
                "For security, thread-safety, caching hash codes, and allowing the String Constant Pool.",
                "Because Java does not have pointers to update values.",
                "To prevent developers from making spelling mistakes in literals.",
                "Because Java classes cannot be extended.",
                "A", "Immutability guarantees security in network/classloading parameters, thread safety without synchronization, and enables String pooling.", "INTERMEDIATE");

addQuestion(t6, "What will: String a = \"hello\"; String b = \"hello\"; System.out.println(a == b); print?",
                "false", "true", "Compilation error", "NullPointerException",
                "B", "Both literals refer to the exact same pooled instance in the String Constant Pool, so their references match.", "BEGINNER");

        addQuestion(t6, "Which class should you choose for frequent string modifications in a single-threaded environment?",
                "String", "StringBuffer", "StringBuilder", "char[]",
                "C", "StringBuilder is mutable and non-synchronized, offering faster performance than synchronized StringBuffer for single threads.", "INTERMEDIATE");

        addQuestion(t6, "What is the purpose of StringBuilder's ensureCapacity method?",
                "To trim the capacity to the current length", "To ensure the capacity is at least the specified argument", "To convert StringBuilder to String", "To reverse the string content",
                "B", "ensureCapacity ensures the internal buffer can hold at least the specified number of characters.", "ADVANCED");

        addQuestion(t6, "Which method converts StringBuilder to a String?",
                "toString()", "toString(String)", "String.valueOf()", "toString()",
                "A", "toString() returns the String representation of the StringBuilder content.", "BEGINNER");

        // ==========================================
        // 7. METHODS
        // ==========================================
        Topic t7 = topicRepository.save(new Topic(
                "Methods",
                "Design reusable logic using method signatures, return types, pass-by-value semantics, method overloading, and recursion.",
                "1. Define modular methods with parameters and return types.\n2. Master Java's pass-by-value evaluation.\n3. Implement method overloading based on signatures.",
                "Methods encapsulate actions within a class. Java strictly uses pass-by-value for all argument passing (passing a copy of the primitive value or a copy of the object reference).",
                "Thinking Java has pass-by-reference; trying to overload methods solely by altering their return types without changing parameter lists.",
                "public class MathHelper {\n    public static int add(int a, int b) {\n        return a + b;\n    }\n    // Overloaded method\n    public static double add(double a, double b) {\n        return a + b;\n    }\n}",
                7
        ));
        seedResource(t7, "Method Design, Signatures & Pass-by-Value", "ARTICLE", "https://docs.oracle.com/javase/tutorial/java/javaI/methods.html", "Deep dive into JVM call frames, stack activation records, and overloading.", "BEGINNER");

        addQuestion(t7, "How does Java pass arguments to methods?",
                "Primitives by value, objects by reference.",
                "Strictly Pass-by-Value for both primitives and object references.",
                "Strictly Pass-by-Reference for all types.",
                "Configurable via compiler flags.",
                "B", "Java is strictly pass-by-value. For objects, a copy of the reference address is passed by value.", "INTERMEDIATE");

        addQuestion(t7, "Which factor distinguishes overloaded methods in Java?",
                "Different return types only",
                "Different parameter lists (number, type, or order of parameters)",
                "Different access modifiers (e.g. public vs private)",
                "Different throws clause exceptions",
                "B", "Method overloading requires changing the parameter count, types, or order. The return type alone cannot distinguish overloads.", "BEGINNER");

        addQuestion(t7, "What is a recursive method in Java?",
                "A method that calls another method with identical parameters.",
                "A method that calls itself and has a defined base case to terminate.",
                "A method executed inside a thread loop.",
                "A method that cannot return any value.",
                "B", "Recursion occurs when a method calls itself to break down a problem, stopping when a base condition is satisfied.", "BEGINNER");

addQuestion(t7, "What keyword allows a method to accept a variable number of arguments (varargs)?",
                "params", "...", "var", "args[]",
                "B", "The ellipsis (...) syntax (e.g. int... numbers) denotes a variable-length argument list treated as an array inside the method.", "INTERMEDIATE");

        addQuestion(t7, "Which of the following can method overloading change?",
                "Method name only", "Parameter list (count, type, or order)", "Return type only", "Visibility modifier",
                "B", "Method overloading requires changing the parameter list (number, type, or order of parameters).", "BEGINNER");

        // ==========================================
        // 8. OBJECT-ORIENTED PROGRAMMING
        // ==========================================
        Topic t8 = topicRepository.save(new Topic(
                "Object-Oriented Programming",
                "Apply the 4 pillars of OOP: Encapsulation, Inheritance, Polymorphism, and Abstraction to construct scalable software.",
                "1. Enforce Encapsulation with private fields and public getters/setters.\n2. Extend classes using Inheritance (extends).\n3. Apply Runtime Polymorphism with method overriding (@Override).\n4. Design contracts with Abstract Classes and Interfaces.",
                "OOP models real-world concepts as objects containing state (fields) and behavior (methods). It promotes reusability, modularity, and maintainable software architecture.",
                "Confusing method overloading with method overriding; forgetting that Java does not support multiple class inheritance.",
                "public abstract class Animal {\n    private String name;\n    public Animal(String name) { this.name = name; }\n    public abstract void makeSound();\n}\n\npublic class Dog extends Animal {\n    public Dog(String name) { super(name); }\n    @Override\n    public void makeSound() {\n        System.out.println(\"Bark!\");\n    }\n}",
                8
        ));
        seedResource(t8, "The 4 Pillars of OOP in Java", "ARTICLE", "https://dev.java/learn/oop/", "Comprehensive breakdown of Encapsulation, Inheritance, Polymorphism, and Abstraction.", "BEGINNER");
        seedResource(t8, "Polymorphism & Dynamic Method Dispatch", "CODE_EXAMPLE", "https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html", "How the JVM resolves method invocations at runtime.", "ADVANCED");

        addQuestion(t8, "Which OOP pillar is demonstrated by declaring fields private and exposing public getters and setters?",
                "Inheritance", "Polymorphism", "Encapsulation", "Abstraction",
                "C", "Encapsulation bundles data with the methods that operate on it and restricts direct access to internal state.", "BEGINNER");

        addQuestion(t8, "Can a Java class directly inherit (extend) from multiple classes?",
                "Yes, using comma-separated class names.",
                "No, Java supports single class inheritance to avoid the diamond problem.",
                "Yes, if all parent classes are abstract.",
                "Only in Java 21 or later.",
                "B", "Java does not support multiple class inheritance, preventing the diamond ambiguity problem. Multiple interface implementation is supported.", "BEGINNER");

        addQuestion(t8, "What is Runtime Polymorphism (Dynamic Method Dispatch)?",
                "Calling different overloaded methods based on compile-time types.",
                "Resolving an overridden method call at runtime based on the actual object instance type.",
                "Converting an integer to a double automatically.",
                "Compiling bytecode into machine instructions.",
                "B", "When a superclass reference points to a subclass instance, the JVM dynamically invokes the subclass's overridden method at runtime.", "INTERMEDIATE");

        addQuestion(t8, "What is the primary difference between an interface and an abstract class in modern Java?",
                "Interfaces cannot have any methods with bodies.",
                "A class can implement multiple interfaces, but can extend only one abstract class.",
                "Abstract classes cannot contain constructors.",
                "Interfaces can store instance state in non-final variables.",
                "B", "A class can extend only one class (abstract or concrete) but can implement multiple interfaces, providing modular contracts.", "INTERMEDIATE");

        addQuestion(t8, "Which of the following is true about Java's default interface methods?",
                "They cannot have a body.", "They can have a body with a default implementation.", "They are private by default.", "They cannot be overridden.",
                "B", "Java 8+ allows interface methods to have default implementation using the 'default' keyword.", "INTERMEDIATE");

        addQuestion(t8, "A class can implement multiple interfaces but can extend only one abstract class. What is this principle called?",
                "Inheritance", "Polymorphism", "Encapsulation", "Abstraction",
                "B", "This is a fundamental design principle in Java allowing modular contracts via interfaces.", "BEGINNER");

        addQuestion(t8, "Which of the following statements about Java interfaces is correct?",
                "Interfaces cannot have any methods with bodies.", "All interface methods must be abstract.", "Interfaces cannot be implemented by classes.", "Interfaces cannot extend other interfaces.",
                "A", "Java 8+ introduced default methods in interfaces, but prior to Java 8, all interface methods were implicitly abstract.", "BEGINNER");

        linkUnassignedTopics(java);
        seedIfEmpty(python);
        seedIfEmpty(dsa);
        seedIfEmpty(dbms);
        seedIfEmpty(networks);
        seedIfEmpty(math);
    }

    private Subject ensureSubject(String code, String name, String description, String icon, int orderIndex) {
        return subjectRepository.findByCodeIgnoreCase(code).orElseGet(() ->
                subjectRepository.save(new Subject(code, name, description, icon, orderIndex)));
    }

    private void backfillLegacyData(Subject java) {
        linkUnassignedTopics(java);

        List<QuizAttempt> legacyAttempts = attemptRepository.findAllBySubjectIsNull();
        for (QuizAttempt attempt : legacyAttempts) {
            if (attempt.getSubjectName() == null
                    || Set.of("Core Java", "Java", "Java Programming").contains(attempt.getSubjectName())) {
                attempt.setSubject(java);
                attemptRepository.save(attempt);
            }
        }
    }

    private void linkUnassignedTopics(Subject java) {
        for (Topic topic : topicRepository.findBySubjectIsNull()) {
            if (topic.getSubjectName() == null
                    || Set.of("Core Java", "Java", "Java Programming").contains(topic.getSubjectName())) {
                topic.setSubject(java);
                topic.setSubjectName(java.getName());
                topicRepository.save(topic);
            }
        }
    }

    private void seedIfEmpty(Subject subject) {
        if (topicRepository.countBySubjectId(subject.getId()) > 0) {
            return;
        }
        switch (subject.getCode()) {
            case "PYTHON" -> seedPython(subject);
            case "DSA" -> seedDsa(subject);
            case "DBMS" -> seedDbms(subject);
            case "CN" -> seedNetworks(subject);
            case "MATH" -> seedMath(subject);
            default -> { }
        }
    }

    private Topic topic(Subject subject, String name, String description, String objectives,
                        String explanation, String mistakes, String example, int order) {
        Topic topic = new Topic(subject, name, description, objectives, explanation,
                mistakes, example, order);
        return topicRepository.save(topic);
    }

    private void seedPython(Subject subject) {
        Topic variables = topic(subject, "Variables and Data Types",
                "Use Python's dynamic typing and core built-in data types.",
                "1. Declare variables.\n2. Use int, float, str, bool, list, dict.\n3. Convert between types.",
                "Python variables are names bound to objects. The type belongs to the object, not the variable name.",
                "Assuming variables are statically typed; mutating a tuple; confusing assignment with equality.",
                "name = \"Ada\"\nage = 21\nscores = [90, 85, 77]\nprofile = {\"name\": name, \"age\": age}\nprint(int(\"42\") + 8)",
                1);
        seedResource(variables, "Python Data Types Guide", "ARTICLE", "https://docs.python.org/3/tutorial/introduction.html", "Official introduction to Python values and types.", "BEGINNER");
        addQuestion(variables, "Which Python type stores an ordered, immutable sequence?",
                "list", "tuple", "dict", "set", "B", "Tuples are ordered and immutable, unlike lists which can change.", "BEGINNER");
        addQuestion(variables, "What is the result of type(3 / 2) in Python 3?",
                "int", "float", "str", "complex", "B", "The / operator always performs true division and returns a float.", "BEGINNER");
        addQuestion(variables, "Which literal creates a dictionary?",
                "[1, 2, 3]", "{1, 2, 3}", "{\"a\": 1}", "(1, 2, 3)", "C", "Curly braces with key-value pairs create a dict; a bare set uses curly braces too.", "BEGINNER");

        Topic control = topic(subject, "Control Flow",
                "Branch and iterate with if, for, while, break, and continue.",
                "1. Write conditional branches.\n2. Iterate sequences.\n3. Use loop control statements.",
                "Python uses indentation to define blocks instead of braces.",
                "Mixing tabs and spaces; forgetting the colon after a condition; modifying a list while iterating it.",
                "for n in range(3):\n    if n == 1:\n        continue\n    print(n)\n\nif scores:\n    print(\"Has scores\")",
                2);
        seedResource(control, "Python Control Flow", "ARTICLE", "https://docs.python.org/3/tutorial/controlflow.html", "if statements, for loops, range, and loop control.", "BEGINNER");
        addQuestion(control, "How many times does range(2, 6) iterate?",
                "3", "4", "5", "6", "B", "range(2, 6) yields 2, 3, 4, 5 inclusive of start and exclusive of stop.", "BEGINNER");
        addQuestion(control, "What does continue do inside a loop?",
                "Exits the loop", "Skips the rest of the current iteration", "Restarts the loop", "Raises an error", "B", "continue jumps to the next iteration without executing the remaining body.", "BEGINNER");
        addQuestion(control, "Which statement is required to end a Python block header line?",
                "Semicolon", "Colon", "Brace", "Keyword end", "B", "Python block headers end with a colon followed by an indented block.", "BEGINNER");

        Topic functions = topic(subject, "Functions",
                "Build reusable functions with parameters, defaults, return values, and scope.",
                "1. Define functions with def.\n2. Use default and keyword arguments.\n3. Understand local and global scope.",
                "Functions group reusable logic. Arguments are passed by object reference, and immutable objects behave like pass-by-value.",
                "Using a mutable default argument; shadowing built-ins; forgetting to return a value.",
                "def greet(name, punctuation=\"!\"):\n    return f\"Hello, {name}{punctuation}\"\n\nprint(greet(\"Ada\"))\nprint(greet(\"Ada\", \"?\"))",
                3);
        seedResource(functions, "Defining Functions", "ARTICLE", "https://docs.python.org/3/tutorial/controlflow.html#defining-functions", "Parameters, defaults, keyword arguments, and return values.", "BEGINNER");
        addQuestion(functions, "What is printed by def f(x=[]): x.append(1); return x; then f(); f()?",
                "[1] then [1]", "[1] then [1, 1]", "[] then [1]", "Raises an error", "B", "Default arguments are evaluated once, so the same list is reused across calls.", "ADVANCED");
        addQuestion(functions, "Which keyword returns a value from a Python function?",
                "yield", "return", "break", "export", "B", "return sends a value back to the caller and ends the function.", "BEGINNER");
        addQuestion(functions, "What is the local variable scope inside a function?",
                "Accessible everywhere", "Accessible throughout the module", "Accessible only inside the function", "Always global", "C", "Variables assigned inside a function are local unless declared global.", "BEGINNER");

        Topic oop = topic(subject, "Object-Oriented Python",
                "Model data and behaviour with classes, inheritance, and special methods.",
                "1. Define classes and instances.\n2. Use __init__ and self.\n3. Override methods and use inheritance.",
                "Python classes create objects with attributes and methods. self refers to the current instance.",
                "Forgetting self in method signatures; mutable class attributes shared across instances; misunderstanding inheritance order.",
                "class Student:\n    def __init__(self, name):\n        self.name = name\n\n    def greet(self):\n        return f\"Hi, {self.name}\"\n\nprint(Student(\"Ada\").greet())",
                4);
        seedResource(oop, "Python Classes Tutorial", "ARTICLE", "https://docs.python.org/3/tutorial/classes.html", "Classes, instances, inheritance, and special methods.", "INTERMEDIATE");
        addQuestion(oop, "Which method initializes a newly created Python object?",
                "__new__", "__init__", "__str__", "__call__", "B", "__init__ runs after instance creation to initialize attributes.", "BEGINNER");
        addQuestion(oop, "What does self refer to inside an instance method?",
                "The class", "The current instance", "The parent class", "The module", "B", "self is the conventional name for the current instance passed automatically.", "BEGINNER");
        addQuestion(oop, "Which syntax inherits from a class named Base?",
                "class Child extends Base:", "class Child(Base):", "class Child inherits Base:", "class Child : Base", "B", "Python places base classes in parentheses after the class name.", "BEGINNER");
    }

    private void seedDsa(Subject subject) {
        Topic arrays = topic(subject, "Arrays",
                "Store contiguous elements and analyse indexing and complexity.",
                "1. Traverse arrays.\n2. Understand O(1) indexing.\n3. Apply two-pointer techniques.",
                "Arrays store elements in contiguous memory, giving constant-time indexing and linear-time insertion or deletion.",
                "Off-by-one errors; confusing length and last index; ignoring space complexity.",
                "nums = [3, 1, 4]\nfor i in range(len(nums)):\n    print(i, nums[i])",
                1);
        seedResource(arrays, "Array Data Structure", "ARTICLE", "https://www.geeksforgeeks.org/array-data-structure/", "Array operations, complexity, and common patterns.", "BEGINNER");
        addQuestion(arrays, "What is the time complexity of accessing an array element by index?",
                "O(1)", "O(log n)", "O(n)", "O(n log n)", "A", "Contiguous storage allows direct address calculation in constant time.", "BEGINNER");
        addQuestion(arrays, "What is the last valid index of an array of length n?",
                "n", "n - 1", "n + 1", "0", "B", "Array indices start at 0, so the final valid index is n - 1.", "BEGINNER");
        addQuestion(arrays, "Which operation is O(n) on an unsorted array?",
                "Index access", "Linear search", "Reading length", "Updating arr[0]", "B", "Linear search may inspect every element in the worst case.", "BEGINNER");

        Topic lists = topic(subject, "Linked Lists",
                "Represent sequences with nodes connected by references.",
                "1. Traverse singly linked lists.\n2. Insert and delete nodes.\n3. Compare linked lists with arrays.",
                "A linked list stores nodes containing data and a reference to the next node, allowing efficient insertion when the position is known.",
                "Losing the head reference; not handling empty lists; forgetting to update tail pointers.",
                "class Node:\n    def __init__(self, value, next=None):\n        self.value = value\n        self.next = next",
                2);
        seedResource(lists, "Linked List Basics", "ARTICLE", "https://www.geeksforgeeks.org/linked-list-data-structure/", "Singly and doubly linked list operations and complexity.", "BEGINNER");
        addQuestion(lists, "What does the last node of a singly linked list point to?",
                "The head", "Itself", "None", "The tail", "C", "The final node's next reference is null/None, marking the end.", "BEGINNER");
        addQuestion(lists, "What is the time complexity of inserting at the head of a linked list?",
                "O(1)", "O(log n)", "O(n)", "O(n^2)", "A", "Inserting at the head only requires updating the new node's next and the head pointer.", "BEGINNER");
        addQuestion(lists, "Which structure supports backward traversal most directly?",
                "Singly linked list", "Doubly linked list", "Stack", "Queue", "B", "Doubly linked lists store previous and next references, enabling backward traversal.", "INTERMEDIATE");

        Topic stacks = topic(subject, "Stacks",
                "Apply last-in-first-out processing for parsing and backtracking.",
                "1. Push and pop elements.\n2. Recognize LIFO behaviour.\n3. Apply stacks to balanced parentheses.",
                "A stack supports push and pop at one end, following last-in-first-out ordering.",
                "Using a stack where FIFO is required; popping an empty stack; confusing top with bottom.",
                "stack = []\nstack.append(1)\nstack.append(2)\nprint(stack.pop())  # 2",
                3);
        seedResource(stacks, "Stack Data Structure", "ARTICLE", "https://www.geeksforgeeks.org/stack-data-structure/", "LIFO operations, implementations, and applications.", "BEGINNER");
        addQuestion(stacks, "Which order does a stack use?",
                "FIFO", "LIFO", "Random", "Priority", "B", "A stack removes the most recently added element first.", "BEGINNER");
        addQuestion(stacks, "Which problem is commonly solved with a stack?",
                "Level-order traversal", "Balanced parentheses", "Shortest path with weights", "Binary search", "B", "Stacks track opening brackets so matching closings can be validated.", "BEGINNER");
        addQuestion(stacks, "What happens when you pop an empty stack?",
                "Returns 0", "Returns None silently", "Raises an error", "Creates an element", "C", "Popping an empty stack is an invalid operation and raises an error in most implementations.", "BEGINNER");

        Topic trees = topic(subject, "Binary Trees",
                "Traverse hierarchical data with depth-first and breadth-first strategies.",
                "1. Define binary tree nodes.\n2. Perform inorder, preorder, and postorder traversal.\n3. Understand binary search trees.",
                "A binary tree has nodes with up to two children. In a binary search tree, left descendants are smaller and right descendants larger.",
                "Confusing tree height with node count; assuming every binary tree is a BST; incorrect recursion base cases.",
                "class TreeNode:\n    def __init__(self, value, left=None, right=None):\n        self.value = value\n        self.left = left\n        self.right = right",
                4);
        seedResource(trees, "Binary Tree Traversal", "ARTICLE", "https://www.geeksforgeeks.org/binary-tree-data-structure/", "Tree terminology, traversals, and BST properties.", "INTERMEDIATE");
        addQuestion(trees, "Which traversal visits left subtree, node, then right subtree?",
                "Preorder", "Inorder", "Postorder", "Level order", "B", "Inorder traversal follows left, node, right and yields sorted order in a BST.", "BEGINNER");
        addQuestion(trees, "What is a leaf node?",
                "A node with one child", "A node with no children", "The root", "Any node at depth 1", "B", "Leaf nodes have no left or right children.", "BEGINNER");
        addQuestion(trees, "What is the maximum number of children a binary tree node can have?",
                "1", "2", "3", "Unlimited", "B", "Binary means at most two children per node.", "BEGINNER");
    }

    private void seedDbms(Subject subject) {
        Topic sql = topic(subject, "SQL Fundamentals",
                "Query and modify relational data with SQL.",
                "1. Write SELECT statements.\n2. Filter with WHERE.\n3. Sort and aggregate results.",
                "SQL is a declarative language for querying and manipulating relational data.",
                "Confusing WHERE with HAVING; forgetting GROUP BY; using SELECT * in production queries.",
                "SELECT department, COUNT(*)\nFROM students\nWHERE active = true\nGROUP BY department\nORDER BY COUNT(*) DESC;",
                1);
        seedResource(sql, "SQL Basics", "ARTICLE", "https://www.w3schools.com/sql/", "SELECT, WHERE, GROUP BY, and ORDER BY examples.", "BEGINNER");
        addQuestion(sql, "Which clause filters individual rows before grouping?",
                "HAVING", "WHERE", "ORDER BY", "LIMIT", "B", "WHERE filters rows before aggregation; HAVING filters grouped results.", "BEGINNER");
        addQuestion(sql, "Which clause groups rows for aggregate functions?",
                "GROUP BY", "ORDER BY", "JOIN", "DISTINCT", "A", "GROUP BY forms groups that aggregate functions summarize.", "BEGINNER");
        addQuestion(sql, "Which command removes all rows but keeps the table structure?",
                "DROP TABLE", "DELETE FROM", "ALTER TABLE", "CREATE INDEX", "B", "DELETE FROM removes rows; DROP TABLE removes the table itself.", "BEGINNER");

        Topic joins = topic(subject, "Joins",
                "Combine data across related tables.",
                "1. Use INNER JOIN.\n2. Distinguish LEFT and RIGHT joins.\n3. Avoid accidental Cartesian products.",
                "Joins combine rows from two tables based on a matching condition. A join without a condition produces a Cartesian product.",
                "Forgetting the join condition; assuming LEFT JOIN filters out unmatched rows; duplicate rows from one-to-many joins.",
                "SELECT s.name, c.title\nFROM students s\nINNER JOIN enrollments e ON e.student_id = s.id\nINNER JOIN courses c ON c.id = e.course_id;",
                2);
        seedResource(joins, "SQL Joins Explained", "ARTICLE", "https://www.w3schools.com/sql/sql_join.asp", "Inner, left, right, and full join behaviour.", "INTERMEDIATE");
        addQuestion(joins, "Which join returns only matching rows from both tables?",
                "INNER JOIN", "LEFT JOIN", "FULL JOIN", "CROSS JOIN", "A", "INNER JOIN keeps rows where the join condition matches in both tables.", "BEGINNER");
        addQuestion(joins, "Which join keeps all rows from the left table?",
                "INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "CROSS JOIN", "B", "LEFT JOIN preserves left table rows and fills unmatched right columns with nulls.", "BEGINNER");
        addQuestion(joins, "What is a Cartesian product?",
                "Every row paired with every row", "Only matched rows", "A sorted join", "A filtered join", "A", "A join without a condition pairs every row of one table with every row of the other.", "INTERMEDIATE");

        Topic normalization = topic(subject, "Normalization",
                "Organize schemas to reduce redundancy and anomalies.",
                "1. Identify functional dependencies.\n2. Apply 1NF, 2NF, and 3NF.\n3. Recognize update, insert, and delete anomalies.",
                "Normalization decomposes tables to eliminate redundancy while preserving data integrity.",
                "Over-normalizing read-heavy systems; ignoring partial dependencies; confusing 2NF and 3NF.",
                "A course_enrollment table should not repeat student name and address for every enrollment.",
                3);
        seedResource(normalization, "Database Normalization", "ARTICLE", "https://www.geeksforgeeks.org/normal-forms-in-dbms/", "1NF through 3NF with examples of anomalies.", "INTERMEDIATE");
        addQuestion(normalization, "Which normal form removes partial dependencies?",
                "1NF", "2NF", "3NF", "BCNF", "B", "2NF requires no non-prime attribute to depend on part of a composite key.", "INTERMEDIATE");
        addQuestion(normalization, "What does 1NF require?",
                "Atomic column values", "No transitive dependencies", "No partial dependencies", "Every table has a foreign key", "A", "1NF requires each column to hold a single atomic value.", "BEGINNER");
        addQuestion(normalization, "What problem does normalization primarily reduce?",
                "Data redundancy and anomalies", "Network latency", "CPU cache misses", "Index fragmentation", "A", "Normalization reduces redundant storage and prevents update, insert, and delete anomalies.", "BEGINNER");

        Topic transactions = topic(subject, "Transactions",
                "Preserve consistency with ACID properties and isolation.",
                "1. Explain ACID.\n2. Use COMMIT and ROLLBACK.\n3. Understand isolation levels.",
                "A transaction is a unit of work that either commits entirely or rolls back entirely.",
                "Leaving transactions open; assuming READ COMMITTED prevents phantom reads; ignoring deadlocks.",
                "BEGIN;\nUPDATE accounts SET balance = balance - 100 WHERE id = 1;\nUPDATE accounts SET balance = balance + 100 WHERE id = 2;\nCOMMIT;",
                4);
        seedResource(transactions, "Transactions and ACID", "ARTICLE", "https://www.geeksforgeeks.org/acid-properties-in-dbms/", "Atomicity, consistency, isolation, durability, and isolation levels.", "INTERMEDIATE");
        addQuestion(transactions, "What does the A in ACID stand for?",
                "Atomicity", "Availability", "Aggregation", "Authorization", "A", "Atomicity guarantees all operations in a transaction succeed or none do.", "BEGINNER");
        addQuestion(transactions, "Which statement permanently saves a transaction?",
                "ROLLBACK", "COMMIT", "SAVEPOINT", "GRANT", "B", "COMMIT makes the transaction changes durable and visible.", "BEGINNER");
        addQuestion(transactions, "Which ACID property prevents concurrent transactions from interfering?",
                "Atomicity", "Consistency", "Isolation", "Durability", "C", "Isolation controls how concurrent transactions observe each other's changes.", "INTERMEDIATE");
    }

    private void seedNetworks(Subject subject) {
        Topic tcpip = topic(subject, "TCP/IP Model",
                "Understand the layered architecture of internet communication.",
                "1. Name the TCP/IP layers.\n2. Map protocols to layers.\n3. Explain encapsulation.",
                "The TCP/IP model organizes networking into application, transport, internet, and network access layers.",
                "Confusing the TCP/IP model with the seven-layer OSI model; placing TCP at the internet layer.",
                "Application -> Transport (TCP/UDP) -> Internet (IP) -> Network Access",
                1);
        seedResource(tcpip, "TCP/IP Model Guide", "ARTICLE", "https://www.geeksforgeeks.org/tcp-ip-model/", "Layers, protocols, and encapsulation in the TCP/IP stack.", "BEGINNER");
        addQuestion(tcpip, "Which layer of TCP/IP contains IP addressing and routing?",
                "Application", "Transport", "Internet", "Network Access", "C", "The internet layer uses IP addresses and routing to move packets between networks.", "BEGINNER");
        addQuestion(tcpip, "Which protocol is connection-oriented?",
                "UDP", "TCP", "IP", "ICMP", "B", "TCP establishes a connection and provides reliable, ordered delivery.", "BEGINNER");
        addQuestion(tcpip, "What is encapsulation?",
                "Encrypting every packet", "Wrapping data with layer-specific headers", "Compressing payloads", "Splitting networks into subnets", "B", "Each layer adds its own header as data moves down the stack.", "INTERMEDIATE");

        Topic routing = topic(subject, "Routing",
                "Learn how routers forward packets across networks.",
                "1. Distinguish static and dynamic routing.\n2. Explain hop-by-hop forwarding.\n3. Compare distance-vector and link-state protocols.",
                "Routing selects paths across networks using routing tables populated by static configuration or dynamic protocols.",
                "Confusing routing with switching; assuming routers change source IP addresses; ignoring longest-prefix match.",
                "A router receives a packet, checks the destination IP against its routing table, and forwards to the next hop.",
                2);
        seedResource(routing, "Routing Fundamentals", "ARTICLE", "https://www.geeksforgeeks.org/types-of-routing/", "Static, dynamic, distance-vector, and link-state routing.", "INTERMEDIATE");
        addQuestion(routing, "Which device forwards packets between different networks?",
                "Hub", "Switch", "Router", "Repeater", "C", "Routers use IP routing tables to forward packets between networks.", "BEGINNER");
        addQuestion(routing, "Which routing protocol is link-state?",
                "RIP", "OSPF", "BGP", "ARP", "B", "OSPF routers build a complete topology using link-state advertisements.", "INTERMEDIATE");
        addQuestion(routing, "Which algorithm does RIP use?",
                "Dijkstra", "Bellman-Ford", "Kruskal", "Floyd-Warshall", "B", "RIP is a distance-vector protocol based on the Bellman-Ford algorithm.", "ADVANCED");

        Topic dns = topic(subject, "DNS",
                "Resolve human-readable names to IP addresses.",
                "1. Explain DNS resolution.\n2. Identify common record types.\n3. Understand caching and TTL.",
                "DNS is a hierarchical distributed naming system that maps domain names to IP addresses.",
                "Forgetting TTL caching effects; confusing A and CNAME records; assuming DNS resolves only IPv4.",
                "A    example.com -> 93.184.216.34\nAAAA example.com -> 2606:2800:220:1::\nCNAME www.example.com -> example.com",
                3);
        seedResource(dns, "How DNS Works", "ARTICLE", "https://www.cloudflare.com/learning/dns/what-is-dns/", "Resolution flow, record types, and caching.", "BEGINNER");
        addQuestion(dns, "Which DNS record maps a name to an IPv4 address?",
                "A", "AAAA", "MX", "CNAME", "A", "A records map hostnames to IPv4 addresses.", "BEGINNER");
        addQuestion(dns, "Which DNS record aliases one name to another?",
                "A", "CNAME", "TXT", "NS", "B", "CNAME records point a name to another canonical name.", "BEGINNER");
        addQuestion(dns, "What does DNS TTL control?",
                "Encryption strength", "Caching duration", "Port number", "Packet size", "B", "TTL tells resolvers how long to cache a record.", "BEGINNER");

        Topic protocols = topic(subject, "Core Protocols",
                "Compare essential application and transport protocols.",
                "1. Distinguish TCP and UDP use cases.\n2. Explain HTTP and HTTPS.\n3. Understand DHCP and ARP.",
                "Different protocols provide different guarantees. HTTP runs over TCP, while DNS commonly uses UDP for simple queries.",
                "Assuming HTTP is encrypted; confusing DHCP with DNS; using UDP where reliability is required.",
                "HTTP  -> application protocol over TCP\nHTTPS -> HTTP over TLS\nDHCP  -> assigns IP addresses\nARP   -> maps IP addresses to MAC addresses",
                4);
        seedResource(protocols, "Network Protocols Overview", "ARTICLE", "https://www.geeksforgeeks.org/network-protocols/", "Common protocols and their roles in the network stack.", "BEGINNER");
        addQuestion(protocols, "Which protocol assigns IP addresses automatically?",
                "DNS", "DHCP", "ARP", "SMTP", "B", "DHCP dynamically provides IP configuration to hosts.", "BEGINNER");
        addQuestion(protocols, "Which protocol maps IP addresses to MAC addresses on a local network?",
                "ARP", "RARP", "ICMP", "DHCP", "A", "ARP resolves a known IP address to the link-layer MAC address.", "BEGINNER");
        addQuestion(protocols, "Which protocol provides encrypted HTTP traffic?",
                "FTP", "HTTPS", "SMTP", "Telnet", "B", "HTTPS is HTTP carried over TLS, providing encryption and integrity.", "BEGINNER");
    }

    private void seedMath(Subject subject) {
        Topic algebra = topic(subject, "Algebra",
                "Solve equations and work with functions and matrices.",
                "1. Solve linear equations.\n2. Manipulate exponents and logarithms.\n3. Apply matrix operations.",
                "Algebra studies symbols and the rules for manipulating them to solve equations and model relationships.",
                "Dropping minus signs when rearranging; confusing log rules; invalid matrix dimensions.",
                "Solve 2x + 3 = 11:\n2x = 8\nx = 4",
                1);
        seedResource(algebra, "Algebra Fundamentals", "ARTICLE", "https://www.khanacademy.org/math/algebra", "Linear equations, functions, and algebraic manipulation.", "BEGINNER");
        addQuestion(algebra, "What is the solution to 2x + 3 = 11?",
                "2", "3", "4", "5", "C", "Subtract 3 to get 2x = 8, then divide by 2 to get x = 4.", "BEGINNER");
        addQuestion(algebra, "What is log2(8)?",
                "2", "3", "4", "8", "B", "2 raised to the power 3 equals 8, so log base 2 of 8 is 3.", "BEGINNER");
        addQuestion(algebra, "If A is 2x3 and B is 3x4, what is the shape of AB?",
                "2x3", "3x4", "2x4", "4x2", "C", "Matrix multiplication takes the rows of A and columns of B, giving 2x4.", "INTERMEDIATE");

        Topic probability = topic(subject, "Probability",
                "Quantify uncertainty with sample spaces and distributions.",
                "1. Compute probabilities.\n2. Use conditional probability.\n3. Apply Bayes' theorem.",
                "Probability measures the likelihood of events in a sample space, ranging from 0 to 1.",
                "Adding probabilities of overlapping events without subtracting the intersection; confusing P(A|B) with P(B|A).",
                "P(A or B) = P(A) + P(B) - P(A and B)\nP(A|B) = P(A and B) / P(B)",
                2);
        seedResource(probability, "Probability Basics", "ARTICLE", "https://www.khanacademy.org/math/statistics-probability/probability-library", "Events, conditional probability, and Bayes' theorem.", "BEGINNER");
        addQuestion(probability, "What is the probability of rolling an even number on a fair six-sided die?",
                "1/6", "1/3", "1/2", "2/3", "C", "There are three even outcomes out of six, giving 3/6 = 1/2.", "BEGINNER");
        addQuestion(probability, "How do you compute conditional probability P(A|B)?",
                "P(A) + P(B)", "P(A and B) / P(B)", "P(A) * P(B)", "P(B) / P(A and B)", "B", "Conditional probability restricts the sample space to event B.", "INTERMEDIATE");
        addQuestion(probability, "What is the sum of probabilities of all mutually exclusive outcomes?",
                "0", "0.5", "1", "Depends on the event", "C", "A complete set of mutually exclusive outcomes must sum to 1.", "BEGINNER");

        Topic statistics = topic(subject, "Statistics",
                "Summarize and interpret data with descriptive statistics.",
                "1. Compute mean, median, and mode.\n2. Measure spread.\n3. Interpret distributions.",
                "Statistics summarizes data using central tendency and dispersion measures.",
                "Confusing mean with median for skewed data; using standard deviation without checking units; ignoring outliers.",
                "mean = sum(values) / len(values)\nmedian = sorted(values)[len(values) // 2]\nrange = max(values) - min(values)",
                3);
        seedResource(statistics, "Descriptive Statistics", "ARTICLE", "https://www.khanacademy.org/math/statistics-probability/summarizing-quantitative-data", "Mean, median, spread, and distribution shapes.", "BEGINNER");
        addQuestion(statistics, "Which measure is most affected by extreme outliers?",
                "Median", "Mode", "Mean", "Range midpoint", "C", "The mean includes every value, so extreme values pull it strongly.", "BEGINNER");
        addQuestion(statistics, "What is the median of [3, 1, 4, 2, 5]?",
                "2", "3", "4", "1", "B", "Sorted values are 1, 2, 3, 4, 5, and the middle value is 3.", "BEGINNER");
        addQuestion(statistics, "What does standard deviation measure?",
                "Central value", "Spread around the mean", "Most frequent value", "Total count", "B", "Standard deviation quantifies how far values typically lie from the mean.", "BEGINNER");
    }

    private void ensureDemoStudent() {
        Student demoStudent = studentRepository.findByEmail(DEMO_EMAIL)
                .orElseGet(() -> new Student(
                        "Priya Sharma",
                        DEMO_EMAIL,
                        passwordEncoder.encode(DEMO_PASSWORD)
                ));

        boolean isNew = demoStudent.getId() == null;
        if (isNew || !passwordEncoder.matches(DEMO_PASSWORD, demoStudent.getPassword())) {
            demoStudent.setPassword(passwordEncoder.encode(DEMO_PASSWORD));
            studentRepository.save(demoStudent);
        }
    }

    private void seedResource(Topic topic, String title, String contentType, String url, String summary, String difficulty) {
        LearningResource res = new LearningResource(topic, title, contentType, url, summary, difficulty);
        resourceRepository.save(res);
    }

    private void repairResourceUrls() {
        Map<String, String> deadUrlFixes = Map.of(
                "https://dev.java/learn/datatypes/",
                "https://docs.oracle.com/javase/tutorial/essential/java/datatypes.html",
                "https://dev.java/learn/control-flow/",
                "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/if.html",
                "https://dev.java/learn/loops/",
                "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/loop.html",
                "https://dev.java/learn/arrays/",
                "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html",
                "https://dev.java/learn/methods/",
                "https://docs.oracle.com/javase/tutorial/java/javaI/methods.html",
                "https://www.geeksforgeeks.org/types-of-network-protocols/",
                "https://www.geeksforgeeks.org/network-protocols/"
        );

        for (LearningResource resource : resourceRepository.findAll()) {
            String replacement = deadUrlFixes.get(resource.getResourceUrl());
            if (replacement != null) {
                resource.setResourceUrl(replacement);
                resourceRepository.save(resource);
            }
        }
    }

    private void backfillRecommendationResources() {
        for (LearningRecommendation recommendation : recommendationRepository.findAll()) {
            if (recommendation.getResource() != null || recommendation.getTopic() == null) {
                continue;
            }
            List<LearningResource> topicResources =
                    resourceRepository.findByTopicId(recommendation.getTopic().getId());
            if (!topicResources.isEmpty()) {
                recommendation.setResource(topicResources.get(0));
                recommendationRepository.save(recommendation);
            }
        }
    }

    private boolean questionExists(Topic topic, String questionText) {
        return questionRepository.existsByTopicIdAndQuestionText(topic.getId(), questionText);
    }

    private void addQuestion(Topic topic, String text, String optA, String optB, String optC, String optD, 
                             String correctKey, String explanation, String difficulty) {
        if (questionExists(topic, text)) {
            return;
        }
        Question q = questionRepository.save(new Question(topic, text, explanation, difficulty));
        optionRepository.save(new QuestionOption(q, "A", optA, "A".equalsIgnoreCase(correctKey)));
        optionRepository.save(new QuestionOption(q, "B", optB, "B".equalsIgnoreCase(correctKey)));
        optionRepository.save(new QuestionOption(q, "C", optC, "C".equalsIgnoreCase(correctKey)));
        optionRepository.save(new QuestionOption(q, "D", optD, "D".equalsIgnoreCase(correctKey)));
    }
}
