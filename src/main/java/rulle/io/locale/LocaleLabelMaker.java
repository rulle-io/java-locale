package rulle.io.locale;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class LocaleLabelMaker {
  public static void main(String[] args) {

    Locale LOCALE_SWEDEN = new Locale.Builder().setLanguage("sv").setRegion("se").build();
    Locale LOCALE_RUSSIA = new Locale.Builder().setLanguage("ru").setRegion("ru").build();

    Locale[] locales =
        new Locale[] {
          Locale.US,
          Locale.CANADA,
          Locale.CANADA_FRENCH,
          Locale.FRANCE,
          Locale.GERMANY,
          Locale.ITALY,
          LOCALE_SWEDEN,
          LOCALE_RUSSIA,
          Locale.CHINA
        };
    Locale[] localesAll = Locale.getAvailableLocales();
    for (Locale locale : locales) {
      makeLocaleLabel(locale).ifPresent(v -> printLocaleLabel(v));
    }
  }

  public static Optional<LocaleLabel> makeLocaleLabel(final Locale currentLocale) {

    String currentLocaleStr = currentLocale.toString();
    if (Objects.isNull(currentLocaleStr) || currentLocaleStr.isEmpty()) {
      return Optional.empty();
    }
  
    // Random input values
    Integer quantity = Integer.valueOf(123456);
    Double amount = Double.valueOf(345987.246);
    LocalDate localDate = LocalDate.of(2025, Month.JANUARY, 1);
    Double currencyAmount = Double.valueOf(9876543.21);
    
    NumberFormat numberFormatter = NumberFormat.getNumberInstance(currentLocale);
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
    
    String quantityOut = numberFormatter.format(quantity);
    String amountOut = numberFormatter.format(amount);

    // TODO static info
    // DecimalFormatSymbols dfs = new DecimalFormatSymbols(currentLocale);
    // dfs.get....

    Currency currentCurrency = null;
    try {
      currentCurrency = Currency.getInstance(currentLocale);
    } catch (IllegalArgumentException e) {
      System.out.println("Exotic locale (ignored): " + currentLocaleStr);
      return Optional.empty();
    }

    String currencyOut = currencyFormatter.format(currencyAmount);

    // TODO: use getValues()
    DateTimeFormatter dateFormatterFull =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(currentLocale);
    ;
    DateTimeFormatter dateFormatterLong =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(currentLocale);
    DateTimeFormatter dateFormatterMedium =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(currentLocale);
    DateTimeFormatter dateFormatterShort =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(currentLocale);
    // DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL);
    String localDateStrFull = getFormattedValue(localDate, dateFormatterFull);
    String localDateStrLong = getFormattedValue(localDate, dateFormatterLong);
    String localDateStrMedium = getFormattedValue(localDate, dateFormatterMedium);
    String localDateStrShort = getFormattedValue(localDate, dateFormatterShort);

    LocaleStrings ls = new LocaleStrings(currentLocale.getDisplayName(), currentLocaleStr);
    LocaleNumberStrings lns = new LocaleNumberStrings(quantityOut, amountOut, currentCurrency.getDisplayName(), currencyOut);
    LocaleDateStrings lds = new LocaleDateStrings(localDateStrFull, localDateStrLong, localDateStrMedium, localDateStrShort);
    LocaleCurrencyStrings lcs = new LocaleCurrencyStrings(currentCurrency.getCurrencyCode(), currentCurrency.getDisplayName(),  currencyOut);
      
    return Optional.of(new LocaleLabel(ls, lns, lds, lcs));
  }

  public static void printLocaleLabel(final LocaleLabel localeLabel) {
    System.out.println("=== Formatting for Locale: " + localeLabel.localePart().localeStr() + " [" + localeLabel.localePart().localeDisplayName() + "]");
    System.out.println("Integer: " + localeLabel.numbersPart().integerStr());
    System.out.println("Double: " + localeLabel.numbersPart().doubleStr());
    System.out.println("Currency: " + localeLabel.currencyPart().currencyDisplayName() + "-" + localeLabel.currencyPart().currencyCode() + ": " + localeLabel.currencyPart().currencyStr());
    System.out.println("Date Full: " + localeLabel.datePart().dateFull());
    System.out.println("Date Long: " + localeLabel.datePart().dateLong());
    System.out.println("Date Medium: " + localeLabel.datePart().dateMedium());
    System.out.println("Date Short: " + localeLabel.datePart().dateShort());

    System.out.println("===");
  }
  
  private static String getFormattedValue(
      LocalDate localDate, DateTimeFormatter dateTimeFormatterFull) {
    try {
      return localDate.format(dateTimeFormatterFull);
    } catch (UnsupportedTemporalTypeException e) {
      return "N/A";
    }
  }
}
