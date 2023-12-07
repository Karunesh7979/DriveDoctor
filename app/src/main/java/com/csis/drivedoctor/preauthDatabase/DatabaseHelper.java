package com.csis.drivedoctor.preauthDatabase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.csis.drivedoctor.Helpers.ModelHelper;
import com.csis.drivedoctor.model.CarsModel;
import com.csis.drivedoctor.model.MyAppointmentsModel;
import com.csis.drivedoctor.model.MyUserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DatabaseHelper {

    //Firebase
    public FirebaseStorage storage;
    public StorageReference storageReference;
    public FirebaseFirestore db;
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    private static DatabaseHelper single_instance = null;

    SharedPreferences pref;
    public static Context applicationContext;

    public ArrayList<CarsModel> carsList = new ArrayList<CarsModel>();
    public ArrayList<MyUserData> providersList = new ArrayList<MyUserData>();
    public ArrayList<MyUserData> clientsList = new ArrayList<MyUserData>();
    public ArrayList<MyAppointmentsModel> appointmentsList = new ArrayList<MyAppointmentsModel>();

    private DatabaseHelper() {
        initialiseFirebase();
    }

    public void initialiseFirebase() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public static synchronized DatabaseHelper getInstance() {
        if (single_instance == null)
            single_instance = new DatabaseHelper();

        return single_instance;
    }

    //Set Data
    public SharedPreferences getPref() {
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return sharedPreferences;
    }


    public void saveUserData(MyUserData usrData, TaskCompletion completion) {

        db.collection("user")
                .add(usrData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("save user data success Userid-->>" + usrData.getUserId());

                        usrData.setDocumentId(documentReference.getId());
                        updateUserData(usrData, new TaskCompletion() {
                            @Override
                            public void taskCompletion(boolean isSuccess) {
                                ModelHelper.getInstance().fetchUser(new TaskCompletion() {
                                    @Override
                                    public void taskCompletion(boolean isSuccess) {
                                        completion.taskCompletion(true);
                                    }
                                });
                            }
                        });
                        Log.w("Database: ", "User Data saved-> successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Database: ", "Error adding user", e);
                        completion.taskCompletion(false);
                    }
                });
    }

    public void saveNewCar(CarsModel car, TaskCompletion completion) {
        db.collection("mycars")
                .add(car)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("User Data saved-> successful");
                        completion.taskCompletion(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completion.taskCompletion(false);
                        Log.w("Datbase: ", "Error adding user", e);
                    }
                });
    }

    public void saveAppointment(MyAppointmentsModel appointment, TaskCompletion completion) {
        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("User Data saved-> successful");
                        appointment.setDocumentId(documentReference.getId());
                        updateAppointment(appointment, new TaskCompletion() {
                            @Override
                            public void taskCompletion(boolean isSuccess) {
                                completion.taskCompletion(true);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completion.taskCompletion(false);
                        Log.w("Datbase: ", "Error adding user", e);
                    }
                });
    }

    //Get Data

    public void getCars(DBCompletionHandler<CarsModel> callback) {
        DatabaseHelper.getInstance().db.collection("mycars")
                .whereEqualTo("userId", ModelHelper.getInstance().getUser().getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isSuccess = false;

                        if (task.isSuccessful()) {
                            carsList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Add each car to car list
                                CarsModel cars = document.toObject(CarsModel.class);
                                carsList.add(cars);
                            }
                            isSuccess = carsList.size() > 0;

                        } else {
                            Log.d("DATABASE", "Error getting documents: ", task.getException());
                        }
                        callback.handle(carsList, isSuccess);
                    }
                });

    }

    public void getProviders(DBCompletionHandler<MyUserData> callback) {
        DatabaseHelper.getInstance().db.collection("user")
                .whereEqualTo("serviceProvider", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isSuccess = false;

                        if (task.isSuccessful()) {
                            providersList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Add each car to car list
                                MyUserData user = document.toObject(MyUserData.class);
                                providersList.add(user);
                            }
                            isSuccess = providersList.size() > 0;

                        } else {
                            Log.d("DATABASE", "Error getting documents: ", task.getException());
                        }
                        callback.handle(providersList, isSuccess);
                    }
                });

    }
    public void getProvidersWithFilter(String city, DBCompletionHandler<MyUserData> callback) {
        DatabaseHelper.getInstance().db.collection("user")
                .whereEqualTo("serviceProvider", true)
                .whereEqualTo("city", city )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isSuccess = false;

                        if (task.isSuccessful()) {
                            providersList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Add each car to car list
                                MyUserData user = document.toObject(MyUserData.class);
                                providersList.add(user);
                            }
                            isSuccess = providersList.size() > 0;

                        } else {
                            Log.d("DATABASE", "Error getting documents: ", task.getException());
                        }
                        callback.handle(providersList, isSuccess);
                    }
                });

    }

    public void getClients(DBCompletionHandler<MyUserData> callback) {
        DatabaseHelper.getInstance().db.collection("user")
                .whereEqualTo("serviceProvider", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean isSuccess = false;

                        if (task.isSuccessful()) {
                            clientsList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Add each car to car list
                                MyUserData user = document.toObject(MyUserData.class);
                                clientsList.add(user);
                            }
                            isSuccess = clientsList.size() > 0;

                        } else {
                            Log.d("DATABASE", "Error getting documents: ", task.getException());
                        }
                        callback.handle(clientsList, isSuccess);
                    }
                });

    }

    public void getAppointments(DBCompletionHandler<MyAppointmentsModel> callback) {
        if (ModelHelper.getInstance().getUser().isServiceProvider()) {
            DatabaseHelper.getInstance().db.collection("appointments")
                    .whereEqualTo("providerID", ModelHelper.getInstance().getUser().getUserId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            boolean isSuccess = false;

                            if (task.isSuccessful()) {
                                appointmentsList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Add each car to car list
                                    MyAppointmentsModel appointment = document.toObject(MyAppointmentsModel.class);
                                    appointmentsList.add(appointment);
                                }
                                isSuccess = appointmentsList.size() > 0;

                            } else {
                                Log.d("DATABASE", "Error getting documents: ", task.getException());
                            }
                            callback.handle(appointmentsList, isSuccess);
                        }
                    });
        } else {

            DatabaseHelper.getInstance().db.collection("appointments")
                    .whereEqualTo("userId", ModelHelper.getInstance().getUser().getUserId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            boolean isSuccess = false;

                            if (task.isSuccessful()) {
                                appointmentsList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Add each car to car list
                                    MyAppointmentsModel appointment = document.toObject(MyAppointmentsModel.class);
                                    appointmentsList.add(appointment);
                                }
                                isSuccess = appointmentsList.size() > 0;

                            } else {
                                Log.d("DATABASE", "Error getting documents: ", task.getException());
                            }
                            callback.handle(appointmentsList, isSuccess);
                        }
                    });

        }
    }

    //UPDATE

    public void deleteUser(String userId, TaskCompletion completion)    {
        //User ID is booking ID here
        db.collection("user").document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        completion.taskCompletion(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completion.taskCompletion(false);
                    }
                });
    }

    public void updateAppointment(MyAppointmentsModel appointment, TaskCompletion completion) {

        db.collection("appointments").document(appointment.getDocumentId())
                .update("serviceCompleted", appointment.isServiceCompleted(), "amount", appointment.getAmount(), "documentId", appointment.getDocumentId())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("User Data saved-> successful");
                        completion.taskCompletion(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completion.taskCompletion(false);
                        Log.w("Firebase: ", "Error adding user", e);
                    }
                });
    }

    public void updateUserData(MyUserData usrData, TaskCompletion completion) {

        System.out.println("updateUserData Userid-->>" + usrData.getUserId());

        db.collection("user").document(usrData.getDocumentId())
                .update("profileURL", usrData.getProfileURL(), "documentId", usrData.getDocumentId(),
                        "firstName",usrData.getFirstName(),
                        "lastName", usrData.getLastName(),
                        "city", usrData.getCity(),
                        "phone", usrData.getPhone(),
                        "address", usrData.getAddress())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("User Data saved-> successful");
                        completion.taskCompletion(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completion.taskCompletion(false);
                        Log.w("Firebase: ", "Error adding user", e);
                    }
                });
    }
}
