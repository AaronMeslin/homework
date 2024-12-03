package org.cis1200.blackjack;

public class Treasure extends Card {
    private final int giftWorth;
    public Treasure(String suit) {
        super(suit, "Treasure");
        giftWorth = (int)Math.round(Math.random()*200);
    }

    public int getTreasureWorth() {
        return giftWorth;
    }
}
