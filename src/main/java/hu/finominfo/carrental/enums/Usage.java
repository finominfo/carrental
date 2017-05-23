package hu.finominfo.carrental.enums;

import java.util.Arrays;

public enum Usage {
    DOMESTIC,
    FOREIGN;
    
    public static Usage get(String usage) throws Throwable {
        for (Usage u : Usage.values()) {
            if (u.toString().equalsIgnoreCase(usage)) {
                return u;
            }
        }
        throw new Throwable("Usage string (" + usage + ") can only be one of the following values: " + Arrays.toString(Usage.values()));
    }
}
