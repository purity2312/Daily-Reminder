package com.dailyreminder.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Task_RecyclerViewAdapter extends RecyclerView.Adapter<Task_RecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<TaskModel> taskModels;

    public Task_RecyclerViewAdapter(Context context, ArrayList<TaskModel> taskModels){
        this.context = context;
        this.taskModels = taskModels;
    }
    @NonNull
    @Override
    public Task_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.recycler_view_row, parent, false);
        return new Task_RecyclerViewAdapter.MyViewHolder(view, context, taskModels);
    }

    @Override
    public void onBindViewHolder(@NonNull Task_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // set values to each of the recycler view row
        holder.tvName.setText(taskModels.get(position).getTaskName());
        holder.tvNote.setText(taskModels.get(position).getTaskNote());
        holder.tvDate.setText(taskModels.get(position).getDate());
        holder.tvTime.setText(taskModels.get(position).getTime());
        holder.tvId.setText(String.valueOf(taskModels.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        // return number of items to be displayed
        return taskModels.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView tvName, tvNote, tvDate, tvTime, tvId;
        ImageButton ibMenu;
        Context context;
        ArrayList<TaskModel> taskModels;

        public MyViewHolder(@NonNull View itemView, Context context, ArrayList<TaskModel> taskModels) {
            super(itemView);
            this.context = context;
            this.taskModels = taskModels;

            tvName = itemView.findViewById(R.id.nameView);
            tvNote = itemView.findViewById(R.id.noteView);
            tvDate = itemView.findViewById(R.id.dateView);
            tvTime = itemView.findViewById(R.id.timeView);
            tvId = itemView.findViewById(R.id.idView);
            ibMenu = itemView.findViewById(R.id.menuButton);
            ibMenu.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }

        private void showPopupMenu(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int itemId = item.getItemId();
            try (DBHandler db = new DBHandler(context)) {
                TaskModel task = taskModels.get(getAdapterPosition());
                if (itemId == R.id.action_popup_edit) {
                    // Create an Intent to open the EditTaskActivity
                    Intent intent = new Intent(context, EditTask.class);
                    intent.putExtra("task_id", String.valueOf(task.getId()));
                    intent.putExtra("task_name", task.getTaskName());
                    intent.putExtra("task_note", task.getTaskNote());
                    intent.putExtra("task_date", task.getDate());
                    intent.putExtra("task_time", task.getTime());
                    context.startActivity(intent);
                    return true;
                } else if (itemId == R.id.action_popup_delete) {
                    db.deleteTask(String.valueOf(task.getId()));
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    return true;

                }
            }

            return false;
        }

    }


}
