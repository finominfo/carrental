package hu.finominfo.carrental.enums;

public enum Brand {
    TOYOTA(true),
    VOLKSWAGEN(false),
    GENERAL_MOTORS(false),
    HYUNDAI(true),
    KIA(true),
    FORD(true),
    NISSAN(true),
    FIAT(true),
    CHRYSLER(false),
    HONDA(false),
    SUZUKI(true),
    RENAULT(true),
    BMW(false),
    MAZDA(false);
    
    private final boolean foreignUsageEnabled;

    private Brand(boolean isForeignEnabled) {
        this.foreignUsageEnabled = isForeignEnabled;
    }

    public boolean isForeignUsageEnabled() {
        return foreignUsageEnabled;
    }
}
