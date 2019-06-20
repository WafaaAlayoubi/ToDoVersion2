package info.androidhive.sqlite;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Note;
import info.androidhive.sqlite.utils.MyDividerItemDecoration;
import info.androidhive.sqlite.utils.RecyclerTouchListener;
import info.androidhive.sqlite.view.Fragment_ListGrid;
import info.androidhive.sqlite.view.NotesAdapter;

public class Main3Activity extends AppCompatActivity {

    ImageView imgHome;
    ImageView imgGrid;

    ImageView personal;

    ImageView bell;
    ImageView finish;
    Fragment tasksFragment;
    ImageView personalon;
    ImageView workon;
    ImageView meetingon;
    ImageView shoppingon;
    ImageView partyon;
    ImageView studyon;
    ImageView dialog_title;
    ImageView dot;

    boolean inGrid,timeTrue ;


    String category = "personal" ;

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

        inGrid = false;


        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inGrid = false;
                timeTrue = true;
                imgGrid.setImageResource(R.drawable.grid);
                imgHome.setImageResource(R.drawable.home);

                db = new DatabaseHelper(Main3Activity.this);

                notesList.clear();

                notesList.addAll(db.getAllNotes("all"));
                mAdapter = new NotesAdapter(Main3Activity.this, notesList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new MyDividerItemDecoration(Main3Activity.this, LinearLayoutManager.VERTICAL, 16));
                recyclerView.setAdapter(mAdapter);

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
                inGrid = false;
                imgHome.setImageResource(R.drawable.home2);
                imgGrid.setImageResource(R.drawable.grid2);
                tasksFragment = new Fragment_Grid();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment,tasksFragment);
                ft.commit();
            }
        });


        recyclerView = findViewById(R.id.recycler_view);


        db = new DatabaseHelper(this);

        notesList.addAll(db.getAllNotes("all"));

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
        n.setDate(note[4]);
        n.setTimestart(note[5]);
        n.setTimeend(note[6]);
        n.setDateend(note[7]);

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
        final ImageView  btnTimePicker2;
        final  TextView txtDate,txtDate2, txtTime,txtTime2;


        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Main3Activity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);


        // very imppoooooooooortant

