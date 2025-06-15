package com.example.doctorappointment;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.*;

import java.util.*;

public class DoctorManagementActivity extends AppCompatActivity {

    EditText editTextDoctorName, editTextSpeciality;
    Button buttonAddDoctor;
    ListView listViewDoctors;
    ArrayAdapter<String> adapter;
    ArrayList<String> doctorList = new ArrayList<>();
    ArrayList<String> docIds = new ArrayList<>();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_management);

        editTextDoctorName = findViewById(R.id.editTextDoctorName);
        editTextSpeciality = findViewById(R.id.editTextSpeciality);
        buttonAddDoctor = findViewById(R.id.buttonAddDoctor);
        listViewDoctors = findViewById(R.id.listViewDoctors);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doctorList);
        listViewDoctors.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        buttonAddDoctor.setOnClickListener(v -> addDoctor());
        listViewDoctors.setOnItemLongClickListener((parent, view, position, id) -> {
            deleteDoctor(docIds.get(position));
            return true;
        });

        loadDoctors();
    }

    private void addDoctor() {
        String name = editTextDoctorName.getText().toString().trim();
        String speciality = editTextSpeciality.getText().toString().trim();

        if (name.isEmpty() || speciality.isEmpty()) {
            Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> doctor = new HashMap<>();
        doctor.put("name", name);
        doctor.put("speciality", speciality);

        db.collection("doctors")
                .add(doctor)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Doctor added", Toast.LENGTH_SHORT).show();
                    editTextDoctorName.setText("");
                    editTextSpeciality.setText("");
                    loadDoctors();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding doctor", Toast.LENGTH_SHORT).show());
    }

    private void loadDoctors() {
        db.collection("doctors")
                .get()
                .addOnSuccessListener(query -> {
                    doctorList.clear();
                    docIds.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        String name = doc.getString("name");
                        String speciality = doc.getString("speciality");
                        doctorList.add(name + " (" + speciality + ")");
                        docIds.add(doc.getId());
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void deleteDoctor(String docId) {
        db.collection("doctors").document(docId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Doctor deleted", Toast.LENGTH_SHORT).show();
                    loadDoctors();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error deleting", Toast.LENGTH_SHORT).show());
    }
}
