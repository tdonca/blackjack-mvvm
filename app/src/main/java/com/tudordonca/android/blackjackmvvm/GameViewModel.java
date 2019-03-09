package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

public class GameViewModel extends ViewModel {

    private GameRepository gameRepository;
    private LiveData<ArrayList<String>> dealerHand;
    private LiveData<ArrayList<String>> playerHand;

    public GameViewModel(){
        this.gameRepository = new GameRepository();
    }


    public void init(){
        if(dealerHand == null){
            dealerHand = gameRepository.getDealerHand();
        }
        if(playerHand == null){
            playerHand = gameRepository.getPlayerHand();
        }
    }


    public LiveData<ArrayList<String>> getDealerHand(){
        return dealerHand;
    }

    public LiveData<ArrayList<String>> getPlayerHand(){
        return playerHand;
    }
}
