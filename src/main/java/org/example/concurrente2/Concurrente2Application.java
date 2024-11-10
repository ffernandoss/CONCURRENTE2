package org.example.concurrente2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Concurrente2Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Concurrente2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        openBrowser("http://localhost:8080");
    }

    private void openBrowser(String url) {
        String os = System.getProperty("os.name").toLowerCase();
        Runtime runtime = Runtime.getRuntime();

        try {
            if (os.contains("win")) {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                runtime.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                runtime.exec("xdg-open " + url);
            } else {
                System.err.println("Unsupported OS. Please open the following URL manually: " + url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}