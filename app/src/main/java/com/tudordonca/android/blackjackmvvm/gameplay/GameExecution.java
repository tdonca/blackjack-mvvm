package com.tudordonca.android.blackjackmvvm.gameplay;

import android.util.Log;

import com.tudordonca.android.blackjackmvvm.GameEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class GameExecution {
    private final String LOG_TAG = "GameExecution";
    private StandardRuleSet ruleSet;
    private Deck deck;
    private Player dealer;
    private Player user;
    private int dealerHandValue;
    private int userHandValue;
    private boolean userTurn;


    private BehaviorSubject<GameEvent<Object>> roundStarted;
    private BehaviorSubject<GameEvent<List<Card>>> userCardsChanged;
    private BehaviorSubject<GameEvent<List<Card>>> dealerCardsChanged;
    private BehaviorSubject<GameEvent<Card>> userHit;
    private BehaviorSubject<GameEvent<Object>> userStay;
    private BehaviorSubject<GameEvent<Card>> dealerHit;
    private BehaviorSubject<GameEvent<Object>> dealerStay;
    private BehaviorSubject<GameEvent<Object>> roundFinished;
    private BehaviorSubject<GameEvent<String>> userWins;
    private BehaviorSubject<GameEvent<String>> dealerWins;




    public GameExecution(){
        deck = new StandardDeck();
        dealer = new DealerPlayer();
        user = new UserPlayer();
        dealerHandValue = 0;
        userHandValue = 0;
        userTurn = false;
        ruleSet = new StandardRuleSet();

        roundStarted = BehaviorSubject.create();
        userCardsChanged = BehaviorSubject.create();
        dealerCardsChanged = BehaviorSubject.create();
        userHit = BehaviorSubject.create();
        userStay = BehaviorSubject.create();
        dealerHit = BehaviorSubject.create();
        dealerStay = BehaviorSubject.create();
        roundFinished = BehaviorSubject.create();
        userWins = BehaviorSubject.create();
        dealerWins = BehaviorSubject.create();

    }

    public void startRound(){
        roundStarted.onNext(new GameEvent<>(new Object()));
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

        userCardsChanged.onNext(new GameEvent<>(user.getCards()));
        dealerCardsChanged.onNext(new GameEvent<>(dealer.getCards()));
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
            dealerWins.onNext(new GameEvent<>("USER BUST"));

        }
        else if(dealerHandValue > 21){
            Log.i(LOG_TAG, "USER WINS!");
            userWins.onNext(new GameEvent<>("DEALER BUST"));
        }
        else if(dealerHandValue >= userHandValue){
            Log.i(LOG_TAG, "DEALER WINS!");
            dealerWins.onNext(new GameEvent<>("GREATER VALUE"));
        }
        else{
            Log.i(LOG_TAG, "USER WINS!");
            userWins.onNext(new GameEvent<>("GREATER VALUE"));
        }

        roundFinished.onNext(new GameEvent<>(new Object()));
    }


    public void userHit(){
        if(userTurn){
            Log.i(LOG_TAG, "User hits, getting a new card...");
            Card card = deck.drawCard();
            userHit.onNext(new GameEvent<>(card));
            user.addCard(card);
            userCardsChanged.onNext(new GameEvent<>(user.getCards()));
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
            userStay.onNext(new GameEvent<>(new Object()));
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
            dealerHit.onNext(new GameEvent<>(card));
            dealer.addCard(card);
            dealerCardsChanged.onNext(new GameEvent<>(dealer.getCards()));
            Log.i(LOG_TAG, "Dealer has " + dealer.getCards().size() + " cards: " + dealer.getCards().toString());
            evaluateCards();
        }

        if(dealerHandValue > 21){
            Log.i(LOG_TAG, "DEALER BUST!");
        }

        finishRound();
    }



    public Observable<GameEvent<Object>> getRoundStarted(){
        return roundStarted;
    }

    public Observable<GameEvent<List<Card>>> getUserCardsChanged(){
        return userCardsChanged;
    }

    public Observable<GameEvent<List<Card>>> getDealerCardsChanged(){
        return dealerCardsChanged;
    }

    public Observable<GameEvent<Card>> getUserHit(){
        return userHit;
    }


    public Observable<GameEvent<Object>> getUserStay(){
        return userStay;
    }

    public Observable<GameEvent<Card>> getDealerHit(){
        return dealerHit;
    }

    public Observable<GameEvent<Object>> getDealerStay(){
        return dealerStay;
    }

    public Observable<GameEvent<Object>> getRoundFinished(){
        return roundFinished;
    }

    public Observable<GameEvent<String>> getUserWins(){
        return userWins;
    }

    public Observable<GameEvent<String>> getDealerWins(){
        return dealerWins;
    }
}
