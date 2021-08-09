package com.example.b07medicalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.MyViewHolder>{
    Context context;
    String list[];

    public PatientsAdapter(Context context, String list[]) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsAdapter.MyViewHolder holder, int position) {
        holder.myText.setText(list[position]);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView myText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
