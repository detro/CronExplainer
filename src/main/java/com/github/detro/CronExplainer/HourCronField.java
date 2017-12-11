package com.github.detro.CronExplainer;

import com.google.common.collect.Range;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;

public class HourCronField extends CronField {

  private static final Range<Integer> HOUR_RANGE = Range.closed((int) HOUR_OF_DAY.range().getMinimum(), (int) HOUR_OF_DAY.range().getMaximum());

  public HourCronField(String input) {
    super(input, HOUR_RANGE);
  }

  @Override
  protected int atStartup() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(jvmStartupTime), ZoneId.systemDefault()).getHour();
  }

  @Override
  public String getLabel() {
    return "hour";
  }
}
