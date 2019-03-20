package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;


import com.tudordonca.android.blackjackmvvm.gameplay.Card;
import com.tudordonca.android.blackjackmvvm.gameplay.GameExecution;

import java.util.List;

public class GameRepository {
    private final String LOG_TAG = "GameRepository";
    private final int MIN_BET = 25;
    private Integer userMoneyAmount = 100;
    private GameExecution game;
    private LiveData<List<String>> dealerHand;
    private LiveData<List<String>> userHand;
    private LiveData<String> winner;
    private MutableLiveData<Integer> userMoney;
    private MutableLiveData<String> roundDenied;
    //TODO: wrap LiveData objects in EVENT wrapper class for consistency

    // Events from Game Execution
    private LiveData<GameEvent<Object>> roundStarted;
    private LiveData<GameEvent<List<Card>>> userCardsChanged;
    private LiveData<GameEvent<List<Card>>> dealerCardsChanged;
    private LiveData<GameEvent<Card>> userHit;
    private LiveData<GameEvent<Object>> userStay;
    private LiveData<GameEvent<Card>> dealerHit;
    private LiveData<GameEvent<Object>> dealerStay;
    private LiveData<GameEvent<Object>> roundFinished;
    private LiveData<GameEvent<String>> userWins;
    private LiveData<GameEvent<String>> dealerWins;

    // Events for UI updates
    private MutableLiveData<UIEvent<Object>> roundStartedUI;
    private MutableLiveData<UIEvent<List<String>>> userCardsUI;
    private MutableLiveData<UIEvent<List<String>>> dealerCardsUI;
    private MutableLiveData<UIEvent<String>> userWinsUI;
    private MutableLiveData<UIEvent<String>> dealerWinsUI;
    private MutableLiveData<UIEvent<Integer>> userMoneyUI;
    private MutableLiveData<UIEvent<Object>> roundFinishedUI;
    private MutableLiveData<UIEvent<String>> roundDeniedUI;


    public GameRepository(){
        game = new GameExecution();

        roundStartedUI = new MutableLiveData<>();
        userCardsUI = new MutableLiveData<>();
        dealerCardsUI = new MutableLiveData<>();
        userWinsUI = new MutableLiveData<>();
        dealerWinsUI = new MutableLiveData<>();
        userMoneyUI = new MutableLiveData<>();
        roundFinishedUI = new MutableLiveData<>();
        roundDeniedUI = new MutableLiveData<>();
    }

    public void init(){
        userMoney = new MutableLiveData<>();
        userMoneyAmount = 100;
        userMoney.setValue(userMoneyAmount);
        roundDenied = new MutableLiveData<>();

        //TODO: replace
        if(dealerHand == null){
            dealerHand = game.getDealerHandDisplay();
        }
        if(userHand == null){
            userHand = game.getUserHandDisplay();
        }
        if(winner == null){
            winner = game.getWinnerDisplay();
        }

        //TODO: with
        if (roundStarted == null) {
            roundStarted = game.getRoundStarted();
        }
        if (userCardsChanged == null) {
            userCardsChanged = game.getUserCardsChanged();
        }
        if (dealerCardsChanged == null) {
            dealerCardsChanged = game.getDealerCardsChanged();
        }
        if (userHit == null) {
            userHit = game.getUserHit();
        }
        if (userStay == null) {
            userStay = game.getUserStay();
        }
        if (dealerHit == null) {
            dealerHit = game.getDealerHit();
        }
        if (dealerStay == null) {
            dealerStay = game.getDealerStay();
        }
        if (roundFinished == null) {
            roundFinished = game.getRoundFinished();
        }
        if (userWins == null) {
            userWins = game.getUserWins();
        }
        if (dealerWins == null) {
            dealerWins = game.getDealerWins();
        }



    }



    public void inputUserHit(){
        game.userHit();
    }

    public void inputUserStay(){
        game.userStay();
    }

    public void inputNewRound(){
        if(userMoneyAmount >= MIN_BET){
            Log.i(LOG_TAG, "User has enough money, starting round...");
            userMoneyAmount -= MIN_BET;
            userMoney.setValue(userMoneyAmount);
            roundStartedUI.setValue(new UIEvent<Object>(new Object()));
            userMoneyUI.setValue(new UIEvent<Integer>(userMoneyAmount));
            game.startRound();
        }
        else{
            Log.i(LOG_TAG, "User doesn't have enough money, can't play.");
            roundDenied.setValue("User does not have enough money to play.");
            roundDeniedUI.setValue(new UIEvent<String>("NOT ENOUGH MONEY"));
        }

    }



    // TODO: Replace

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


    //TODO: with

    public LiveData<UIEvent<Object>> getRoundStartedUI(){ return roundStartedUI; }
    public LiveData<UIEvent<List<String>>> getUserCardsUI(){ return userCardsUI; }
    public LiveData<UIEvent<List<String>>> getDealerCardsUI(){ return dealerCardsUI; }
    public LiveData<UIEvent<String>> getUserWinsUI(){ return userWinsUI; }
    public LiveData<UIEvent<String>> getDealerWinsUI(){ return dealerWinsUI; }
    public LiveData<UIEvent<Integer>> getUserMoneyUI(){ return userMoneyUI; }
    public LiveData<UIEvent<Object>> getRoundFinishedUI(){ return roundFinishedUI; }
    public LiveData<UIEvent<String>> getRoundDeniedUI(){ return roundDeniedUI; }


}
