package sdi.com.currencywizard.model;

/**
 * Created by twilightuser on 4/7/16.
 */
public class BasketList {

    private int id;
    private String basket_title;
    private String create_date_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_date_time() {
        return create_date_time;
    }

    public void setCreate_date_time(String create_date_time) {
        this.create_date_time = create_date_time;
    }

    public String getBasket_title() {
        return basket_title;
    }

    public void setBasket_title(String basket_title) {
        this.basket_title = basket_title;
    }
}
