// send me file

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

// Simulates the application saving a customer's cart to disk
// (in a real system, this could be a cookie, session file, or DB blob).

public class SendCart {
    public static void main(String[] args) throws IOException {
        CartObject cart = new CartObject("customer_42");
        cart.addItem("Laptop");
        cart.addItem("Mouse");

        System.out.println("Saving: " + cart);

        FileOutputStream fileOut = new FileOutputStream("CartObject.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(cart);
        out.close();
        fileOut.close();
    }
}
