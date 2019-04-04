package com.tudordonca.android.blackjackmvvm.gamemechanics;

import java.util.List;

public interface Player {

    List<Card> getCards();

    List<String> getCardsDisplay();

    void addCard(Card card);

    void clearCards();
}
