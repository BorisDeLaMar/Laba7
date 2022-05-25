package ru.itmo.client;

import java.io.BufferedReader;
import java.io.IOException;

public interface Command {
    String executeCommand(BufferedReader bf) throws IOException;
    String getName();
}
