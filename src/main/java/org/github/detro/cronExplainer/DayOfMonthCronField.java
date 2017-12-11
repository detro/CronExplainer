package org.github.detro.cronExplainer;

import com.google.common.collect.Range;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;

public class DayOfMonthCronField extends CronField {

  private static final Range<Integer> DAY_OF_MONTH_RANGE = Range.closed((int) DAY_OF_MONTH.range().getMinimum(), (int) DAY_OF_MONTH.range().getMaximum());

  public DayOfMonthCronField(String input) {
    super(input, DAY_OF_MONTH_RANGE);
  }

  @Override
  protected int atStartup() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(jvmStartupTime), ZoneId.systemDefault()).getDayOfMonth();
  }

  @Override
  public String getLabel() {
    return "day of month";
  }
}
