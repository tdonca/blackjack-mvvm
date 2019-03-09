package com.tudordonca.android.blackjackmvvm.gameplay;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

public class GameExecution {
    private final String LOG_TAG = "GameExecution";
    private StandardRuleSet ruleSet;
    private Deck deck;
    private Player dealer;
    private Player user;
    private int dealerHandValue;
    private int userHandValue;
    MutableLiveData<List<String>> dealerHandDisplay;
    MutableLiveData<List<String>> userHandDisplay;


    public GameExecution(){
        deck = new StandardDeck();
        dealer = new DealerPlayer();
        user = new UserPlayer();
        dealerHandValue = 0;
        userHandValue = 0;
        dealerHandDisplay = new MutableLiveData<>();
        dealerHandDisplay.setValue(dealer.getCardsDisplay());
        userHandDisplay = new MutableLiveData<>();
        userHandDisplay.setValue(user.getCardsDisplay());

    }

    public void startRound(){

        dealer.clearCards();
        user.clearCards();

        dealCards();

        evaluateCards();

//            waitForPlayer();
//
//            waitForDealer();
//
//            finishRound();


    }

    void dealCards(){
        Log.i(LOG_TAG, "Dealing cards to the user...");
        user.addCard(deck.drawCard());
        user.addCard(deck.drawCard());
        Log.i(LOG_TAG, "User has " + user.getCards().size() + " cards: " + user.getCards().toString());

        Log.i(LOG_TAG, "Dealing cards to the dealer...");
        dealer.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
        Log.i(LOG_TAG, "Dealer has " + dealer.getCards().size() + " cards: " + dealer.getCards().toString());

        dealerHandDisplay.setValue(dealer.getCardsDisplay());
        userHandDisplay.setValue(user.getCardsDisplay());
    }

    void evaluateCards(){
        dealerHandValue = ruleSet.getValue(dealer.getCards());
        Log.i(LOG_TAG, "Dealer's hand value: " + dealerHandValue);

        userHandValue = ruleSet.getValue(user.getCards());
        Log.i(LOG_TAG, "User's hand value: " + userHandValue);
    }

    void waitForPlayer(){

    }

    void waitForDealer(){

    }

    void finishRound(){

    }


    public LiveData<List<String>> getDealerHandDisplay(){
        return dealerHandDisplay;
    }

    public LiveData<List<String>> getUserHandDisplay(){
        return userHandDisplay;
    }

}
