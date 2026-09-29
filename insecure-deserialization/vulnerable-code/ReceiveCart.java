// read me java file

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

// Simulates the application restoring a customer's cart on their next visit.

public class ReceiveCart {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        FileInputStream fileIn = new FileInputStream("CartObject.ser");
        try (ObjectInputStream in = new ObjectInputStream(fileIn)) {
            CartObject cart = (CartObject) in.readObject();
            System.out.println("Restored: " + cart);
        }
    }
}
