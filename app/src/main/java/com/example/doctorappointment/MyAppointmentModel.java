package com.example.doctorappointment;
public class MyAppointmentModel {
    public String doctor, date, time, reason;

    public MyAppointmentModel() {}

    public MyAppointmentModel(String doctor, String date, String time, String reason) {
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.reason = reason;
    }
}
