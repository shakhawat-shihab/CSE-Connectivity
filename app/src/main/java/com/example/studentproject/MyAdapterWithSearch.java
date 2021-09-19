package com.example.studentproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyAdapterWithSearch extends RecyclerView.Adapter<MyAdapterWithSearch.MyViewHolder> implements Filterable {
    private Context context;
    private int i;
    private List<User> userList;
    private List<User>userListFull;
    private MyAdapter.OnItemClickListener listener;

    public MyAdapterWithSearch(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        userListFull=new ArrayList<>(userList);
    }

    public MyAdapterWithSearch(java.util.List<User> userList,int i) {
        this.userList = userList;
        userListFull=new ArrayList<>(userList);
        this.i=i;
    }
    public void setSelect(int i)
    {
        this.i=i;
    }
    /*public MyAdapterWithSearch(int i) {
        this.i = i;
    }*/

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
    public void onBindViewHolder(@NonNull MyAdapterWithSearch.MyViewHolder holder, int position) {
        User user=userList.get(position);
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
        return userList.size();
    }
    public void setOnItemClickListener(MyAdapter.OnItemClickListener listener)
    {
        this.listener=listener;

    }


    @Override
    public Filter getFilter() {
        return listFilter;
    }
    private Filter listFilter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User>filteredList=new ArrayList<>();
            if (constraint==null||constraint.length()==0)
            {
                filteredList.addAll(userListFull);
            }
            else
            {
                String filterPattern=constraint.toString().toLowerCase().trim();
                for (User user:userListFull)
                {
                    if (i==1)
                    {
                        if (user.getId().toLowerCase().contains(filterPattern))
                        {
                            filteredList.add(user);
                        }
                    }
                    if (i==2)
                    {
                        if (user.getBlood().toLowerCase().contains(filterPattern))
                        {
                            filteredList.add(user);
                        }
                    }
                    if (i==3)
                    {
                        if (user.getName().toLowerCase().contains(filterPattern))
                        {
                            filteredList.add(user);
                        }
                    }
                    if (i==4)
                    {
                        if (user.getMobile().toLowerCase().contains(filterPattern))
                        {
                            filteredList.add(user);
                        }
                    }
                    if (i==5)
                    {
                        if (user.getHome().toLowerCase().contains(filterPattern))
                        {
                            filteredList.add(user);
                        }
                    }



                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList.clear();
            userList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}

