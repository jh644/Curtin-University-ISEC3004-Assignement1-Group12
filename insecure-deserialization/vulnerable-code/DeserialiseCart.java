// deserialise code

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simulates a server-side cart-restore endpoint.
 *
 * VULNERABILITY (CWE-502: Deserialization of Untrusted Data):
 * This program reads a raw byte stream from an external, untrusted
 * source (the "saved cart" file, which in a real deployment could come
 * from a cookie, request body, or session store) and passes it directly
 * into ObjectInputStream.readObject() with no validation of:
 *   - where the data came from
 *   - what class it actually deserializes to
 *   - whether the class is on an allow-list of "safe" types
 *
 * Because ObjectInputStream will instantiate ANY class on the classpath
 * whose bytecode matches the stream (not just CartObject), an attacker
 * who controls the "cart file" can substitute a completely different,
 * malicious serialized object. If that object (or something reachable
 * from it) has "magic methods" like readObject(), hashCode(), or
 * equals() that trigger dangerous behaviour, the attacker's code runs
 * the moment readObject() is called -- before the (wrongly cast) result
 * is ever used by this program.
 */

public class DeserialiseCart {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // VULNERABLE LINE: no type filtering, no ObjectInputFilter,
        // no signature/HMAC check on the incoming bytes before this call.

        Path path = Paths.get(args[0]);
        byte[] data = Files.readAllBytes(path);
        InputStream input = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(input);
        ois.readObject();
    }
}
