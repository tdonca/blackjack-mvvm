package com.tudordonca.android.blackjackmvvm;

public class GameEvent<T> extends Event<T> {

    public GameEvent(T content){
        super(content);
    }
}
