import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class CreateAccount {
    public static void start() {
        System.out.println("You are now in the Create Account section.");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter User Name: ");
            String username = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            System.out.println("\nYou entered:");
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);

            System.out.print("Are you sure? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("yes")) {
                try {
                    File file = new File("users.txt");
                    StringBuilder decryptedContent = new StringBuilder();

                    if (file.exists()) {
                        String encryptedData = Files.readString(file.toPath());
                        if (!encryptedData.isEmpty()) {
                            decryptedContent.append(EncryptionUtility.decrypt(encryptedData.trim())).append("\n");
                        }
                    }

                    decryptedContent.append("Username: ").append(username)
                            .append(", Password: ").append(password).append("\n");
                    decryptedContent.append("===\n");

                    String encryptedToWrite = EncryptionUtility.encrypt(decryptedContent.toString());
                    Files.write(file.toPath(), encryptedToWrite.getBytes());

                    System.out.println("Account successfully created and saved.");
                } catch (IOException e) {
                    System.out.println("An error occurred while writing to the file.");
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("Encryption error.");
                    e.printStackTrace();
                }
                break;
            } else {
                System.out.println("Let's try again.\n");
            }
        }
    }
}
