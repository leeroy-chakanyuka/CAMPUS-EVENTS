package za.ac.cput;

import javax.swing.*;
import java.awt.*;

/** Lightweight reusable views shared by student, organiser and admin screens. */
public final class UiComponents {
    private UiComponents() { }

    public static JPanel pageHeader(String eyebrow, String title, String description, JComponent action) {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setOpaque(false);
        JPanel copy = new JPanel();
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));
        copy.setOpaque(false);
        JLabel kicker = new JLabel(eyebrow == null ? "" : eyebrow.toUpperCase());
        kicker.setFont(UiTheme.label());
        kicker.setForeground(UiTheme.BLUE);
        kicker.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel heading = new JLabel(title == null ? "" : title);
        heading.setFont(UiTheme.heading());
        heading.setForeground(UiTheme.INK);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        copy.add(kicker);
        copy.add(Box.createVerticalStrut(8));
        copy.add(heading);
        if (description != null && !description.isBlank()) {
            copy.add(Box.createVerticalStrut(8));
            JTextArea note = UiText.wrapping(description, 2);
            note.setFont(UiTheme.body());
            note.setForeground(UiTheme.MUTED);
            note.setAlignmentX(Component.LEFT_ALIGNMENT);
            copy.add(note);
        }
        header.add(copy, BorderLayout.CENTER);
        if (action != null) header.add(action, BorderLayout.EAST);
        return header;
    }

    public static JPanel statCard(String label, String value) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(UiTheme.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                UiTheme.paddedBorder(22, 22, 22, 22)));
        JLabel name = new JLabel(label);
        name.setFont(UiTheme.label());
        name.setForeground(UiTheme.MUTED);
        JLabel number = new JLabel(value == null ? "—" : value);
        number.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        number.setForeground(UiTheme.INK);
        card.add(name, BorderLayout.NORTH);
        card.add(number, BorderLayout.CENTER);
        return card;
    }

    public static JPanel emptyState(String title, String message, JButton action) {
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        JLabel heading = new JLabel(title);
        heading.setFont(UiTheme.section());
        heading.setForeground(UiTheme.INK);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea detail = UiText.wrapping(message, 2);
        detail.setFont(UiTheme.body());
        detail.setForeground(UiTheme.MUTED);
        detail.setAlignmentX(Component.CENTER_ALIGNMENT);
        detail.setPreferredSize(new Dimension(350, 55));
        body.add(heading);
        body.add(Box.createVerticalStrut(10));
        body.add(detail);
        if (action != null) {
            action.setAlignmentX(Component.CENTER_ALIGNMENT);
            body.add(Box.createVerticalStrut(14));
            body.add(action);
        }
        container.add(body);
        return container;
    }

    public static JLabel statusPill(String state) {
        JLabel pill = new JLabel(state == null ? "" : state);
        pill.setOpaque(true);
        pill.setFont(UiTheme.label());
        boolean active = "Open".equalsIgnoreCase(state) || "Active".equalsIgnoreCase(state);
        pill.setForeground(active ? UiTheme.SUCCESS : UiTheme.MUTED);
        pill.setBackground(active ? new Color(0xE5, 0xF5, 0xEF) : UiTheme.CANVAS);
        pill.setBorder(UiTheme.paddedBorder(6, 10, 6, 10));
        return pill;
    }
}
