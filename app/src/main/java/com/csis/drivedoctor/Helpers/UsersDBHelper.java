package com.csis.drivedoctor.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.csis.drivedoctor.model.CarsModel;
import com.csis.drivedoctor.model.MyAppointmentsModel;
import com.csis.drivedoctor.model.MyUserData;

import java.util.ArrayList;

final public class UsersDBHelper extends SQLiteOpenHelper {

    final static String DATABASE_NAME = "AutoMechanicDB";
    final static int DATABASE_VERSION = 1;
    final static String TABLE1_NAME = "Users_table";
    final static String TABLE2_NAME = "Appointments_table";
    final static String TABLE3_NAME = "Cars_table";

    final static String T1C1 = "Id";
    final static String T1C2 = "First_name";
    final static String T1C3 = "Last_name";
    final static String T1C4 = "Profile_url";
    final static String T1C5 = "Is_Service_Provider";
    final static String T1C6 = "Email";
    final static String T1C7 = "City";
    final static String T1C8 = "Address";
    final static String T1C9 = "Phone";


    final static String T2C1 = "userId";
    final static String T2C2 = "username";
    final static String T2C3 = "providername";
    final static String T2C4 = "carid";
    final static String T2C5 = "carmodel";
    final static String T2C6 = "carnameplate";
    final static String T2C7 = "carcompany";
    final static String T2C8 = "appointmentId";
    final static String T2C9 = "date";
    final static String T2C10 = "description";
    final static String T2C11 = "amount";
    final static String T2C12 = "doucumentId";
    final static String T2C13 = "isservice_completed";
    final static String T2C14 = "is_dropOffNeeded";
    final static String T2C15 = "isPickupNeeded";
    final static String T2C16 = "providerID";
    final static String T2C17 = "serviceType";


    final static String T3C1 = "company";
    final static String T3C2 = "carNo";
    final static String T3C3 = "carName";
    final static String T3C4 = "carId";
    final static String T3C5 = "userId";
    final static String T3C6 = "documentId";

    public UsersDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE1_NAME + "("
                + T1C1 + " TEXT PRIMARY KEY, "
                + T1C2 + " TEXT,"
                + T1C3 + " TEXT, "
                + T1C4 + " TEXT, "
                + T1C5 + " INTEGER, "
                + T1C6 + " TEXT, "
                + T1C7 + " TEXT, "
                + T1C8 + " TEXT, "
                + T1C9 + " TEXT "
                + ")";
        sqLiteDatabase.execSQL(query);
        String query2 = "CREATE TABLE " + TABLE2_NAME + "("
                + T2C1 + " TEXT, "
                + T2C2 + " TEXT, "
                + T2C3 + " TEXT, "
                + T2C4 + " TEXT, "
                + T2C5 + " TEXT, "
                + T2C6 + " TEXT, "
                + T2C7 + " TEXT, "
                + T2C8 + " TEXT PRIMARY KEY, "
                + T2C9 + " TEXT, "
                + T2C10 + " TEXT, "
                + T2C11 + " TEXT, "
                + T2C12 + " TEXT, "
                + T2C13 + " INTEGER, "
                + T2C14 + " INTEGER, "
                + T2C15 + " INTEGER, "
                + T2C16 + " TEXT, "
                + T2C17 + " TEXT "

                + ")";
        sqLiteDatabase.execSQL(query2);

        String query3 = "CREATE TABLE " + TABLE3_NAME + "("
                + T3C1 + " TEXT, "
                + T3C2 + " TEXT, "
                + T3C3 + " TEXT, "
                + T3C4 + " TEXT PRIMARY KEY, "
                + T3C5 + " TEXT, "
                + T3C6 + " TEXT "
                + ")";

        sqLiteDatabase.execSQL(query3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        onCreate(sqLiteDatabase);
    }


    //USERS OPERATIONS

    public boolean addNewUser(MyUserData user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T1C1, user.getUserId());
        values.put(T1C2, user.getFirstName());
        values.put(T1C3, user.getLastName());
        values.put(T1C4, user.getProfileURL());
        int isServiceProvicer = user.isServiceProvider() ? 1 : 0;
        values.put(T1C5, isServiceProvicer);
        values.put(T1C6, user.getEmail());
        values.put(T1C7, user.getCity());
        values.put(T1C8, user.getAddress());
        values.put(T1C9, user.getPhone());

