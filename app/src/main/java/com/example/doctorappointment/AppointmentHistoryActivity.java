package com.example.doctorappointment;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.*;

public class AppointmentHistoryActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<MyAppointmentModel> myList;
    MyAppointmentAdapter adapter;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        listView = findViewById(R.id.listViewMyAppointments);
        myList = new ArrayList<>();
        adapter = new MyAppointmentAdapter(this, myList);
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loadMyAppointments();
    }

    private void loadMyAppointments() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("appointments")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(query -> {
                    myList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        MyAppointmentModel model = new MyAppointmentModel(
                                doc.getString("doctor"),
                                doc.getString("date"),
                                doc.getString("time"),
                                doc.getString("reason")
                        );
                        myList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
