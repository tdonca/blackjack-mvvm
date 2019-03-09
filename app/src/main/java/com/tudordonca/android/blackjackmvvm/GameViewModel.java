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

    public GameViewModel(){
        gameRepository = new GameRepository();
        gameRepository.init();
    }


    public void init(){
        if(dealerHand == null){
            dealerHand = gameRepository.getDealerHand();
        }
        if(playerHand == null){
            playerHand = gameRepository.getPlayerHand();
        }
        if(winner == null){
            winner = gameRepository.getWinner();
        }
    }


    public LiveData<List<String>> getDealerHand(){
        return dealerHand;
    }

    public LiveData<List<String>> getPlayerHand(){
        return playerHand;
    }

    public LiveData<String> getWinner(){
        return winner;
    }

    public void onUserHit(){
        gameRepository.userHit();
    }

    public void onUserStay(){
        gameRepository.userStay();
    }

    public void onNewRound(){
        gameRepository.newRound();
    }
}
