package com.tudordonca.android.blackjackmvvm;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


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


    private void processUIStateUpdate(UIGameState uiState){
        if(uiState != null){
            switch(uiState.getState()){
                case WELCOME:
                    showWelcome(uiState.getMessage(), uiState.getUserMoney());
                    break;
                case DENIED:
                    showUserMoney(uiState.getUserMoney());
                    showRoundDenied(uiState.getMessage());
                    break;
                case IN_PROGRESS:
                    showNewRound();
                    showUserMoney(uiState.getUserMoney());
                    showDealerCards(uiState.getDealerCards());
                    showUserCards(uiState.getUserCards());
                    break;
                case DEALER_WIN:
                    showRoundFinished();
                    showDealerWins(uiState.getMessage());
                    showUserMoney(uiState.getUserMoney());
                    showDealerCards(uiState.getDealerCards());
                    showUserCards(uiState.getUserCards());
                    break;
                case USER_WIN:
                    showRoundFinished();
                    showUserWins(uiState.getMessage());
                    showUserMoney(uiState.getUserMoney());
                    showDealerCards(uiState.getDealerCards());
                    showUserCards(uiState.getUserCards());
                    break;
                case TIE:
                    showRoundFinished();
                    showTie(uiState.getMessage());
                    showUserMoney(uiState.getUserMoney());
                    showDealerCards(uiState.getDealerCards());
                    showUserCards(uiState.getUserCards());
                default:
                    break;
            }
        }
    }


    public void showWelcome(String message, int userMoney){
        // hide UI elements
        dealerTitle.setVisibility(View.INVISIBLE);
        playerTitle.setVisibility(View.INVISIBLE);
        dealerCards.setVisibility(View.INVISIBLE);
        playerCards.setVisibility(View.INVISIBLE);
        winnerDisplay.setVisibility(View.INVISIBLE);
        winnerReasonDisplay.setVisibility(View.INVISIBLE);
        buttonHit.setVisibility(View.INVISIBLE);
        buttonStay.setVisibility(View.INVISIBLE);


        buttonNewRound.setVisibility(View.VISIBLE);
        showUserMoney(userMoney);
        Toast toast = Toast.makeText(this, message + " Buyin is $25.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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

    public void showTie(String reason){
        String display = "TIE";
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

    }
}
