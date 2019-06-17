package info.androidhive.sqlite.database.model;

/**
 * Created by ravi on 20/02/18.
 */

public class Note {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_CATAGORY = "catagory";
    public static final String COLUMN_ALART = "alart"   ;
    public static final String COLUMN_CHECK = "check";
    public static final String COLUMN_TIMESTART = "timeStart";
    public static final String COLUMN_TIMEEND = "timeEnd";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String note;
    private String catagory;
    private String alart;
    private String check;
    private String timeStart;
    private String timeEnd;
    private String date;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getAlart() {
        return alart;
    }

    public void setAlart(String alart) {
        this.alart = alart;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_CATAGORY + " TEXT,"
                    + COLUMN_ALART + " TEXT,"
                    + COLUMN_CHECK + " TEXT,"
                    + COLUMN_TIMESTART + " TEXT,"
                    + COLUMN_TIMEEND + " TEXT,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Note() {
    }

    public Note(int id, String note,String catagory,String alart,String check,String timeStart,String timeEnd,String date, String timestamp) {
        this.id = id;
        this.note = note;
        this.date = date;
        this.catagory = catagory;
        this.alart = alart;
        this.check = check;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public void setId(int id) {
        this.id = id;
    }

}
