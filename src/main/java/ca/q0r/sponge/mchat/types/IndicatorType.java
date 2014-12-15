package ca.q0r.sponge.mchat.types;

import ca.q0r.sponge.mchat.config.main.MainType;

/**
 * Enum for Different Indicator Types.
 */
public enum IndicatorType {
    /**
     * Miscellaneous Variable Type.
     */
    MISC_VAR(MainType.MCHAT_VAR_INDICATOR),
    /**
     * Custom Variable Type.
     */
    CUS_VAR(MainType.MCHAT_CUS_VAR_INDICATOR),
    /**
     * Locale Variable Type.
     */
    LOCALE_VAR(MainType.MCHAT_LOCALE_VAR_INDICATOR);

    private MainType type;

    private IndicatorType(MainType type) {
        this.type = type;
    }

    /**
     * Indicator Value.
     *
     * @return Value of Indicator Type.
     */
    public String getValue() {
        return type.getString();
    }
}