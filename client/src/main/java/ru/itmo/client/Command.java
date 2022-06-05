package ru.itmo.client;

import ru.itmo.common.authorization.User;

import java.io.BufferedReader;
import java.io.IOException;

public interface Command {
    String executeCommand(BufferedReader bf, User user) throws IOException;
    String getName();
}
