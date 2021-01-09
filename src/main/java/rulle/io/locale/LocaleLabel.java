package rulle.io.locale;

public record LocaleLabel(
  LocaleStrings localePart,
  LocaleNumberStrings numbersPart,
  LocaleDateStrings datePart,
  LocaleCurrencyStrings currencyPart
) {
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(
                (String.format(
                        "=== Formatting for Locale: %s [%s]\n",
                        localePart().localeStr(), localePart().localeDisplayName())));

        buf.append((String.format("Integer: %s\n", numbersPart().integerStr())));
        buf.append((String.format("Double: %s\n", numbersPart().doubleStr())));
        buf.append((String.format("Currency: %s [%s]: %s \n",
                currencyPart().currencyDisplayName(),
                currencyPart().currencyCode(),
                currencyPart().currencyStr()
        )));
        buf.append((String.format("Date Full: %s\n", datePart().dateFull())));
        buf.append((String.format("Date Long: %s\n", datePart().dateLong())));
        buf.append((String.format("Date Medium: %s\n", datePart().dateMedium())));
        buf.append((String.format("Date Short: %s\n", datePart().dateShort())));
        buf.append("===");
        
        return buf.toString();
    }
}
