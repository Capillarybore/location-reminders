package com.example.realshitmaybe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Task> taskList;
    private Context mContext;
    private OnNoteListener mOnNoteListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView taskName, taskTime, taskLocation;
        RelativeLayout parentLayout;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            taskName = itemView.findViewById(R.id.task_name);
            taskTime = itemView.findViewById(R.id.task_time);
            taskLocation = itemView.findViewById(R.id.task_location);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public RecyclerViewAdapter(Context context, List<Task> taskList, OnNoteListener onNoteListener) {
        this.taskList = taskList;
        this.mContext = context;
        this.mOnNoteListener = onNoteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnNoteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Task task = taskList.get(position);
        holder.taskName.setText(task.taskN);
        holder.taskTime.setText(task.dateTime);
        holder.taskLocation.setText(task.latitude+", "+task.longitude);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void removeTask(int position){
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreTask(Task task, int position){
        taskList.add(position,task);
        notifyItemInserted(position);
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}