        return  sqLiteDatabase.insert(TABLE1_NAME, null, values) > 0;
    }

    public ArrayList<MyUserData> getAllUsers() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE1_NAME;
        Cursor crsr = sqLiteDatabase.rawQuery(query, null);

        ArrayList<MyUserData> usrs = new ArrayList<MyUserData>();

        if (crsr.getCount() > 0) {
            while (crsr.moveToNext()) {
                MyUserData usr = new MyUserData(false);
                usr.setUserId(crsr.getString(0));
                usr.setFirstName(crsr.getString(0));
                usr.setLastName(crsr.getString(0));
                usr.setProfileURL(crsr.getString(0));
                boolean isServ = crsr.getInt(0) == 1 ? true : false;
                usr.setServiceProvider(isServ);
                usr.setEmail(crsr.getString(0));
                usr.setCity(crsr.getString(0));
                usr.setAddress(crsr.getString(0));
                usr.setPhone(crsr.getString(0));

                usrs.add(usr);
            }
        }
        return usrs;
    }

    public boolean deleteRec(String userId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE1_NAME,"Id=?",
                new String[]{userId}) > 0;
    }

    public MyUserData getUserWithId(String userId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE1_NAME + " WHERE Id = ?";
        Cursor crsr = sqLiteDatabase.rawQuery(query, new String[]{userId});

        MyUserData usr = new MyUserData(false);

        if (crsr.getCount() > 0) {
            while (crsr.moveToNext()) {
                usr.setUserId(crsr.getString(0));
                usr.setFirstName(crsr.getString(0));
                usr.setLastName(crsr.getString(0));
                usr.setProfileURL(crsr.getString(0));
                boolean isServ = crsr.getInt(0) == 1 ? true : false;
                usr.setServiceProvider(isServ);
                usr.setEmail(crsr.getString(0));
                usr.setCity(crsr.getString(0));
                usr.setAddress(crsr.getString(0));
                usr.setPhone(crsr.getString(0));
            }
        }
        return usr;
    }

    public boolean UpdateUser(MyUserData user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T1C2, user.getFirstName());
        values.put(T1C3, user.getLastName());
        values.put(T1C4, user.getProfileURL());
        int isServiceProvicer = user.isServiceProvider() ? 1 : 0;
        values.put(T1C5, isServiceProvicer);
        values.put(T1C6, user.getEmail());
        values.put(T1C7, user.getCity());
        values.put(T1C8, user.getAddress());
        values.put(T1C9, user.getPhone());

        return sqLiteDatabase.update(TABLE1_NAME, values, "Id=?",
                new String[]{user.getUserId()}) > 0;
    }


