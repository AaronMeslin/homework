package org.cis1200.blackjack;

public class Peek extends Card {
    public Peek (String suit){
        super(suit, "Peek");
    }

    public Card peek(Player dealer){
        return dealer.getHand().getCards().get(1);
    }
}
