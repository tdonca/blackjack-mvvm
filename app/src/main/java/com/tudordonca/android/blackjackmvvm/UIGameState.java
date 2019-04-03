package com.tudordonca.android.blackjackmvvm;

import java.util.ArrayList;
import java.util.List;

public class UIGameState {

    public enum State {INVALID, WELCOME, DENIED, IN_PROGRESS, DEALER_WIN, USER_WIN, TIE};
    private State state;
    private List<String> dealerCards;
    private List<String> userCards;
    private int userMoney;
    private String message;

    public UIGameState(State state){
        this.state = state;
        dealerCards = new ArrayList<>();
        userCards = new ArrayList<>();
    }

    public State getState(){
        return state;
    }

    public int getUserMoney(){
        return userMoney;
    }

    public List<String> getDealerCards(){
        return dealerCards;
    }

    public List<String> getUserCards(){
        return userCards;
    }

    public String getMessage(){
        return message;
    }



    public void setMessage(String message){
        this.message = message;
    }

    public void setDealerCards(List<String> cards){
        dealerCards.clear();
        dealerCards.addAll(cards);
    }

    public void setUserCards(List<String> cards){
        userCards.clear();
        userCards.addAll(cards);
    }

    public void setUserMoney(int money){
        userMoney = money;
    }

}
