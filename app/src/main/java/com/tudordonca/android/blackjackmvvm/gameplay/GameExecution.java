package com.tudordonca.android.blackjackmvvm.gameplay;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.tudordonca.android.blackjackmvvm.GameEvent;

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


    private MutableLiveData<GameEvent<Object>> roundStarted;
    private MutableLiveData<GameEvent<List<Card>>> userCardsChanged;
    private MutableLiveData<GameEvent<List<Card>>> dealerCardsChanged;
    private MutableLiveData<GameEvent<Card>> userHit;
    private MutableLiveData<GameEvent<Object>> userStay;
    private MutableLiveData<GameEvent<Card>> dealerHit;
    private MutableLiveData<GameEvent<Object>> dealerStay;
    private MutableLiveData<GameEvent<Object>> roundFinished;
    private MutableLiveData<GameEvent<String>> userWins;
    private MutableLiveData<GameEvent<String>> dealerWins;




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

        roundStarted = new MutableLiveData<>();
        userCardsChanged = new MutableLiveData<>();
        dealerCardsChanged = new MutableLiveData<>();
        userHit = new MutableLiveData<>();
        userStay = new MutableLiveData<>();
        dealerHit = new MutableLiveData<>();
        dealerStay = new MutableLiveData<>();
        roundFinished = new MutableLiveData<>();
        userWins = new MutableLiveData<>();
        dealerWins = new MutableLiveData<>();

    }

    public void startRound(){
        roundStarted.setValue(new GameEvent<Object>(new Object()));
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

        //Todo: replace
        dealerHandDisplay.setValue(dealer.getCardsDisplay());
        userHandDisplay.setValue(user.getCardsDisplay());
        //Todo: with
        userCardsChanged.setValue(new GameEvent<>(user.getCards()));
        dealerCardsChanged.setValue(new GameEvent<>(dealer.getCards()));
        evaluateCards();
    }

    private void evaluateCards(){
        dealerHandValue = ruleSet.getValue(dealer.getCards());
        Log.i(LOG_TAG, "Dealer's hand value: " + dealerHandValue);

        userHandValue = ruleSet.getValue(user.getCards());
        Log.i(LOG_TAG, "User's hand value: " + userHandValue);
    }


    private void finishRound(){
        if(userHandValue > 21){
            Log.i(LOG_TAG, "DEALER WINS!");
            winnerDisplay.setValue("DEALER");
            //TODO: replace with
            dealerWins.setValue(new GameEvent<String>("USER BUST"));

        }
        else if(dealerHandValue > 21){
            Log.i(LOG_TAG, "USER WINS!");
            winnerDisplay.setValue("USER");
            //TODO: replace with
            userWins.setValue(new GameEvent<String>("DEALER BUST"));
        }
        else if(dealerHandValue >= userHandValue){
            Log.i(LOG_TAG, "DEALER WINS!");
            winnerDisplay.setValue("DEALER");
            //TODO: replace with
            dealerWins.setValue(new GameEvent<String>("GREATER VALUE"));
        }
        else{
            Log.i(LOG_TAG, "USER WINS!");
            winnerDisplay.setValue("USER");
            //TODO: replace with
            userWins.setValue(new GameEvent<String>("GREATER VALUE"));
        }
    }


    public void userHit(){
        if(userTurn){
            Log.i(LOG_TAG, "User hits, getting a new card...");
            Card card = deck.drawCard();
            userHit.setValue(new GameEvent<Card>(card));
            user.addCard(card);
            userHandDisplay.setValue(user.getCardsDisplay());
            //TODO: replace with
            userCardsChanged.setValue(new GameEvent<List<Card>>(user.getCards()));
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
            userStay.setValue(new GameEvent<Object>(new Object()));
            userTurn = false;
            dealerTurn();
        }
        else{
            Log.e(LOG_TAG, "Not the user's turn right now!");
        }


    }


    private void dealerTurn(){
        while(dealerHandValue < 17){
            Log.i(LOG_TAG, "Dealer hits, getting a new card...");
            Card card = deck.drawCard();
            dealerHit.setValue(new GameEvent<Card>(card));
            dealer.addCard(card);
            dealerHandDisplay.setValue(dealer.getCardsDisplay());
            //TODO: replace with
            dealerCardsChanged.setValue(new GameEvent<List<Card>>(dealer.getCards()));
            Log.i(LOG_TAG, "Dealer has " + dealer.getCards().size() + " cards: " + dealer.getCards().toString());
            evaluateCards();
        }

        if(dealerHandValue > 21){
            Log.i(LOG_TAG, "DEALER BUST!");
        }

        finishRound();
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

    public LiveData<GameEvent<Object>> getRoundStarted(){
        return roundStarted;
    }

    public LiveData<GameEvent<List<Card>>> getUserCardsChanged(){
        return userCardsChanged;
    }

    public LiveData<GameEvent<List<Card>>> getDealerCardsChanged(){
        return dealerCardsChanged;
    }

    public LiveData<GameEvent<Card>> getUserHit(){
        return userHit;
    }


    public LiveData<GameEvent<Object>> getUserStay(){
        return userStay;
    }

    public LiveData<GameEvent<Card>> getDealerHit(){
        return dealerHit;
    }

    public LiveData<GameEvent<Object>> getDealerStay(){
        return dealerStay;
    }

    public LiveData<GameEvent<Object>> getRoundFinished(){
        return roundFinished;
    }

    public LiveData<GameEvent<String>> getUserWins(){
        return userWins;
    }

    public LiveData<GameEvent<String>> getDealerWins(){
        return dealerWins;
    }
}
