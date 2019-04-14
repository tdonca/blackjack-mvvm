package com.tudordonca.android.blackjackmvvm.gameplay.gamemechanics;

import com.tudordonca.android.blackjackmvvm.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StandardDeck implements Deck {
    private final int NUM_CARDS = 52;
    private List<Card> cards;
    private Random randomGenerator;

    public StandardDeck(){
        cards = new ArrayList<>();
        randomGenerator = new Random();
        populateDeck();
    }

    public Card drawCard(){
        return cards.get(randomGenerator.nextInt(NUM_CARDS));
    }

    void populateDeck(){
        cards.clear();
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.TWO));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.THREE));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.FOUR));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.FIVE));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.SIX));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.SEVEN));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.EIGHT));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.NINE));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.TEN));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.JACK));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.QUEEN));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.KING));
        cards.add(new StandardCard(StandardCard.Suit.HEART, StandardCard.Face.ACE));

        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.TWO));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.THREE));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.FOUR));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.FIVE));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.SIX));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.SEVEN));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.EIGHT));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.NINE));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.TEN));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.JACK));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.QUEEN));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.KING));
        cards.add(new StandardCard(StandardCard.Suit.DIAMOND, StandardCard.Face.ACE));

        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.TWO));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.THREE));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.FOUR));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.FIVE));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.SIX));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.SEVEN));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.EIGHT));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.NINE));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.TEN));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.JACK));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.QUEEN));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.KING));
        cards.add(new StandardCard(StandardCard.Suit.CLUB, StandardCard.Face.ACE));

        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.TWO));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.THREE));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.FOUR));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.FIVE));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.SIX));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.SEVEN));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.EIGHT));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.NINE));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.TEN));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.JACK));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.QUEEN));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.KING));
        cards.add(new StandardCard(StandardCard.Suit.SPADE, StandardCard.Face.ACE));

        if (BuildConfig.DEBUG && (cards.size() != NUM_CARDS)) { throw new AssertionError(); }
    }
}
