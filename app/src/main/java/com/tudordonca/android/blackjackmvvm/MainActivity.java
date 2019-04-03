package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.tudordonca.android.blackjackmvvm.gameplay.GameState;

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

        Toast toast = Toast.makeText(this, "Buyin is $25", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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


    private void processUIStateUpdate(UIGameState uiState){
        if(uiState.getGameState() == null){
            showRoundDenied(uiState.getMessage());
        }
        else{
            GameState state = uiState.getGameState();
            switch(state.getState()){
                case WELCOME:
                    showWelcome();
                    break;
                case USER_TURN:

                    break;
                case ROUND_FINISHED:

                    break;
                default: break;

            }
        }
    }


    public void showWelcome(){
        // hide UI elements
        dealerTitle.setVisibility(View.INVISIBLE);
        playerTitle.setVisibility(View.INVISIBLE);
        dealerCards.setVisibility(View.INVISIBLE);
        playerCards.setVisibility(View.INVISIBLE);
        winnerDisplay.setVisibility(View.INVISIBLE);
        winnerReasonDisplay.setVisibility(View.INVISIBLE);
        buttonHit.setVisibility(View.INVISIBLE);
        buttonStay.setVisibility(View.INVISIBLE);

        // show round start button
        buttonNewRound.setVisibility(View.VISIBLE);
    }

    public void showNewRound(){
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

    public void showRoundFinished(){
        buttonHit.setVisibility(View.INVISIBLE);
        buttonStay.setVisibility(View.INVISIBLE);
        buttonNewRound.setVisibility(View.VISIBLE);
    }

    public void showRoundDenied(String reason){
        Snackbar.make(buttonNewRound, reason, Snackbar.LENGTH_LONG).show();
    }

    public void showUserCards(List<String> cards){
        StringBuilder cardsString = new StringBuilder();
        for(String c : cards){
            cardsString.append(c).append(" ");
        }
        playerCards.setText(cardsString.toString());
    }

    public void showDealerCards(List<String> cards){
        StringBuilder cardsString = new StringBuilder();
        for(String c : cards){
            cardsString.append(c).append(" ");
        }
        dealerCards.setText(cardsString.toString());
    }

    public void showUserWins(String reason){
        String display = "USER WINS!";
        winnerDisplay.setText(display);
        winnerReasonDisplay.setText(reason);
        winnerDisplay.setVisibility(View.VISIBLE);
        winnerReasonDisplay.setVisibility(View.VISIBLE);
    }

    public void showDealerWins(String reason){
        String display = "DEALER WINS!";
        winnerDisplay.setText(display);
        winnerReasonDisplay.setText(reason);
        winnerDisplay.setVisibility(View.VISIBLE);
        winnerReasonDisplay.setVisibility(View.VISIBLE);
    }


    public void showUserMoney(Integer money){
        String display = "Money: $" + money;
        userMoney.setText(display);

    }




    private void setupUIObservers(){


        viewModel.getUIState().observe(this, new Observer<UIGameState>() {
            @Override
            public void onChanged(@Nullable UIGameState uiGameState) {
                Log.i(LOG_TAG, "New UI State received...");
                processUIStateUpdate(uiGameState);
            }
        });





        viewModel.getRoundStartedUI().observe(this, new Observer<UIEvent<Object>>() {
            @Override
            public void onChanged(@Nullable UIEvent<Object> objectUIEvent) {
                Log.i(LOG_TAG, "Round Started UI Event Received...");
                showNewRound();
            }
        });

        viewModel.getRoundFinishedUI().observe(this, new Observer<UIEvent<Object>>() {
            @Override
            public void onChanged(@Nullable UIEvent<Object> objectUIEvent) {
                Log.i(LOG_TAG, "Round Finished UI Event Received...");
                showRoundFinished();
            }
        });

        viewModel.getRoundDeniedUI().observe(this, new Observer<UIEvent<String>>() {
            @Override
            public void onChanged(@Nullable UIEvent<String> stringUIEvent) {
                Log.i(LOG_TAG, "Round Denied UI Event Received...");
                if (stringUIEvent != null) {
                    showRoundDenied(stringUIEvent.getContentIfNotHandled());
                }
                else{
                    Log.e(LOG_TAG, "Invalid Null event object received!");
                }
            }
        });

        viewModel.getDealerCardsUI().observe(this, new Observer<UIEvent<List<String>>>() {
            @Override
            public void onChanged(@Nullable UIEvent<List<String>> listUIEvent) {
                Log.i(LOG_TAG, "Dealer Cards UI Event Received...");
                if (listUIEvent != null) {
                    showDealerCards(listUIEvent.getContentIfNotHandled());
                }
                else{
                    Log.e(LOG_TAG, "Invalid Null event object received!");
                }
            }
        });

        viewModel.getUserCardsUI().observe(this, new Observer<UIEvent<List<String>>>() {
            @Override
            public void onChanged(@Nullable UIEvent<List<String>> listUIEvent) {
                Log.i(LOG_TAG, "User Cards UI Event Received...");
                if (listUIEvent != null) {
                    showUserCards(listUIEvent.getContentIfNotHandled());
                }
                else{
                    Log.e(LOG_TAG, "Invalid Null event object received!");
                }
            }
        });


        viewModel.getUserWinsUI().observe(this, new Observer<UIEvent<String>>() {
            @Override
            public void onChanged(@Nullable UIEvent<String> stringUIEvent) {
                Log.i(LOG_TAG, "User Wins UI Event Received...");
                if (stringUIEvent != null) {
                    showUserWins(stringUIEvent.getContentIfNotHandled());
                }
                else{
                    Log.e(LOG_TAG, "Invalid Null event object received!");
                }
            }
        });

        viewModel.getDealerWinsUI().observe(this, new Observer<UIEvent<String>>() {
            @Override
            public void onChanged(@Nullable UIEvent<String> stringUIEvent) {
                Log.i(LOG_TAG, "Dealer Wins UI Event Received...");
                if (stringUIEvent != null) {
                    showDealerWins(stringUIEvent.getContentIfNotHandled());
                }
                else{
                    Log.e(LOG_TAG, "Invalid Null event object received!");
                }
            }
        });

        viewModel.getUserMoneyUI().observe(this, new Observer<UIEvent<Integer>>() {
            @Override
            public void onChanged(@Nullable UIEvent<Integer> integerUIEvent) {
                Log.i(LOG_TAG, "User Money UI Event Received...");
                if (integerUIEvent != null) {
                    showUserMoney(integerUIEvent.getContentIfNotHandled());
                }
                else{
                    Log.e(LOG_TAG, "Invalid Null event object received!");
                }
            }
        });
    }
}
