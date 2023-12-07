package com.csis.drivedoctor.postAuth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.csis.drivedoctor.Helpers.Loader;
import com.csis.drivedoctor.Helpers.UsersDBHelper;
import com.csis.drivedoctor.preauthDatabase.TaskCompletion;
import com.csis.drivedoctor.R;
import com.csis.drivedoctor.model.CarsModel;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;

public class AddCarActivity extends AppCompatActivity {
    EditText editTextCarName;
    EditText editTextNumberPlate;
    Spinner companySpinner;
    Spinner yearSpinner;
    Button buttonaddCar;
    UsersDBHelper carDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        carDB = new UsersDBHelper(this);

        editTextCarName = findViewById(R.id.id_addCar_CarName_EditView);
        editTextNumberPlate = findViewById(R.id.id_addCar_PlateNumber_EditView);
        companySpinner = findViewById(R.id.id_addcar_selectCompanySpinner);
        yearSpinner = findViewById(R.id.id_addcar_selectyear_spinner);
        buttonaddCar = findViewById(R.id.idButtonAddCar);
        companySpinner.setSelection(0);
        yearSpinner.setSelection(0);

        buttonaddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCarAction ();
            }
        });


    }
    void addCarAction () {
        validate();
    }

    private  void validate() {
        String company = companySpinner.getSelectedItem().toString();
        String year = yearSpinner.getSelectedItem().toString();

        if (company.equals("")) {
            Toast.makeText(this, "Please select car company", Toast.LENGTH_SHORT).show();
        } else if (year.equals("")) {
            Toast.makeText(this, "Please select year of manufacturing", Toast.LENGTH_SHORT).show();
        } else if (editTextCarName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter car name.", Toast.LENGTH_SHORT).show();
        } else if (editTextNumberPlate.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter car plate name.", Toast.LENGTH_SHORT).show();
        } else {

            CarsModel newcar = new CarsModel(
                    company,
                    editTextNumberPlate.getText().toString().trim(),
                    editTextCarName.getText().toString().trim(),
                    year
            );
            Loader.show(this);
            carDB.addNewCar(newcar);
            saveData(newcar);
        }
    }

    private void saveData(CarsModel car) {
        DatabaseHelper.getInstance().saveNewCar(car, new TaskCompletion() {
            @Override
            public void taskCompletion(boolean isSuccess) {
                Loader.dismiss();
                if (isSuccess) {
                    finish();
                } else {
                    Toast.makeText(AddCarActivity.this,"", Toast.LENGTH_LONG ).show();
                }
            }
        });
    }
}
