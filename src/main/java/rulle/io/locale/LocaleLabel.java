package rulle.io.locale;

public record LocaleLabel(
  LocaleStrings localePart,
  LocaleNumberStrings numbersPart,
  LocaleDateStrings datePart,
  LocaleCurrencyStrings currencyPart
) {}
