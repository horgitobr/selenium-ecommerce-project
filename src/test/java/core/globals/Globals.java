package core.globals;
/**
 * Global runtime variables shared between tests.
 * Used for cross-test communication (e.g. storing registered credentials).
 */
public class Globals {

    // Email/password generated and stored during registration flows
    public static String registeredEmail;
    public static String registeredPassword;
}
