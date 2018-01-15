package com.github.detro.CronExplainer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static java.time.DayOfWeek.*;
import static java.time.format.TextStyle.FULL;
import static java.time.format.TextStyle.SHORT;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.util.Locale.ENGLISH;

public class DayOfWeekCronField extends CronField {

  // NOTE: As defined in https://en.wikipedia.org/wiki/Cron, "Day of Week" are defined by the range "[0-6]" (i.e. "0-indexed"), not "[1-7]"
  private static final Range<Integer> DAY_OF_WEEK_RANGE = Range.closed((int) DAY_OF_WEEK.range().getMinimum() - 1, (int) DAY_OF_WEEK.range().getMaximum() - 1);

  private static Map<String, String> dayOfWeekNamesToInt = new ImmutableMap.Builder<String, String>()
    // Monday
    .put(MONDAY.getDisplayName(FULL, ENGLISH).toLowerCase(), "0")
    .put(MONDAY.getDisplayName(SHORT, ENGLISH).toLowerCase(), "0")
    // Tuesday
    .put(TUESDAY.getDisplayName(FULL, ENGLISH).toLowerCase(), "1")
    .put(TUESDAY.getDisplayName(SHORT, ENGLISH).toLowerCase(), "1")
    // Wednesday
    .put(WEDNESDAY.getDisplayName(FULL, ENGLISH).toLowerCase(), "2")
    .put(WEDNESDAY.getDisplayName(SHORT, ENGLISH).toLowerCase(), "2")
    // Thursday
    .put(THURSDAY.getDisplayName(FULL, ENGLISH).toLowerCase(), "3")
    .put(THURSDAY.getDisplayName(SHORT, ENGLISH).toLowerCase(), "3")
    // Friday
    .put(FRIDAY.getDisplayName(FULL, ENGLISH).toLowerCase(), "4")
    .put(FRIDAY.getDisplayName(SHORT, ENGLISH).toLowerCase(), "4")
    // Saturday
    .put(SATURDAY.getDisplayName(FULL, ENGLISH).toLowerCase(), "5")
    .put(SATURDAY.getDisplayName(SHORT, ENGLISH).toLowerCase(), "5")
    // Sunday
    .put(SUNDAY.getDisplayName(FULL, ENGLISH).toLowerCase(), "6")
    .put(SUNDAY.getDisplayName(SHORT, ENGLISH).toLowerCase(), "6")
    .build();

  public DayOfWeekCronField(String input) {
    super(normalizeInput(input), DAY_OF_WEEK_RANGE);
  }

  @Override
  protected int atStartup() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(jvmStartupTime), ZoneId.systemDefault()).getDayOfWeek().ordinal(); //< not "getValue()" as it's not "0-indexed"
  }

  @Override
  public String getLabel() {
    return "day of week";
  }

  /**
   * Normalizes the input for this field.
   * <p>
   * If the "day of the week" is expressed using a "synonym" instead of the code (i.e. "mon" or "monday"), we do some
   * simple in-place replacement, so the final input is made of the expected "0 to 6" value range.
   *
   * @param input Raw input string, as it comes from the user
   * @return Normalized, "0 to 6" made input string
   */
  private static String normalizeInput(final String input) {
    String result = input;

    for (Map.Entry<String, String> entry : dayOfWeekNamesToInt.entrySet()) {
      if (result.toLowerCase().contains(entry.getKey())) {
        result = result.toLowerCase().replace(entry.getKey(), entry.getValue());
      }
    }

    return result;
  }
}
