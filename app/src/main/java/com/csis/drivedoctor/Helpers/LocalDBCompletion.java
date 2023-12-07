package com.csis.drivedoctor.Helpers;

import java.util.ArrayList;

public interface LocalDBCompletion<T> {

    void handle(ArrayList<T> myList, boolean isSuccess) ;
}
