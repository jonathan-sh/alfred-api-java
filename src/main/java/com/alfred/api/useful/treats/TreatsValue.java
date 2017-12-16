package com.alfred.api.useful.treats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TreatsValue {
    private static Logger log = LoggerFactory.getLogger(TreatsValue.class);

    public static void nullOrEmpty(String value) {
        boolean status = value != null && !value.isEmpty();
        if (!status)
        {
            String msg = "Valor nulo ou vazio.";
            log.error(msg);
            throw new RuntimeException(msg);
        }
    }

    public static Integer[] toIntegerArrayFromLocalDateTime(LocalDateTime localDateTime) {
        Integer[] result = new Integer[6];
        result[0] = localDateTime.getYear();
        result[1] = localDateTime.getMonthValue();
        result[2] = localDateTime.getDayOfMonth();
        result[3] = localDateTime.getHour();
        result[4] = localDateTime.getMinute();
        result[5] = localDateTime.getSecond();
        return result;
    }

    public static LocalDateTime toLocalDateTimeFromArrayInteger(Integer[] start) {
        return LocalDateTime.of(start[0],start[1],start[2],start[3],start[4],start[5]);
    }

    public static Long getLogFromArrayDateTime(Integer[] dateTime)
    {
        try
        {
            return toLocalDateTimeFromArrayInteger(dateTime).atZone(ZoneId.systemDefault())
                                                            .toInstant()
                                                            .toEpochMilli();
        }
        catch (Exception e)
        {

        }
        return  0L;
    }

    public static Long getLogFromLocalDateTime(LocalDateTime localDateTime)
    {
        try
        {
            return localDateTime.atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli();
        }
        catch (Exception e)
        {

        }
        return  0L;
    }
}


