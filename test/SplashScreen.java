package test;
import javax.swing.*;
import java.awt.*;

public class SplashScreen {

    public static void showSplashScreen() {
        // Create the splash screen JFrame
        JFrame splashScreen = new JFrame();
        splashScreen.setUndecorated(true);
        splashScreen.setSize(600, 400);
        splashScreen.setLocationRelativeTo(null);

        // Set the background color
        splashScreen.getContentPane().setBackground(Color.decode("#233c4b"));
        splashScreen.setLayout(new BorderLayout());

        // Load the logo image
        JLabel logoLabel = new JLabel(new ImageIcon("lib/images/logo_dsa.png"), SwingConstants.CENTER);
        splashScreen.add(logoLabel, BorderLayout.CENTER);

        // Add the loading bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        splashScreen.add(progressBar, BorderLayout.SOUTH);

        splashScreen.setVisible(true);

        // Simulate loading
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(50); // Simulate loading time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressBar.setValue(i); // Update progress bar
            }

            // Close the splash screen
            splashScreen.dispose();
            SwingUtilities.invokeLater(Main::showLoginScreen);
        }).start();
    }
}
