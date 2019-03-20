package com.tudordonca.android.blackjackmvvm;

public class GameEvent<T> extends Event {

    public GameEvent(T content){
        super(content);
    }
}
