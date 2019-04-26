package yazdaniscodelab.topnoteapp.Model;

/**
 * Created by Yazdani on 4/27/2019.
 */

public class Data {

    String title;
    String budget;
    String note;
    String date;
    String id;

    public Data(){

    }

    public Data(String title, String budget, String note, String date, String id) {
        this.title = title;
        this.budget = budget;
        this.note = note;
        this.date = date;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
