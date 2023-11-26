package com.phuoc.mynotemvvm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.phuoc.mynotemvvm.databinding.EachRvBinding;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    private static final DiffUtil.ItemCallback<Note> CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDisp().equals(newItem.getDisp());
        }
    };

    public NoteAdapter() {
        super(CALLBACK);

    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_rv, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        holder.binding.titleRv.setText(note.getTitle());
        holder.binding.dispRv.setText(note.getDisp());
    }

    public Note getNote(int position) {
        return getItem(position);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private EachRvBinding binding;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = EachRvBinding.bind(itemView);
        }
    }
}
