package org.github.detro.cronExplainer;

import com.google.common.base.Joiner;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.management.ManagementFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * Generic CRON Field.
 * <p>
 * It's the abstraction of a single CRON field and should be subclassed into the specific field type (i.e. hours, minutes, day-of-month, ...).
 * <p>
 * See <a href=https://en.wikipedia.org/wiki/Cron>specs</a>.
 */
public abstract class CronField {
  private static final Logger LOG = LoggerFactory.getLogger(CronField.class);

  private static final String PATTERN_GROUP_SINGLE = "SINGLE";
  private static final String PATTERN_GROUP_ALL = "ALL";
  private static final String PATTERN_GROUP_RANGE = "RANGE";
  private static final String PATTERN_GROUP_LIST = "LIST";
  private static final String PATTERN_GROUP_STARTUP_TIME = "NOW";
  private static final String PATTERN_GROUP_LAST = "LAST";
  private static final String PATTERN_GROUP_STEP = "STEP";

  private static final Pattern POSSIBLE_FIELD_FORMATS = Pattern.compile("^" +
    "(" +
    "(?<" + PATTERN_GROUP_RANGE + ">\\d+-\\d+)" +
    "|" +
    "(?<" + PATTERN_GROUP_LIST + ">\\d+(,\\d+)+)" +
    "|" +
    "(?<" + PATTERN_GROUP_LAST + ">L)" +
    "|" +
    "(?<" + PATTERN_GROUP_STARTUP_TIME + ">\\?)" +
    "|" +
    "(?<" + PATTERN_GROUP_ALL + ">\\*)" +
    "|" +
    "(?<" + PATTERN_GROUP_SINGLE + ">\\d+)" +
    ")" +
    "(/(?<" + PATTERN_GROUP_STEP + ">\\d+))?" +
    "$");

  protected final ImmutableSortedSet<Integer> values;
  protected final long jvmStartupTime;

  public CronField(String input, Range<Integer> possibleValues) {
    // TODO handle frequency i.e. "*/10"

    // Used by sub-classes to figure out the "startup time"
    jvmStartupTime = ManagementFactory.getRuntimeMXBean().getStartTime();

    // Match input against our clever matcher above
    final Matcher matcher = POSSIBLE_FIELD_FORMATS.matcher(input);
    final boolean matchFound = matcher.find();

    if (matchFound) {
      if (null != matcher.group(PATTERN_GROUP_ALL)) {
        LOG.debug("Matched ALL pattern group for input '{}'", input);

        // All possible values are selected
        values = rangeToSet(possibleValues, matcher.group(PATTERN_GROUP_STEP));
      } else if (null != matcher.group(PATTERN_GROUP_SINGLE)) {
        LOG.debug("Matched SINGLE pattern group for input '{}'", input);

        // Only a single value is selected
        final int single = Integer.parseInt(matcher.group(PATTERN_GROUP_SINGLE));

        // Verify the selected value is a possible one
        if (possibleValues.contains(single)) {
          values = ImmutableSortedSet.of(single);
        } else {
          throw new RuntimeException("Invalid single value selected for Field: " + single);
        }
      } else if (null != matcher.group(PATTERN_GROUP_RANGE)) {
        LOG.debug("Matched RANGE pattern group for input '{}'", input);

        // A range of possible values is selected
        final String range = matcher.group(PATTERN_GROUP_RANGE);
        final String[] rangeSplit = range.split("-");
        final int rangeStart = Integer.parseInt(rangeSplit[0]);
        final int rangeEnd = Integer.parseInt(rangeSplit[1]);

        // Verify the selected range belongs to the possible values
        if (possibleValues.contains(rangeStart) && possibleValues.contains(rangeEnd)) {
          // All possible values, but separated by "step"
          values = rangeToSet(Range.closed(rangeStart, rangeEnd), matcher.group(PATTERN_GROUP_STEP));
        } else {
          throw new RuntimeException("Invalid range selected for Field: " + range);
        }
      } else if (null != matcher.group(PATTERN_GROUP_LIST)) {
        LOG.debug("Matched LIST pattern group for input '{}'", input);

        // A list of possible values is selected
        final String list = matcher.group(PATTERN_GROUP_LIST);
        final String[] listSplit = list.split(",");

        // Verify every element in the list belongs to the possible values
        if (Stream.of(listSplit).map(Integer::parseInt).allMatch(possibleValues::contains)) {
          final ImmutableSortedSet.Builder<Integer> builder = ImmutableSortedSet.naturalOrder();
          Stream
            .of(listSplit)
            .map(Integer::parseInt)
            .forEach(builder::add);

          values = builder.build();
        } else {
          throw new RuntimeException("Invalid list selected for Field: " + list);
        }
      } else if (null != matcher.group(PATTERN_GROUP_LAST)) {
        LOG.debug("Matched LAST pattern group for input '{}'", input);

        // Only the last of the possible values is selected
        values = ImmutableSortedSet.of(possibleValues.upperEndpoint());
      } else if (null != matcher.group(PATTERN_GROUP_STARTUP_TIME)) {
        LOG.debug("Matched STARTUP-TIME pattern group for input '{}'", input);

        // Only the value at startup time is selected (startup of the JVM)
        values = ImmutableSortedSet.of(atStartup());
      } else {
        // WARN: This should never happen!
        throw new RuntimeException("Not Supported (yet?) format: " + input);
      }
    } else {
      throw new RuntimeException("Invalid format for Field: " + input);
    }
  }

  abstract protected int atStartup();

  abstract public String getLabel();

  public ImmutableSortedSet<Integer> getValues() {
    return values;
  }

  @Override
  public String toString() {
    return String.format("%-13s %s", getLabel(), Joiner.on(" ").join(getValues()));
  }

  private static ImmutableSortedSet<Integer> rangeToSet(Range<Integer> range, @Nullable String stepPatternGroup) {
    LOG.debug("Converting Range {} to Set", range);

    if (null != stepPatternGroup) {
      final int step = Integer.parseInt(stepPatternGroup);
      LOG.debug("  Will apply step increment of {}", step);

      // Iterate over the given range, using the "step" as increment
      final ImmutableSortedSet.Builder<Integer> builder = ImmutableSortedSet.naturalOrder();
      for (int i = range.lowerEndpoint(); i <= range.upperEndpoint(); i += step) {
        builder.add(i);
      }

      return builder.build();
    } else {
      // Return the entire range
      return ContiguousSet.create(range, DiscreteDomain.integers());
    }
  }

}
