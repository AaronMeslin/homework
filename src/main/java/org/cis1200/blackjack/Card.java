package org.cis1200.blackjack;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card{
    private final String suit;
    private final String rank;
    private final int value;


    public Card(String suit, String rank){
        this.suit = suit;
        this.rank = rank;
        if(rank.equals("Ace")){
            this.value = 11;
        }
        else if (rank.equals("Jack") || rank.equals("Queen") || rank.equals("King") || rank.equals("Treasure")){
            this.value = 10;
        }
        else{
            this.value = Integer.parseInt(rank);
        }

    }

    public String getSuit(){
        return suit;
    }
    public String getRank(){
        return rank;
    }
    public int getValue() { return value; }

    public String toString(){
        return rank + "_of_" + suit;
    }

    public JLabel toImage(){
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("C:\\Users\\Aaron\\IdeaProjects\\homework1\\src\\main\\java\\org\\cis1200\\blackjack\\cardImages\\"+ this.toString()+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image scaledImage = myPicture.getScaledInstance(100, 150, Image.SCALE_SMOOTH); // Set desired width and height
        return new JLabel(new ImageIcon(scaledImage));
    }
}