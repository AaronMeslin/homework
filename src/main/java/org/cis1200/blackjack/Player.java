package org.cis1200.blackjack;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Player {
    private int chipCount;
    private String name;
    private Hand hand;


    public Player(String name, int chipCount) {
        this.name = name;
        this.chipCount = chipCount;
        this.hand = new Hand();
    }
    public Player(String name) {
        this.name = name;
        this.chipCount = 0;
        this.hand = new Hand();
    }
    public void addCard(Deck deck){
        Card currentCard = deck.drawCard();
        hand.addCard(currentCard);
    }

    public String getHandString(){
        return hand.getCards().toString();
    }

    public Hand getHand(){
        return hand;
    }

    public String getName() {
        return name;
    }

    public int getChipCount() {
        return chipCount;
    }

    public void betChipAmount(int bet) {
        chipCount -= bet;
        updateChipCount();
    }

    public void winChipAmount(int win) {
        chipCount += win;
        updateChipCount();
    }

    public void resetHand(){
        hand = new Hand();
    }

    public void resetChipCount(){
        chipCount = 1000;
        updateChipCount();
    }

    public void updateChipCount() {
        File file = new File("C:\\Users\\Aaron\\IdeaProjects\\homework1\\src\\main\\java\\org\\cis1200\\blackjack\\save.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String content = "";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("chip count:")) {
                line = "chip count: "+ chipCount;
            }

            content += line + "\n";
        }
        scanner.close();


        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
