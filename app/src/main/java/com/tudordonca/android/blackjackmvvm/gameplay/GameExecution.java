package com.tudordonca.android.blackjackmvvm.gameplay;

import android.util.Log;


public class GameExecution {
    private final String LOG_TAG = "GameExecution";
    private StandardRuleSet ruleSet;
    private Deck deck;
    private Player dealer;
    private Player user;
    private int dealerHandValue;
    private int userHandValue;
    private boolean userTurn;


    public GameExecution(){
        deck = new StandardDeck();
        dealer = new DealerPlayer();
        user = new UserPlayer();
        dealerHandValue = 0;
        userHandValue = 0;
        userTurn = false;
        ruleSet = new StandardRuleSet();


    }

    public GameState startRound(){
        dealer.clearCards();
        dealerHandValue = 0;
        user.clearCards();
        userHandValue = 0;
        dealCards();

        if(userHandValue == 21){
            Log.d(LOG_TAG, "USER BLACKJACK!");
            userTurn = false;
            return finishRound();
        }
        else{
            userTurn = true;
            return new GameState(GameState.GameProgressState.USER_TURN, dealer.getCards(), user.getCards());
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

        evaluateCards();
    }

    private void evaluateCards(){
        dealerHandValue = ruleSet.getValue(dealer.getCards());
        Log.i(LOG_TAG, "Dealer's hand value: " + dealerHandValue);

        userHandValue = ruleSet.getValue(user.getCards());
        Log.i(LOG_TAG, "User's hand value: " + userHandValue);
    }


    public GameState userHit(){
        if(userTurn){
            Log.i(LOG_TAG, "User hits, getting a new card...");
            Card card = deck.drawCard();
            user.addCard(card);
            Log.i(LOG_TAG, "User has " + user.getCards().size() + " cards: " + user.getCards().toString());
            evaluateCards();

            if(userHandValue > 21){
                Log.i(LOG_TAG, "USER BUST!");
                userTurn = false;
                return finishRound();
            }
            else{
                return new GameState(GameState.GameProgressState.USER_TURN, dealer.getCards(), user.getCards());
            }
        }
        else{
            Log.e(LOG_TAG, "Not the user's turn right now!");
            return new GameState(GameState.GameProgressState.INVALID);
        }

    }


    public GameState userStay(){
        if(userTurn){
            Log.i(LOG_TAG, "User stays, moving to dealer's turn...");
            userTurn = false;
            return dealerTurn();
        }
        else{
            Log.e(LOG_TAG, "Not the user's turn right now!");
            return new GameState(GameState.GameProgressState.INVALID);
        }


    }


    private GameState dealerTurn(){
        while(dealerHandValue < 17){
            Log.i(LOG_TAG, "Dealer hits, getting a new card...");
            Card card = deck.drawCard();
            dealer.addCard(card);
            Log.i(LOG_TAG, "Dealer has " + dealer.getCards().size() + " cards: " + dealer.getCards().toString());
            evaluateCards();
        }

        if(dealerHandValue > 21){
            Log.i(LOG_TAG, "DEALER BUST!");
        }

        return finishRound();
    }


    private GameState finishRound(){
        GameState state = new GameState(GameState.GameProgressState.ROUND_FINISHED, dealer.getCards(), user.getCards());

        if(userHandValue > 21){
            Log.i(LOG_TAG, "DEALER WINS!");
            state.setWinner("DEALER");
            state.setMessage("USER BUST");
        }
        else if(dealerHandValue > 21){
            Log.i(LOG_TAG, "USER WINS!");
            state.setWinner("USER");
            state.setMessage("DEALER BUST");
        }
        else if(dealerHandValue >= userHandValue){
            Log.i(LOG_TAG, "DEALER WINS!");
            state.setWinner("DEALER");
            state.setMessage("GREATER VALUE");
        }
        else{
            Log.i(LOG_TAG, "USER WINS!");
            state.setWinner("USER WINS");
            state.setMessage("GREATER VALUE");
        }


        return state;
    }


}
