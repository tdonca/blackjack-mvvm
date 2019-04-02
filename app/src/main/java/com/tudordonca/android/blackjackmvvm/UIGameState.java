package com.tudordonca.android.blackjackmvvm;

import com.tudordonca.android.blackjackmvvm.gameplay.GameState;

public class UIGameState {

    private GameState gameState;
    private int userMoney;
    private String message;

    public UIGameState(GameState gameState, int userMoney){
        this.gameState = gameState;
        this.userMoney = userMoney;
    }

    public GameState getGameState(){
        return gameState;
    }

    public int getUserMoney(){
        return userMoney;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

}
