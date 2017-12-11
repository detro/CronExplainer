package org.github.detro.cronExplainer;

import com.google.common.collect.Range;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.temporal.ChronoField.MONTH_OF_YEAR;

public class MonthCronField extends CronField {

  private static final Range<Integer> MONTH_RANGE = Range.closed((int) MONTH_OF_YEAR.range().getMinimum(), (int) MONTH_OF_YEAR.range().getMaximum());

  public MonthCronField(String input) {
    super(input, MONTH_RANGE);
  }

  @Override
  protected int atStartup() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(jvmStartupTime), ZoneId.systemDefault()).getMonthValue();
  }

  @Override
  public String getLabel() {
    return "month";
  }
}
