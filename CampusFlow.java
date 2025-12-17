// ===============================
// CampusFlow - Smart University Resource Optimization System
// Console-based Java Application (VS Code compatible)
// ===============================

import java.util.*;
import java.io.*;

// ---------- USER CLASSES ----------
abstract class User {
    protected String name;
    protected String role;

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}

class Admin extends User {
    public Admin(String name) {
        super(name, "ADMIN");
    }
}

class Organizer extends User {
    public Organizer(String name) {
        super(name, "ORGANIZER");
    }
}

class Student extends User {
    public Student(String name) {
        super(name, "STUDENT");
    }
}

// ---------- RESOURCE CLASSES ----------
class Resource {
    String resourceId;
    String type;
    int capacity;

    public Resource(String resourceId, String type, int capacity) {
        this.resourceId = resourceId;
        this.type = type;
        this.capacity = capacity;
    }
}

// ---------- EVENT CLASS ----------
class Event {
    String eventName;
    String resourceId;
    int startTime;
    int endTime;

    public Event(String eventName, String resourceId, int startTime, int endTime) {
        this.eventName = eventName;
        this.resourceId = resourceId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

// ---------- SCHEDULER ----------
class Scheduler {
    List<Event> events = new ArrayList<>();

    public boolean addEvent(Event event) {
        for (Event e : events) {
            if (e.resourceId.equals(event.resourceId)) {
                if (!(event.endTime <= e.startTime || event.startTime >= e.endTime)) {
                    return false; // conflict
                }
            }
        }
        events.add(event);
        return true;
    }

    public void showSchedule() {
        if (events.isEmpty()) {
            System.out.println("No events scheduled.");
            return;
        }
        for (Event e : events) {
            System.out.println(e.eventName + " | Resource: " + e.resourceId + " | " + e.startTime + "-" + e.endTime);
        }
    }
}

// ---------- ANALYTICS ----------
class UsageAnalyzer {
    Map<String, Integer> usageMap = new HashMap<>();

    public void recordUsage(String resourceId) {
        usageMap.put(resourceId, usageMap.getOrDefault(resourceId, 0) + 1);
    }

    public void showUsageStats() {
        System.out.println("\nResource Usage Analytics:");
        for (String key : usageMap.keySet()) {
            System.out.println("Resource " + key + " used " + usageMap.get(key) + " times");
        }
    }
}

// ---------- MAIN CLASS ----------
public class CampusFlow {
    static Scanner sc = new Scanner(System.in);
    static List<Resource> resources = new ArrayList<>();
    static Scheduler scheduler = new Scheduler();
    static UsageAnalyzer analyzer = new UsageAnalyzer();

    public static void main(String[] args) {
        seedResources();
        System.out.println("=== Welcome to CampusFlow ===");

        while (true) {
            System.out.println("\n1. Admin\n2. Organizer\n3. Student\n4. Exit");
            System.out.print("Choose role: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    adminMenu();
                    break;
                case 2:
                    organizerMenu();
                    break;
                case 3:
                    studentMenu();
                    break;
                case 4:
                    System.out.println("Exiting CampusFlow...");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    static void seedResources() {
        resources.add(new Resource("R101", "Classroom", 60));
        resources.add(new Resource("L201", "Lab", 40));
        resources.add(new Resource("A301", "Auditorium", 200));
    }

    static void adminMenu() {
        System.out.println("\n--- Admin Dashboard ---");
        scheduler.showSchedule();
        analyzer.showUsageStats();
    }

    static void organizerMenu() {
        System.out.println("\n--- Organizer Dashboard ---");
        System.out.print("Event Name: ");
        sc.nextLine();
        String name = sc.nextLine();

        showResources();
        System.out.print("Choose Resource ID: ");
        String resId = sc.next();

        System.out.print("Start Time (0-24): ");
        int start = sc.nextInt();
        System.out.print("End Time (0-24): ");
        int end = sc.nextInt();

        Event event = new Event(name, resId, start, end);
        if (scheduler.addEvent(event)) {
            analyzer.recordUsage(resId);
            System.out.println("Event scheduled successfully.");
        } else {
            System.out.println("Conflict detected! Scheduling failed.");
        }
    }

    static void studentMenu() {
        System.out.println("\n--- Student View ---");
        scheduler.showSchedule();
    }

    static void showResources() {
        System.out.println("Available Resources:");
        for (Resource r : resources) {
            System.out.println(r.resourceId + " | " + r.type + " | Capacity: " + r.capacity);
        }
    }
}
