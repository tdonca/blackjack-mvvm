package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;


import com.tudordonca.android.blackjackmvvm.gameplay.Card;
import com.tudordonca.android.blackjackmvvm.gameplay.GameExecution;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

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
    private Observable<GameEvent<Object>> roundStarted;
    private Observable<GameEvent<List<Card>>> userCardsChanged;
    private Observable<GameEvent<List<Card>>> dealerCardsChanged;
    private Observable<GameEvent<Card>> userHit;
    private Observable<GameEvent<Object>> userStay;
    private Observable<GameEvent<Card>> dealerHit;
    private Observable<GameEvent<Object>> dealerStay;
    private Observable<GameEvent<Object>> roundFinished;
    private Observable<GameEvent<String>> userWins;
    private Observable<GameEvent<String>> dealerWins;

    // Events for UI updates
    private BehaviorSubject<UIEvent<Object>> roundStartedUI;
    private BehaviorSubject<UIEvent<List<String>>> userCardsUI;
    private BehaviorSubject<UIEvent<List<String>>> dealerCardsUI;
    private BehaviorSubject<UIEvent<String>> userWinsUI;
    private BehaviorSubject<UIEvent<String>> dealerWinsUI;
    private BehaviorSubject<UIEvent<Integer>> userMoneyUI;
    private BehaviorSubject<UIEvent<Object>> roundFinishedUI;
    private BehaviorSubject<UIEvent<String>> roundDeniedUI;



    public GameRepository(){
        game = new GameExecution();

        roundStartedUI = BehaviorSubject.create();
        userCardsUI = BehaviorSubject.create();
        dealerCardsUI = BehaviorSubject.create();
        userWinsUI = BehaviorSubject.create();
        dealerWinsUI = BehaviorSubject.create();
        userMoneyUI = BehaviorSubject.create();
        roundFinishedUI = BehaviorSubject.create();
        roundDeniedUI = BehaviorSubject.create();

        setupGameExecutionObservers();
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

        // TODO: with

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
            roundStartedUI.onNext(new UIEvent<Object>(new Object()));
            userMoneyUI.onNext(new UIEvent<Integer>(userMoneyAmount));
            game.startRound();
        }
        else{
            Log.i(LOG_TAG, "User doesn't have enough money, can't play.");
            roundDenied.setValue("User does not have enough money to play.");
            roundDeniedUI.onNext(new UIEvent<String>("NOT ENOUGH MONEY"));
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

    public Observable<UIEvent<Object>> getRoundStartedUI(){ return roundStartedUI; }
    public Observable<UIEvent<List<String>>> getUserCardsUI(){ return userCardsUI; }
    public Observable<UIEvent<List<String>>> getDealerCardsUI(){ return dealerCardsUI; }
    public Observable<UIEvent<String>> getUserWinsUI(){ return userWinsUI; }
    public Observable<UIEvent<String>> getDealerWinsUI(){ return dealerWinsUI; }
    public Observable<UIEvent<Integer>> getUserMoneyUI(){ return userMoneyUI; }
    public Observable<UIEvent<Object>> getRoundFinishedUI(){ return roundFinishedUI; }
    public Observable<UIEvent<String>> getRoundDeniedUI(){ return roundDeniedUI; }


    private void setupGameExecutionObservers(){

        //TODO: create RxJava observers for these
        game.getRoundStarted().subscribe(new Observer<GameEvent<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GameEvent<Object> objectGameEvent) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        game.getRoundFinished();

        game.getUserCardsChanged();

        game.getDealerCardsChanged();

        game.getUserHit();

        game.getUserStay();

        game.getDealerHit();

        game.getDealerStay();

        game.getRoundFinished();

        game.getUserWins();

        game.getDealerWins();

    }
}
