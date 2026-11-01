package za.ac.cput;

import com.formdev.flatlaf.FlatLightLaf;
import com.sun.net.httpserver.HttpServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manual screenshot evidence for Stage 1, using the real Swing dashboard and
 * an in-process fixture HTTP server. Fixtures never ship in the application.
 */
public final class Stage1Capture {
    private Stage1Capture() { }

    public static void main(String[] args) throws Exception {
        HttpServer mock = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
        mock.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String body = switch (path) {
                case "/faculty" -> "[{\"id\":1,\"name\":\"Faculty of Informatics and Design\",\"active\":true}]";
                case "/event" -> "[" +
                        "{\"id\":1,\"title\":\"Creative Futures and the International Student Portfolio Exhibition\"," +
                        "\"facultyName\":\"Faculty of Informatics and Design\",\"venueName\":\"Design Studio, District Six Campus, Cape Town\"," +
                        "\"eventDate\":\"2026-11-12T14:00:00\",\"open\":true,\"capacity\":120,\"ticketsSold\":34}," +
                        "{\"id\":2,\"title\":\"Student Research, Innovation and Entrepreneurship Showcase 2026\"," +
                        "\"facultyName\":\"Faculty of Engineering and the Built Environment\",\"venueName\":\"Innovation Auditorium\"," +
                        "\"eventDate\":\"2026-11-14T09:00:00\",\"open\":true,\"capacity\":60,\"ticketsSold\":58}," +
                        "{\"id\":3,\"title\":\"Music and Arts on Campus\",\"facultyName\":\"Faculty of Informatics and Design\"," +
                        "\"venueName\":\"Main Campus Hall\",\"eventDate\":\"2026-11-18T17:00:00\"," +
                        "\"open\":true,\"capacity\":240,\"ticketsSold\":78}," +
                        "{\"id\":4,\"title\":\"Welcome to Your Careers Network\",\"facultyName\":\"Faculty of Informatics and Design\"," +
                        "\"venueName\":\"Student Centre\",\"eventDate\":\"2026-11-20T10:00:00\"," +
                        "\"open\":true,\"capacity\":90,\"ticketsSold\":17}]";
                default -> "[]";
            };
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (var out = exchange.getResponseBody()) { out.write(bytes); }
        });
        mock.start();
        FlatLightLaf.setup();
        UiTheme.installDefaults();

        final StudentDashboard[] holder = new StudentDashboard[1];
        SwingUtilities.invokeAndWait(() -> {
            StudentDashboard frame = new StudentDashboard(1L);
            frame.setLocation(30, 30);
            frame.setVisible(true);
            for (Component component : frame.getContentPane().getComponents()) {
                if (component instanceof JPanel sidebar) {
                    for (Component item : sidebar.getComponents()) {
                        if (item instanceof JButton button && "Browse Events".equals(button.getText())) {
                            button.doClick();
                        }
                    }
                }
            }
            holder[0] = frame;
        });
        // Allow the application to read fixture data through its normal HTTP code path.
        Thread.sleep(1300);
        capture(holder[0], 1200, 700, "stage-1-student-browse-1200.png");
        capture(holder[0], 960, 720, "stage-1-student-browse-960.png");
        SwingUtilities.invokeAndWait(holder[0]::dispose);
        mock.stop(0);
    }

    private static void capture(JFrame frame, int width, int height, String filename) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame.setSize(width, height);
            frame.setLocation(30, 30);
            frame.toFront();
        });
        Robot robot = new Robot();
        robot.waitForIdle();
        Thread.sleep(250);
        Rectangle bounds = frame.getBounds();
        BufferedImage image = robot.createScreenCapture(bounds);
        Path out = Path.of("ui-stage-1-shots", filename);
        Files.createDirectories(out.getParent());
        ImageIO.write(image, "png", out.toFile());
        System.out.println("Captured " + out + " (" + width + "x" + height + ")");
    }
}
