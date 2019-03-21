package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "MainActivity";
    private GameViewModel viewModel;
    private TextView dealerTitle;
    private TextView playerTitle;
    private TextView dealerCards;
    private TextView playerCards;
    private TextView winnerDisplay;
    private TextView winnerReasonDisplay;
    private TextView userMoney;
    private Button buttonHit;
    private Button buttonStay;
    private Button buttonNewRound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // find UI elements
        dealerTitle = findViewById(R.id.dealer_hand_title);
        playerTitle = findViewById(R.id.player_hand_title);
        dealerCards = findViewById(R.id.dealer_cards_text);
        playerCards = findViewById(R.id.player_cards_text);
        winnerDisplay = findViewById(R.id.winner_display_text);
        winnerReasonDisplay = findViewById(R.id.winner_reason_display_text);
        userMoney = findViewById(R.id.user_money_text);
        buttonHit = findViewById(R.id.button_hit);
        buttonStay = findViewById(R.id.button_stay);
        buttonNewRound = findViewById(R.id.button_new_round);

        // hide UI elements
        dealerTitle.setVisibility(View.INVISIBLE);
        playerTitle.setVisibility(View.INVISIBLE);
        dealerCards.setVisibility(View.INVISIBLE);
        playerCards.setVisibility(View.INVISIBLE);
        winnerDisplay.setVisibility(View.INVISIBLE);
        winnerReasonDisplay.setVisibility(View.INVISIBLE);
        buttonHit.setVisibility(View.INVISIBLE);
        buttonStay.setVisibility(View.INVISIBLE);


        buttonHit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                viewModel.inputUserHit();
            }
        });
        buttonStay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                viewModel.inputUserStay();
            }
        });
        buttonNewRound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                viewModel.inputNewRound();
            }
        });

        // ViewModel
        viewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        viewModel.init();

        setupUIObservers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void showCards(List<String> cards, TextView view){
        StringBuilder cardsString = new StringBuilder();
        for(String c : cards){
            cardsString.append(c).append(" ");
        }

        view.setText(cardsString.toString());
    }

    public void showWinnerDisplay(String winner, TextView view){
        String display = winner + " WINS!";
        view.setText(display);
    }


    public void showUserMoney(Integer money, TextView view){
        String display = "Money: $" + money;
        view.setText(display);
    }


    public void showUserCards(List<String> cards){

    }

    public void showDealerCards(List<String> cards){

    }

    public void showWinnerDisplay(String winner, String reason){
        String display = winner + " WINS!";
        winnerDisplay.setText(display);
        winnerReasonDisplay.setText(reason);
    }


    public void showUserMoney(Integer money){
        String display = "Money: $" + money;
        //view.setText(display);
    }



    private void setupUIObservers(){

        //TODO: replace this

        // setup dealer hand observer
        viewModel.getDealerHand().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.i(LOG_TAG, "Dealer hand display has changed, updating view...");
                showCards(strings, dealerCards);
            }
        });

        // setup player hand observer
        viewModel.getPlayerHand().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.i(LOG_TAG, "User hand display has changed, updating view...");
                showCards(strings, playerCards);
            }
        });

        viewModel.getWinner().observe(this, new Observer<String>() {

            @Override
            public void onChanged(@Nullable String s) {
                Log.i(LOG_TAG, "Somebody won! Updating view...");
                showWinnerDisplay(s, winnerDisplay);
                winnerDisplay.setVisibility(View.VISIBLE);
                buttonNewRound.setVisibility(View.VISIBLE);

                buttonHit.setVisibility(View.INVISIBLE);
                buttonStay.setVisibility(View.INVISIBLE);
            }
        });

        viewModel.getUserMoney().observe(this, new Observer<Integer>(){

            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.i(LOG_TAG, "Money amount changed. Updating view...");
                showUserMoney(integer, userMoney);
            }
        });

        viewModel.getRoundDenied().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //TODO: display snackbar
                Snackbar.make(buttonNewRound, s, Snackbar.LENGTH_LONG).show();
            }
        });


        //TODO: with this

        viewModel.getRoundStartedUI().observe(this, new Observer<UIEvent<Object>>() {
            @Override
            public void onChanged(@Nullable UIEvent<Object> objectUIEvent) {
                Log.i(LOG_TAG, "Round Started UI Event Received...");
                winnerDisplay.setVisibility(View.INVISIBLE);
                winnerReasonDisplay.setVisibility(View.INVISIBLE);
                buttonNewRound.setVisibility(View.INVISIBLE);
                dealerTitle.setVisibility(View.VISIBLE);
                playerTitle.setVisibility(View.VISIBLE);
                dealerCards.setVisibility(View.VISIBLE);
                playerCards.setVisibility(View.VISIBLE);
                buttonHit.setVisibility(View.VISIBLE);
                buttonStay.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getDealerCardsUI().observe(this, new Observer<UIEvent<List<String>>>() {
            @Override
            public void onChanged(@Nullable UIEvent<List<String>> listUIEvent) {
                Log.i(LOG_TAG, "Dealer Cards UI Event Received...");

            }
        });

        viewModel.getUserCardsUI().observe(this, new Observer<UIEvent<List<String>>>() {
            @Override
            public void onChanged(@Nullable UIEvent<List<String>> listUIEvent) {
                Log.i(LOG_TAG, "User Cards UI Event Received...");

            }
        });

        viewModel.getRoundFinishedUI().observe(this, new Observer<UIEvent<Object>>() {
            @Override
            public void onChanged(@Nullable UIEvent<Object> objectUIEvent) {
                Log.i(LOG_TAG, "Round Finished UI Event Received...");

            }
        });

        viewModel.getRoundDeniedUI().observe(this, new Observer<UIEvent<String>>() {
            @Override
            public void onChanged(@Nullable UIEvent<String> stringUIEvent) {
                Log.i(LOG_TAG, "Round Denied UI Event Received...");

            }
        });

        viewModel.getUserWinsUI().observe(this, new Observer<UIEvent<String>>() {
            @Override
            public void onChanged(@Nullable UIEvent<String> stringUIEvent) {
                Log.i(LOG_TAG, "User Wins UI Event Received...");

            }
        });

        viewModel.getDealerWinsUI().observe(this, new Observer<UIEvent<String>>() {
            @Override
            public void onChanged(@Nullable UIEvent<String> stringUIEvent) {
                Log.i(LOG_TAG, "Dealer Wins UI Event Received...");

            }
        });

        viewModel.getUserMoneyUI().observe(this, new Observer<UIEvent<Integer>>() {
            @Override
            public void onChanged(@Nullable UIEvent<Integer> integerUIEvent) {
                Log.i(LOG_TAG, "User Money UI Event Received...");

            }
        });
    }
}
