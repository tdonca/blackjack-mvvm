package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;


public class GameViewModel extends ViewModel {

    private GameRepository gameRepository;
    private LiveData<List<String>> dealerHand;
    private LiveData<List<String>> playerHand;
    private LiveData<String> winner;
    private LiveData<Integer> userMoney;
    private LiveData<String> roundDenied;


    private LiveData<UIEvent<Object>> roundStartedUI;
    private LiveData<UIEvent<List<String>>> userCardsUI;
    private LiveData<UIEvent<List<String>>> dealerCardsUI;
    private LiveData<UIEvent<String>> userWinsUI;
    private LiveData<UIEvent<String>> dealerWinsUI;
    private LiveData<UIEvent<Integer>> userMoneyUI;
    private LiveData<UIEvent<Object>> roundFinishedUI;
    private LiveData<UIEvent<String>> roundDeniedUI;

    public GameViewModel(){
        gameRepository = new GameRepository();
        gameRepository.init();
    }


    public void init(){
        //TODO: replace
        if(dealerHand == null){
            dealerHand = gameRepository.getDealerHand();
        }
        if(playerHand == null){
            playerHand = gameRepository.getPlayerHand();
        }
        if(winner == null){
            winner = gameRepository.getWinner();
        }
        if(userMoney == null){
            userMoney = gameRepository.getUserMoney();
        }
        if(roundDenied == null){
            roundDenied = gameRepository.getRoundDenied();
        }

        //TODO: with
        if(roundStartedUI == null){
            roundStartedUI = gameRepository.getRoundStartedUI();
        }
        if(dealerCardsUI == null){
            dealerCardsUI = gameRepository.getDealerCardsUI();
        }
        if(userCardsUI == null){
            userCardsUI = gameRepository.getUserCardsUI();
        }
        if(userWinsUI == null){
            userWinsUI = gameRepository.getUserWinsUI();
        }
        if(dealerWinsUI == null){
            dealerWinsUI = gameRepository.getDealerWinsUI();
        }
        if(userMoneyUI == null){
            userMoneyUI = gameRepository.getUserMoneyUI();
        }
        if(roundFinishedUI == null){
            roundFinishedUI = gameRepository.getRoundFinishedUI();
        }
        if(roundDeniedUI == null){
            roundDeniedUI = gameRepository.getRoundDeniedUI();
        }



    }


    public void inputUserHit(){
        gameRepository.inputUserHit();
    }

    public void inputUserStay(){
        gameRepository.inputUserStay();
    }

    public void inputNewRound(){
        gameRepository.inputNewRound();
    }


    //TODO: replace this
    public LiveData<List<String>> getDealerHand(){
        return dealerHand;
    }

    public LiveData<List<String>> getPlayerHand(){
        return playerHand;
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

    //TODO: with this
    public LiveData<UIEvent<Object>> getRoundStartedUI() {return roundStartedUI;}
    public LiveData<UIEvent<List<String>>> getUserCardsUI () {return userCardsUI;}
    public LiveData<UIEvent<List<String>>> getDealerCardsUI () {return dealerCardsUI;}
    public LiveData<UIEvent<String>> getUserWinsUI() {return userWinsUI;}
    public LiveData<UIEvent<String>> getDealerWinsUI() {return dealerWinsUI;}
    public LiveData<UIEvent<Integer>> getUserMoneyUI() {return userMoneyUI;}
    public LiveData<UIEvent<Object>> getRoundFinishedUI() {return roundFinishedUI;}
    public LiveData<UIEvent<String>> getRoundDeniedUI() {return roundDeniedUI;}


}
