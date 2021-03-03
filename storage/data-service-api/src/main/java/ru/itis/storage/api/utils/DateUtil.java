package ru.itis.storage.api.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtil {
    public static Date convertToDate(LocalDateTime dateToConvert) {
        return convertToDate(dateToConvert, ZoneOffset.UTC);
    }

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return convertToLocalDateTime(dateToConvert, ZoneOffset.UTC);
    }

    public static Date convertToDate(LocalDateTime dateToConvert, ZoneId zoneId) {
        if(dateToConvert == null) {
            return null;
        }
        return java.util.Date
                .from(dateToConvert.atZone(zoneId)
                        .toInstant());
    }

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert, ZoneId zoneId) {
        if(dateToConvert == null) {
            return null;
        }
        return dateToConvert.toInstant()
                .atZone(zoneId)
                .toLocalDateTime();
    }
}
