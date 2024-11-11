import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

class Question implements Serializable {
    public String id;
    public String question;
    public List<String> answers;
    public int correctAnswer;

    public Question(String id, String question, List<String> answers, int correctAnswer) {
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }
}

class User implements Serializable {
    public String id;
    public int points;
    public List<Question> questions;

    public User(String id, int points, List<Question> questions) {
        this.id = id;
        this.points = points;
        this.questions = questions;
    }

    public void getInfo() {
        System.out.println("Id: " + id + "\nPunkty: " + points + "\nPytania: " + questions);
    }

    public String getId() {
        return this.id;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public int getPoints() {
        return this.points;
    }

    public void addPoint() {
        this.points++;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}

public class Main {
    private static void saveState(User user) {
        try {
            FileOutputStream fileOut = new FileOutputStream(user.getId() + "_state.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(user);
            out.close();
            fileOut.close();
            System.out.println("Zapisano stan testu.");
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisu stanu testu.");
            e.printStackTrace();
        }
    }

    private static List<Question> loadState(String userId, User user) {
        try {
            FileInputStream fileIn = new FileInputStream(userId + "_state.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            User loadedUser = (User) in.readObject();

            in.close();
            fileIn.close();

            System.out.println("Wczytano stan testu.");

            user.setPoints(loadedUser.getPoints());
            user.setQuestions(loadedUser.getQuestions());
            user.getInfo();
            return user.getQuestions();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Nie udało się wczytać stanu testu.");
            e.printStackTrace();
        }

        return null;
    }

    private static void checkResults() {
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith("_state.ser"));

        if (listOfFiles != null) {
            StringBuilder message = new StringBuilder();

            for (File file : listOfFiles) {
                try {
                    FileInputStream fileIn = new FileInputStream(file);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    User user = (User) in.readObject();
                    in.close();
                    fileIn.close();

                    message.append("Identyfikator użytkownika: ").append(user.getId()).append("\n");
                    message.append("Punkty: ").append(user.getPoints()).append("\n");
                    if (user.getQuestions() != null && !user.getQuestions().isEmpty()) {
                        message.append("Test nie został jeszcze zakończony.\n\n");
                    } else {
                        message.append("\n");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Wystąpił błąd podczas odczytu stanu testu użytkownika: " + file.getName());
                    e.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(null, message.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Nie ma żadnych testów");
        }
    }

    private static void startTest(String userId, List<Question> loadedQuestions, User user) {
        try {
            if (loadedQuestions == null) {
                FileReader readFile = new FileReader("questions.txt");
                BufferedReader bufferRead = new BufferedReader(readFile);
                String singleLine = null;
                List<Question> questions = new ArrayList<>();

                while ((singleLine = bufferRead.readLine()) != null) {
                    String[] parts = singleLine.split(";");
                    Question question = new Question(parts[0], parts[1], Arrays.asList(Arrays.copyOfRange(parts, 2, 6)), Integer.parseInt(parts[6].trim()));
                    questions.add(question);
                }
                readFile.close();
                bufferRead.close();

                Collections.shuffle(questions);

                Iterator<Question> iterator = questions.iterator();
                while (iterator.hasNext()) {
                    Question question = iterator.next();
                    List<String> options = question.getAnswers();

                    StringBuilder optionsString = new StringBuilder();
                    for (int i = 0; i < options.size(); i++) {
                        optionsString.append(i + 1).append(". ").append(options.get(i)).append("\n");
                    }

                    int userAnswer;
                    String userInput;
                    do {
                        userInput = JOptionPane.showInputDialog(question.getQuestion() + "\n" + optionsString.toString() + "Podaj numer poprawnej odpowiedzi:");
                        if (userInput == null || userInput.equals("")) {
                            user.setQuestions(questions);
                            saveState(user);
                            return;
                        }
                        userAnswer = Integer.parseInt(userInput);
                    } while (userAnswer < 1 || userAnswer > 4);

                    if (userAnswer == question.correctAnswer) {
                        JOptionPane.showMessageDialog(null, "Poprawna odpowiedź!");
                        user.addPoint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Niepoprawna odpowiedź!");
                    }

                    iterator.remove();
                }

                user.setQuestions(questions);
                saveState(user);
            } else {
                Collections.shuffle(loadedQuestions);

                Iterator<Question> iterator = loadedQuestions.iterator();
                while (iterator.hasNext()) {
                    Question question = iterator.next();
                    List<String> options = question.getAnswers();

                    StringBuilder optionsString = new StringBuilder();
                    for (int i = 0; i < options.size(); i++) {
                        optionsString.append(i + 1).append(". ").append(options.get(i)).append("\n");
                    }

                    int userAnswer;
                    String userInput;
                    do {
                        userInput = JOptionPane.showInputDialog(question.getQuestion() + "\n" + optionsString.toString() + "Podaj numer poprawnej odpowiedzi:");
                        if (userInput == null || userInput.equals("")) {
                            user.setQuestions(loadedQuestions);
                            saveState(user);
                            return;
                        }
                        userAnswer = Integer.parseInt(userInput);
                    } while (userAnswer < 1 || userAnswer > 4);

                    if (userAnswer == question.correctAnswer) {
                        JOptionPane.showMessageDialog(null, "Poprawna odpowiedź!");
                        user.addPoint();
                    } else {
                        JOptionPane.showMessageDialog(null, "Błędna odpowiedź!");
                    }

                    iterator.remove();
                }

                user.setQuestions(loadedQuestions);
                saveState(user);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Nie można odnaleźć pliku 'questions.txt'.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Wystąpił błąd odczytu pliku.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Podano nieprawidłowy numer odpowiedzi.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String userId;
        do {
            userId = JOptionPane.showInputDialog("Podaj identyfikator użytkownika: ");
        } while(Objects.equals(userId, ""));

        User newUser = new User(userId, 0, null);

        if (!userId.equals("admin")) {
            String[] options = {"Rozpocznij test", "Wczytaj niedokończony test"};
            int choice = JOptionPane.showOptionDialog(null, "Wybierz opcję:", "Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    startTest(userId, null, newUser);
                    break;
                case 1:
                    List<Question> loadedQuestions = loadState(userId, newUser);
                    startTest(userId, loadedQuestions, newUser);
                    break;
                default:
                    System.out.println("Niepoprawny wybór.");
            }
        } else {
            checkResults();
        }
    }
}
