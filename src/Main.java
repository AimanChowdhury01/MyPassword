import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to My Passwords !!!");
        while (true) {//starts loop for create and login
            System.out.print("Press 'C' to Create account and 'L' to Login: ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("C")) {
                CreateAccount.start();
                break;
            }
            else if (choice.equalsIgnoreCase("L")) {
                Login.start();
                break;
            }
            else {
                System.out.println("Invalid input. Please enter 'C' or 'L'.");//redirects to start of loop
            }
        }
        scanner.close();
    }
}