//APPOINTMENTS OPERATIONS
    public boolean addNewAppointment(MyAppointmentsModel appointment) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T2C1, appointment.getUserId());
        values.put(T2C2, appointment.getUserName());
        values.put(T2C3, appointment.getProviderName());
        values.put(T2C4, appointment.getCarID());
        values.put(T2C5, appointment.getCarmodel());
        values.put(T2C6, appointment.getCarNamePlate());
        values.put(T2C7, appointment.getCarCompany());
        values.put(T2C8, appointment.getAppointmentID());
        values.put(T2C9, appointment.getDate());
        values.put(T2C10, appointment.getDescription());
        values.put(T2C11, Double.toString(appointment.getAmount()));
        values.put(T2C12, appointment.getDocumentId());


        int isServicecomppleted = appointment.isServiceCompleted() ? 1 : 0;
        int dropoffneeded = appointment.isDropOffNeeded() ? 1 : 0;
        int pickupneeded = appointment.isPickupNeeded() ? 1 : 0;

        values.put(T2C13, isServicecomppleted);
        values.put(T2C14, dropoffneeded);
        values.put(T2C15, pickupneeded);
        values.put(T2C16, appointment.getProviderID());
        values.put(T2C17, appointment.getServiceType());

        return  sqLiteDatabase.insert(TABLE2_NAME, null, values) > 0;
    }

    public boolean deleteAppointment(String appoinmentId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE2_NAME,"appointmentId=?",
                new String[]{appoinmentId}) > 0;
    }

    public ArrayList<MyAppointmentsModel> getAppointmentsWithUserId(String userId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE2_NAME + " WHERE userId = ?";
        Cursor crsr = sqLiteDatabase.rawQuery(query, new String[]{userId});

        ArrayList<MyAppointmentsModel> lst = new ArrayList<MyAppointmentsModel>();

        if (crsr.getCount() > 0) {
            while (crsr.moveToNext()) {
                MyAppointmentsModel appointmentsModel = new MyAppointmentsModel();

                appointmentsModel.setUserId(crsr.getString(0));
                appointmentsModel.setUserName(crsr.getString(0));
                appointmentsModel.setProviderName(crsr.getString(0));
                appointmentsModel.setCarID(crsr.getString(0));
                appointmentsModel.setCarmodel(crsr.getString(0));
                appointmentsModel.setCarNamePlate(crsr.getString(0));
                appointmentsModel.setCarCompany(crsr.getString(0));
                appointmentsModel.setAppointmentID(crsr.getString(0));
                appointmentsModel.setDate(crsr.getString(0));
                appointmentsModel.setDescription(crsr.getString(0));
                appointmentsModel.setAmount(Double.parseDouble(crsr.getString(0)));
                appointmentsModel.setDocumentId(crsr.getString(0));

                boolean isServCompleted = crsr.getInt(0) == 1 ? true : false;
                boolean dropOffNeeded = crsr.getInt(0) == 1 ? true : false;
                boolean pickupNeeded = crsr.getInt(0) == 1 ? true : false;

                appointmentsModel.setServiceCompleted(isServCompleted);
                appointmentsModel.setDropOffNeeded(dropOffNeeded);
                appointmentsModel.setPickupNeeded(pickupNeeded);
                appointmentsModel.setProviderID(crsr.getString(0));
                appointmentsModel.setServiceType(crsr.getString(0));

                lst.add(appointmentsModel);
            }
        }
        return lst;
    }


    public ArrayList<MyAppointmentsModel> getAppointmentsForProvider(String userId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE2_NAME + " WHERE providerID = ?";
        Cursor crsr = sqLiteDatabase.rawQuery(query, new String[]{userId});

        ArrayList<MyAppointmentsModel> lst = new ArrayList<MyAppointmentsModel>();

        if (crsr.getCount() > 0) {
            while (crsr.moveToNext()) {
                MyAppointmentsModel appointmentsModel = new MyAppointmentsModel();

                appointmentsModel.setUserId(crsr.getString(0));
                appointmentsModel.setUserName(crsr.getString(0));
                appointmentsModel.setProviderName(crsr.getString(0));
                appointmentsModel.setCarID(crsr.getString(0));
                appointmentsModel.setCarmodel(crsr.getString(0));
                appointmentsModel.setCarNamePlate(crsr.getString(0));
                appointmentsModel.setCarCompany(crsr.getString(0));
                appointmentsModel.setAppointmentID(crsr.getString(0));
                appointmentsModel.setDate(crsr.getString(0));
                appointmentsModel.setDescription(crsr.getString(0));
                appointmentsModel.setAmount(Double.parseDouble(crsr.getString(0)));
                appointmentsModel.setDocumentId(crsr.getString(0));

                boolean isServCompleted = crsr.getInt(0) == 1 ? true : false;
                boolean dropOffNeeded = crsr.getInt(0) == 1 ? true : false;
                boolean pickupNeeded = crsr.getInt(0) == 1 ? true : false;

                appointmentsModel.setServiceCompleted(isServCompleted);
                appointmentsModel.setDropOffNeeded(dropOffNeeded);
                appointmentsModel.setPickupNeeded(pickupNeeded);
                appointmentsModel.setProviderID(crsr.getString(0));
                appointmentsModel.setServiceType(crsr.getString(0));

                lst.add(appointmentsModel);
            }
        }
        return lst;
    }


    public boolean UpdateAppointment(MyAppointmentsModel appointment) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T2C1, appointment.getUserId());
        values.put(T2C2, appointment.getUserName());
        values.put(T2C3, appointment.getProviderName());
        values.put(T2C4, appointment.getCarID());
        values.put(T2C5, appointment.getCarmodel());
        values.put(T2C6, appointment.getCarNamePlate());
        values.put(T2C7, appointment.getCarCompany());
        values.put(T2C8, appointment.getAppointmentID());
        values.put(T2C9, appointment.getDate());
        values.put(T2C10, appointment.getDescription());
        values.put(T2C11, Double.toString(appointment.getAmount()));
        values.put(T2C12, appointment.getDocumentId());


        int isServicecomppleted = appointment.isServiceCompleted() ? 1 : 0;
        int dropoffneeded = appointment.isDropOffNeeded() ? 1 : 0;
        int pickupneeded = appointment.isPickupNeeded() ? 1 : 0;

        values.put(T2C13, isServicecomppleted);
        values.put(T2C14, dropoffneeded);
        values.put(T2C15, pickupneeded);
        values.put(T2C16, appointment.getProviderID());
        values.put(T2C17, appointment.getServiceType());
        return sqLiteDatabase.update(TABLE2_NAME, values, "appointmentId=?",
                new String[]{appointment.getAppointmentID()}) > 0;
    }

    //CARS OPERATIONS

    public ArrayList<CarsModel> getCarsForUserId(String userId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE3_NAME + " WHERE userId = ?";
        Cursor crsr = sqLiteDatabase.rawQuery(query, new String[]{userId});

        ArrayList<CarsModel> lst = new ArrayList<CarsModel>();

        if (crsr.getCount() > 0) {
            while (crsr.moveToNext()) {
                CarsModel car = new CarsModel();

                car.setCompany(crsr.getString(0));
                car.setCarNo(crsr.getString(0));
                car.setCarName(crsr.getString(0));
                car.setCarId(crsr.getString(0));
                car.setUserId(crsr.getString(0));
                car.setDocumentId(crsr.getString(0));


                lst.add(car);
            }
        }
        return lst;
    }

    public boolean addNewCar(CarsModel car) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T3C1, car.getCompany());
        values.put(T3C2, car.getCarNo());
        values.put(T3C3, car.getCarName());
        values.put(T3C4, car.getCarId());
        values.put(T3C5, car.getUserId());
        values.put(T3C6, car.getDocumentId());

        return  sqLiteDatabase.insert(TABLE3_NAME, null, values) > 0;
    }

}
