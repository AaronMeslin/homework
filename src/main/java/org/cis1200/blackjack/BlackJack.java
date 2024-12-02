package org.cis1200.blackjack;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class BlackJack {
    private Deck deck;
    private int betAmount;
    private Player player;
    private Player dealer;
    private JPanel betPanel = new JPanel();
    private JFrame frame;
    private JButton hitButton;
    private JButton standButton;
    private JButton dealButton;
    private JButton doubleButton;
    private JButton splitButton;
    private JTextArea gameArea;
    private JTextField betField;
    private JLabel betLabel;
    private JLabel chipCountLabel;
    private JLabel playerLabel;
    private JLabel dealerLabel;
    private JPanel playerPanel;
    private JPanel dealerPanel;

    public BlackJack() {
        player = new Player("Player", 1000);
        dealer = new Player("Dealer");
        deck = new Deck();
        deck.populateDeck();
        deck.shuffle();
        setupGameBoard();
    }

    private void setupGameBoard() {
        frame = new JFrame("Blackjack Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        gameArea = new JTextArea();
        gameArea.setEditable(false);
        frame.add(new JScrollPane(gameArea), BorderLayout.CENTER);

        betLabel = new JLabel("Enter your bet:");
        betField = new JTextField(10);
        dealButton = new JButton("Deal");

        betPanel.add(betLabel);
        betPanel.add(betField);
        betPanel.add(dealButton);
        frame.add(betPanel, BorderLayout.NORTH);


        JPanel chipPanel = new JPanel();
        chipCountLabel = new JLabel("Chips: " + player.getChipCount());
        chipPanel.add(chipCountLabel);
        frame.add(chipPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new GridLayout(1, 3));

        JPanel leftPanel = new JPanel(new BorderLayout());
        JLabel gameAreaLabel = new JLabel("Game Area");
        leftPanel.add(gameAreaLabel, BorderLayout.NORTH);
        gameArea = new JTextArea();
        gameArea.setEditable(false);
        leftPanel.add(new JScrollPane(gameArea), BorderLayout.CENTER);

        playerPanel = new JPanel(new BorderLayout());
        playerLabel = new JLabel("Player Total: ");
        playerPanel.add(playerLabel, BorderLayout.NORTH);
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));


        dealerPanel = new JPanel(new BorderLayout());
        dealerLabel = new JLabel("Dealer Total: ");
        dealerPanel.add(dealerLabel, BorderLayout.NORTH);
        dealerPanel.setLayout(new BoxLayout(dealerPanel, BoxLayout.Y_AXIS));

        centerPanel.add(leftPanel);
        centerPanel.add(playerPanel);
        centerPanel.add(dealerPanel);

        frame.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        doubleButton = new JButton("Double");
        splitButton = new JButton("Split");

        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(dealButton);
        buttonPanel.add(doubleButton);
        buttonPanel.add(splitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.setVisible(false);
        standButton.setVisible(false);
        doubleButton.setVisible(false);
        splitButton.setVisible(false);

        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerHit();
            }
        });

        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerStand();
            }
        });

        doubleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerDouble();
            }
        });

        splitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerSplit();
            }
        });

        dealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get the bet amount from the text field and validate it
                    betAmount = Integer.parseInt(betField.getText());
                    if (betAmount <= 0 || betAmount > player.getChipCount()) {
                        gameArea.setText("Invalid bet");
                    } else {
                        playerPanel.removeAll();
                        playerPanel.add(playerLabel, BorderLayout.NORTH);
                        dealerPanel.removeAll();
                        dealerPanel.add(dealerLabel, BorderLayout.NORTH);
                        gameArea.setText("Bet placed: " + betAmount + "\n");
                        player.betChipAmount(betAmount);
                        chipCountLabel.setText("Chips: " + player.getChipCount());
                        betPanel.setVisible(false);
                        hitButton.setVisible(true);
                        standButton.setVisible(true);
                        doubleButton.setVisible(true);
                        splitButton.setVisible(true);
                        dealButton.setVisible(false);
                        dealInitialCards();
                    }
                } catch (NumberFormatException ex) {
                    gameArea.setText("Invalid bet. Please enter a valid bet");
                }
            }
        });
        frame.setVisible(true);
    }

    public void dealInitialCards(){
        player.addCard(deck);
        player.addCard(deck);
        dealer.addCard(deck);
        dealer.addCard(deck);
        playerLabel.setText("Player Total: " + player.getHand().calculateValue());
        dealerLabel.setText("Dealer Showing: " + dealer.getHand().getCards().get(0));

        for (Card card : player.getHand().getCards()){
            playerPanel.add(card.toImage(), BorderLayout.CENTER);

        }

        dealerPanel.add((dealer.getHand().getCards().get(0)).toImage(), BorderLayout.CENTER);


        if(player.getHand().calculateValue() == 21){
            gameArea.append("Player wins: " + betAmount*1.5 + " chips!"+"\n");
            player.winChipAmount((int)Math.round(betAmount + betAmount*1.5));
            evaluateWinner(true);
        }
    }
