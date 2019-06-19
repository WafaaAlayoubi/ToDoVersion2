package info.androidhive.sqlite;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Note;
import info.androidhive.sqlite.utils.MyDividerItemDecoration;
import info.androidhive.sqlite.utils.RecyclerTouchListener;
import info.androidhive.sqlite.view.NotesAdapter;

public class Main3Activity extends AppCompatActivity {

    ImageView imgHome;
    ImageView imgGrid;
    ImageView bell;
    ImageView finish;
    Fragment homeFragment;
    Fragment gridFragment;
    Fragment tasksFragment;

    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
  //  private TextView noNotesView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        imgHome = (ImageView) findViewById(R.id.home);
        imgGrid = (ImageView) findViewById(R.id.grid);


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.getNotesCount() > 0) {
                    tasksFragment = new Fragment_List();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment,tasksFragment);
                    ft.commit();
                } else {
                    tasksFragment = new Fragment_NoTasks();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment,tasksFragment);
                    ft.commit();
                }


            }
        });

        imgGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridFragment = new Fragment_Grid();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment,gridFragment);
                ft.commit();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);


        db = new DatabaseHelper(this);

        notesList.addAll(db.getAllNotes());

        ImageView fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteDialog(false, null, -1);

            }
        });

        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

               bell = (ImageView)view.findViewById(R.id.bell);
               finish = (ImageView)view.findViewById(R.id.finsih);


                finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Note n = notesList.get(position);

                        if(n.getFinish().equals("0")) {
                            // updating note text
                            n.setFinish("1");

                            // updating note in db
                            db.updateFinish("1", n);

                            // refreshing the list
                            notesList.set(position, n);
                            mAdapter.notifyItemChanged(position);
                            toggleEmptyNotes();

                        }else if(n.getFinish().equals("1")) {
                            // updating note text
                            n.setFinish("0");

                            // updating note in db
                            db.updateFinish("0", n);

                            // refreshing the list
                            notesList.set(position, n);
                            mAdapter.notifyItemChanged(position);
                            toggleEmptyNotes();

                        }



                    }
                });

                bell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Note n = notesList.get(position);

                        if(n.getAlert().equals("0")) {
                            // updating note text
                            n.setAlert("1");

                            // updating note in db
                           db.updateAlert("1", n);

                            // refreshing the list
                            notesList.set(position, n);
                            mAdapter.notifyItemChanged(position);
                            toggleEmptyNotes();

                        }else if(n.getAlert().equals("1")) {
                            // updating note text
                            n.setAlert("0");

                            // updating note in db
                            db.updateAlert("0", n);

                            // refreshing the list
                            notesList.set(position, n);
                            mAdapter.notifyItemChanged(position);
                            toggleEmptyNotes();

                        }



                    }
                });


            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }


    /**
     * Inserting new note in db
     * and refreshing the list
     */
    private void createNote(String[] note) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(note);

        // get the newly inserted note from db
        Note n = db.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
    }

    /**
     * Updating note in db and updating
     * item in the list by its position
     */
    private void updateNote(String[] note,String note1, int position) {
        Note n = notesList.get(position);
        // updating note text
        n.setNote(note1);
        n.setCategory(note[1]);
        n.setAlert(note[2]);
        n.setFinish(note[3]);
        n.setDate(note[4]);
        n.setTimestart(note[5]);
        n.setTimeend(note[6]);

        // updating note in db
        db.updateNote(note,n);

        // refreshing the list
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    private void deleteNote(int position) {
        // deleting the note from db
        db.deleteNote(notesList.get(position));

        // removing the note from the list
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, notesList.get(position), position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        final ImageView btnDatePicker, btnTimePicker ,btnTimePicker2;
        final  TextView txtDate, txtTime,txtTime2;


        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Main3Activity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);


        // very imppoooooooooortant

//        TextView dialogTitle = view.findViewById(R.id.dialog_title);
//        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        btnDatePicker=view.findViewById(R.id.btn_date);
        btnTimePicker=view.findViewById(R.id.btn_time);
        btnTimePicker2=view.findViewById(R.id.btn_time3);
        txtDate=view.findViewById(R.id.in_date);
        txtTime=view.findViewById(R.id.in_time);
        txtTime2=view.findViewById(R.id.in_time3);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Main3Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Main3Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        btnTimePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Main3Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime2.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        if (shouldUpdate && note != null) {
            inputNote.setText(note.getNote());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(Main3Activity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(txtDate.getText().toString())) {
                    Toast.makeText(Main3Activity.this, "Enter date!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(txtTime.getText().toString())) {
                    Toast.makeText(Main3Activity.this, "Enter start time!", Toast.LENGTH_SHORT).show();
                    return;
                }  else if (TextUtils.isEmpty(txtTime2.getText().toString())) {
                    Toast.makeText(Main3Activity.this, "Enter end time!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    String[] note = new String[7];
                    note[0] = inputNote.getText().toString();
                    note[1] = "personal";
                    note[4] = txtDate.getText().toString();
                    note[5] = txtTime.getText().toString();
                    note[6] = txtTime2.getText().toString();
                    updateNote(note,inputNote.getText().toString(), position);
                } else {
                    // create new note
                    String[] note = new String[7];
                    note[0] = inputNote.getText().toString();
                    note[1] = "personal";
                    note[4] = txtDate.getText().toString();
                    note[5] = txtTime.getText().toString();
                    note[6] = txtTime2.getText().toString();

                    createNote(note);
                }
            }
        });


    }

    /**
     * Toggling list and empty notes view
     */
    public void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getNotesCount() > 0) {
            tasksFragment = new Fragment_List();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment,tasksFragment);
            ft.commit();
        } else {
            tasksFragment = new Fragment_NoTasks();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment,tasksFragment);
            ft.commit();
        }
    }

//    public void bellon(View view){
//        ImageView bell = (ImageView)view.findViewById(R.id.bell);
//        bell.setImageResource(R.drawable.smallbellon);
//
//    }
}

