package com.nitish.notesapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nitish.notesapp.storage.NotesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<NotesModel> arrNotes;
    private OnItemClickListener onItemClickListener;
    private OnLongClickListener onLongClickListener;

    // ... other methods in your adapter

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnLongClickListener{
        void onItemLongClick(int position,Context context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnLongClickListener(OnLongClickListener listener){
        this.onLongClickListener = listener;
    }

    public  NotesRecyclerAdapter(Context context,ArrayList<NotesModel> arrNotes){
        this.context = context;
        this.arrNotes = (List)(arrNotes);
    }

    public void setSearchFilterArray(ArrayList<NotesModel> filterArray){
        this.arrNotes = (List)(filterArray);
        notifyDataSetChanged();
    }

    @Override
    public NotesRecyclerAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_notes,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( NotesRecyclerAdapter.ViewHolder holder, int position) {
       holder.bindData(arrNotes.get(position));
    }

    @Override
    public int getItemCount() {
        return arrNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView heading,description;
        View colorDot;
        public ViewHolder( View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.tvNoteHeading);
            description = itemView.findViewById(R.id.tvNoteDescription);
            colorDot = itemView.findViewById(R.id.dot);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        int id = arrNotes.get(position).getId();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(id);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onLongClickListener != null){
                        int position = getAdapterPosition();
                        int id = arrNotes.get(position).getId();
                        Context context = v.getContext();
                        if (position!= RecyclerView.NO_POSITION){
                            onLongClickListener.onItemLongClick(id,context);
                        }
                    }
                    return false;
                }
            });

        }

        public void bindData(NotesModel notesModel){
            if (notesModel != null){
                heading.setText(notesModel.getHeading());
                description.setText(notesModel.getDescription());
// Random color set
                try {
                    setRandomColor();
                    colorDot.setBackground(getDrawableDot());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        public GradientDrawable drawableDot;
        public GradientDrawable getDrawableDot(){
            return drawableDot;
        }

        public void setRandomColor(){
            Random random = new Random();
            int colorDot = Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));

            drawableDot = new GradientDrawable();
            drawableDot.setShape(GradientDrawable.OVAL);
            drawableDot.setColor(colorDot);
        }

    }

}