q\
    public void playerHit(){
        doubleButton.setVisible(false);
        splitButton.setVisible(false);
        player.addCard(deck);
        playerLabel.setText("Player Total: " + player.getHand().calculateValue());
        Card newcard = player.getHand().getCards().get(player.getHand().getCards().size() - 1);
        playerPanel.add(newcard.toImage(), BorderLayout.CENTER);
        if(player.getHand().calculateValue() >= 21){
            DealerFinish();
            evaluateWinner(false);
        }
    }

    public void playerStand(){
        DealerFinish();
        evaluateWinner(false);
    }

    public void playerDouble(){
        player.addCard(deck);
        playerLabel.setText("Player Total: " + player.getHand().calculateValue());
        Card newcard = player.getHand().getCards().get(player.getHand().getCards().size() - 1);
        playerPanel.add(newcard.toImage(), BorderLayout.CENTER);
        player.betChipAmount(betAmount);
        betAmount *= 2;
        chipCountLabel.setText("Chips: " + player.getChipCount());
        doubleButton.setVisible(false);
        splitButton.setVisible(false);
        DealerFinish();
        evaluateWinner(false);
    }

    public void playerSplit(){
        player.betChipAmount(betAmount);
        betAmount *= 2;
        doubleButton.setVisible(false);
        splitButton.setVisible(false);
    }


    public void DealerFinish(){
        while(dealer.getHand().calculateValue() < 17){
            dealer.addCard(deck);
        }
        dealerLabel.setText("Dealer Total: " + dealer.getHand().calculateValue());
        dealerPanel.remove(1);
        for (Card card : dealer.getHand().getCards()){
            dealerPanel.add(card.toImage(), BorderLayout.CENTER);
        }
    }

    public void evaluateWinner(boolean blackJack) {
        hitButton.setVisible(false);
        standButton.setVisible(false);
        doubleButton.setVisible(false);
        splitButton.setVisible(false);
        int playerVal = player.getHand().calculateValue();
        int dealerVal = dealer.getHand().calculateValue();

        if(!blackJack){
            if(playerVal > 21){
                gameArea.append("Player lost: " + betAmount + " chips!"+"\n");
            }
            else if (dealerVal > 21){
                gameArea.append("Player wins: " + betAmount + " chips!"+"\n");
                player.winChipAmount(betAmount*2);
            }
            else if(playerVal == dealerVal){
                gameArea.append("Tie!");
                player.winChipAmount(betAmount);
            }
            else {
                if (playerVal > dealerVal){
                    gameArea.append("Player won: " + betAmount + " chips!"+"\n");
                    player.winChipAmount(betAmount*2);
                }
                if (playerVal < dealerVal){
                    gameArea.append("Player lost: " + betAmount + " chips!"+"\n");
                }
            }
        }
        chipCountLabel.setText("Chips: " + player.getChipCount());
        betLabel.setVisible(true);
        player.resetHand();
        dealer.resetHand();
        betAmount = 0;
        betLabel.setVisible(true);
        dealButton.setVisible(true);
        betField.setVisible(true);
        betPanel.setVisible(true);
    }



    public static void main(String[] args) {
        new BlackJack();
    }
}
