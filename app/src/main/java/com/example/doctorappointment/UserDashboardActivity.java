package com.example.doctorappointment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class UserDashboardActivity extends AppCompatActivity {
    Spinner spinnerDoctor;
    String selectedDoctor = "";

    EditText editTextName, editTextDoctor, editTextDate, editTextTime, editTextReason;
    Button buttonBook;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        editTextName = findViewById(R.id.editTextName);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextReason = findViewById(R.id.editTextReason);
        buttonBook = findViewById(R.id.buttonBook);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonBook.setOnClickListener(v -> bookAppointment());
        Button buttonLogout = findViewById(R.id.buttonLogout);
        Button buttonHistory = findViewById(R.id.buttonHistory);

        buttonLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        buttonHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, AppointmentHistoryActivity.class));
        });
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        ArrayList<String> doctorList = new ArrayList<>();
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        //ArrayList<String> doctorList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctorList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctor.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("doctors")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    doctorList.clear();
                    doctorList.add("Select Doctor");
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String doctorName = doc.getString("name");
                        String specialty = doc.getString("speciality");
                        doctorList.add(doctorName + " (" + specialty + ")");
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load doctors", Toast.LENGTH_SHORT).show()
                );

        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDoctor = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

//        ArrayAdapter<String> doctorAdapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_spinner_item,
//                new String[]{"Select Doctor", "Dr. Ahsan (Cardiologist)", "Dr. Mim (Skin)", "Dr. Rifat (Ortho)"}
//        );
//
//        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDoctor.setAdapter(doctorAdapter);
//
//        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedDoctor = parent.getItemAtPosition(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                selectedDoctor = "";
//            }
//        });

        editTextDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n") DatePickerDialog datePicker = new DatePickerDialog(this,
                    (view, year1, month1, dayOfMonth) ->
                            editTextDate.setText(year1 + "-" + (month1 + 1) + "-" + dayOfMonth),
                    year, month, day);
            datePicker.show();
        });

        editTextTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            @SuppressLint({"SetTextI18n", "DefaultLocale"}) TimePickerDialog timePicker = new TimePickerDialog(this,
                    (view, hourOfDay, minute1) -> {
                        String amPm = hourOfDay >= 12 ? "PM" : "AM";
                        int displayHour = hourOfDay % 12;
                        if (displayHour == 0) displayHour = 12;
                        editTextTime.setText(displayHour + ":" + String.format("%02d", minute1) + " " + amPm);
                    }, hour, minute, false);
            timePicker.show();
        });

    }

    private void bookAppointment() {
        String name = editTextName.getText().toString();
       // String doctor = editTextDoctor.getText().toString();
        String date = editTextDate.getText().toString();
        String time = editTextTime.getText().toString();
        String reason = editTextReason.getText().toString();
        if (selectedDoctor.equals("Select Doctor") || selectedDoctor.isEmpty()) {
            Toast.makeText(this, "Please select a doctor", Toast.LENGTH_SHORT).show();
            return;
        }
        String doctor = selectedDoctor;


        if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("name", name);
        appointment.put("doctor", doctor);
        appointment.put("date", date);
        appointment.put("time", time);
        appointment.put("reason", reason);
        appointment.put("uid", uid);

        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        editTextName.setText("");
        editTextDoctor.setText("");
        editTextDate.setText("");
        editTextTime.setText("");
        editTextReason.setText("");
    }
}
