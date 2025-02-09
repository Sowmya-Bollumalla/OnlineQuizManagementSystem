import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorfulQuizApp {
    private static JFrame frame;
    private static JPanel panel;
    private static CardLayout cardLayout;
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private static ButtonGroup optionGroup;
    private static int score = 0;
    private static int currentQuestionIndex = 0;
    private static Timer timer;
    private static int timeLeft = 10;  // 10 seconds per question
    private static JLabel timerLabel;

    private static String[] questions = {
        "What is the capital of France?",
        "Which planet is known as the Red Planet?",
        "What is 2 + 2?"
    };

    private static String[][] options = {
        {"Paris", "London", "Rome", "Berlin"},
        {"Mars", "Venus", "Earth", "Jupiter"},
        {"3", "4", "5", "6"}
    };

    private static int[] correctAnswers = {0, 0, 1}; // Correct answers (index starting from 0)

    public static void main(String[] args) {
        // Initialize frame and layout
        frame = new JFrame("Colorful Quiz App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);

        // Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        loginPanel.setBackground(Color.CYAN);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());  // Empty label for alignment
        loginPanel.add(loginButton);

        // Quiz Panel
        JPanel quizPanel = new JPanel();
        quizPanel.setLayout(new BorderLayout());
        quizPanel.setBackground(Color.LIGHT_GRAY);

        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(7, 1));
        questionPanel.setBackground(Color.LIGHT_GRAY);

        JLabel questionLabel = new JLabel("Question will appear here", JLabel.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        questionLabel.setForeground(Color.BLACK);

        questionPanel.add(questionLabel);

        optionGroup = new ButtonGroup();
        JRadioButton option1 = new JRadioButton();
        JRadioButton option2 = new JRadioButton();
        JRadioButton option3 = new JRadioButton();
        JRadioButton option4 = new JRadioButton();

        optionGroup.add(option1);
        optionGroup.add(option2);
        optionGroup.add(option3);
        optionGroup.add(option4);

        option1.setBackground(Color.LIGHT_GRAY);
        option2.setBackground(Color.LIGHT_GRAY);
        option3.setBackground(Color.LIGHT_GRAY);
        option4.setBackground(Color.LIGHT_GRAY);

        questionPanel.add(option1);
        questionPanel.add(option2);
        questionPanel.add(option3);
        questionPanel.add(option4);

        timerLabel = new JLabel("Time: " + timeLeft + "s", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timerLabel.setForeground(Color.RED);
        questionPanel.add(timerLabel);

        JButton nextButton = new JButton("Next");
        nextButton.setBackground(Color.GREEN);
        nextButton.setForeground(Color.WHITE);
        questionPanel.add(nextButton);

        quizPanel.add(questionPanel, BorderLayout.CENTER);

        // Result Panel
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBackground(Color.YELLOW);

        JLabel resultLabel = new JLabel("Your score: 0", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabel.setForeground(Color.BLUE);
        resultPanel.add(resultLabel, BorderLayout.CENTER);

        JButton restartButton = new JButton("Restart");
        restartButton.setBackground(Color.ORANGE);
        restartButton.setForeground(Color.WHITE);
        resultPanel.add(restartButton, BorderLayout.SOUTH);

        // Add panels to the cardLayout
        panel.add(loginPanel, "Login");
        panel.add(quizPanel, "Quiz");
        panel.add(resultPanel, "Result");

        frame.setContentPane(panel);
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);

        // Action Listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.equals("user1") && password.equals("123")) {
                    cardLayout.show(panel, "Quiz");
                    loadNextQuestion();
                    startTimer();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password.");
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAnswerCorrect()) {
                    score++;
                }
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    loadNextQuestion();
                    resetTimer();
                } else {
                    showResult();
                }
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                score = 0;
                currentQuestionIndex = 0;
                loadNextQuestion();
                resetTimer();
                cardLayout.show(panel, "Quiz");
            }
        });
    }

    // Method to load the next question
    private static void loadNextQuestion() {
        if (currentQuestionIndex < questions.length) {
            JPanel questionPanel = (JPanel) ((JPanel) panel.getComponent(1)).getComponent(0);
            JLabel questionLabel = (JLabel) questionPanel.getComponent(0);
            JRadioButton option1 = (JRadioButton) questionPanel.getComponent(1);
            JRadioButton option2 = (JRadioButton) questionPanel.getComponent(2);
            JRadioButton option3 = (JRadioButton) questionPanel.getComponent(3);
            JRadioButton option4 = (JRadioButton) questionPanel.getComponent(4);

            questionLabel.setText(questions[currentQuestionIndex]);
            option1.setText(options[currentQuestionIndex][0]);
            option2.setText(options[currentQuestionIndex][1]);
            option3.setText(options[currentQuestionIndex][2]);
            option4.setText(options[currentQuestionIndex][3]);

            optionGroup.clearSelection();
        }
    }

    // Method to check if the selected answer is correct
    private static boolean isAnswerCorrect() {
        JPanel questionPanel = (JPanel) ((JPanel) panel.getComponent(1)).getComponent(0);
        JRadioButton option1 = (JRadioButton) questionPanel.getComponent(1);
        JRadioButton option2 = (JRadioButton) questionPanel.getComponent(2);
        JRadioButton option3 = (JRadioButton) questionPanel.getComponent(3);
        JRadioButton option4 = (JRadioButton) questionPanel.getComponent(4);

        if (option1.isSelected() && correctAnswers[currentQuestionIndex] == 0) {
            return true;
        } else if (option2.isSelected() && correctAnswers[currentQuestionIndex] == 1) {
            return true;
        } else if (option3.isSelected() && correctAnswers[currentQuestionIndex] == 2) {
            return true;
        } else if (option4.isSelected() && correctAnswers[currentQuestionIndex] == 3) {
            return true;
        }
        return false;
    }

    // Method to start the timer for each question
    private static void startTimer() {
        timeLeft = 10; // Reset to 10 seconds per question
        JPanel questionPanel = (JPanel) ((JPanel) panel.getComponent(1)).getComponent(0);
        timerLabel = (JLabel) questionPanel.getComponent(6);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerLabel.setText("Time: " + timeLeft + "s");
                if (timeLeft == 0) {
                    timer.stop();
                    nextQuestion();
                }
            }
        });
        timer.start();
    }

    // Method to reset the timer for the next question
    private static void resetTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        startTimer();
    }

    // Method to go to the next question when the timer runs out
    private static void nextQuestion() {
        if (isAnswerCorrect()) {
            score++;
        }
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            loadNextQuestion();
            resetTimer();
        } else {
            showResult();
        }
    }

    // Method to display the result after the quiz is finished
    private static void showResult() {
        JPanel resultPanel = (JPanel) panel.getComponent(2);
        JLabel resultLabel = (JLabel) resultPanel.getComponent(0);
        resultLabel.setText("Your score: " + score);
        cardLayout.show(panel, "Result");
    }
}
