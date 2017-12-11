# Cron Explainer

This is a simple utility, written in Java and using Gradle, that accepts  as input arguments a line of Cron configuration,
and prints out an _"explaination"_ for it.

In other words, given the [Cron format](https://en.wikipedia.org/wiki/Cron), it translates it into the
corresponding _minute/hour/day-of-month..._ values.

## What Cron value formats does it handle?

The defaults

* single (duh!): `30`
* range: `10-20`
* all: `*`
* list: `1,5,10,15`
* range step: `range/5` or `*/15`

as well as some specials

* _last_ possible value for field: `L`
* _startup time_: `?`


## Build executable

The packages the applicaiton as a JAR and provides portable executable that make launching the JAR really easy.

```bash
$> ./gradlew installDist 

#...

$> ls ./build/install/cron-explainer/bin/
cron-explainer*     cron-explainer.bat*
```

## Execution examples

Those are just some working examples, to show how the output looks like.

```bash
$> ./build/install/cron-explainer/bin/cron-explainer */12 0 1,15 */1 1-5 /usr/bin/find
minute        0 12 24 36 48
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find

$> ./build/install/cron-explainer/bin/cron-explainer ? ? 1,15 */1 1-5 /usr/bin/find
minute        31
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find

$> ./build/install/cron-explainer/bin/cron-explainer "*/10 * L */1 1-5 /usr/bin/find"
minute        0 10 20 30 40 50
hour          0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
day of month  31
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find
```

## Error examples

```bash
$> ./build/install/cron-explainer/bin/cron-explainer */12 0 1,15 */1 1-5
2017-12-11 00:29:57,984 [main] ERROR org.github.detro.cronExplainer.Main - Malformed input '[*/12, 0, 1,15, */1, 1-5]'. It should be of exactly 6 fields and look something like this: '*/15 0 1,15 * 1-5 /usr/bin/find'

$> ./build/install/cron-explainer/bin/cron-explainer */12 0 1,15 */1 55 /usr/bin/find
minute        0 12 24 36 48
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
2017-12-11 00:30:19,087 [main] ERROR org.github.detro.cronExplainer.Main - Error processing field for input '[*/12, 0, 1,15, */1, 55, /usr/bin/find]'
2017-12-11 00:30:19,088 [main] ERROR org.github.detro.cronExplainer.Main - Invalid single value selected for Field: 55
```

## What's left to do

* Provide every `CronField` with a complete set of test: right now only `MinuteCronField` is well tested
* Handle special cron value formats, like `@yearly` or `@monthly`
* Handle input via `STDIN` instead of just arguments, allowing the command to be _pipelined_  
