import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RobotVisual extends JPanel {
    private static final int GRID_SIZE = 10; // Tamaño de cada celda de la cuadrícula
    private int x = 0; // Coordenada x inicial del robot
    private int y = 0; // Coordenada y inicial del robot
    private int direction = 0; // Dirección inicial del robot (0: arriba, 1: izquierda, 2: abajo, 3: derecha)
    private List<Point> path = new ArrayList<>(); // Lista para almacenar el recorrido del robot

    public RobotVisual() {
        setPreferredSize(new Dimension(800, 800));
        path.add(new Point(x, y)); // Añadir la posición inicial al recorrido
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawPath(g);
        drawRobot(g);
    }

    private void drawGrid(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.LIGHT_GRAY);

        // Dibujar líneas de la cuadrícula
        for (int i = 0; i <= width; i += GRID_SIZE) {
            g.drawLine(i, 0, i, height);
        }
        for (int i = 0; i <= height; i += GRID_SIZE) {
            g.drawLine(0, i, width, i);
        }
    }

    private void drawPath(Graphics g) {
        g.setColor(Color.BLUE);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Dibujar el recorrido del robot
        for (int i = 0; i < path.size() - 1; i++) {
            int x1 = centerX + path.get(i).x * GRID_SIZE;
            int y1 = centerY - path.get(i).y * GRID_SIZE;
            int x2 = centerX + path.get(i + 1).x * GRID_SIZE;
            int y2 = centerY - path.get(i + 1).y * GRID_SIZE;
            g.drawLine(x1, y1, x2, y2);
        }
    }

    private void drawRobot(Graphics g) {
        g.setColor(Color.RED);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int robotX = centerX + x * GRID_SIZE;
        int robotY = centerY - y * GRID_SIZE;
        g.fillOval(robotX - GRID_SIZE / 2, robotY - GRID_SIZE / 2, GRID_SIZE, GRID_SIZE);
    }

    public void moveRobot(int[] steps) {
        for (int step : steps) {
            switch (direction) {
                case 0: // Hacia arriba (eje y positivo)
                    y += step;
                    break;
                case 1: // Hacia la izquierda (eje x negativo)
                    x -= step;
                    break;
                case 2: // Hacia abajo (eje y negativo)
                    y -= step;
                    break;
                case 3: // Hacia la derecha (eje x positivo)
                    x += step;
                    break;
            }
            direction = (direction + 1) % 4;
            path.add(new Point(x, y)); // Añadir la nueva posición al recorrido
            repaint();
            try {
                Thread.sleep(1000); // Esperar 1 segundo entre movimientos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Movimiento del Robot");
        RobotVisual robotVisual = new RobotVisual();
        frame.add(robotVisual);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        String input = JOptionPane.showInputDialog(frame, "Ingrese la secuencia de pasos (separados por comas):");
        if (input != null && !input.trim().isEmpty()) {
            String[] stepsStr = input.split(",");
            int[] steps = new int[stepsStr.length];
            try {
                for (int i = 0; i < stepsStr.length; i++) {
                    steps[i] = Integer.parseInt(stepsStr[i].trim());
                }
                robotVisual.moveRobot(steps);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Entrada no válida. Por favor, ingrese solo números enteros separados por comas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No se ingresó ninguna secuencia de pasos.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
