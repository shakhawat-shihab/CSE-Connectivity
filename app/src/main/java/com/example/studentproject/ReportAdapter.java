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

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    private Context context;
    private List<Report> reportList;
    private OnItemClickListener listener;

    public ReportAdapter(Context context, List<Report>reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener,View.OnClickListener {
        TextView TV_Name,TV_Subject;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TV_Name=itemView.findViewById(R.id.subject_id);
            TV_Subject=itemView.findViewById(R.id.report_by_name_id);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Choose an Action");
            MenuItem onDelete=menu.add(Menu.NONE,1,1,"Delete");
            onDelete.setOnMenuItemClickListener(this);
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
                        listener.onDelete(position);
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
        View view=layoutInflater.inflate(R.layout.sample_view_report,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.TV_Name.setText( report.getSubject());
        holder.TV_Subject.setText(report.getReportBy_name());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
    public interface OnItemClickListener
    {
        void onDelete(int position);
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener=  listener;
    }

}

