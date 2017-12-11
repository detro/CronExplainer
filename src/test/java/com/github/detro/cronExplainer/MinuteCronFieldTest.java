package com.github.detro.cronExplainer;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class MinuteCronFieldTest {

  @Test
  public void shouldHandleMinutesField() {
    MinuteCronField field;

    field = new MinuteCronField("*");
    for (int i = 0; i< 60; ++i) {
      assertTrue(field.getValues().contains(i));
    }
    assertEquals(60, field.getValues().size());

    field = new MinuteCronField("11-15");
    assertTrue(field.getValues().containsAll(Lists.newArrayList(11, 12, 13, 14, 15)));
    assertEquals(5, field.getValues().size());

    field = new MinuteCronField("13,15,7,10,1");
    assertTrue(field.getValues().containsAll(Lists.newArrayList(1, 7, 10, 13, 15)));
    assertEquals(5, field.getValues().size());

    field = new MinuteCronField("L");
    assertTrue(field.getValues().contains(59));
    assertEquals(1, field.getValues().size());

    field = new MinuteCronField("12");
    assertTrue(field.getValues().contains(12));
    assertEquals(1, field.getValues().size());

    field = new MinuteCronField("?");
    assertTrue(field.getValues().contains(LocalTime.now().getMinute()));
    assertEquals(1, field.getValues().size());
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowForMalformedSingleValue() {
    new MinuteCronField("11x");
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowForInvalidSingleValue() {
    new MinuteCronField("60");
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowForMalformedRange() {
    new MinuteCronField("1-3a");
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowForMalformedRange2() {
    new MinuteCronField("1-3-4");
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowForMalformedList() {
    new MinuteCronField("15,12,99,23");
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowForMalformedList2() {
    new MinuteCronField("1,2,3,a");
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowForMalformedLast() {
    new MinuteCronField("LL");
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowForMalformedStartupTime() {
    new MinuteCronField("??");
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowForMalformedStartupTime2() {
    new MinuteCronField("2?");
  }

}
