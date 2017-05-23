package hu.finominfo.carrental.enums;

import java.util.Arrays;

public enum EuropeanCountry {

    Albania,
    Andorra,
    Armenia,
    Austria,
    Azerbaijan,
    Belarus,
    Belgium,
    Bosnia_and_Herzegovina,
    Bulgaria,
    Croatia,
    Cyprus,
    Czech_Republic,
    Denmark,
    Estonia,
    Finland,
    France,
    Georgia,
    Germany,
    Greece,
    Hungary,
    Iceland,
    Ireland,
    Italy,
    Kazakhstan,
    Kosovo,
    Latvia,
    Liechtenstein,
    Lithuania,
    Luxembourg,
    Macedonia,
    Malta,
    Moldova,
    Monaco,
    Montenegro,
    Netherlands,
    Norway,
    Poland,
    Portugal,
    Romania,
    Russia,
    San_Marino,
    Serbia,
    Slovakia,
    Slovenia,
    Spain,
    Sweden,
    Switzerland,
    Turkey,
    Ukraine,
    United_Kingdom,
    Vatican_City;

    public static EuropeanCountry get(String country) throws Throwable {
        for (EuropeanCountry europeanCountry : EuropeanCountry.values()) {
            if (europeanCountry.toString().equalsIgnoreCase(country)) {
                return europeanCountry;
            }
        }
        throw new Throwable("Variable countries (" + country + ") can only be one or more of the following values: " + Arrays.toString(EuropeanCountry.values()));
    }
}
