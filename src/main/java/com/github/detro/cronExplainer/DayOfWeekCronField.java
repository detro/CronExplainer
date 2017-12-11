package com.github.detro.cronExplainer;

import com.google.common.collect.Range;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;

public class DayOfWeekCronField extends CronField {

  // NOTE: As defined in https://en.wikipedia.org/wiki/Cron, "Day of Week" are defined by the range "[0-6]" (i.e. "0-indexed"), not "[1-7]"
  private static final Range<Integer> DAY_OF_WEEK_RANGE = Range.closed((int) DAY_OF_WEEK.range().getMinimum() - 1, (int) DAY_OF_WEEK.range().getMaximum() - 1);

  public DayOfWeekCronField(String input) {
    super(input, DAY_OF_WEEK_RANGE);
  }

  @Override
  protected int atStartup() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(jvmStartupTime), ZoneId.systemDefault()).getDayOfWeek().ordinal(); //< not "getValue()" as it's not "0-indexed"
  }

  @Override
  public String getLabel() {
    return "day of week";
  }
}
