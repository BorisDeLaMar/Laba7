package ru.itmo.client;

import ru.itmo.common.connection.*;

import java.util.ArrayList;

public interface ServerAPI {
    Response add(ArrayList<String> args);
    Response add_if_min(ArrayList<String> args);
    Response info();
    Response show();
    Response clear();
    Response exit();
    Response history();
    Response execute_script(String filename);
    Response filter_less_than_status(String state);
    Response help();
    Response print_descending();
    Response print_unique_status();
    Response remove(long id);
    Response remove_lower(long id);
    Response update(ArrayList<String> args);
}
