package info.androidhive.sqlite.database.model;

/**
 * Created by ravi on 20/02/18.
 */

public class Note {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_ALERT = "alert";
    public static final String COLUMN_FINISH = "finish";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DATEEND = "dateend";
    public static final String COLUMN_TIMESTART = "timestart";
    public static final String COLUMN_TIMEEND = "timeend";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String note;
    private String category;
    private String alert;
    private String finish;
    private String date;
    private String dateend;
    private String timestart;
    private String timeend;
    private String timestamp;



    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_CATEGORY + " TEXT,"
                    + COLUMN_ALERT + " TEXT DEFAULT 0,"
                    + COLUMN_FINISH + " TEXT DEFAULT 0,"
                    + COLUMN_DATE + " TEXT,"
                    + COLUMN_DATEEND + " TEXT,"
                    + COLUMN_TIMESTART + " TEXT,"
                    + COLUMN_TIMEEND + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Note() {
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Note(int id, String note, String category,String alert,String finish, String date, String dateend, String timestart, String timeend, String timestamp) {
        this.id = id;
        this.note = note;
        this.category = category;
        this.alert = alert;
        this.finish = finish;
        this.date = date;
        this.dateend = dateend;
        this.timestart = timestart;
        this.timeend = timeend;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getAlert() {
        return alert;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getDate() {
        return date;
    }

    public String getDateend() {
        return dateend;
    }

    public void setDateend(String dateend) {
        this.dateend = dateend;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}