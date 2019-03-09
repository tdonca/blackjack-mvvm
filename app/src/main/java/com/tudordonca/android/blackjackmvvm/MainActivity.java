package com.tudordonca.android.blackjackmvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String LOG_TAG = "MainActivity";
    private GameViewModel viewModel;
    private TextView dealerCards;
    private TextView playerCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // find UI elements
        dealerCards = findViewById(R.id.dealer_cards_text);
        playerCards = findViewById(R.id.player_cards_text);

        // ViewModel
        viewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        viewModel.init();

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



}
