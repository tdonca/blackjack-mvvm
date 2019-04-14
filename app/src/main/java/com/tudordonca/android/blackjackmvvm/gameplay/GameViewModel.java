package com.tudordonca.android.blackjackmvvm.gameplay;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.app.Application;
import android.util.Log;

import com.tudordonca.android.blackjackmvvm.gameplay.gamemechanics.Card;
import com.tudordonca.android.blackjackmvvm.gameplay.gamemechanics.GameExecution;
import com.tudordonca.android.blackjackmvvm.gameplay.gamemechanics.GameState;
import com.tudordonca.android.blackjackmvvm.gameplay.userdata.User;
import com.tudordonca.android.blackjackmvvm.gameplay.userdata.UserRepository;

import java.util.ArrayList;
import java.util.List;


public class GameViewModel extends AndroidViewModel {

    private String LOG_TAG = "GameViewModel";
    private final String USERID = "user1";
    private final int DEFAULT_MONEY = 100;
    private GameExecution gameExecution;
    private UserRepository userRepository;
    private User userData;
    private final int minBet = 25;
    private MutableLiveData<UIGameState> displayUI;
    private MutableLiveData<Integer> userMoneyUI;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public GameViewModel(Application application){
        super(application);
        gameExecution = new GameExecution();
        userRepository = new UserRepository(application);
        displayUI = new MutableLiveData<>();
        userMoneyUI = new MutableLiveData<>();
        setupObservers();
    }

    void displayWelcome(){
        UIGameState state = new UIGameState(UIGameState.State.WELCOME);
        state.setUserMoney(userData.getUserMoney());
        state.setMessage("Welcome to Blackjack.");
        Log.i(LOG_TAG, "Updating UI State...");
        displayUI.setValue(state);
    }

    void inputUserHit(){
        GameState state = gameExecution.userHit();
        UIGameState stateUI = gameStateToUI(state);
        if(stateUI.getState() == UIGameState.State.USER_WIN){
            userData.addUserMoney(2*minBet);
            stateUI.setUserMoney(userData.getUserMoney());
            userRepository.updateMoney(userData.getUserMoney());
        }
        Log.i(LOG_TAG, "Updating UI State...");
        displayUI.setValue(stateUI);
    }

    void inputUserStay(){
        GameState state = gameExecution.userStay();
        UIGameState stateUI = gameStateToUI(state);
        if(stateUI.getState() == UIGameState.State.USER_WIN){
            userData.addUserMoney(2*minBet);
            stateUI.setUserMoney(userData.getUserMoney());
            userRepository.updateMoney(userData.getUserMoney());

        }
        else if(stateUI.getState() == UIGameState.State.TIE){
            userData.addUserMoney(minBet);
            stateUI.setUserMoney(userData.getUserMoney());
            userRepository.updateMoney(userData.getUserMoney());
        }
        Log.i(LOG_TAG, "Updating UI State...");
        displayUI.setValue(stateUI);

    }

    void inputNewRound(){
        if(userData.getUserMoney() >= minBet){
            userData.addUserMoney(-minBet);
            userRepository.updateMoney(userData.getUserMoney());
            Log.i(LOG_TAG, "User has enough money, starting round...");
            GameState state = gameExecution.startRound();
            UIGameState stateUI = gameStateToUI(state);
            // immediate blackjack
            if(stateUI.getState() == UIGameState.State.USER_WIN){
                userData.addUserMoney(2*minBet);
                userRepository.updateMoney(userData.getUserMoney());
            }
            Log.i(LOG_TAG, "Updating UI State...");
            displayUI.setValue(stateUI);


        }
        else{
            Log.i(LOG_TAG, "User doesn't have enough money, can't play.");
            UIGameState stateUI = new UIGameState(UIGameState.State.DENIED);
            stateUI.setUserMoney(userData.getUserMoney());
            stateUI.setMessage("Not enough money to play. Come back tomorrow.");
            Log.i(LOG_TAG, "Updating UI State...");
            displayUI.setValue(stateUI);

        }
    }



    LiveData<UIGameState> getUIState(){
        return displayUI;
    }
    LiveData<Integer> getUserMoney() {return userMoneyUI;}

    private void setupObservers(){
        // create an RX observer for the user data observable from Room
        disposable.add(userRepository.getUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if(user != null){
                        userData = user;
                        Log.i(LOG_TAG, "Got Result from Single");
                        displayWelcome();
                    }
                    else{
                        Log.e(LOG_TAG, "Returned user object is null!");
                    }

                }, throwable -> {
                    Log.e(LOG_TAG, "Failed to retrieve user data from database!");
                    throwable.printStackTrace();

                    Log.i(LOG_TAG, "Creating new default user.");
                    userData = new User(USERID);
                    userData.setUserMoney(DEFAULT_MONEY);
                    userRepository.insertUser(userData);
                    displayWelcome();
                }));
        Log.i(LOG_TAG, "Subscribed to getUser() without error");
    }

    public void disposeObservers(){
        disposable.dispose();
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
            uiState.setUserMoney(userData.getUserMoney());
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
