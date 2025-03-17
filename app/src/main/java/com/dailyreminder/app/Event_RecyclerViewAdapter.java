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
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Event_RecyclerViewAdapter extends RecyclerView.Adapter<Event_RecyclerViewAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<EventModel> eventModels;

    public Event_RecyclerViewAdapter(Context context, ArrayList<EventModel> eventModels){
        this.context = context;
        this.eventModels = eventModels;
    }
    @NonNull
    @Override
    public Event_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.recycler_view_row, parent, false);
        return new Event_RecyclerViewAdapter.MyViewHolder(view, context, eventModels);
    }

    @Override
    public void onBindViewHolder(@NonNull Event_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // set values to each of the recycler view row
        holder.tvName.setText(eventModels.get(position).getEventName());
        holder.tvNote.setText(eventModels.get(position).getEventNote());
        holder.tvDate.setText(eventModels.get(position).getDate());
        holder.tvTime.setText(eventModels.get(position).getTime());
        holder.scNotification.setChecked(eventModels.get(position).isNotification());
        holder.tvId.setText(String.valueOf(eventModels.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        // return number of items to be displayed
        return eventModels.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView tvName, tvNote, tvDate, tvTime, tvId;
        SwitchCompat scNotification;
        ImageButton ibMenu;
        Context context;
        ArrayList<EventModel> eventModels;

        public MyViewHolder(@NonNull View itemView, Context context, ArrayList<EventModel> eventModels) {
            super(itemView);
            this.context = context;
            this.eventModels = eventModels;

            tvName = itemView.findViewById(R.id.nameView);
            tvNote = itemView.findViewById(R.id.noteView);
            tvDate = itemView.findViewById(R.id.dateView);
            tvTime = itemView.findViewById(R.id.timeView);
            scNotification = itemView.findViewById(R.id.notificationSwitch);
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
            DBHandler db = new DBHandler(context);
            EventModel event = eventModels.get(getAdapterPosition());
            if (itemId == R.id.action_popup_edit){
                // Create an Intent to open the EditEventActivity
                Intent intent = new Intent(context, EditEvent.class);
                intent.putExtra("event_id", String.valueOf(event.getId()));
                intent.putExtra("event_name", event.getEventName());
                intent.putExtra("event_note", event.getEventNote());
                intent.putExtra("event_date", event.getDate());
                intent.putExtra("event_time", event.getTime());
                intent.putExtra("event_notification", event.isNotification());
                context.startActivity(intent);
                return true;
            } else if (itemId == R.id.action_popup_delete) {
                db.deleteEvent(String.valueOf(event.getId()));
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                return true;

            }

            return false;
        }

    }


}
