import java.io.FileInputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.InvalidClassException;

/**
 * Secure Cart Restoration Endpoint (Mitigated against CWE-502).
 * 
 * COMPLIANCE: SEI CERT Oracle Coding Standard for Java - Rule SER01-J[cite: 1, 119]
 * DEFENSE MECHANISM: Utilizes ObjectInputFilter to enforce an explicit 
 * class allow-list, blocking malicious gadget chains before bytecode reconstruction.
 */
public class DeserialiseCartSecure {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java DeserialiseCartSecure <serialized-file>");
            return;
        }

        try (
            FileInputStream fileIn = new FileInputStream(args[0]);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn)
        ) {
            /*
             * SECURITY MITIGATION (SER01-J / ObjectInputFilter):
             * Restricts deserialization to explicitly permitted application 
             * classes and core runtime types, blocking arbitrary gadget chains.
             */
            ObjectInputFilter filter = info -> {
                // Defensive resource limits to prevent DoS
                if (info.depth() > 10 || info.references() > 100 || info.streamBytes() > 100000) {
                    return ObjectInputFilter.Status.REJECTED;
                }

                Class<?> clazz = info.serialClass();
                if (clazz == null) {
                    return ObjectInputFilter.Status.UNDECIDED;
                }

                String className = clazz.getName();

                // 1. Permit the legitimate application object type
                if (className.equals("CartObject")) {
                    return ObjectInputFilter.Status.ALLOWED;
                }

                // 2. Permit standard Java base types (like String)
                if (className.startsWith("java.lang.")) {
                    return ObjectInputFilter.Status.ALLOWED;
                }

                // 3. Permit ArrayList (the concrete list type used inside CartObject)
                if (className.equals("java.util.ArrayList")) {
                    return ObjectInputFilter.Status.ALLOWED;
                }
		
		        // 4. Permit Object arrays (used internally by ArrayList to store its elements)
                if (className.equals("[Ljava.lang.Object;")) {
                    return ObjectInputFilter.Status.ALLOWED;
                }

                // Everything else (HashMap, TriggerBox, ExecAction, etc.) is rejected
                System.out.println("[DEBUG] Rejected by filter: " + className);
                return ObjectInputFilter.Status.REJECTED;
            };

            objectIn.setObjectInputFilter(filter);

            // Attempt to deserialize the stream safely
            Object obj = objectIn.readObject();

            // Defence-in-depth: Verify expected top-level type
            if (obj == null || !obj.getClass().getName().equals("CartObject")) {
                throw new SecurityException("Unexpected or unauthorized serialized object type");
            }

            System.out.println("[+] Deserialization successful. Safe object restored.");

        } catch (InvalidClassException | SecurityException e) {
            System.err.println("\n[BLOCK] Deserialization attack thwarted by security filter!");
            System.err.println("[+] Reason: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.err.println("[-] Error during deserialization: " + e.getMessage());
        }
    }
}
