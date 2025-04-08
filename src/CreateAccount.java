import java.io.FileWriter;
import java.io.IOException;
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
                try (FileWriter writer = new FileWriter("users.txt", true)) {
                    writer.write("Username: " + username + ", Password: " + password + "\n");
                    writer.write("===\n"); // 🔥 THIS is the key line
                    System.out.println("Account successfully created and saved.");
                } catch (IOException e) {
                    System.out.println("An error occurred while writing to the file.");
                    e.printStackTrace();
                }
                break;
            } else {
                System.out.println("Let's try again.\n");
            }
        }
    }
}
