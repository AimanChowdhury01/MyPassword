import java.io.File;
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
                StringBuilder encryptedContent = new StringBuilder();

                while (fileScanner.hasNextLine()) {
                    encryptedContent.append(fileScanner.nextLine()).append("\n");
                }
                fileScanner.close();

                // 🔥 FIXED: trim to remove hidden newline/carriage return chars
                String decryptedContent = EncryptionUtility.decrypt(encryptedContent.toString().trim());
                Scanner decryptedScanner = new Scanner(decryptedContent);

                while (decryptedScanner.hasNextLine()) {
                    String line = decryptedScanner.nextLine();
                    if (line.contains("Username: " + enteredUsername + ", Password: " + enteredPassword)) {
                        isValid = true;
                        break;
                    }
                }

                decryptedScanner.close();
            } catch (Exception e) {
                System.out.println("Error during login: " + e.getMessage());
                return;
            }

            if (isValid) {
                System.out.println("Login successful!");
                Menu.start(enteredUsername, enteredPassword);
                break;
            } else {
                System.out.println("Invalid credentials. Please try again.\n");
            }
        }
    }
}
