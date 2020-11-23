package server.Snake.Entity.Memento;

import server.Snake.Interface.IMemento;

public class Memento implements IMemento {
    Object state;

    public Memento(){}

    public Memento(Object state){
        this.state = state;
    }

    @Override
    public void setState(Object state) {
        this.state = state;
    }

    @Override
    public Object getState() {
        return this.state;
    }
}