//        TextView dialogTitle = view.findViewById(R.id.dialog_title);
//        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        personalon = (ImageView) view.findViewById(R.id.personal);
        workon = (ImageView) view.findViewById(R.id.work);
        meetingon = (ImageView) view.findViewById(R.id.meeting);
        shoppingon = (ImageView) view.findViewById(R.id.shopping);
        partyon = (ImageView) view.findViewById(R.id.party);
        studyon = (ImageView) view.findViewById(R.id.study);

        personalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalon.setImageResource(R.drawable.personalon);
                workon.setImageResource(R.drawable.work);
                meetingon.setImageResource(R.drawable.meeting);
                shoppingon.setImageResource(R.drawable.shopping);
                partyon.setImageResource(R.drawable.party);
                studyon.setImageResource(R.drawable.study);
                category = "personal";
            }
        });

        workon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workon.setImageResource(R.drawable.workon);
                personalon.setImageResource(R.drawable.personal);
                meetingon.setImageResource(R.drawable.meeting);
                shoppingon.setImageResource(R.drawable.shopping);
                partyon.setImageResource(R.drawable.party);
                studyon.setImageResource(R.drawable.study);
                category = "work";
            }
        });

        meetingon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetingon.setImageResource(R.drawable.meetingon);
                workon.setImageResource(R.drawable.work);
                personalon.setImageResource(R.drawable.personal);
                shoppingon.setImageResource(R.drawable.shopping);
                partyon.setImageResource(R.drawable.party);
                studyon.setImageResource(R.drawable.study);
                category = "meeting";
            }
        });

        shoppingon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingon.setImageResource(R.drawable.shoppingon);
                workon.setImageResource(R.drawable.work);
                meetingon.setImageResource(R.drawable.meeting);
                personalon.setImageResource(R.drawable.personal);
                partyon.setImageResource(R.drawable.party);
                studyon.setImageResource(R.drawable.study);
                category = "shopping";
            }
        });

        partyon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partyon.setImageResource(R.drawable.partyon);
                workon.setImageResource(R.drawable.work);
                meetingon.setImageResource(R.drawable.meeting);
                shoppingon.setImageResource(R.drawable.shopping);
                personalon.setImageResource(R.drawable.personal);
                studyon.setImageResource(R.drawable.study);
                category = "party";
            }
        });

        studyon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studyon.setImageResource(R.drawable.studyon);
                workon.setImageResource(R.drawable.work);
                meetingon.setImageResource(R.drawable.meeting);
                shoppingon.setImageResource(R.drawable.shopping);
                partyon.setImageResource(R.drawable.party);
                personalon.setImageResource(R.drawable.personal);
                category = "study";
            }
        });


        txtDate=view.findViewById(R.id.in_date);
        txtTime=view.findViewById(R.id.in_time);
        txtTime2=view.findViewById(R.id.in_time3);
        txtDate2=view.findViewById(R.id.in_dateend);


        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                txtTime2.setText("Time End");
                txtDate2.setText("Date End");


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
        txtDate2.setOnClickListener(new View.OnClickListener() {
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

                                try {

                                    String sDate1= txtDate.getText().toString();
                                    Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(sDate1);
                                    String sDate2= dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    Date date2=new SimpleDateFormat("dd-MM-yyyy").parse(sDate2);

                                    if(date1.compareTo(date2) > 0){
                                        Toast.makeText(Main3Activity.this, "Date1 is after Date2", Toast.LENGTH_LONG).show();
                                    }
                                    else if (date1.compareTo(date2) == 0){
                                        timeTrue = false;
                                        txtDate2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    }
                                    else {
                                        timeTrue = true;
                                        txtDate2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    }

                                } catch (ParseException e) {              // Insert this block.
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                txtTime2.setText("Time End");
                txtDate2.setText("Date End");

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Main3Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);

                    Intent alertIntent = new Intent(getApplicationContext(), AlertReceiver.class);
                    AlarmManager alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );

                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), PendingIntent.getBroadcast(getApplicationContext(), 0, alertIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT ));

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        txtTime2.setOnClickListener(new View.OnClickListener() {
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
                                if (timeTrue) {
                                    txtTime2.setText(hourOfDay + ":" + minute);
                                }
                                else {
                                    String string1 = txtTime.getText().toString();
                                    String[] parts1 = string1.split(":");
                                    int hour = Integer.parseInt(parts1[0]);
                                    int minute1 = Integer.parseInt(parts1[1]);
                                    if(hour > hourOfDay){
                                        Toast.makeText(Main3Activity.this, "Time end befor Time start!", Toast.LENGTH_LONG).show();

                                    }else if (hour == hourOfDay){
                                        if(minute1 < minute){
                                            timeTrue = true;
                                            txtTime2.setText(hourOfDay + ":" + minute);
                                        }
                                        else{
                                            Toast.makeText(Main3Activity.this, "Time end should be after Time start!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else{
                                        timeTrue = true;
                                        txtTime2.setText(hourOfDay + ":" + minute);
                                    }

                                }
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        if (shouldUpdate && note != null) {

            dialog_title = (ImageView) view.findViewById(R.id.dialog_title);
            dialog_title.setImageResource(R.drawable.updatetask);

            inputNote.setText(note.getNote());
            txtDate.setText(note.getDate());
            txtTime.setText(note.getTimestart());
            txtTime2.setText(note.getTimeend());

            if(note.getCategory().equals("personal")){
                personalon.setImageResource(R.drawable.personalon);
                category = "personal";
            }
            if(note.getCategory().equals("work")){
                workon.setImageResource(R.drawable.workon);
                personalon.setImageResource(R.drawable.personal);
                category = "work";
            }
            if(note.getCategory().equals("meeting")){
                meetingon.setImageResource(R.drawable.meetingon);
                personalon.setImageResource(R.drawable.personal);
                category = "meeting";
            }
            if(note.getCategory().equals("shopping")){
                shoppingon.setImageResource(R.drawable.shoppingon);
                personalon.setImageResource(R.drawable.personal);
                category = "shopping";
            }
            if(note.getCategory().equals("party")){
                partyon.setImageResource(R.drawable.partyon);
                personalon.setImageResource(R.drawable.personal);
                category = "party";
            }
            if(note.getCategory().equals("study")){
                studyon.setImageResource(R.drawable.studyon);
                personalon.setImageResource(R.drawable.personal);
                category = "study";
            }

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
                } else if (txtDate.getText().equals("Date Start")) {
                    Toast.makeText(Main3Activity.this, "Enter date!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (txtDate2.getText().equals("Date End")) {
                    Toast.makeText(Main3Activity.this, "Enter date!", Toast.LENGTH_SHORT).show();
                    return;
                }  else if (txtTime.getText().equals("Time Start")) {
                    Toast.makeText(Main3Activity.this, "Enter start time!", Toast.LENGTH_SHORT).show();
                    return;
                }  else if (txtTime2.getText().equals("Time End")) {
                    Toast.makeText(Main3Activity.this, "Enter end time!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    String[] note = new String[8];
                    note[0] = inputNote.getText().toString();
                    note[1] = category;
                    note[4] = txtDate.getText().toString();
                    note[5] = txtTime.getText().toString();
                    note[6] = txtTime2.getText().toString();
                    note[7] = txtDate2.getText().toString();


                    updateNote(note,inputNote.getText().toString(), position);
                } else {
                    // create new note
                    String[] note = new String[8];
                    note[0] = inputNote.getText().toString();
                    note[1] = category;
                    note[4] = txtDate.getText().toString();
                    note[5] = txtTime.getText().toString();
                    note[6] = txtTime2.getText().toString();
                    note[7] = txtDate2.getText().toString();

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



    public void gridWork(View view){
        inGrid = true;
        db = new DatabaseHelper(this);

        notesList.clear();

        notesList.addAll(db.getAllNotes("work"));
        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


                if (notesList.size() > 0) {
                    tasksFragment = new Fragment_ListGrid();
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
    public void gridPersonal(View view){
        inGrid = true;
        db = new DatabaseHelper(this);

        notesList.clear();

        notesList.addAll(db.getAllNotes("personal"));
        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


        if (notesList.size() > 0) {
            tasksFragment = new Fragment_ListGrid();
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
    public void gridMeeting(View view){
        inGrid = true;
        db = new DatabaseHelper(this);

        notesList.clear();

        notesList.addAll(db.getAllNotes("meeting"));
        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


        if (notesList.size() > 0) {
            tasksFragment = new Fragment_ListGrid();
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
    public void gridShopping(View view){
        inGrid = true;
        db = new DatabaseHelper(this);

        notesList.clear();

        notesList.addAll(db.getAllNotes("shopping"));
        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


        if (notesList.size() > 0) {
            tasksFragment = new Fragment_ListGrid();
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
    public void gridParty(View view){
        inGrid = true;
        db = new DatabaseHelper(this);

        notesList.clear();

        notesList.addAll(db.getAllNotes("party"));
        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


        if (notesList.size() > 0) {
            tasksFragment = new Fragment_ListGrid();
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
    public void gridStudy(View view){

        inGrid = true;
        db = new DatabaseHelper(this);

        notesList.clear();

        notesList.addAll(db.getAllNotes("study"));
        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


        if (notesList.size() > 0) {
            tasksFragment = new Fragment_ListGrid();
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
    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (inGrid) {
            inGrid = false;
            tasksFragment = new Fragment_Grid();
            FragmentManager fm1 = getFragmentManager();
            FragmentTransaction ft = fm1.beginTransaction();
            ft.replace(R.id.fragment,tasksFragment);
            ft.commit();
        }
    }

}

