package org.example.concurrente2;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class BrowserLauncher implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            openBrowser("http://localhost:8080");
        } catch (java.awt.HeadlessException e) {
            System.err.println("Headless environment detected. Unable to open browser.");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void openBrowser(String url) throws IOException, URISyntaxException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            System.err.println("Desktop or browse action not supported. Please open the following URL manually: " + url);
        }
    }
}