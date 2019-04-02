package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.tudordonca.android.blackjackmvvm.gameplay.GameExecution;
import com.tudordonca.android.blackjackmvvm.gameplay.GameState;


public class GameViewModel extends ViewModel {

    private String LOG_TAG = "GameViewModel";
    private GameExecution gameExecution;
    int userMoney;
    int minBet;
    private MutableLiveData<UIGameState> displayUI;


    public GameViewModel(){
        gameExecution = new GameExecution();
        minBet = 25;
    }


    public void init(){
        userMoney = getUserMoney();
        displayUI = new MutableLiveData<>();
    }


    public void inputUserHit(){
        GameState state = gameExecution.userHit();
        UIGameState stateUI = new UIGameState(state, userMoney);
        displayUI.setValue(stateUI);
    }

    public void inputUserStay(){
        GameState state = gameExecution.userStay();
        UIGameState stateUI = new UIGameState(state, userMoney);
        displayUI.setValue(stateUI);

    }

    public void inputNewRound(){
        if(userMoney >= minBet){
            userMoney -= minBet;
            Log.i(LOG_TAG, "User has enough money, starting round...");
            GameState state = gameExecution.startRound();
            UIGameState stateUI = new UIGameState(state, userMoney);
            displayUI.setValue(stateUI);


        }
        else{
            //TODO: this doesn't feel like a good way to do this
            Log.i(LOG_TAG, "User doesn't have enough money, can't play.");
            UIGameState stateUI = new UIGameState(null, userMoney);
            stateUI.setMessage("Not enough money to play. Come back tomorrow.");
            displayUI.setValue(stateUI);

        }
    }


    public int getUserMoney() {
        return 100;
    }
}
