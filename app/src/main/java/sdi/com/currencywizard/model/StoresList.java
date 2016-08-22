package sdi.com.currencywizard.model;

/**
 * Created by twilightuser on 4/7/16.
 */
public class StoresList {

    private int id;
    private String basket_id;
    private String note;
    private String from_amt;
    private String from_code;
    private String from_sym;
    private String to_amt;
    private String to_code;

    public String getFrom_sym() {
        return from_sym;
    }

    public void setFrom_sym(String from_sym) {
        this.from_sym = from_sym;
    }

    public String getTo_sym() {
        return to_sym;
    }

    public void setTo_sym(String to_sym) {
        this.to_sym = to_sym;
    }

    private String to_sym;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTo_code() {
        return to_code;
    }

    public void setTo_code(String to_code) {
        this.to_code = to_code;
    }

    public String getTo_amt() {
        return to_amt;
    }

    public void setTo_amt(String to_amt) {
        this.to_amt = to_amt;
    }

    public String getFrom_code() {
        return from_code;
    }

    public void setFrom_code(String from_code) {
        this.from_code = from_code;
    }

    public String getFrom_amt() {
        return from_amt;
    }

    public void setFrom_amt(String from_amt) {
        this.from_amt = from_amt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBasket_id() {
        return basket_id;
    }

    public void setBasket_id(String basket_id) {
        this.basket_id = basket_id;
    }
}
