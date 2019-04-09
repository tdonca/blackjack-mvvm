package com.tudordonca.android.blackjackmvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.tudordonca.android.blackjackmvvm.userdata.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
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

    NotificationManager notificationManager;


    // Notification ID.
    private static final int NOTIFICATION_ID = 1;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

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
        setupUIObservers();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Intent notifyIntent = new Intent(MainActivity.this, MoneyAlarmReceiver.class);
        boolean alarmUp = (PendingIntent.getBroadcast(MainActivity.this, NOTIFICATION_ID,
                notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmUp){
            menu.getItem(0).setTitle(R.string.turn_off_deposit);
        }
        else{
            menu.getItem(0).setTitle(R.string.turn_on_deposit);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.deposit_setting) {

            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent notifyIntent = new Intent(MainActivity.this, MoneyAlarmReceiver.class);
            boolean alarmUp = (PendingIntent.getBroadcast(MainActivity.this, NOTIFICATION_ID,
                    notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);

            // Only register a new alarm if it doesn't yet exist
            if(!alarmUp){
                PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                        (MainActivity.this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                long repeatInterval = AlarmManager.INTERVAL_DAY;
                long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;
                if (alarmManager != null) {
                    alarmManager.setInexactRepeating
                            (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerTime, repeatInterval, notifyPendingIntent);
                    // enable app notifications.
                    createNotificationChannel();
                    //update text
                    item.setTitle(R.string.turn_off_deposit);
                    Log.i(LOG_TAG, "Turned on Daily Deposits and notifications");
                }
            }
            // Otherwise turn off the alarm
            else{
                PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                        (MainActivity.this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_NO_CREATE);
                if (alarmManager != null) {
                    alarmManager.cancel(notifyPendingIntent);
                    notificationManager.cancelAll();
                    // update text
                    item.setTitle(R.string.turn_on_deposit);
                    Log.i(LOG_TAG, "Turned off Daily Deposits and notifications");
                }
            }

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
        playerCards.setVisibility(View.VISIBLE);
    }

    public void showDealerCards(List<String> cards){
        StringBuilder cardsString = new StringBuilder();
        for(String c : cards){
            cardsString.append(c).append(" ");
        }
        dealerCards.setText(cardsString.toString());
        dealerCards.setVisibility(View.VISIBLE);
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

        viewModel.getUserMoney().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.i(LOG_TAG, "New money value received...");
                showUserMoney(integer);
            }
        });
    }


    public void createNotificationChannel() {

        // Create a notification manager object.
        notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every 15 minutes to " +
                    "stand up and walk");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        viewModel.disposeObservers();
    }

}
