package com.github.detro.CronExplainer;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class DayOfWeekCronFieldTest {

    @Test
    public void shouldSupportStandardFormats() {
        final DayOfWeekCronField range = new DayOfWeekCronField("0-3");
        assertArrayEquals(new Integer[]{0, 1, 2, 3}, range.getValues().toArray());

        final DayOfWeekCronField list = new DayOfWeekCronField("1,3,6");
        assertArrayEquals(new Integer[]{1, 3, 6}, list.getValues().toArray());

        final DayOfWeekCronField every2 = new DayOfWeekCronField("*/2");
        assertArrayEquals(new Integer[]{0, 2, 4, 6}, every2.getValues().toArray());
    }

    @Test
    public void shouldSupportDayOfWeekNamesInVariousCases() {
        DayOfWeekCronField range = new DayOfWeekCronField("Mon-Thu");
        assertArrayEquals(new Integer[]{0, 1, 2, 3}, range.getValues().toArray());
        range = new DayOfWeekCronField("mon-thu");
        assertArrayEquals(new Integer[]{0, 1, 2, 3}, range.getValues().toArray());
        range = new DayOfWeekCronField("monday-Thursday");
        assertArrayEquals(new Integer[]{0, 1, 2, 3}, range.getValues().toArray());

        DayOfWeekCronField list = new DayOfWeekCronField("tue,Thu,SunDay");
        assertArrayEquals(new Integer[]{1, 3, 6}, list.getValues().toArray());

        final DayOfWeekCronField every2 = new DayOfWeekCronField("Monday-Fri/2");
        assertArrayEquals(new Integer[]{0, 2, 4}, every2.getValues().toArray());
    }

}
