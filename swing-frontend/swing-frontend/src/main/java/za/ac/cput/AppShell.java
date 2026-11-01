package za.ac.cput;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Shared Swing dashboard framing. Screens supply their existing CardLayout and callbacks;
 * this class never decides roles, permissions, navigation destinations or API calls.
 */
public final class AppShell {
    private AppShell() { }

    public static void configureWindow(JFrame frame) {
        frame.setMinimumSize(new Dimension(860, 580));
        frame.setSize(1200, 700);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
    }

    public static JPanel navigation(String role) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(232, 700));
        sidebar.setMinimumSize(new Dimension(200, 0));
        sidebar.setBackground(UiTheme.NAVY);
        sidebar.setBorder(UiTheme.paddedBorder(28, 16, 20, 16));

        JLabel mark = new JLabel("CAMPUS / EVENTS");
        mark.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        mark.setForeground(UiTheme.WHITE);
        mark.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(mark);

        JLabel subtitle = new JLabel(role == null ? "Workspace" : role.toUpperCase() + " WORKSPACE");
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        subtitle.setForeground(new Color(0xA7, 0xBA, 0xD6));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(UiTheme.paddedBorder(7, 0, 0, 0));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(36));
        return sidebar;
    }

    public static JButton navButton(String title, String svg) {
        JButton button = new JButton(title);
        button.setFont(UiTheme.label());
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(14);
        button.setForeground(new Color(0xD6, 0xE2, 0xF2));
        button.setBackground(UiTheme.NAVY);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(true);
        button.setFocusable(true);
        button.setBorder(UiTheme.paddedBorder(11, 14, 11, 12));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("campus.nav", Boolean.TRUE);
        button.getAccessibleContext().setAccessibleName(title);
        if (svg != null) {
            try {
                button.setIcon(new FlatSVGIcon("icons/" + svg + ".svg", 19, 19));
            } catch (RuntimeException ignored) {
                // Text remains usable if a packaged SVG resource cannot be loaded.
            }
        }
        return button;
    }

    public static void setActive(JPanel sidebar, JButton selected) {
        for (Component component : sidebar.getComponents()) {
            if (component instanceof JButton button
                    && Boolean.TRUE.equals(button.getClientProperty("campus.nav"))) {
                boolean active = button == selected;
                button.setBackground(active ? new Color(0x25, 0x3D, 0x64) : UiTheme.NAVY);
                button.setForeground(UiTheme.WHITE);
                button.setFont(new Font(Font.SANS_SERIF,
                        active ? Font.BOLD : Font.PLAIN, 14));
            }
        }
        sidebar.repaint();
    }

    public static JPanel content(CardLayout layout) {
        JPanel content = new JPanel(layout);
        content.setBackground(UiTheme.CANVAS);
        return content;
    }
}
