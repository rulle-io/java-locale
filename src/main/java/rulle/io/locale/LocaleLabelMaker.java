package rulle.io.locale;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
          Locale.UK,
          LOCALE_SWEDEN,
          LOCALE_RUSSIA,
          Locale.CHINA,
          Locale.JAPAN,
          Locale.KOREA
        };
    Locale[] localesAll = Locale.getAvailableLocales();
    Stream<Locale> stream = Arrays.stream(localesAll);
    List<LocaleLabel> labels = stream
            .map(l -> makeLocaleLabel(l))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());

    LocaleStats dateShort = new LocaleStats("dateShort"); 
    labels.forEach(label -> {
      printLocaleLabel(label);
      // TODO: extend to all tracked parameters
      // status regarding usage of a pattern:
      // YYYY-MM-DD => [USA, UK, IT]
      dateShort.put(label.datePart().dateShort(), label.localePart().localeStr());
    });

    System.out.println("DateLong pattern: " + dateShort.toString());
    
  }

  public static Optional<LocaleLabel> makeLocaleLabel(final Locale currentLocale) {

    String currentLocaleStr = currentLocale.toString();
    if (Objects.isNull(currentLocaleStr) || currentLocaleStr.isEmpty()) {
      return Optional.empty();
    }

    // Random input values
    Integer quantity = Integer.valueOf(123456);
    Double amount = Double.valueOf(345987.246);
    LocalDate localDate = LocalDate.of(2025, Month.DECEMBER, 31);
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
    LocaleNumberStrings lns =
        new LocaleNumberStrings(
            quantityOut, amountOut, currentCurrency.getDisplayName(), currencyOut);
    LocaleDateStrings lds =
        new LocaleDateStrings(
            localDateStrFull, localDateStrLong, localDateStrMedium, localDateStrShort);
    LocaleCurrencyStrings lcs =
        new LocaleCurrencyStrings(
            currentCurrency.getCurrencyCode(), currentCurrency.getDisplayName(), currencyOut);

    return Optional.of(new LocaleLabel(ls, lns, lds, lcs));
  }

  public static class LocaleStats {
    private final String name;
    private final SetMultimap<String, String> stats = HashMultimap.create();

    public LocaleStats(final String name) {
      this.name = name;
    }


    public boolean put(final String metricName, final String metricValue) {
      return stats.put(metricName, metricValue);
    }

    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(String.format("LocaleStats [%s]\n", name));
      stats.asMap().entrySet().forEach(
              stat ->
                      buf.append(String.format("[%s] -> [%s]\n", stat.getKey(), stat.getValue().toString())));
      buf.append(" - === -\n");
      return buf.toString();
    }
  }

  public static void printLocaleLabel(final LocaleLabel localeLabel) {
    System.out.println(localeLabel.toString());
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
