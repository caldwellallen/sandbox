package com.allenc.util;

import static com.allenc.CommonsTestConstants.ACCEPTABLE_TIME_DIFF;
import static com.allenc.CommonsTestConstants.DATE_FORMAT_TO_MILLI;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.extern.log4j.Log4j2;

/**
 * @author Copyright 2022
 */
@Log4j2
public class DateValidator {

    private DateValidator() {
        // purely a static class
    }

    public static boolean isThisDateValid(String dateToValidate, String dateFormat) {
        if (dateToValidate == null) {
            return false;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFormat);
        try {
            fmt.parse(dateToValidate); // if not valid, it will throw DateTimeParseException
            log.debug("{} is a valid date comparing to {}", dateToValidate, dateFormat);
        } catch (DateTimeParseException e) {
            log.debug("{} is NOT a valid date comparing to {}", dateToValidate, dateFormat);
            return false;
        }
        return true;
    }

    public static boolean isDateCloseToNow(String dateToVerify) {
        return isDateCloseTo(dateToVerify, Instant.now());
    }

    public static boolean isDateCloseToNow(String dateToVerify, String format) {
        return isDateCloseTo(dateToVerify, Instant.now(), format);
    }

    public static boolean isDateCloseTo(String dateToVerify, Instant dateToCompareTo) {
        return isDateCloseTo(dateToVerify, dateToCompareTo, DATE_FORMAT_TO_MILLI);
    }

    public static boolean isDateCloseTo(String dateToVerify, Instant dateToCompareTo, Long acceptableTimeDiff) {
        return isDateCloseTo(dateToVerify, dateToCompareTo, DATE_FORMAT_TO_MILLI, acceptableTimeDiff);
    }

    public static boolean isDateCloseTo(String dateToVerify, Instant dateToCompareTo, String format) {
        return isDateCloseTo(dateToVerify, dateToCompareTo, format, ACCEPTABLE_TIME_DIFF);
    }

    public static boolean isDateCloseTo(String dateToVerify, Instant dateToCompareTo, String format, Long acceptableTimeDiff) {
        Long difference;
        if (Duration.between(Instant.parse(dateToVerify), dateToCompareTo).toMillis() >= 0) {
            difference = Duration.between(Instant.parse(dateToVerify), dateToCompareTo).toMillis();
        } else {
            difference = Duration.between(dateToCompareTo, Instant.parse(dateToVerify)).toMillis();
        }
        log.debug("Difference between {} and {} is {} millis", dateToCompareTo, dateToVerify, difference);
        if (difference > acceptableTimeDiff) {
            return false;
        }
        return isThisDateValid(dateToVerify, format);
    }
}
