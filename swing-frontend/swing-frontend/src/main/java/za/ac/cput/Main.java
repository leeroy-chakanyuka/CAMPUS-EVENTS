package za.ac.cput;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            boolean initialized;
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/admin/system-status"))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonNode json = new ObjectMapper().readTree(response.body());
                initialized = json.path("initialized").asBoolean(false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Could not reach the backend. Is the Spring Boot app running?",
                        "Connection error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (initialized) {
                new Login().setVisible(true);
            } else {
                new CreateFirstAdmin().setVisible(true);
            }
        });
    }
}
