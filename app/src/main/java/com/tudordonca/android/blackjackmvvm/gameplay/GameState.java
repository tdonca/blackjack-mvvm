package com.tudordonca.android.blackjackmvvm.gameplay;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    public enum GameProgressState {INVALID, USER_TURN, ROUND_FINISHED};

    private GameProgressState state;
    private List<Card> dealerCards;
    private List<Card> userCards;
    private String winner;
    private String message;

    public GameState(GameProgressState state){
        this.state = state;
        dealerCards = new ArrayList<>();
        userCards = new ArrayList<>();
    }

    public GameState(GameProgressState state, List<Card> dealerCards, List<Card> userCards){
        this.state = state;
        this.dealerCards = new ArrayList<>(dealerCards);
        this.userCards = new ArrayList<>(userCards);
    }

    public GameProgressState getState(){
        return state;
    }

    public List<Card> getDealerCards(){
        return dealerCards;
    }

    public List<Card> getUserCards(){
        return userCards;
    }

    public String getWinner(){
        return winner;
    }

    public String getMessage(){
        return message;
    }
    void setWinner(String winner){
        this.winner = winner;
    }

    void setMessage(String message){
        this.message = message;
    }




}
