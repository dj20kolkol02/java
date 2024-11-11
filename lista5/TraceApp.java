import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class TraceApp extends JFrame {
    private ArrayList<Point> trace = new ArrayList<>();
    private boolean isTraceOn = false;

    private boolean isgumkaon = false;
    private JPanel drawPanel;
    private JCheckBox traceCheckBox;
    private JCheckBox gumkaCheckBox; // Dodany checkbox "gumka"
    private JButton resetButton, saveButton, loadButton;
    private JButton upButton, downButton, leftButton, rightButton;
    private Rectangle rectangle = new Rectangle(0, 0, 50, 50);
    private int traceSize = 20;
    private int step = 50; // Wielkość kroku
    private int maxSteps = 9; // Maksymalna ilość kroków w jednym kierunku
    private int panelSize = step * maxSteps + rectangle.width;

    public TraceApp() {
        super("Warunek Java");
        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

                // Rysowanie czerwonego kwadratu
                g.setColor(Color.RED);
                g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

                // Rysowanie żółtego śladu, z pominięciem części znajdujących się na czerwonym kwadracie
                g.setColor(Color.YELLOW);
                for (Point point : trace) {
                    int x = point.x + (rectangle.width - traceSize) / 2;
                    int y = point.y + (rectangle.height - traceSize) / 2;

                    Rectangle traceRectangle = new Rectangle(x, y, traceSize, traceSize);
                    if (!rectangle.intersects(traceRectangle)) {
                        g.fillRect(x, y, traceSize, traceSize);
                    }
                }
            }
        };

        drawPanel.setPreferredSize(new Dimension(panelSize, panelSize));
        drawPanel.setBackground(Color.WHITE);

        traceCheckBox = new JCheckBox("Zostaw ślad");
        traceCheckBox.addActionListener(e -> {
            isTraceOn = traceCheckBox.isSelected();
            if (isTraceOn) {
                trace.add(new Point(rectangle.x, rectangle.y));
                drawPanel.repaint();
            }
        });

        gumkaCheckBox = new JCheckBox("Gumka"); // Inicjalizacja checkboxa "gumka"
        gumkaCheckBox.addActionListener(e -> {
            isgumkaon = gumkaCheckBox.isSelected();
            gumka();
        }); // Podłączenie akcji do checkboxa "gumka"

        resetButton = new JButton("RESET");
        resetButton.addActionListener(e -> {
            trace.clear();
            rectangle.setLocation(0, 0);
            drawPanel.repaint();
        });

        saveButton = new JButton("Zapisz");
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Wybierz lokalizację do zapisu");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                    oos.writeObject(trace);
                    JOptionPane.showMessageDialog(this, "Stan gry został zapisany pomyślnie.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Błąd podczas zapisywania: " + ex.getMessage());
                }
            }
        });

        loadButton = nowyJButton("Odczytaj", (ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Wybierz plik do odczytu");
            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileToLoad))) {
                    trace = (ArrayList<Point>) ois.readObject();

                    // Walidacja odczytanych punktów
                    boolean isTraceValid = true;
                    for (Point point : trace) {
                        if (point.x < 0 || point.x > drawPanel.getWidth() - rectangle.width ||
                                point.y < 0 || point.y > drawPanel.getHeight() - rectangle.height) {
                            isTraceValid = false;
                            break;
                        }
                    }

                    if (isTraceValid && !trace.isEmpty()) {
                        Point lastPoint = trace.get(trace.size() - 1);
                        rectangle.x = lastPoint.x;
                        rectangle.y = lastPoint.y;
                        drawPanel.repaint();
                        JOptionPane.showMessageDialog(this, "Stan gry został wczytany pomyślnie.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Błędne dane śladu. Odczyt przerwany.");
                        trace.clear();
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Błąd podczas odczytywania: " + ex.getMessage());
                }
            }
        });

        upButton = new JButton("Góra");
        upButton.addActionListener(e -> moveRectangle(0, -step));

        downButton = nowyJButton("Dół", (ActionEvent e) -> moveRectangle(0, step));

        leftButton = nowyJButton("Lewo", (ActionEvent e) -> moveRectangle(-step, 0));

        rightButton = nowyJButton("Prawo", (ActionEvent e) -> moveRectangle(step, 0));

        // Fragment z GridBagLayout odpowiedzialny za dodawanie przycisków
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout()); // Ustawienie layoutu na GridBagLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER; // Dodajemy, by wyśrodkować przyciski
        gbc.fill = GridBagConstraints.BOTH; // Dodatkowe wypełnienie, aby przyciski były dobrze rozmieszczone

        // Nowa konfiguracja rozmieszczenia przycisków
        // Ustawienie przycisku "Góra" w pierwszym rzędzie, pośrodku
        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(upButton, gbc);

        // Ustawienie przycisku "Lewo" w drugim rzędzie, po lewej
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(leftButton, gbc);

        // Ustawienie przycisku "Prawo" w drugim rzędzie, po prawej
        gbc.gridx = 2;
        buttonPanel.add(rightButton, gbc);

        // Przeniesienie przycisku "Dół" do panelu obok "Prawo"
        gbc.gridx = 1;
        gbc.gridy = 2;
        buttonPanel.add(downButton, gbc);

        // Pozostałe przyciski umieszczone poniżej, pośrodku
        gbc.gridx = 1;
        gbc.gridy = 3;
        buttonPanel.add(resetButton, gbc);

        gbc.gridy = 4;
        buttonPanel.add(traceCheckBox, gbc);

        gbc.gridy = 5;
        buttonPanel.add(saveButton, gbc);

        gbc.gridy = 6;
        buttonPanel.add(loadButton, gbc);

        // Dodanie checkboxa "gumka"
        gbc.gridy = 7;
        buttonPanel.add(gumkaCheckBox, gbc);

        setLayout(new BorderLayout());
        add(drawPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.WEST);

        setResizable(false);

        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void moveRectangle(int dx, int dy) {
        int nowyX = rectangle.x + dx;
        int nowyY = rectangle.y + dy;

        int maxX = drawPanel.getWidth() - rectangle.width;
        int maxY = drawPanel.getHeight() - rectangle.height;

        if (nowyX < 0 || nowyX > maxX) {
            nowyX = rectangle.x;
        }

        if (nowyY < 0 || nowyY > maxY) {
            nowyY = rectangle.y;
        }

        rectangle.x = nowyX;
        rectangle.y = nowyY;

        if (isTraceOn) {
            trace.add(new Point(rectangle.x, rectangle.y));

        }

        if (isgumkaon) {
            gumka();
        }
        drawPanel.repaint();
    }

    private void gumka() {
        // Sprawdzenie czy checkbox "gumka" został zaznaczony
        if (gumkaCheckBox.isSelected()) {
            // Iteracja przez ślad
            for (int i = 0; i < trace.size(); i++) {
                Point point = trace.get(i);
                // Sprawdzenie, czy punkt znajduje się wewnątrz czerwonego kwadratu
                if (rectangle.contains(point)) {
                    // Usunięcie punktu z listy śladu
                    trace.remove(i);
                    i--; // Zmniejszenie iteratora, ponieważ element został usunięty
                }
            }
        }
    }

    private JButton nowyJButton(String nazwa, ActionListener sluchacz) {
        JButton button = new JButton(nazwa);
        button.addActionListener(sluchacz);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TraceApp::new);
    }
}
