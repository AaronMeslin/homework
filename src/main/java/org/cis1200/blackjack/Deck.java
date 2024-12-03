package org.cis1200.blackjack;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;


/*Deck to hold the cards that have not been dealt yet
 * 52 Cards, 4 suits, 13 ranks
 * If deck is empty - all 52 cards are added back and the deck is shuffled
*/

public class Deck{
    List<Card> CurrentDeck = new ArrayList<Card>();
    private int cardCount;
    String[] suits = {"hearts", "diamonds", "clubs", "spades"};
    String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

    public Deck(){
        this.cardCount = 0;
    }

    public void shuffle(){
        Collections.shuffle(CurrentDeck);
    }

    public void populateDeck() {
        for (String suit : suits){
            for (String rank : ranks){
                CurrentDeck.add(new Card(suit,rank));
                cardCount++;
            }
            CurrentDeck.add(new Treasure(suit));
            cardCount++;

            CurrentDeck.add(new Peek(suit));
            cardCount++;
        }



    }

    public List<Card> getCurrentDeck(){
        return CurrentDeck;
    }

    public int getCardCount(){
        return cardCount;
    }

    public Card drawCard(){
        Card cardDrawn = CurrentDeck.get(0);
        CurrentDeck.remove(0);
        cardCount--;
        if(cardCount < 12){
            CurrentDeck.clear();
            populateDeck();
            shuffle();
        }
        return cardDrawn;
    }

}

