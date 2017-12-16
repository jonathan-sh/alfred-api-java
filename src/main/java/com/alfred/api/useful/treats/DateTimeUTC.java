package com.alfred.api.useful.treats;

import com.alfred.api.useful.AlfredConfig;
import com.alfred.api.useful.constants.App;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class DateTimeUTC {

    public static GenericDateTimeMethods setUtc(Integer utc) {
        return new GenericDateTimeMethods().setLocalTimeZone(utc);
    }

    public static LocalDateTime now() {
        return new GenericDateTimeMethods().getLocateDateTimeUtc();
    }

    public static LocalDateTime applyUtc(LocalDateTime localDateTime, Integer utc) {
        return localDateTime.plusHours(utc);
    }


    public static class GenericDateTimeMethods {

        private ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);
        private Integer localUTC = AlfredConfig.UTC;

        private GenericDateTimeMethods setLocalTimeZone(Integer utc) {
            this.localUTC = utc;
            return this;
        }

        public LocalDateTime getLocateDateTimeUtc() {
            return nowUTC.toLocalDateTime().plusHours(localUTC);
        }

    }
}
