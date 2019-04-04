package com.tudordonca.android.blackjackmvvm.gamemechanics;



public class StandardCard implements Card {
    private Suit suit;
    private Face face;

    public enum Suit {HEART, DIAMOND, CLUB, SPADE};
    public enum Face {TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE};

    public StandardCard(Suit suit, Face face){
        this.suit = suit;
        this.face = face;
    }

    public String getName(){
        return toString();
    }

    public Face getFace(){
        return face;
    }


    public String toString(){
        String name = "";
        switch(face) {
            case TWO:
                name += "2";
                break;
            case THREE:
                name += "3";
                break;
            case FOUR:
                name += "4";
                break;
            case FIVE:
                name += "5";
                break;
            case SIX:
                name += "6";
                break;
            case SEVEN:
                name += "7";
                break;
            case EIGHT:
                name += "8";
                break;
            case NINE:
                name += "9";
                break;
            case TEN:
                name += "10";
                break;
            case JACK:
                name += "J";
                break;
            case QUEEN:
                name += "Q";
                break;
            case KING:
                name += "K";
                break;
            case ACE:
                name += "A";
                break;
            default:
                break;
        }

        switch(suit) {
            case HEART:
                name += "heart";
                break;
            case DIAMOND:
                name += "diamond";
                break;
            case CLUB:
                name += "club";
                break;
            case SPADE:
                name += "spade";
                break;
            default:
                break;
        }


        return name;
    }

}
