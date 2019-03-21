package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;


import com.tudordonca.android.blackjackmvvm.gameplay.Card;
import com.tudordonca.android.blackjackmvvm.gameplay.GameExecution;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

        setupGameExecutionObservers();
    }

    public void init(){
        userMoney = new MutableLiveData<>();
        userMoneyAmount = 100;
        userMoneyUI.setValue(new UIEvent<>(userMoneyAmount));
        userMoney.setValue(userMoneyAmount);
        roundDenied = new MutableLiveData<>();
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
            userMoneyUI.setValue(new UIEvent<>(userMoneyAmount));
            game.startRound();
        }
        else{
            Log.i(LOG_TAG, "User doesn't have enough money, can't play.");
            roundDenied.setValue("User does not have enough money to play.");
            roundDeniedUI.setValue(new UIEvent<>("NOT ENOUGH MONEY"));
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


    private void setupGameExecutionObservers(){

        //TODO: create RxJava observers for these
        game.getRoundStarted().subscribe(new Observer<GameEvent<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GameEvent<Object> objectGameEvent) {
                roundStartedUI.setValue(new UIEvent<>(new Object()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        game.getRoundFinished().subscribe(new Observer<GameEvent<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GameEvent<Object> objectGameEvent) {
                roundFinishedUI.setValue(new UIEvent<>(new Object()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        game.getUserCardsChanged().subscribe(new Observer<GameEvent<List<Card>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GameEvent<List<Card>> listGameEvent) {
                List<Card> cards = listGameEvent.getContentIfNotHandled();
                List<String> cardsDisplay = new ArrayList<>();
                for(Card c : cards){
                    cardsDisplay.add(c.getName());
                }
                userCardsUI.setValue(new UIEvent<>(cardsDisplay));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        game.getDealerCardsChanged().subscribe(new Observer<GameEvent<List<Card>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GameEvent<List<Card>> listGameEvent) {
                List<Card> cards = listGameEvent.getContentIfNotHandled();
                List<String> cardsDisplay = new ArrayList<>();
                for(Card c : cards){
                    cardsDisplay.add(c.getName());
                }
                dealerCardsUI.setValue(new UIEvent<>(cardsDisplay));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        game.getUserHit();

        game.getUserStay();

        game.getDealerHit();

        game.getDealerStay();

        game.getUserWins().subscribe(new Observer<GameEvent<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GameEvent<String> stringGameEvent) {
                userWinsUI.setValue(new UIEvent<>(stringGameEvent.getContentIfNotHandled()));
                userMoneyAmount += 2*MIN_BET;
                userMoneyUI.setValue(new UIEvent<>(userMoneyAmount));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        game.getDealerWins().subscribe(new Observer<GameEvent<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GameEvent<String> stringGameEvent) {
                dealerWinsUI.setValue(new UIEvent<>(stringGameEvent.getContentIfNotHandled()));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
