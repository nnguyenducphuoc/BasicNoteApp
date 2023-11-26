package com.phuoc.mynotemvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.phuoc.mynotemvvm.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    NoteAdapter noteAdapter;
    private  NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                ).get(NoteViewModel.class);
        noteAdapter = new NoteAdapter();

        binding.recyclerview.setAdapter(noteAdapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setHasFixedSize(true);
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DataInsertActivity.class);
                intent.putExtra("type", "addMode");
                startActivityForResult(intent, 1);
            }
        });

        noteViewModel.getAllNote().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.submitList(notes);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction==ItemTouchHelper.RIGHT) {
                    Intent intent = new Intent(MainActivity.this, DataInsertActivity.class);
                    intent.putExtra("type", "update");
                    intent.putExtra("title", noteAdapter.getNote(viewHolder.getAdapterPosition()).getTitle());
                    intent.putExtra("disp", noteAdapter.getNote(viewHolder.getAdapterPosition()).getDisp());
                    intent.putExtra("id", noteAdapter.getNote(viewHolder.getAdapterPosition()).getId());
                    startActivityForResult(intent, 2);
                } else if (direction==ItemTouchHelper.LEFT) {
                    noteViewModel.delete(noteAdapter.getNote(viewHolder.getAdapterPosition()));
                    Toast.makeText(MainActivity.this, "note deleted", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(binding.recyclerview);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String disp = data.getStringExtra("disp");
            Note note = new Note(title, disp);

            noteViewModel.insert(note);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String disp = data.getStringExtra("disp");
            Note note = new Note(title, disp);
            note.setId(data.getIntExtra("id", 0));
            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Add Note Failed", Toast.LENGTH_SHORT).show();

        } else if (requestCode == 2 && resultCode == RESULT_CANCELED) {
            noteViewModel.getAllNote().observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    noteAdapter.submitList(notes);
                    noteAdapter.notifyDataSetChanged();
                }
            });
            Toast.makeText(this, "Update Note Failed", Toast.LENGTH_SHORT).show();
        }
    }
}