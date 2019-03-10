package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;


import com.tudordonca.android.blackjackmvvm.gameplay.GameExecution;

import java.util.List;

public class GameRepository {
    private final String LOG_TAG = "GameRepository";
    private final int MIN_BET = 25;
    private GameExecution game;
    private LiveData<List<String>> dealerHand;
    private LiveData<List<String>> userHand;
    private LiveData<String> winner;
    private MutableLiveData<Integer> userMoney;
    private MutableLiveData<String> roundDenied;
    //TODO: wrap LiveData objects in EVENT wrapper class for consistency


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

        userMoney = new MutableLiveData<>();
        userMoney.setValue(100);
        roundDenied = new MutableLiveData<>();
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

    public LiveData<Integer> getUserMoney(){
        return userMoney;
    }

    public LiveData<String> getRoundDenied(){
        return roundDenied;
    }

    public void userHit(){
        game.userHit();
    }

    public void userStay(){
        game.userStay();
    }

    public void newRound(){
        if(userMoney.getValue() >= MIN_BET){
            Log.i(LOG_TAG, "User has enough money, starting round...");
            userMoney.setValue(userMoney.getValue() - MIN_BET);
            game.startRound();
        }
        else{
            Log.i(LOG_TAG, "User doesn't have enough money, can't play.");
            roundDenied.setValue("User does not have enough money to play.");
        }

    }
}
