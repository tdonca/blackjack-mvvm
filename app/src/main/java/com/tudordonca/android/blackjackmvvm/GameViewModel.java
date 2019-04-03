package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.tudordonca.android.blackjackmvvm.gameplay.Card;
import com.tudordonca.android.blackjackmvvm.gameplay.GameExecution;
import com.tudordonca.android.blackjackmvvm.gameplay.GameState;

import java.util.ArrayList;
import java.util.List;


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
        UIGameState state = new UIGameState(UIGameState.State.WELCOME);
        state.setUserMoney(userMoney);
        state.setMessage("Welcome to Blackjack. Press the button to begin a new round.");
        Log.i(LOG_TAG, "Updating UI State...");
        displayUI.setValue(state);
    }


    public void inputUserHit(){
        GameState state = gameExecution.userHit();
        UIGameState stateUI = gameStateToUI(state);
        Log.i(LOG_TAG, "Updating UI State...");
        displayUI.setValue(stateUI);
    }

    public void inputUserStay(){
        GameState state = gameExecution.userStay();
        UIGameState stateUI = gameStateToUI(state);
        Log.i(LOG_TAG, "Updating UI State...");
        displayUI.setValue(stateUI);

    }

    public void inputNewRound(){
        if(userMoney >= minBet){
            userMoney -= minBet;
            Log.i(LOG_TAG, "User has enough money, starting round...");
            GameState state = gameExecution.startRound();
            UIGameState stateUI = gameStateToUI(state);
            Log.i(LOG_TAG, "Updating UI State...");
            displayUI.setValue(stateUI);


        }
        else{
            Log.i(LOG_TAG, "User doesn't have enough money, can't play.");
            UIGameState stateUI = new UIGameState(UIGameState.State.DENIED);
            stateUI.setMessage("Not enough money to play. Come back tomorrow.");
            Log.i(LOG_TAG, "Updating UI State...");
            displayUI.setValue(stateUI);

        }
    }


    public int getUserMoney() {
        return 100;
    }


    LiveData<UIGameState> getUIState(){
        return displayUI;
    }

    private UIGameState gameStateToUI(GameState state){
        if(state == null){
            return new UIGameState(UIGameState.State.INVALID);
        }
        else if(state.getState() == GameState.GameProgressState.INVALID){
            return new UIGameState(UIGameState.State.INVALID);
        }
        else{
            UIGameState uiState;
            // UI state
            switch(state.getState()){
                case ROUND_FINISHED:
                    if(state.getWinner().equals("DEALER"))
                        uiState = new UIGameState(UIGameState.State.DEALER_WIN);
                    else if(state.getWinner().equals("USER"))
                        uiState = new UIGameState(UIGameState.State.USER_WIN);
                    else
                        return new UIGameState(UIGameState.State.INVALID);
                    break;
                default:
                    uiState = new UIGameState(UIGameState.State.IN_PROGRESS);
                    break;
            }
            uiState.setMessage(state.getMessage());
            uiState.setUserMoney(userMoney);
            // Convert dealer cards to display format
            List<String> cards = new ArrayList<>();
            for(Card c: state.getDealerCards()){
                cards.add(c.getName());
            }
            uiState.setDealerCards(cards);
            // Convert user cards to display format
            cards.clear();
            for(Card c: state.getUserCards()){
                cards.add(c.getName());
            }
            uiState.setUserCards(cards);

            return uiState;
        }
    }
}
