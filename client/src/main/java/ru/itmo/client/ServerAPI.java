package ru.itmo.client;

import ru.itmo.common.authorization.User;
import ru.itmo.common.connection.*;

import java.util.ArrayList;

public interface ServerAPI {
    Response add(ArrayList<String> args, User user);
    Response add_if_min(ArrayList<String> args, User user);
    Response info(User user);
    Response show(User user);
    Response clear(User user);
    Response exit(User user);
    Response history(User user);
    Response execute_script(String filename, User user);
    Response filter_less_than_status(String state, User user);
    Response help(User user);
    Response print_descending(User user);
    Response print_unique_status(User user);
    Response remove(long id, User user);
    Response remove_lower(long id, User user);
    Response update(ArrayList<String> args, User user);
}
