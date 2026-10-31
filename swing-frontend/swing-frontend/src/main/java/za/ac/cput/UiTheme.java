package za.ac.cput;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/** Central visual tokens for the Swing UI. No backend or business logic belongs here. */
public final class UiTheme {
    private UiTheme() { }

    public static final Color NAVY = new Color(0x10, 0x1E, 0x36);
    public static final Color BLUE = new Color(0x2E, 0x65, 0xED);
    public static final Color INK = new Color(0x14, 0x24, 0x3E);
    public static final Color MUTED = new Color(0x6B, 0x78, 0x90);
    public static final Color CANVAS = new Color(0xF5, 0xF7, 0xFB);
    public static final Color BORDER = new Color(0xE6, 0xEB, 0xF2);
    public static final Color WHITE = Color.WHITE;
    public static final Color SUCCESS = new Color(0x16, 0x81, 0x66);
    public static final Color DANGER = new Color(0xC8, 0x43, 0x53);

    public static final int SPACE_1 = 8;
    public static final int SPACE_2 = 16;
    public static final int SPACE_3 = 24;
    public static final int SPACE_4 = 32;

    public static Font body() { return new Font(Font.SANS_SERIF, Font.PLAIN, 14); }
    public static Font small() { return new Font(Font.SANS_SERIF, Font.PLAIN, 12); }
    public static Font section() { return new Font(Font.SANS_SERIF, Font.BOLD, 19); }
    public static Font heading() { return new Font(Font.SANS_SERIF, Font.BOLD, 29); }
    public static Font label() { return new Font(Font.SANS_SERIF, Font.BOLD, 13); }

    public static Border paddedBorder(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    /** Call after FlatLaf has been installed, before constructing any frames. */
    public static void installDefaults() {
        UIManager.put("Panel.background", CANVAS);
        UIManager.put("Label.foreground", INK);
        UIManager.put("Label.font", body());
        UIManager.put("Button.font", label());
        UIManager.put("Button.arc", 14);
        UIManager.put("Component.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("ScrollBar.width", 11);
        UIManager.put("Table.rowHeight", 38);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("Table.selectionBackground", new Color(0xE9, 0xF0, 0xFF));
        UIManager.put("Table.selectionForeground", INK);
        UIManager.put("TableHeader.background", CANVAS);
        UIManager.put("TableHeader.foreground", MUTED);
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(label());
        button.setBackground(BLUE);
        button.setForeground(WHITE);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(true);
        button.putClientProperty("JButton.buttonType", "roundRect");
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(label());
        button.setBackground(WHITE);
        button.setForeground(INK);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");
        return button;
    }
}
