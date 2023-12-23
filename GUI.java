import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    // Constants for content size
    private final int CONTENT_WIDTH = 1000;
    private final int CONTENT_HEIGHT = 1000;

    public GUI(ArrayList<ArrayList<Double>> boxes_D, ArrayList<ArrayList<Double>> qpoints_D) {
        // Set the title.
        setTitle("Visualization of 2D inputs");

        // Specify what happens when the close button is clicked.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close the GUI window will NOT terminate the program

        // Calculate the tightest bounding box for the boxes
        double minX = getMinValue(boxes_D, 0); // get the min x coordinate of the boxes
        double maxX = getMaxValue(boxes_D, 1); // get the max x coordinate of the boxes
        double minY = getMinValue(boxes_D, 2); // get the min y coordinate of the boxes
        double maxY = getMaxValue(boxes_D, 3); // get the max y coordinate of the boxes

        // Calculate the range of the x and y coordinates
        double rangeX = maxX - minX;
        double rangeY = maxY - minY;

        // Calculate scaling factors for x and y directions
        double scaleX = CONTENT_WIDTH / rangeX;
        double scaleY = CONTENT_HEIGHT / rangeY;
        double scale = Math.min(scaleX, scaleY);

        // Calculate translation offsets to center the content
        // offset is the distance from the left or top edge of all the boses to the left or top edge of the content window
        double offsetX;
        double offsetY;
        if (scaleX < scaleY) {
            offsetX = 0;
            offsetY = (CONTENT_WIDTH - (rangeY * scale)) / 2;
        } 
        else {
            offsetX = (CONTENT_WIDTH - (rangeX * scale)) / 2;
            offsetY = 0;
        }

        // Build the panel that contains all the boxes and qpoints
        JPanel panel = buildPanel(boxes_D, qpoints_D, minX, minY, scale, offsetX, offsetY);


        // Add the panel to the content pane.
        add(panel, BorderLayout.CENTER);

        // Calculate the usable content area by subtracting the window decorations
        Insets insets = getInsets();
        int usableWidth = CONTENT_WIDTH + insets.left + insets.right;
        int usableHeight = CONTENT_HEIGHT + insets.top + insets.bottom;

        // Set the size of the content area (excluding decorations)
        setPreferredSize(new Dimension(usableWidth, usableHeight));

        // Pack the frame to adjust for the preferred size of the content area
        pack();

        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Display the window
        setVisible(false);
    }

    private JPanel buildPanel(ArrayList<ArrayList<Double>> boxes_D, ArrayList<ArrayList<Double>> qpoints_D, double minX, double minY, double scale, double offsetX, double offsetY) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Draw boxes
                for (ArrayList<Double> box : boxes_D) {
                    drawBoxes(g, box.get(0), box.get(1), box.get(2), box.get(3), minX, minY, scale, offsetX, offsetY);
                }

                // Draw qpoints
                for (ArrayList<Double> qpoint : qpoints_D) {
                    drawQPoints(g, qpoint.get(0), qpoint.get(1), minX, minY, scale, offsetX, offsetY);
                }
            }
        };
        return panel;
    }

    // Draw a box
    private void drawBoxes(Graphics g, double x1, double x2, double y1, double y2, double minX, double minY, double scale, double offsetX, double offsetY) {
        g.setColor(Color.BLACK); // Set the color to black

        // Calculate the scaled coordinates
        double scaledX1 = (x1 - minX) * scale + offsetX;
        double scaledX2 = (x2 - minX) * scale + offsetX;
        double scaledY1 = (y1 - minY) * scale + offsetY;
        double scaledY2 = (y2 - minY) * scale + offsetY;

        // Calculate the width and height of the rectangle
        double width = scaledX2 - scaledX1;
        double height = scaledY2 - scaledY1;

        g.drawRect((int) scaledX1, (int) scaledY1, (int) width, (int) height); // Draw the rectangle
    }

    // Draw a qpoint
    private void drawQPoints(Graphics g, double x, double y, double minX, double minY, double scale, double offsetX, double offsetY) {
        g.setColor(Color.BLACK); // Set the color to black

        int dotSize = 4; // Set the size of the dot

        // Calculate the scaled coordinates
        double scaledX = (x - minX) * scale + offsetX;
        double scaledY = (y - minY) * scale + offsetY;

        g.fillOval((int) scaledX - dotSize / 2, (int) scaledY - dotSize / 2, dotSize, dotSize); // Draw the dot
    }

    // get the max value of the coordinate x and y of the boxes
    private double getMaxValue(ArrayList<ArrayList<Double>> boxes_D, int index) {
        double max = Double.NEGATIVE_INFINITY; // set the initial max value to be the smallest possible value
        for (ArrayList<Double> box : boxes_D) {
            double value = box.get(index);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    // get the min value of the coordinate x and y of the boxes
    private double getMinValue(ArrayList<ArrayList<Double>> boxes_D, int index) {
        double min = Double.POSITIVE_INFINITY; // set the initial min value to be the largest possible value
        for (ArrayList<Double> box : boxes_D) {
            double value = box.get(index);
            if (value < min) {
                min = value;
            }
        }
        return min;
    }
}