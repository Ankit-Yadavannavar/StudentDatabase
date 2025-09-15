package StudentDatabase;

import java.io.*;
import java.util.*;

class Student implements Serializable {
    private int id;
    private String name;
    private String course;
    private double cgpa;
    private String placementStatus;

    public Student(int id, String name, String course, double cgpa, String placementStatus) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.cgpa = cgpa;
        this.placementStatus = placementStatus;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCourse() { return course; }
    public double getCgpa() { return cgpa; }
    public String getPlacementStatus() { return placementStatus; }

    public void setPlacementStatus(String status) { this.placementStatus = status; }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Course: " + course +
               " | CGPA: " + cgpa + " | Status: " + placementStatus;
    }
}

public class PlacementDatabase {
    private static ArrayList<Student> students = new ArrayList<>();
    private static final String FILE_NAME = "students.dat";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadFromFile(); // Load existing data if available
        int choice;

        do {
            System.out.println("\n===== Student Placement Database =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. View Placed Students");
            System.out.println("4. View Not Placed Students");
            System.out.println("5. Update Placement Status");
            System.out.println("6. Delete Student");
            System.out.println("7. Save to File");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> viewAllStudents();
                case 3 -> viewPlacedStudents();
                case 4 -> viewNotPlacedStudents();
                case 5 -> updatePlacementStatus(sc);
                case 6 -> deleteStudent(sc);
                case 7 -> saveToFile();
                case 8 -> {
                    saveToFile();
                    System.out.println("Exiting... Data saved.");
                }
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 8);
    }

    private static void addStudent(Scanner sc) {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Course: ");
        String course = sc.nextLine();
        System.out.print("Enter CGPA: ");
        double cgpa = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Placement Status (Placed/Not Placed): ");
        String status = sc.nextLine();

        students.add(new Student(id, name, course, cgpa, status));
        System.out.println("Student added successfully!");
    }

    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found!");
        } else {
            students.forEach(System.out::println);
        }
    }

    private static void viewPlacedStudents() {
        students.stream()
                .filter(s -> s.getPlacementStatus().equalsIgnoreCase("Placed"))
                .forEach(System.out::println);
    }

    private static void viewNotPlacedStudents() {
        students.stream()
                .filter(s -> s.getPlacementStatus().equalsIgnoreCase("Not Placed"))
                .forEach(System.out::println);
    }

    private static void updatePlacementStatus(Scanner sc) {
        System.out.print("Enter Student ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Student s : students) {
            if (s.getId() == id) {
                System.out.print("Enter new Placement Status (Placed/Not Placed): ");
                String status = sc.nextLine();
                s.setPlacementStatus(status);
                System.out.println("Status updated successfully!");
                return;
            }
        }
        System.out.println("Student not found!");
    }

    private static void deleteStudent(Scanner sc) {
        System.out.print("Enter Student ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        students.removeIf(s -> s.getId() == id);
        System.out.println("Student deleted successfully!");
    }

    private static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (ArrayList<Student>) ois.readObject();
            System.out.println("Data loaded from file.");
        } catch (Exception e) {
            students = new ArrayList<>(); // start fresh if file not found
        }
    }
}
