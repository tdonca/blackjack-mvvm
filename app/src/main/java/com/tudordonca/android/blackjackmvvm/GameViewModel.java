package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.tudordonca.android.blackjackmvvm.gameplay.GameExecution;

import java.util.List;


public class GameViewModel extends ViewModel {

    private GameExecution gameExecution;


    public GameViewModel(){
        gameExecution = new GameExecution();
    }


    public void init(){



        // create mutablelivedata

    }


    public void inputUserHit(){
        GameState state = gameExecution.userHit();
    }

    public void inputUserStay(){
        GameState state = gameExecution.userStay();
    }

    public void inputNewRound(){
        GameState state = gameExecution.startRound();
    }



    public LiveData<UIEvent<Object>> getRoundStartedUI() {return roundStartedUI;}
    public LiveData<UIEvent<List<String>>> getUserCardsUI () {return userCardsUI;}
    public LiveData<UIEvent<List<String>>> getDealerCardsUI () {return dealerCardsUI;}
    public LiveData<UIEvent<String>> getUserWinsUI() {return userWinsUI;}
    public LiveData<UIEvent<String>> getDealerWinsUI() {return dealerWinsUI;}
    public LiveData<UIEvent<Integer>> getUserMoneyUI() {return userMoneyUI;}
    public LiveData<UIEvent<Object>> getRoundFinishedUI() {return roundFinishedUI;}
    public LiveData<UIEvent<String>> getRoundDeniedUI() {return roundDeniedUI;}


}
