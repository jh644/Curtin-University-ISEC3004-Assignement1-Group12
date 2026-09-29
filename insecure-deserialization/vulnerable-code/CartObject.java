// value object file

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// A simple serializable class representing a customer's shopping cart.
// This is the "expected" object type the application wants to deserialize.

public class CartObject implements Serializable {
    private static final long serialVersionUID = 1L;

    String customerId;
    List<String> items;

    public CartObject() {
        this("guest");
    }

    public CartObject(String customerId) {
        this.customerId = customerId;
        this.items = new ArrayList<>();
    }

    public void addItem(String item) {
        items.add(item);
    }

    public String toString() {
        return "Cart[" + customerId + "] = " + items;
    }
}
