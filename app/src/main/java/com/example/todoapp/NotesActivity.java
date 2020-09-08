package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;


import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import sharefirebasepreferences.crysxd.de.lib.SharedFirebasePreferences;

public class NotesActivity extends AppCompatActivity implements NotesRecyclerAdapter.NoteListener {

    FirebaseAuth mAuth;
    Button logOutButton;
    FloatingActionButton floatingActionButton;
    ListView listView;
    static ArrayList<String> notes = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;
    //SharedPreferences sharedPreferences;
    SharedFirebasePreferences mPreferences;
    NotesRecyclerAdapter notesRecyclerAdapter;
    RecyclerView recyclerView;

    //Note note = new Note


    @Override
    protected void onStart() {
        super.onStart();
        initRecyclerView(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(notesRecyclerAdapter != null){
            notesRecyclerAdapter.stopListening();
        }
    }

    private void initRecyclerView(FirebaseUser user){

        recyclerView = findViewById(R.id.recyclerView);
        Query query = FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("userId", user.getUid());

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        notesRecyclerAdapter = new NotesRecyclerAdapter(options,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        notesRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(notesRecyclerAdapter);

        notesRecyclerAdapter.startListening();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        //sharedPreferences = getApplicationContext().getSharedPreferences("com.example.todoapp", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        //recyclerView = findViewById(R.id.recyclerView);
        //listView = findViewById(R.id.listView);

        if(mAuth.getCurrentUser() != null){
            initRecyclerView(mAuth.getCurrentUser());
        }


        FloatingActionButton logoutButton = findViewById(R.id.logoutButton);

        //initRecycler(FirebaseAuth.getInstance().getCurrentUser());
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mAuth.signOut();
                new AlertDialog.Builder(NotesActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.signOut();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });


        mAuth = FirebaseAuth.getInstance();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialogue();
                initRecyclerView(mAuth.getCurrentUser());


            }
        });
        /*logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //send to login activity
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });*/
    }

    private void showAlertDialogue() {

        final EditText noteEditText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Add TODO")
                .setView(noteEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AddNote(noteEditText.getText().toString());

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void AddNote(String text) {

        Note note = new Note(text, false, FirebaseAuth.getInstance().getCurrentUser().getUid(),new Timestamp(new Date()));
        FirebaseFirestore.getInstance().collection("notes")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("Messaage", "Notes added Successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NotesActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });





    }

    ItemTouchHelper.SimpleCallback simpleCallback=  new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if(direction == ItemTouchHelper.LEFT){
                    Toast.makeText(NotesActivity.this, "Deleting TODO", Toast.LENGTH_SHORT).show();

                    NotesRecyclerAdapter.NoteViewHolder noteViewHolder = (NotesRecyclerAdapter.NoteViewHolder) viewHolder;
                    noteViewHolder.deleteItem();
                }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(NotesActivity.this, R.color.colorAccent))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();




            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void handleCheckChanged(Boolean isChecked, DocumentSnapshot snapshot) {
        Log.i("MEssage",isChecked.toString());
        snapshot.getReference().update("completed",isChecked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("On Success:","done!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Message",e.getLocalizedMessage());
                    }
                });



    }

    @Override
    public void handleEditNote(final DocumentSnapshot snapshot) {

        final Note note = snapshot.toObject(Note.class);

        final EditText editText = new EditText(this);
        editText.setText(note.getText().toString());
        editText.setSelection(note.getText().length());
        new AlertDialog.Builder(this)
                .setTitle("Edit Todo")
                .setView(editText)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newText = editText.getText().toString();
                        note.setText(newText);
                        snapshot.getReference().set(note)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i("Message","Successfully Updated");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("Message","Update Failed");

                                    }
                                });


                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

    @Override
    public void handleDeleteItem(DocumentSnapshot snapshot) {
        snapshot.getReference().delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       Log.i("Message","Item Deleted");
                    }
                });
    }
}