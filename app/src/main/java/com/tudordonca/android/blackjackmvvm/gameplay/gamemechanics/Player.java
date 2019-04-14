package com.tudordonca.android.blackjackmvvm.gameplay.gamemechanics;

import java.util.List;

public interface Player {

    List<Card> getCards();

    List<String> getCardsDisplay();

    void addCard(Card card);

    void clearCards();
}
