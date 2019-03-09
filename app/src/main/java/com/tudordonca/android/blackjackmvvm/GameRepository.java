package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.LiveData;


import com.tudordonca.android.blackjackmvvm.gameplay.GameExecution;

import java.util.List;

public class GameRepository {
    private GameExecution game;
    private LiveData<List<String>> dealerHand;
    private LiveData<List<String>> userHand;
    private LiveData<String> winner;


    public GameRepository(){
        game = new GameExecution();

    }

    public void init(){
        if(dealerHand == null){
            dealerHand = game.getDealerHandDisplay();
        }
        if(userHand == null){
            userHand = game.getUserHandDisplay();
        }
        if(winner == null){
            winner = game.getWinnerDisplay();
        }
    }

    public LiveData<List<String>> getDealerHand(){
        return dealerHand;
    }

    public LiveData<List<String>> getPlayerHand(){
        return userHand;
    }

    public LiveData<String> getWinner(){
        return winner;
    }

    public void userHit(){
        game.userHit();
    }

    public void userStay(){
        game.userStay();
    }

    public void newRound(){
        game.startRound();
    }
}
