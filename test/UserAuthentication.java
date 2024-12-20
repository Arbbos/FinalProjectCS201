package test;
import java.util.HashMap;

public class UserAuthentication {
    private HashMap<String, String> userDatabase = new HashMap<>(); // Stores username and password
    private String currentUser;

    // Register a new user
    public boolean registerUser(String username, String password) {
        if (userDatabase.containsKey(username)) {
            System.out.println("Username already exists. Please choose another username.");
            return false;
        }
        userDatabase.put(username, password);
        System.out.println("User registered successfully.");
        return true;
    }

    // Log in an existing user
    public boolean loginUser(String username, String password) {
        if (!userDatabase.containsKey(username)) {
            System.out.println("Username not found.");
            return false;
        }
        if (!userDatabase.get(username).equals(password)) {
            System.out.println("Incorrect password.");
            return false;
        }
        currentUser = username;
        System.out.println("User logged in successfully. Welcome, " + username + "!");
        return true;
    }

    // Log out the current user
    public void logoutUser() {
        if (currentUser != null) {
            System.out.println("User " + currentUser + " logged out.");
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    // Get the current logged-in user
    public String getCurrentUser() {
        return currentUser;
    }

    // Check if a user is logged in
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }
}