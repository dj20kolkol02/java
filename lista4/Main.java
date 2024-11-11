import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

class Animation extends JPanel {
    private final int width;
    private final int height;
    private final ScheduledExecutorService executorService;
    private final int priority;
    private final int maxIterations = 1000;
    private double moveX = 0;
    private double moveY = 0;
    private double zoom = 300;
    private int offsetX = 0;
    private int direction = 1;
    private int frameCount = 0; // Dodany licznik klatek
    private final int sleepInterval; // Nowa zmienna dla interwału

    public Animation(int width, int height, int priority, int sleepInterval) {
        this.width = width;
        this.height = height;
        this.priority = priority;
        this.sleepInterval = sleepInterval; // Ustawienie wartości z konstruktoru
        executorService = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setPriority(priority);
            return thread;
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawFractal(g);
        frameCount++; // Zwiększ licznik klatek
    }

    private void drawFractal(Graphics g) {
        double zx, zy, tmp;
        double cX = -0.7;
        double cY = 0.27015;
        double escapeRadius = 2;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                zx = (x - width / 2) / zoom + moveX;
                zy = (y - height / 2) / zoom + moveY;
                int iter = maxIterations;

                while (zx * zx + zy * zy < escapeRadius * escapeRadius && iter > 0) {
                    tmp = zx * zx - zy * zy;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp + cX;
                    iter--;
                }

                if (iter == 0) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.getHSBColor((maxIterations / (float) iter) % 1, 1, 1));
                }
                g.fillRect(x + offsetX, y, 1, 1);
                g.fillRect(width - x - offsetX, y, 1, 1);
            }
        }
    }

    public void startAnimation() {
        executorService.scheduleAtFixedRate(this::moveFractal, 0, sleepInterval, TimeUnit.MILLISECONDS); // Użycie sleepInterval
    }

    private void moveFractal() {
        offsetX += direction * 2;
        if (offsetX >= width || offsetX <= 0) {
            direction *= -1;
        }
        repaint();
    }

    public void stopAnimation() {
        executorService.shutdown();
    }

    public int getFrameCount() { // Metoda zwracająca licznik klatek
        return frameCount;
    }
}

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridLayout(2, 5));

        // Zapytanie o preferowany priorytet
        Integer[] priorityOptions = {
                Thread.MIN_PRIORITY,
                Thread.NORM_PRIORITY,
                Thread.MAX_PRIORITY
        };
        Integer selectedPriority = (Integer) JOptionPane.showInputDialog(
                null,
                "Wybierz priorytet animacji:",
                "Wybór priorytetu",
                JOptionPane.QUESTION_MESSAGE,
                null,
                priorityOptions,
                Thread.NORM_PRIORITY
        );

        if (selectedPriority == null) {
            System.exit(0);
        }

        // Zapytanie o czas opóźnienia między klatkami
        String sleepInput = JOptionPane.showInputDialog(
                "Podaj opóźnienie między klatkami w milisekundach (np. 50):",
                "50" // Domyślna wartość
        );

        if (sleepInput == null || sleepInput.isEmpty()) {
            System.exit(0); // Zamknij, jeśli brak wartości
        }

        int sleepInterval;
        try {
            sleepInterval = Integer.parseInt(sleepInput); // Konwersja na int
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Nieprawidłowa wartość opóźnienia. Proszę podać liczbę całkowitą.",
                    "Błąd",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
            return;
        }

        Animation[] animations = new Animation[10];
        for (int i = 0; i < 10; i++) {
            Animation animation = new Animation(800, 600, selectedPriority, sleepInterval); // Przekazanie sleepInterval
            frame.add(animation);
            animations[i] = animation;
            animation.startAnimation();
        }

        frame.setVisible(true);

        // Wyświetlanie liczby klatek dla każdej animacji
        new Timer(1000, e -> {
            StringBuilder message = new StringBuilder("Liczba klatek w animacjach:\n");
            for (int i = 0; i < animations.length; i++) {
                message.append("Animacja ").append(i + 1).append(": ")
                        .append(animations[i].getFrameCount()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, message.toString(), "Liczba klatek", JOptionPane.INFORMATION_MESSAGE);
        }).start(); // Uruchomienie timera co 1 sekundę, aby zliczyć klatki w każdej animacji
    }
}
