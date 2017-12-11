package com.github.detro.CronExplainer;

import com.google.common.collect.Range;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

public class MinuteCronField extends CronField {

  private static final Range<Integer> MINUTE_RANGE = Range.closed((int) MINUTE_OF_HOUR.range().getMinimum(), (int) MINUTE_OF_HOUR.range().getMaximum());

  public MinuteCronField(String input) {
    super(input, MINUTE_RANGE);
  }

  @Override
  protected int atStartup() {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(jvmStartupTime), ZoneId.systemDefault()).getMinute();
  }

  @Override
  public String getLabel() {
    return "minute";
  }
}
