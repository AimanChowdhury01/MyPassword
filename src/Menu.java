import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;

public class Menu {

    public static void start(String currentUser, String accountPassword) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Password Manager, " + currentUser + "!");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add new password");
            System.out.println("2. Access saved password");
            System.out.println("3. Edit a password");
            System.out.println("4. Delete a password");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addPassword(currentUser);
                    break;
                case "2":
                    accessPassword(currentUser, accountPassword);
                    break;
                case "3":
                    editPassword(currentUser);
                    break;
                case "4":
                    deletePassword(currentUser);
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addPassword(String user) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter site/app name (or type 'exit' to cancel): ");
        String site = scanner.nextLine();

        if (site.equalsIgnoreCase("exit")) {
            System.out.println("Cancelled adding new password.");
            return;
        }

        System.out.println("Choose an option:");
        System.out.println("1. Generate a strong password");
        System.out.println("2. Enter password manually");
        System.out.println("3. Cancel");

        System.out.print("Your choice: ");
        String option = scanner.nextLine();

        String finalPassword = "";

        switch (option) {
            case "1":
                finalPassword = generateStrongPassword(12);
                System.out.println("Generated password: " + finalPassword);
                break;
            case "2":
                System.out.print("Enter your password: ");
                finalPassword = scanner.nextLine();
                break;
            case "3":
                System.out.println("Cancelled adding new password.");
                return;
            default:
                System.out.println("Invalid option. Returning to menu.");
                return;
        }

        String passwordToSave = finalPassword;

        modifyUserBlock(user, lines -> {
            int index = lines.lastIndexOf("===");
            lines.add(index, site + ": " + passwordToSave);
            return lines;
        });

        System.out.println("Password saved!");
    }

    private static void accessPassword(String user, String correctPassword) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Re-enter your password to access saved passwords: ");
        String enteredPassword = scanner.nextLine();

        if (!enteredPassword.equals(correctPassword)) {
            System.out.println("Incorrect password. Access denied.");
            return;
        }

        System.out.print("Enter the site/app name: ");
        String site = scanner.nextLine().trim().toLowerCase();

        boolean found = false;

        try (Scanner fileScanner = new Scanner(new File("users.txt"))) {
            boolean insideUser = false;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                if (line.startsWith("Username: ") && line.contains(user)) {
                    insideUser = true;
                } else if (line.equals("===")) {
                    insideUser = false;
                } else if (insideUser && line.toLowerCase().startsWith(site + ":")) {
                    System.out.println("Password found: " + line);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("No password found for: " + site);
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }

    private static void editPassword(String user) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter site/app to edit: ");
        String site = scanner.nextLine().trim();

        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();

        modifyUserBlock(user, lines -> {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).toLowerCase().startsWith(site.toLowerCase() + ":")) {
                    lines.set(i, site + ": " + newPass);
                    System.out.println("Password updated.");
                    return lines;
                }
            }
            System.out.println("Site not found.");
            return lines;
        });
    }

    private static void deletePassword(String user) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter site/app to delete: ");
        String site = scanner.nextLine().trim();

        modifyUserBlock(user, lines -> {
            boolean removed = lines.removeIf(line -> line.toLowerCase().startsWith(site.toLowerCase() + ":"));
            if (removed) {
                System.out.println("Password deleted.");
            } else {
                System.out.println("Site not found.");
            }
            return lines;
        });
    }

    private static void modifyUserBlock(String user, Function<List<String>, List<String>> modifier) {
        try {
            File file = new File("users.txt");
            List<String> allLines = new ArrayList<>(Files.readAllLines(file.toPath()));
            List<String> updatedLines = new ArrayList<>();
            boolean insideUser = false;
            List<String> userBlock = new ArrayList<>();

            for (String line : allLines) {
                if (line.startsWith("Username: ") && line.contains(user)) {
                    insideUser = true;
                    userBlock = new ArrayList<>();
                    userBlock.add(line);
                } else if (insideUser && line.equals("===")) {
                    userBlock.add("===");
                    userBlock = modifier.apply(userBlock);
                    updatedLines.addAll(userBlock);
                    insideUser = false;
                } else if (insideUser) {
                    userBlock.add(line);
                } else {
                    updatedLines.add(line);
                }
            }

            Files.write(file.toPath(), updatedLines);
        } catch (IOException e) {
            System.out.println("Error updating file.");
        }
    }

    private static String generateStrongPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+<>?";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
