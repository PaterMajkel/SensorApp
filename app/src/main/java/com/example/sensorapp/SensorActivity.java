package com.example.sensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;

    private RecyclerView recyclerView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (Sensor sensor : sensorList) {
            Log.d("APKA_BOZA", "Name: " + sensor.getName() + "   " + "Vendor: " + sensor.getVendor() + "   " + "Max range: " + sensor.getMaximumRange());
        }

        if (adapter == null) {
            adapter = new Adapter(sensorList);
            recyclerView.setAdapter(adapter);
        }
        else
            adapter.notifyDataSetChanged();
    }

    private class Adapter extends RecyclerView.Adapter<Holder> {
        private final List<Sensor> sensorList;

        public Adapter(List<Sensor> sensorList) {
            this.sensorList = sensorList;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflate = LayoutInflater.from(parent.getContext());
            return new Holder(inflate, parent);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensorList.size();
        }
    }

    private class Holder extends RecyclerView.ViewHolder {

        private final TextView sensorNameTextView;
        private final TextView sensorTypeTextView;
        private final ImageView sensorImageView;

        public Holder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            sensorNameTextView = itemView.findViewById(R.id.sensorNameText);
            sensorImageView = itemView.findViewById(R.id.sensorImage);
            sensorTypeTextView = itemView.findViewById(R.id.sensorTypeText);
        }

        public void bind(Sensor sensor) {
            sensorNameTextView.setText(sensor.getName());
            sensorImageView.setImageResource(R.drawable.acceletrometre_foreground);
            sensorTypeTextView.setText(String.valueOf(sensor.getType()));
            View itemContainer = itemView.findViewById(R.id.list_item_sensor);
            itemContainer.setBackgroundColor(Color.WHITE);
            itemContainer.setClickable(false);
            int sensorType = sensor.getType();
            if (sensorType == Sensor.TYPE_ROTATION_VECTOR || sensorType == Sensor.TYPE_LIGHT) {
                itemContainer.setBackgroundColor(Color.CYAN);
                itemContainer.setOnClickListener(v -> {
                    Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                    intent.putExtra("sensorDetails", sensor.getType());
                    startActivityForResult(intent, 0);
                });
            }
        }
    }
}

