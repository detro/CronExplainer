package org.github.detro.cronExplainer;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class Main {
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    // Handle both cases:
    // - input provided as single argument (ex. 'java -jar cron-explainer.jar "*/15 0 1,15 * 1-5 /usr/bin/find"')
    // - input provided as separate arguments (ex. 'java -jar cron-explainer.jar */15 0 1,15 * 1-5 /usr/bin/find')
    //
    // This is needed because the exercise is a bit ambiguous when it says:
    //
    //   "The input will be on a single line. The cron string will be passed to your application as a single argument"
    //
    // So, to be safe, this handles both cases.
    final List<String> arguments = Lists.newArrayList(Splitter.on(Pattern.compile("\\s+"))
      .trimResults()
      .split(Joiner.on(" ").join(args)));
    LOG.debug("Normalized Input Arguments: {}", arguments);

    // Check input is of the "right size": validation of every single field will happen later
    if (arguments.size() != 6) {
      LOG.error("Malformed input '{}'. It should be of exactly 6 fields and look something like this: '*/15 0 1,15 * 1-5 /usr/bin/find'", arguments);
      System.exit(1);
    }

    try {
      // Validate and print input for fields, one by one
      System.out.println(new MinuteCronField(arguments.get(0)).toString());
      System.out.println(new HourCronField(arguments.get(1)).toString());
      System.out.println(new DayOfMonthCronField(arguments.get(2)).toString());
      System.out.println(new MonthCronField(arguments.get(3)).toString());
      System.out.println(new DayOfWeekCronField(arguments.get(4)).toString());

      // If we are here, it's time for the easy bit: the command
      System.out.println(format("%-13s %s", "command", arguments.get(5)));
    } catch (RuntimeException e) {
      LOG.error("Error processing field for input '{}'", arguments);
      LOG.error(e.getMessage());
      System.exit(1);
    }
  }

}
