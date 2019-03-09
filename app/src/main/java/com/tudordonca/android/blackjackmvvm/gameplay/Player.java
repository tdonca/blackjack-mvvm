package com.tudordonca.android.blackjackmvvm.gameplay;

import java.util.ArrayList;
import java.util.List;

public interface Player {

    List<Card> getCards();

    List<String> getCardsDisplay();

    void addCard(Card card);

    void clearCards();
}
