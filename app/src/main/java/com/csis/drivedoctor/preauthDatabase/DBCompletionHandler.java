package com.csis.drivedoctor.preauthDatabase;

import java.util.ArrayList;

public interface DBCompletionHandler<T> {
    void handle(ArrayList<T> myList, boolean isSuccess) ;


}
