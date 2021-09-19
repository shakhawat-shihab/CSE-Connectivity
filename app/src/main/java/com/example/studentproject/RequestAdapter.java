package com.example.studentproject;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
   private Context context;
   private List<User> userList;
    private OnItemClickListener listener;

    public RequestAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener,View.OnClickListener {
        TextView TV_Name,TV_Id,TV_Batch;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TV_Name=itemView.findViewById(R.id.name_id);
            TV_Id=itemView.findViewById(R.id.id_id);
            TV_Batch=itemView.findViewById(R.id.batch_id);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Choose an Action");
            MenuItem onAcceptRequest=menu.add(Menu.NONE,1,1,"Accept Request");
            MenuItem onIgnoreRequest=menu.add(Menu.NONE,2,2,"Ignore Request");
            onAcceptRequest.setOnMenuItemClickListener(this);
            onIgnoreRequest.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (listener!=null)
            {
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION)
                {
                    if (item.getItemId()==1)
                    {
                        listener.onAcceptRequest(position);
                        return true;
                    }
                    if (item.getItemId()==2)
                    {
                        listener.onIgnoreRequest(position);
                        return true;
                    }
                }
            }
            return false;
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
        View view=layoutInflater.inflate(R.layout.sample_view_request,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.TV_Name.setText(user.getName());
        holder.TV_Id.setText(user.getId());
        holder.TV_Batch.setText( user.getBatch());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public interface OnItemClickListener
    {
        void onAcceptRequest(int position);
        void onIgnoreRequest(int position);
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener=  listener;
    }

}
