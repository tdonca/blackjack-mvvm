package com.tudordonca.android.blackjackmvvm.gameplay.gamemechanics;

import java.util.ArrayList;
import java.util.List;

public class UserPlayer implements Player {
    List<Card> cards;

    public UserPlayer(){
        cards = new ArrayList<>();
    }

    public List<Card> getCards(){
        return cards;
    }


    public List<String> getCardsDisplay(){
        List<String> cardDisplays = new ArrayList<>();
        for(Card c : cards){
            cardDisplays.add(c.getName());
        }
        return cardDisplays;
    }


    public void addCard(Card card){
        cards.add(card);
    }


    public void clearCards(){
        cards.clear();
    }
}
