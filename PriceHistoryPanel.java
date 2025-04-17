import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * PriceHistoryPanel: Draws a graph of the price history.
 * - The y-axis is fixed to originalPrice ± $200 with tick marks every $50.
 *   The top tick’s number is omitted so that it does not mix with the rotated "Price" label.
 * - The x-axis displays dates (one per day, with the last data point as today).
 *   If there are 10 or more points, only every 10th tick is labeled (formatted as MM/dd).
 */
public class PriceHistoryPanel extends JPanel {
    private double[] history;
    private double originalPrice;

    public PriceHistoryPanel(double[] history, double originalPrice) {
        this.history = history;
        this.originalPrice = originalPrice;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int leftPad = 60, topPad = 40, bottomPad = 40, rightPad = 20;
        int graphWidth = getWidth() - leftPad - rightPad;
        int graphHeight = getHeight() - topPad - bottomPad;
        int xAxisY = getHeight() - bottomPad;

        if (history == null || history.length == 0) {
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            String msg = "No price history available";
            int msgWidth = fm.stringWidth(msg);
            g2.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
            return;
        }

        double minBound = originalPrice - 200;
        double maxBound = originalPrice + 200;
        double range = maxBound - minBound; // 400

        g2.setColor(Color.BLACK);
        g2.drawLine(leftPad, xAxisY, getWidth() - rightPad, xAxisY); // x-axis.
        g2.drawLine(leftPad, topPad, leftPad, xAxisY); // y-axis.

        FontMetrics fm = g2.getFontMetrics();
        for (double tick = minBound; tick <= maxBound; tick += 50) {
            if (Math.abs(tick - maxBound) < 0.001)
                continue;
            int y = xAxisY - (int)(((tick - minBound) / range) * graphHeight);
            g2.drawLine(leftPad - 5, y, leftPad + 5, y);
            String tickLabel = String.format("$%.2f", tick);
            int tickLabelWidth = fm.stringWidth(tickLabel);
            g2.drawString(tickLabel, leftPad - tickLabelWidth - 10, y + (fm.getAscent()/2) - 2);
        }

        int n = history.length;
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(n - 1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd");
        int spacing = (n > 1) ? graphWidth / (n - 1) : graphWidth;
        for (int i = 0; i < n; i++) {
            int x = leftPad + i * spacing;
            g2.drawLine(x, xAxisY - 5, x, xAxisY + 5);
            if (n < 10 || i % 10 == 0 || i == n - 1) {
                LocalDate date = startDate.plusDays(i);
                String dateLabel = date.format(dtf);
                int labelW = fm.stringWidth(dateLabel);
                g2.drawString(dateLabel, x - labelW / 2, xAxisY + 20);
            }
        }
        String xAxisLabel = "Date";
        int xAxisLabelW = fm.stringWidth(xAxisLabel);
        g2.drawString(xAxisLabel, leftPad + (graphWidth - xAxisLabelW) / 2, getHeight() - 10);

        String yAxisLabel = "Price";
        AffineTransform orig = g2.getTransform();
        g2.rotate(-Math.PI/2);
        int yAxisLabelW = fm.stringWidth(yAxisLabel);
        g2.drawString(yAxisLabel, -(topPad + (graphHeight + yAxisLabelW)/2), 20);
        g2.setTransform(orig);

        int[] xPoints = new int[n];
        int[] yPoints = new int[n];
        for (int i = 0; i < n; i++) {
            xPoints[i] = leftPad + i * spacing;
            yPoints[i] = xAxisY - (int)(((history[i] - minBound) / range) * graphHeight);
        }
        g2.setColor(Color.DARK_GRAY);
        for (int i = 0; i < n - 1; i++) {
            g2.drawLine(xPoints[i], yPoints[i], xPoints[i+1], yPoints[i+1]);
        }
        g2.setColor(Color.BLACK);
        for (int i = 0; i < n; i++) {
            g2.fillOval(xPoints[i] - 3, yPoints[i] - 3, 6, 6);
        }
    }
}

