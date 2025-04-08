import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Login {
    public static void start() {
        System.out.println("You are now in the Login section.");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter Username: ");
            String enteredUsername = scanner.nextLine();

            System.out.print("Enter Password: ");
            String enteredPassword = scanner.nextLine();

            boolean isValid = false;

            try {
                File file = new File("users.txt");
                Scanner fileScanner = new Scanner(file);

                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    if (line.contains("Username: " + enteredUsername + ", Password: " + enteredPassword)) {
                        isValid = true;
                        break;
                    }
                }

                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("User file not found.");
                e.printStackTrace();
                return;
            }

            if (isValid) {
                System.out.println("Login successful!");
                Menu.start(enteredUsername, enteredPassword); // Pass credentials to menu
                break;
            } else {
                System.out.println("Invalid credentials. Please try again.\n");
            }
        }
    }
}
