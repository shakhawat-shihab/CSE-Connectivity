package com.example.studentproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private java.util.List<User> List;
    private OnItemClickListener listener;

    public MyAdapter(Context context, java.util.List<User> list) {
        this.context = context;
        List = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView I;
        TextView T;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            I=itemView.findViewById(R.id.sample_image);
            T=itemView.findViewById(R.id.sample_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            if (position!=RecyclerView.NO_POSITION)
            {
                listener.onItemClick(position);
            }

        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.sample_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user=List.get(position);
        String imageUrl=user.getImageUrl();
        if (imageUrl.equals("0"))
        {
            holder.I.setImageResource(R.drawable.guest_red);
        }
        else if (imageUrl.equals("1"))
        {
            holder.I.setImageResource(R.drawable.guest_blue);
        }
        else if (imageUrl.equals("2"))
        {
            holder.I.setImageResource(R.drawable.guest_yellow);
        }
        else if (imageUrl.equals("3"))
        {
            holder.I.setImageResource(R.drawable.guest_green);
        }
        else
        {
            Picasso.get().load(imageUrl).placeholder(R.drawable.default_pic).into(holder.I);

        }
        holder.T.setText("Id: "+user.getId());
    }

    @Override
    public int getItemCount() {
        return List.size();
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener= listener;

    }



}

