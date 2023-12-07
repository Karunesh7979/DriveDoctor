package com.csis.drivedoctor.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.csis.drivedoctor.model.MyUserData;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;
import com.csis.drivedoctor.preauthDatabase.TaskCompletion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ModelHelper {

    private static ModelHelper single_instance = null;
    private  MyUserData user;
    public static Context applicationContext;


    public static synchronized ModelHelper getInstance() {
        if (single_instance == null)
            single_instance = new ModelHelper();
        return single_instance;
    }

    public void destroyInstance() {
        single_instance = null;
    }

    private ModelHelper() {
        fetchUser(new TaskCompletion() {
            @Override
            public void taskCompletion(boolean isSuccess) {
                if (isSuccess) {
                    Log.w("Database ::", "User fetched");

                } else {
                    Log.w("Database Failed ::", "User fetched");
                }
            }
        });

    }

    private SharedPreferences getPref() {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public MyUserData getUser() {
        return user;
    }


    public void setUser(MyUserData user) {
        this.user = user;
    }



    public void getMyAppointments() {

    }

    public void getMyCars() {

    }

    public void fetchUser(TaskCompletion completion) {

        try {
            String userID= getPref().getString("userId", "");
            DatabaseHelper.getInstance().db.collection("user")
                    .whereEqualTo("userId", userID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Add each car to car list
                                    Toast.makeText(applicationContext, "User Fetched.", Toast.LENGTH_SHORT).show();
                                    user = document.toObject(MyUserData.class);
                                }
                                completion.taskCompletion(true);
                            } else {
                                Toast.makeText(applicationContext, "No User Fetched.", Toast.LENGTH_SHORT).show();
                                Log.d("DATABASE", "Error getting documents: ", task.getException());
                            }
                            completion.taskCompletion(false);
                        }
                    });

        } catch (Exception e)
        {
            Log.d("Modelhelper-->>", "fetchUser method -->>"+ e.getLocalizedMessage());
        }


    }

}
