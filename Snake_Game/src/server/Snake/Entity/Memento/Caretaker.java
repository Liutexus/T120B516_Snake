package server.Snake.Entity.Memento;

import java.util.ArrayDeque;

public class Caretaker {
    ArrayDeque<Memento> states;

    public Caretaker(){
        states = new ArrayDeque<>();
    }

    public void addSnapshot(Memento memento){
        if(states.size() > 50)
            states.removeLast();
        states.addFirst(memento);
    }

    public Memento get(){
        return states.pop();
    }

}
