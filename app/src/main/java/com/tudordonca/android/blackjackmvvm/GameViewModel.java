package com.tudordonca.android.blackjackmvvm;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import android.app.Application;
import android.util.Log;

import com.tudordonca.android.blackjackmvvm.gamemechanics.Card;
import com.tudordonca.android.blackjackmvvm.gamemechanics.GameExecution;
import com.tudordonca.android.blackjackmvvm.gamemechanics.GameState;
import com.tudordonca.android.blackjackmvvm.userdata.User;
import com.tudordonca.android.blackjackmvvm.userdata.UserRepository;

import java.util.ArrayList;
import java.util.List;


public class GameViewModel extends AndroidViewModel {

    private String LOG_TAG = "GameViewModel";
    private GameExecution gameExecution;
    private UserRepository userRepository;
    private int userMoney = -1;
    private final int minBet = 25;
    private MutableLiveData<UIGameState> displayUI;

    public GameViewModel(Application application){
        super(application);
        gameExecution = new GameExecution();
        userRepository = new UserRepository(application);
        setupObservers();
        displayUI = new MutableLiveData<>();
        UIGameState state = new UIGameState(UIGameState.State.WELCOME);
        state.setUserMoney(userMoney);
        state.setMessage("Welcome to Blackjack. Press the button to begin a new round.");
        Log.i(LOG_TAG, "Updating UI State...");
        displayUI.setValue(state);
    }



    void inputUserHit(){
        GameState state = gameExecution.userHit();
        UIGameState stateUI = gameStateToUI(state);
        if(stateUI.getState() == UIGameState.State.USER_WIN){
            userMoney += 2*minBet;
            stateUI.setUserMoney(userMoney);
        }
        Log.i(LOG_TAG, "Updating UI State...");
        displayUI.setValue(stateUI);
    }

    void inputUserStay(){
        GameState state = gameExecution.userStay();
        UIGameState stateUI = gameStateToUI(state);
        if(stateUI.getState() == UIGameState.State.USER_WIN){
            userMoney += 2*minBet;
            stateUI.setUserMoney(userMoney);

        }
        else if(stateUI.getState() == UIGameState.State.TIE){
            userMoney += minBet;
            stateUI.setUserMoney(userMoney);
        }
        Log.i(LOG_TAG, "Updating UI State...");
        displayUI.setValue(stateUI);

    }

    void inputNewRound(){
        if(userMoney >= minBet){
            userMoney -= minBet;
            Log.i(LOG_TAG, "User has enough money, starting round...");
            GameState state = gameExecution.startRound();
            UIGameState stateUI = gameStateToUI(state);
            if(stateUI.getState() == UIGameState.State.USER_WIN){
                userMoney += 2*minBet;
                stateUI.setUserMoney(userMoney);
            }
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



    LiveData<UIGameState> getUIState(){
        return displayUI;
    }


    private void setupObservers(){
        // create an RX observer for the user data observable from Room
        userRepository.getUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(User user) {
                userMoney = user.getUserMoney();
                Log.i(LOG_TAG, "Received value from User Observable with money: " + user.getUserMoney());
            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onComplete() {

            }
        });
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
                    if(state.getWinner().equals("DEALER")) {
                        uiState = new UIGameState(UIGameState.State.DEALER_WIN);
                    }
                    else if(state.getWinner().equals("USER")) {
                        uiState = new UIGameState(UIGameState.State.USER_WIN);
                    }
                    else if(state.getWinner().equals("TIE")){
                        uiState = new UIGameState(UIGameState.State.TIE);
                    }
                    else {
                        return new UIGameState(UIGameState.State.INVALID);
                    }
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
