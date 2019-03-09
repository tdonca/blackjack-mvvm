package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class GameRepository {
    private ArrayList<String> dealerHand;
    private ArrayList<String> playerHand;

    public GameRepository(){
        dealerHand = new ArrayList<>();
        dealerHand.add("7Diamonds");
        dealerHand.add("10Clubs");

        playerHand = new ArrayList<>();
        playerHand.add("2Hearts");
        playerHand.add("3Spades");
    }

    public LiveData<ArrayList<String>> getDealerHand(){
        final MutableLiveData<ArrayList<String>> dh = new MutableLiveData<>();
        dh.setValue(dealerHand);
        return dh;
    }

    public LiveData<ArrayList<String>> getPlayerHand(){
        final MutableLiveData<ArrayList<String>> ph = new MutableLiveData<>();
        ph.setValue(playerHand);
        return ph;
    }
}
