package org.cis1200.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public String getHandString(){
        String handString = "";
        for (Card card : cards) {
            handString += card.toString() + ", ";
        }
        return handString.substring(0, handString.length() - 2);
    }

    public int calculateValue(){
        int totalValue = 0;
        int aceCount = 0;

        for (Card card : cards) {
            totalValue += card.getValue();
            if (card.getRank().equals("Ace")) {
                aceCount++;
            }
        }
        while (totalValue > 21 && aceCount > 0) {
            totalValue -= 10;
            aceCount--;
        }

        return totalValue;
    }
    public void removeLastCard(){
        cards.remove(cards.size() - 1);
    }

}
