package ru.itmo.server.src.containers;

import ru.itmo.server.src.Comms.Commands;

import java.util.ArrayDeque;

public class stringQueue {
    private final String string;
    private final ArrayDeque<Commands> q;

    public stringQueue(String string, ArrayDeque<Commands> q){
        this.string = string;
        this.q = q;
    }

    public String getString() {
        return string;
    }

    public ArrayDeque<Commands> getQueue() {
        return q;
    }
}
