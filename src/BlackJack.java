import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackJack {
    Player player;
    Player dealer;
    List<Card> deck;

    //window
    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 600;
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 150;

    JFrame frame = new JFrame("21");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            try {
                //draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                g.drawImage(hiddenCardImg, 20, 20, CARD_WIDTH, CARD_HEIGHT, null);

                //draw dealer hand
                  for (int i = 0; i < dealer.getSizeOfHand(); i++) {
                    Card card = dealer.getPlayerHand().get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, CARD_WIDTH + 30 + (CARD_WIDTH + 10) * i, 20, CARD_WIDTH, CARD_HEIGHT, null);
                }

                //draw players hand
                for (int i = 0; i < player.getSizeOfHand(); i++) {
                    Card card = player.getPlayerHand().get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 30 + (CARD_WIDTH + 10) * i, 300, CARD_WIDTH, CARD_HEIGHT, null);
                }

                if (!stayButton.isEnabled()) {
                    player.reducePlayerAce();
                    dealer.reducePlayerAce();
                    String message = getResultMessage();

                    g.setFont(new Font("Arial", Font.PLAIN, 50));
                    g.setColor(Color.white);
                    g.drawString(message, 240, 250);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Взять карту");
    JButton stayButton = new JButton("Вскрываемся");
    JButton restartButton = new JButton("Рестарт");

    private String getResultMessage() {
        String message;
        int playerSum = player.getPlayerSum();
        int dealerSum = dealer.getPlayerSum();
        if (playerSum > 21) {
            message = "Вы проиграли!";
        } else if (dealerSum > 21) {
            message = "Вы выиграли!";
        } else if (playerSum == dealerSum) {
            message = "Ничья";
        } else if (playerSum > dealerSum) {
            message = "Вы выиграли!";
        } else {
            message = "Вы проиграли!";
        }
        return message;
    }

    public BlackJack() {
        startGame();

        frame.setVisible(true);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(55, 105, 80));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        restartButton.setFocusable(false);
        buttonPanel.add(restartButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card card = deck.removeLast();
                updatePlayerDate(card, player);
                if (player.reducePlayerAce() > 21) {
                    hitButton.setEnabled(false);
                }

                gamePanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealer.getPlayerSum() < 17) {
                    Card card = deck.removeLast();
                    updatePlayerDate(card, dealer);
                }
                gamePanel.repaint();
            }
        });
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
                stayButton.setEnabled(true);
                hitButton.setEnabled(true);
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();
    }

    private void updatePlayerDate(Card card, Player curPlayer) {
        curPlayer.increaseSum(card.getValue());
        curPlayer.increaseAceCount((card.isAce()) ? 1 : 0);
        curPlayer.addCardToHand(card);
    }

    public void startGame() {
        buildDeck();
        shuffleDeck();

        player = new Player();
        dealer = new Player();

        Card card = deck.removeLast();
        updatePlayerDate(card, dealer);

        for (int i = 0; i < 2; i++) {
            card = deck.removeLast();
            updatePlayerDate(card, player);
        }
    }

    private void buildDeck() {
        deck = new ArrayList<>();
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] type = {"S", "D", "C", "H"};

        for (String s : type) {
            for (String value : values) {
                Card card = new Card(value, s);
                deck.add(card);
            }
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }
}