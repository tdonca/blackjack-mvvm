package com.tudordonca.android.blackjackmvvm.gameplay;

import java.util.List;

public class StandardRuleSet {

    static int getValue(List<Card> hand){
        int value = 0;
        int numAces = 0;
        for (Card c : hand){
            if(((StandardCard)c).getFace() == StandardCard.Face.ACE){
                numAces += 1;
            }
            value += getValue((StandardCard)c);
        }

        while(value > 21 && numAces > 0){
            value -= 10;
            numAces -= 1;
        }

        return value;
    }


    static int getValue(StandardCard card){
        switch(card.getFace()){
            case TWO: return 2;
            case THREE: return 3;
            case FOUR: return 4;
            case FIVE: return 5;
            case SIX: return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE: return 9;
            case TEN: return 10;
            case JACK: return 10;
            case QUEEN: return 10;
            case KING: return 10;
            case ACE: return 11;
            default: return 0;
        }
    }
}
