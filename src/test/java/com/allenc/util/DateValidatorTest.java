package com.allenc.util;

import static org.assertj.core.api.Assertions.assertThat;

import static com.allenc.CommonsTestConstants.DATE_FORMAT_TO_MICRO;
import static com.allenc.CommonsTestConstants.DATE_FORMAT_TO_MILLI;
import static com.allenc.util.DateValidator.isDateCloseTo;
import static com.allenc.util.DateValidator.isDateCloseToNow;

import com.allenc.TestBase;

import java.time.Instant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Copyright 2022
 */
class DateValidatorTest extends TestBase {

    // isDateCloseToNow
    @Test
    void isDateCloseToNowTrue() {
        Assertions.assertThat(DateValidator.isDateCloseToNow(getNowToMillis().plusSeconds(5).toString())).isTrue();
    }

    @Test
    void isDateCloseToNowFalse() {
        Assertions.assertThat(DateValidator.isDateCloseToNow(getNowToMillis().plusSeconds(20).toString())).isFalse();
    }

    @Test
    void isDateCloseToNowWithFormatTrue() {
        Assertions.assertThat(DateValidator.isDateCloseToNow(getNowToMillis().plusSeconds(5).toString(), DATE_FORMAT_TO_MILLI)).isTrue();
    }

    @Test
    void isDateCloseToNowWithFormatFalse() {
        Assertions.assertThat(DateValidator.isDateCloseToNow(getNowToMillis().plusSeconds(20).toString(), DATE_FORMAT_TO_MILLI)).isFalse();
    }

    @Test
    void isDateCloseToNowWithFormatTrueDifferentFormat() {
        Assertions.assertThat(DateValidator.isDateCloseToNow(Instant.now().plusSeconds(5).toString(), DATE_FORMAT_TO_MICRO)).isTrue();
    }

    @Test
    void isDateCloseToNowWithFormatFalseDifferentFormat() {
        Assertions.assertThat(DateValidator.isDateCloseToNow(Instant.now().plusSeconds(20).toString(), DATE_FORMAT_TO_MICRO)).isFalse();
    }

    // isDateCloseTo
    @Test
    void isDateCloseToTrue() {
        Assertions.assertThat(DateValidator.isDateCloseTo(getNowToMillis().toString(), getNowToMillis().plusSeconds(5))).isTrue();
    }

    @Test
    void isDateCloseToFalse() {
        Assertions.assertThat(DateValidator.isDateCloseTo(getNowToMillis().toString(), getNowToMillis().plusSeconds(20))).isFalse();
    }

    @Test
    void isDateCloseToWithAcceptableTimeDiffTrue() {
        Assertions.assertThat(isDateCloseTo(getNowToMillis().toString(), getNowToMillis().plusSeconds(5), 100000L)).isTrue();
    }

    @Test
    void isDateCloseToWithAcceptableTimeDiffFalse() {
        Assertions.assertThat(isDateCloseTo(getNowToMillis().toString(), getNowToMillis().plusSeconds(20), 10L)).isFalse();
    }

    @Test
    void isDateCloseToWithFormaTrue() {
        Assertions.assertThat(isDateCloseTo(getNowToMillis().toString(), getNowToMillis().plusSeconds(5), DATE_FORMAT_TO_MILLI)).isTrue();
    }

    @Test
    void isDateCloseToWithFormatFalse() {
        Assertions.assertThat(isDateCloseTo(getNowToMillis().toString(), getNowToMillis().plusSeconds(20), DATE_FORMAT_TO_MILLI)).isFalse();
    }

    @Test
    void isDateCloseToWithFormatWithAcceptableTimeDiffTrue() {
        Assertions.assertThat(DateValidator.isDateCloseTo(getNowToMillis().toString(), getNowToMillis().plusSeconds(5), DATE_FORMAT_TO_MILLI, 100000L)).isTrue();
    }

    @Test
    void isDateCloseToWithFormatWithAcceptableTimeDiffFalse() {
        Assertions.assertThat(DateValidator.isDateCloseTo(getNowToMillis().toString(), getNowToMillis().plusSeconds(20), DATE_FORMAT_TO_MILLI, 10L)).isFalse();
    }

    // isThisDateValid
    @Test
    void isThisDateValidValidDate() {
        Assertions.assertThat(DateValidator.isThisDateValid("2017-01-31T18:04:00.663Z", DATE_FORMAT_TO_MILLI)).isTrue();
    }

    @Test
    void isThisDateValidValidDateMillisToTheHundredth() {
        Assertions.assertThat(DateValidator.isThisDateValid("2017-01-31T18:04:00.99Z", DATE_FORMAT_TO_MILLI)).isTrue();
    }

    @Test
    void isThisDateValidValidDateMillisToTheTenth() {
        Assertions.assertThat(DateValidator.isThisDateValid("2017-01-31T18:04:00.6Z", DATE_FORMAT_TO_MILLI)).isTrue();
    }

    @Test
    void isThisDateValidValidDateNoMillis() {
        Assertions.assertThat(DateValidator.isThisDateValid("2017-01-31T18:04:59Z", DATE_FORMAT_TO_MILLI)).isTrue();
    }

    @Test
    void isThisDateValidDateIsInMillis() {
        Assertions.assertThat(DateValidator.isThisDateValid("1485885840917", DATE_FORMAT_TO_MILLI)).isFalse();
    }

    @Test
    void isThisDateValidDateIsNull() {
        Assertions.assertThat(DateValidator.isThisDateValid(null, DATE_FORMAT_TO_MILLI)).isFalse();
    }

    @Test
    void isThisDateValidDayIsInvalid() {
        Assertions.assertThat(DateValidator.isThisDateValid("2017-01-32T18:04:00.663Z", DATE_FORMAT_TO_MILLI)).isFalse();
    }

    @Test
    void isThisDateValidMonthIsInvalid() {
        Assertions.assertThat(DateValidator.isThisDateValid("2017-13-01T18:04:00.663Z", DATE_FORMAT_TO_MILLI)).isFalse();
    }

    @Test
    void isThisDateValidYearIsInvalid() {
        Assertions.assertThat(DateValidator.isThisDateValid("ZZZ-01-31T18:04:00.663Z", DATE_FORMAT_TO_MILLI)).isFalse();
    }

    @Test
    void isThisDateValidHourIsInvalid() {
        Assertions.assertThat(DateValidator.isThisDateValid("2017-01-01T25:04:00.663Z", DATE_FORMAT_TO_MILLI)).isFalse();
    }

    @Test
    void isThisDateValidMinuteIsInvalid() {
        Assertions.assertThat(DateValidator.isThisDateValid("2017-01-01T18:60:00.663Z", DATE_FORMAT_TO_MILLI)).isFalse();
    }

    @Test
    void isThisDateValidSecondIsInvalid() {
        Assertions.assertThat(DateValidator.isThisDateValid("2017-01-01T18:04:60.663Z", DATE_FORMAT_TO_MILLI)).isFalse();
    }
}
