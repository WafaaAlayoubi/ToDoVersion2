package info.androidhive.sqlite.view;

/**
 * Created by ravi on 20/02/18.
 */

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.model.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context context;
    private List<Note> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public ImageView dot;
        public TextView timestart;
        public ImageView bell;
        public ImageView finish;


        public MyViewHolder(final View view) {
            super(view);

            note = view.findViewById(R.id.note);
            dot = view.findViewById(R.id.dot);
            timestart = view.findViewById(R.id.timestart);
            bell =   view.findViewById(R.id.bell);
            finish =   view.findViewById(R.id.finsih);

        }
    }


    public NotesAdapter(Context context, List<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = notesList.get(position);

        holder.note.setText(note.getCategory());
        holder.timestart.setText(note.getTimestart());


        if(note.getAlert().equals("0")) {
            holder.bell.setImageResource(R.drawable.smallbellof);
        }
        else {
            holder.bell.setImageResource(R.drawable.smallbellon);
        }

        if(note.getFinish().equals("0")) {
            holder.finish.setImageResource(R.drawable.finishoff);
            holder.note.setPaintFlags( holder.note.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

        }
        else {
            holder.finish.setImageResource(R.drawable.finishon);
            holder.note.setPaintFlags(holder.note.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if(note.getCategory().equals("personal")){
            holder.dot.setImageResource(R.drawable.yellow);
        }
        if(note.getCategory().equals("work")){
            holder.dot.setImageResource(R.drawable.green);
        }
        if(note.getCategory().equals("meeting")){
            holder.dot.setImageResource(R.drawable.ligthpurple);
        }
        if(note.getCategory().equals("shopping")){
            holder.dot.setImageResource(R.drawable.orange);
        }
        if(note.getCategory().equals("party")){
            holder.dot.setImageResource(R.drawable.blue);
        }if(note.getCategory().equals("study")){
            holder.dot.setImageResource(R.drawable.darkpurple);
        }



        // Displaying dot from HTML character code
       // holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
