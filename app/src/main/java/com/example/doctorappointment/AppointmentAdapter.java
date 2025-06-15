package com.example.doctorappointment;
import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.*;

public class AppointmentAdapter extends ArrayAdapter<AppointmentModel> {
    Context context;
    List<AppointmentModel> list;

    public AppointmentAdapter(Context context, List<AppointmentModel> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppointmentModel appt = list.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        }

        TextView tvPatient = convertView.findViewById(R.id.tvPatient);
        TextView tvDoctor = convertView.findViewById(R.id.tvDoctor);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvTime = convertView.findViewById(R.id.tvTime);

        tvPatient.setText("Patient: " + appt.name);
        tvDoctor.setText("Doctor: " + appt.doctor);
        tvDate.setText("Date: " + appt.date);
        tvTime.setText("Time: " + appt.time);

        return convertView;
    }
}
