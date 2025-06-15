package com.example.doctorappointment;
import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MyAppointmentAdapter extends ArrayAdapter<MyAppointmentModel> {
    Context context;
    List<MyAppointmentModel> list;

    public MyAppointmentAdapter(Context context, List<MyAppointmentModel> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyAppointmentModel appt = list.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_appointment_history, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.tvDoctor)).setText("Doctor: " + appt.doctor);
        ((TextView) convertView.findViewById(R.id.tvDate)).setText("Date: " + appt.date);
        ((TextView) convertView.findViewById(R.id.tvTime)).setText("Time: " + appt.time);
        ((TextView) convertView.findViewById(R.id.tvReason)).setText("Reason: " + appt.reason);

        return convertView;
    }
}
