package com.example.doctorappointment;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.*;
import java.util.*;

public class AdminDashboardActivity extends AppCompatActivity {
    ListView listView;
    AppointmentAdapter adapter;
    ArrayList<AppointmentModel> appointmentList;
    //    ListView listView;
//    ArrayAdapter<String> adapter;
//    ArrayList<String> appointmentList;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

//        listView = findViewById(R.id.listViewAppointments);
//        appointmentList = new ArrayList<>();
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentList);
//        listView.setAdapter(adapter);
        listView = findViewById(R.id.listViewAppointments);
        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(this, appointmentList);
        listView.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadAppointments();
        Button buttonManageDoctors = findViewById(R.id.buttonManageDoctors);
        buttonManageDoctors.setOnClickListener(v -> {
            startActivity(new Intent(this, DoctorManagementActivity.class));
        });
        Button buttonLogout = findViewById(R.id.buttonLogout);

        buttonLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });


    }
    private void loadAppointments() {
        db.collection("appointments")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    appointmentList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        AppointmentModel appt = new AppointmentModel(
                                doc.getString("name"),
                                doc.getString("doctor"),
                                doc.getString("date"),
                                doc.getString("time")
                        );
                        appointmentList.add(appt);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
//    private void loadAppointments() {
//        db.collection("appointments")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    appointmentList.clear();
//                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                        String item = "Patient: " + doc.getString("name") +
//                                "\nDoctor: " + doc.getString("doctor") +
//                                "\nDate: " + doc.getString("date") +
//                                "\nTime: " + doc.getString("time");
//                        appointmentList.add(item);
//                    }
//                    adapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(this, "Failed to load: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
}
