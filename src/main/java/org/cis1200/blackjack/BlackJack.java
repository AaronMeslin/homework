package org.cis1200.blackjack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BlackJack implements  Runnable {
    private Deck deck;
    private int betAmount;
    private Player player;
    private Player dealer;
    private JPanel betPanel = new JPanel();
    private JFrame frame;
    private JButton hitButton;
    private JButton standButton;
    private JButton resetButton;
    private JButton dealButton;
    private JButton doubleButton;
    private JTextArea gameArea;
    private JTextField betField;
    private JLabel betLabel;
    private JLabel chipCountLabel;
    private JLabel playerLabel;
    private JLabel dealerLabel;
    private JPanel playerPanel;
    private JPanel dealerPanel;
    private JButton instructionsButton;
    private JFrame instructionsFrame;
    private JLabel instructionsLabel;

    public BlackJack() {
        File file = new File("C:\\Users\\Aaron\\IdeaProjects\\homework1\\src\\main\\java\\org\\cis1200\\blackjack\\save.txt");
        try {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.contains("chip count: ")) {
                    String[] parts = line.split(": ");
                    player = new Player("Player",  Integer.parseInt(parts[1]));
                    scanner.close();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        dealer = new Player("Dealer");
        deck = new Deck();
        deck.populateDeck();
        deck.shuffle();
        setupGameBoard();
    }

    private void setupGameBoard() {

        instructionsFrame = new JFrame("Instructions");

        instructionsLabel = new JLabel("<html>Blackjack: <br> <br>" +
                "A game where the player competes against the dealer to have a hand value closer to 21" +
                "without exceeding it.<br>  Each card has a point value: numbered cards are " +
                "worth their face value: <br>  Face cards are worth 10 and aces can count as either 1 or 11.<br> " +
                " Players are dealt two cards initially and can choose to: <br> \"hit\" (take another card) <br> " +
                "\"stand\" (keep their current total) <br> \"double\" (take another card but double the bet<br> " +
                "The player aims to beat the dealer's hand or avoid going \"bust\" (exceeding 21).<br> " +
                "The deck is a standard 52 card deck with the following unique cards: <br> <br>" +

                "How To Play: <br> <br> "  +
                "Enter you bet amount with keyboard input. It must be a number<br> " +
                "The deal button will deal the initial cards once the bet is set <br> " +
                "The hit button gives you another card<br> " +
                "The stand button ends your hand and compares it to the dealer<br> " +
                "The double button gives you one more card and doubles your bet. You cannot get another card after you double<br> " +
                "</html>"
        );

        instructionsFrame.setSize(800, 400);
        instructionsFrame.setLayout(new BorderLayout());
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.add(instructionsLabel, GroupLayout.Alignment.CENTER);
        instructionsFrame.add(instructionsPanel, BorderLayout.CENTER);

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


        resetButton = new JButton("Restart");

        instructionsButton = new JButton("Instructions");

        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(dealButton);
        buttonPanel.add(doubleButton);

        JPanel resetPanel = new JPanel();
        resetPanel.add(resetButton);
        resetPanel.add(instructionsButton);




        frame.add(resetPanel, BorderLayout.EAST);


        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.setVisible(false);
        standButton.setVisible(false);
        doubleButton.setVisible(false);

        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerHit();
            }
        });

        instructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionsFrame.setVisible(true);
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


        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
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

    public void playerHit(){
        doubleButton.setVisible(false);
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

    public void reset(){
        frame.dispose();
        player.resetChipCount();
        new BlackJack();
    }

    public void playerDouble(){

        if(player.getChipCount() >= betAmount){
            player.addCard(deck);
            playerLabel.setText("Player Total: " + player.getHand().calculateValue());
            Card newcard = player.getHand().getCards().get(player.getHand().getCards().size() - 1);
            playerPanel.add(newcard.toImage(), BorderLayout.CENTER);
            player.betChipAmount(betAmount);
            betAmount *= 2;
            chipCountLabel.setText("Chips: " + player.getChipCount());
            doubleButton.setVisible(false);
            DealerFinish();
            evaluateWinner(false);
        }
        else{
            gameArea.append("Not enough chips to double!\n");
        }

    }

    public void playerSplit(){
        player.betChipAmount(betAmount);
        betAmount *= 2;
        doubleButton.setVisible(false);
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

    @Override
    public void run() {
        new BlackJack();
    }
}
