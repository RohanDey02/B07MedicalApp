 package com.example.b07medicalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

 public class  MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    String list[];

    public MyAdapter(Context context, String list[]) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.listtext1.setText(list[position]);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences p = context.getSharedPreferences("current_user_info", 0);
                boolean isDoctor = p.getBoolean("isDoctor", false);
                Log.i("click", String.valueOf(isDoctor));
                // Checks whether option clicked is valid or not
                if ((!isDoctor) || (list[holder.getAdapterPosition()].split(", ")[1].equals("None"))) {
                    Snackbar snackbar = Snackbar.make(view, R.string.invalid_click_recycler_view, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                // Goes to patient info page
                else {
                    Intent intent = new Intent(context, PatientInfo.class);
                    intent.putExtra("username", list[holder.getAdapterPosition()].split(", ")[1]);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listtext1;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            listtext1 = (TextView) itemView.findViewById(R.id.rowtextView4);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
