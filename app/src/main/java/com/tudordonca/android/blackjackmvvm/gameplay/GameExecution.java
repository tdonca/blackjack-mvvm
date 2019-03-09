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
    private boolean userTurn;
    private MutableLiveData<List<String>> dealerHandDisplay;
    private MutableLiveData<List<String>> userHandDisplay;
    private MutableLiveData<String> winnerDisplay;



    public GameExecution(){
        deck = new StandardDeck();
        dealer = new DealerPlayer();
        user = new UserPlayer();
        dealerHandValue = 0;
        userHandValue = 0;
        userTurn = false;
        dealerHandDisplay = new MutableLiveData<>();
        userHandDisplay = new MutableLiveData<>();
        winnerDisplay = new MutableLiveData<>();
        ruleSet = new StandardRuleSet();


    }

    public void startRound(){
        dealer.clearCards();
        dealerHandValue = 0;
        user.clearCards();
        userHandValue = 0;
        dealCards();

        if(userHandValue == 21){
            Log.d(LOG_TAG, "USER BLACKJACK!");
            userTurn = false;
            finishRound();
        }
        else{
            userTurn = true;
        }
    }

    private void dealCards(){
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
        evaluateCards();
    }

    private void evaluateCards(){
        dealerHandValue = ruleSet.getValue(dealer.getCards());
        Log.i(LOG_TAG, "Dealer's hand value: " + dealerHandValue);

        userHandValue = ruleSet.getValue(user.getCards());
        Log.i(LOG_TAG, "User's hand value: " + userHandValue);
    }


    private void dealerTurn(){
        while(dealerHandValue < 17){
            Log.i(LOG_TAG, "Dealer hits, getting a new card...");
            dealer.addCard(deck.drawCard());
            dealerHandDisplay.setValue(dealer.getCardsDisplay());
            Log.i(LOG_TAG, "Dealer has " + dealer.getCards().size() + " cards: " + dealer.getCards().toString());
            evaluateCards();
        }

        if(dealerHandValue > 21){
            Log.i(LOG_TAG, "DEALER BUST!");
        }

        finishRound();
    }

    private void finishRound(){
        if(userHandValue > 21){
            Log.i(LOG_TAG, "DEALER WINS!");
            winnerDisplay.setValue("DEALER");
        }
        else if(dealerHandValue > 21){
            Log.i(LOG_TAG, "USER WINS!");
            winnerDisplay.setValue("USER");
        }
        else if(dealerHandValue >= userHandValue){
            Log.i(LOG_TAG, "DEALER WINS!");
            winnerDisplay.setValue("DEALER");
        }
        else{
            Log.i(LOG_TAG, "USER WINS!");
            winnerDisplay.setValue("USER");
        }
    }


    public LiveData<List<String>> getDealerHandDisplay(){
        return dealerHandDisplay;
    }

    public LiveData<List<String>> getUserHandDisplay(){
        return userHandDisplay;
    }

    public LiveData<String> getWinnerDisplay() {
        return winnerDisplay;
    }

    public void userHit(){
        if(userTurn){
            Log.i(LOG_TAG, "User hits, getting a new card...");
            user.addCard(deck.drawCard());
            userHandDisplay.setValue(user.getCardsDisplay());
            Log.i(LOG_TAG, "User has " + user.getCards().size() + " cards: " + user.getCards().toString());
            evaluateCards();

            if(userHandValue > 21){
                Log.i(LOG_TAG, "USER BUST!");
                userTurn = false;
                finishRound();
            }
        }
        else{
            Log.e(LOG_TAG, "Not the user's turn right now!");
        }

    }

    public void userStay(){
        if(userTurn){
            Log.i(LOG_TAG, "User stays, moving to dealer's turn...");
            userTurn = false;
            dealerTurn();
        }
        else{
            Log.e(LOG_TAG, "Not the user's turn right now!");
        }


    }
}
