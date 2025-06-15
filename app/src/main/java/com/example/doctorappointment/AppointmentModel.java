package com.example.doctorappointment;

public class AppointmentModel {
    public String name, doctor, date, time;

    public AppointmentModel() {} // Required for Firebase

    public AppointmentModel(String name, String doctor, String date, String time) {
        this.name = name;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }
}
