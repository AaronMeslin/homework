package org.cis1200.blackjack;

import java.util.*;

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
    }

    public void winChipAmount(int win) {
        chipCount += win;
    }

    public void resetHand(){
        hand = new Hand();
    }

}
