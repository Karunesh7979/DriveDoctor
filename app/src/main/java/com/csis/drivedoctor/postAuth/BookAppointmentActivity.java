package com.csis.drivedoctor.postAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.csis.drivedoctor.Helpers.Loader;
import com.csis.drivedoctor.Helpers.ReminderHelper;
import com.csis.drivedoctor.Helpers.UsersDBHelper;
import com.csis.drivedoctor.preauthDatabase.TaskCompletion;
import com.csis.drivedoctor.R;
import com.csis.drivedoctor.model.CarsModel;
import com.csis.drivedoctor.model.MyAppointmentsModel;
import com.csis.drivedoctor.model.MyUserData;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;
import com.csis.drivedoctor.Helpers.ModelHelper;

import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class BookAppointmentActivity extends AppCompatActivity {

    boolean isProvider = ModelHelper.getInstance().getUser().isServiceProvider();
    EditText descriptionTextView;
    EditText dateTextView;
    EditText amountTextView;
    MyAppointmentsModel appointmentsModel;
    CheckBox pickUpNeeded;
    CheckBox dropOffNeeded;
    Spinner serviceType;
    UsersDBHelper appointmentDB;
    long seletedTime = 0;
    String remindermsg = "";
    String billContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        appointmentDB = new UsersDBHelper(this);
        Button btnClick = findViewById(R.id.idBookAppointmentActivityAddbtn);
        dateTextView = findViewById(R.id.idBookAppointmentActivityDate);
        amountTextView = findViewById(R.id.idBookAppointmentActivityAmount);
        descriptionTextView = findViewById(R.id.idBookAppointmentActivityDes);
        pickUpNeeded = findViewById(R.id.id_signup_pickupNeeded);
        dropOffNeeded = findViewById(R.id.id_signup_dropOffNeeded);
        serviceType = findViewById(R.id.idBookAppointmentSelectServiceType);

        pickUpNeeded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dropOffNeeded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        if (!isProvider) {
            amountTextView.setVisibility(View.GONE);
        } else {
            appointmentsModel = (MyAppointmentsModel) getIntent().getSerializableExtra("appointment");
            dateTextView.setText(appointmentsModel.getDate());
            descriptionTextView.setText(appointmentsModel.getDescription());
            serviceType.setEnabled(false);
            serviceType.setFocusable(false);
            String[] servicesArray = getResources().getStringArray(R.array.car_services);
            int index = Arrays.asList(servicesArray).indexOf(appointmentsModel.getServiceType());
            serviceType.setSelection(index);
            pickUpNeeded.setChecked(appointmentsModel.isPickupNeeded());
            dropOffNeeded.setChecked(appointmentsModel.isDropOffNeeded());
            dateTextView.setEnabled(false);
            dateTextView.setFocusable(false);
            btnClick.setText("Finish Job");
        }

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener datedialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalendar();
            }

            private void updateCalendar() {
                String Format = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.CANADA);
                dateTextView.setText(sdf.format(calendar.getTime()));
                seletedTime = calendar.getTimeInMillis();
            }
        };

        // Set the minimum date to the current date
        long now = Calendar.getInstance().getTimeInMillis();
        DatePickerDialog datePickerDialog = new DatePickerDialog(BookAppointmentActivity.this, R.style.DatePickerTheme, datedialog, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(now);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });


        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {

        if (dateTextView.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
        } else if (descriptionTextView.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Mention what needs to be serviced in description.", Toast.LENGTH_SHORT).show();
        } else if (amountTextView.getText().toString().trim().equals("") && isProvider) {
            Toast.makeText(this, "Please enter amount to complete the JOB.", Toast.LENGTH_SHORT).show();
        } else {
            processAppointment();
        }
    }

    private void processAppointment() {
        Loader.show(this);

        if (isProvider) {
            appointmentsModel.setAmount(Integer.parseInt(amountTextView.getText().toString().trim()));
            appointmentsModel.setServiceCompleted(true);
            billContent = "Your car service is completed \n Your total due amount is " + appointmentsModel.getAmount()+
                    "$ \n Receipt no- " + appointmentsModel.getAppointmentID() +"\nIt was our pleasure to help you." ;
        } else {

            String service = serviceType.getSelectedItem().toString();

            MyUserData serviceProvider = (MyUserData) getIntent().getSerializableExtra("serviceprovider");
            CarsModel selectedCar = (CarsModel) getIntent().getSerializableExtra("car");
            String providerName = serviceProvider.getFirstName() + " " + serviceProvider.getLastName();
            String carmodel = selectedCar.getCarName() + " " + selectedCar.getModelyear();
            String carCompany = selectedCar.getCompany();

            remindermsg = "Your car " + selectedCar.getCompany() + selectedCar.getCarName() + " "
                    + selectedCar.getModelyear()+ "with nameplate " + selectedCar.getCarNo() +
                    " has appointment with " + serviceProvider.getFirstName() + " in"
                    + serviceProvider.getCity();

            appointmentsModel = new MyAppointmentsModel(
                    providerName,
                    serviceProvider.getUserId(),
                    selectedCar.getCarId(),
                    carmodel,
                    carCompany,
                    selectedCar.getCarNo(),
                    (selectedCar.getCarId() + "appointment" + DatabaseHelper.getInstance().providersList.size() + 1),
                    dateTextView.getText().toString().trim(),
                    descriptionTextView.getText().toString().trim(),
                    service
            );
            appointmentsModel.setDropOffNeeded(dropOffNeeded.isChecked());
            appointmentsModel.setPickupNeeded(pickUpNeeded.isChecked());
        }
        saveAppointment();
    }

    private void saveAppointment() {
        if (isProvider) {
            saveReceipt(appointmentsModel.getAppointmentID(), billContent);
            appointmentDB.UpdateAppointment(appointmentsModel);
            DatabaseHelper.getInstance().updateAppointment(appointmentsModel, new TaskCompletion() {
                @Override
                public void taskCompletion(boolean isSuccess) {
                    goBack();
                }
            });
        } else {

            ReminderHelper.setReminder(this, "AutoMechanic", remindermsg, seletedTime);

            appointmentDB.addNewAppointment(appointmentsModel);
            DatabaseHelper.getInstance().saveAppointment(appointmentsModel, new TaskCompletion() {
                @Override
                public void taskCompletion(boolean isSuccess) {
                    goBack();
                }
            });
        }
    }

    void saveReceipt(String appointmentId, String content) {
        try {
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput(appointmentId+".txt", MODE_APPEND));
            fout.write(content + "\n");
            //DATA->DATA->COM.CSIS->FILES
            Toast.makeText(this, "Your receipt is generated",
                    Toast.LENGTH_LONG).show();
            fout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void goBack() {
        Loader.dismiss();
        finish();
        Toast.makeText(this, "Your appointment is updated, Check Appointments", Toast.LENGTH_SHORT).show();
    }
}