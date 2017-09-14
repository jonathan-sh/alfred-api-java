package com.alfred.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

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
}


