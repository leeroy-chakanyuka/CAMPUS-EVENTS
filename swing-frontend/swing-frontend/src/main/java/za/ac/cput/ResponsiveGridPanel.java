package za.ac.cput;

import javax.swing.*;
import java.awt.*;

/**
 * Grid whose columns follow the actual scroll viewport, rather than the window size.
 * The cards maintain a readable minimum width, including at larger UI scales.
 */
public final class ResponsiveGridPanel extends JPanel implements Scrollable {
    private int columns = 1;
    private final int minCardWidth;
    private final int gap;

    public ResponsiveGridPanel(int minCardWidth, int gap) {
        super(new GridLayout(0, 1, gap, gap));
        this.minCardWidth = Math.max(200, minCardWidth);
        this.gap = Math.max(0, gap);
        setBackground(UiTheme.CANVAS);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        if (minCardWidth <= 0) return; // JPanel constructor invokes setBounds first
        int desired = Math.max(1, Math.min(3, (width + gap) / (minCardWidth + gap)));
        if (desired != columns) {
            columns = desired;
            setLayout(new GridLayout(0, columns, gap, gap));
            revalidate();
        }
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 24;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return Math.max(120, visibleRect.height - 24);
    }

    @Override
    public boolean getScrollableTracksViewportWidth() { return true; }

    @Override
    public boolean getScrollableTracksViewportHeight() { return false; }
}
