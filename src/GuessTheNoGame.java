import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// import java.time.Period;

public class GuessTheNoGame {
    public static void main(String[] args) {
        new guessingNumber("irfan");
    }
}

class guessingNumber extends JFrame {
    // front end
    JLabel l1, l2, l3, title, GNbackground, tt;
    JTextField t1;
    JButton ok, restart, exit;
    ImageIcon gameIcon;
    Image gameImage;
    JPanel pN;
    JPanel pC;
    JPanel pW;
    JPanel pS;

    // back end
    int chances = 100;
    int highScore;
    Jdbc conJdbc;
    int number = 1 + (int) (10000 * Math.random());
    int guess;
    int K = chances;
    int flag = 0;
    int win = 0;

    public guessingNumber(String username) {

        conJdbc = new Jdbc(username);
        highScore = conJdbc.fetchHighscore(1);
        System.out.println(highScore);

        System.out.println(number);
        gameIcon = new ImageIcon(getClass().getResource("/Images/GNbackground.jpg"));
        gameImage = gameIcon.getImage();
        gameImage = gameImage.getScaledInstance(325, 310, java.awt.Image.SCALE_SMOOTH);
        gameIcon = new ImageIcon(gameImage);

        setTitle("User : " + username);
        l1 = new JLabel("Enter the number from 1-10000", SwingConstants.CENTER);
        l2 = new JLabel("Start Guessing.....", SwingConstants.CENTER);
        l3 = new JLabel("Trials left: " + chances, SwingConstants.CENTER);
        l1.setForeground(Color.orange);
        l2.setForeground(Color.white);
        l3.setForeground(Color.orange);
        GNbackground = new JLabel(gameIcon);
        // GNbackground.setSize(50,50);
        t1 = new JTextField(25);
        ok = new JButton("Ok");
        restart = new JButton("RESTART");
        exit = new JButton("EXIT");
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());
        c.setBackground(new Color(145, 255, 229));
        // Top elements
        pN = new JPanel();
        tt = new JLabel("Guess the number game...?");
        tt.setForeground(Color.orange);
        pN.add(tt);
        pN.setBackground(Color.black);
        c.add(pN, BorderLayout.NORTH);

        // center elements

        pC = new JPanel();
        pC.setLayout(new FlowLayout(4, 4, 4));
        pC.setBackground(Color.black);
        pW = new JPanel();
        pW.setLayout(new GridLayout(5, 1, 0, 20));
        pW.setBackground(Color.black);
        pW.add(l3);
        pW.add(l1);
        t1.setBackground(Color.gray);
        t1.setForeground(Color.black);
        pW.add(t1);
        ok.setBackground(Color.gray);
        ok.setBorder(null);
        pW.add(ok);
        pW.add(l2);
        pC.add(pW);
        JLabel label = new JLabel("Center Box", SwingConstants.CENTER);
        label.setOpaque(true);
        c.add(GNbackground);
        c.add(pC, BorderLayout.WEST);

        // Bottom elements
        pS = new JPanel();
        pS.setLayout(new FlowLayout());
        restart.setBackground(Color.orange);
        restart.setForeground(Color.BLACK);
        restart.setBorder(BorderFactory.createBevelBorder(1, Color.orange, Color.orange));
        exit.setBackground(Color.orange);
        exit.setForeground(Color.BLACK);
        exit.setBorder(BorderFactory.createBevelBorder(1, Color.orange, Color.orange));
        pS.add(restart);
        pS.add(exit);
        pS.setBackground(Color.BLACK);
        c.add(pS, BorderLayout.SOUTH);

        // setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(3);
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guess = Integer.parseInt(t1.getText());
                if (chances >= 0 && flag == 0) {
                    if (number == guess) {
                        l2.setText("You Guessed Correctly ");
                        l2.setForeground(Color.green);
                        flag = 1;
                        chances--;
                        win = 1;

                        if (chances < highScore || highScore == 0) {
                            conJdbc.updatescore(chances, 1);
                        }
                    } else if (number > guess) {
                        l2.setText(
                                "The number is "
                                        + "greater than " + guess);
                        chances--;
                    } else if (number < guess) {
                        l2.setText(
                                "The number is"
                                        + " less than " + guess);
                        chances--;
                    }

                    if (chances == 0 && win == 0) {
                        l2.setText(
                                "You Failed," +
                                        "The number was " + number);
                        l2.setForeground(Color.red);
                        flag = -1;

                        JOptionPane.showMessageDialog(guessingNumber.this,
                                "Game Over",
                                "Try Again",
                                JOptionPane.ERROR_MESSAGE);
                        if (chances < highScore || highScore == 0) {
                            conJdbc.updatescore(chances, 1);
                        }
                        conJdbc.connectionClose();
                        dispose();
                        new guessingNumber(username);
                    }

                    l3.setText("Trials left: " + chances);
                } else if (flag == -1) {
                    l2.setText(
                            "You Failed, Restart the game to play again!");

                    JOptionPane.showMessageDialog(guessingNumber.this,
                            "Game Over",
                            "Try Again",
                            JOptionPane.ERROR_MESSAGE);
                    if (chances < highScore || highScore == 0) {
                        conJdbc.updatescore(chances, 1);
                    }
                    conJdbc.connectionClose();
                    dispose();
                    new guessingNumber(username);
                } else if (flag == 1) {
                    l2.setText("You Won!..." + "Please restart the game to play again");
                    if (chances < highScore || highScore == 0) {
                        conJdbc.updatescore(chances, 1);
                    }
                }
            }

        });

        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new guessingNumber(username);
                dispose();
                if (chances < highScore || highScore == 0) {
                    conJdbc.updatescore(chances, 1);
                }
                conJdbc.connectionClose();
                dispose();
            }
        });
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MainFrame(username);
                dispose();
                if (chances < highScore || highScore == 0) {
                    conJdbc.updatescore(chances, 1);
                }
                conJdbc.connectionClose();
                dispose();

            }
        });
    }
}