package com.dongVu1105.libraryManagement.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class DateTimeFormat {
    public String format (Instant instant){
        if (Objects.isNull(instant)){
            return "";
        }
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        if(elapseSeconds < 60 ){
            return elapseSeconds + " seconds";
        } else if (elapseSeconds < 60*60){
            long elapseMinute = ChronoUnit.MINUTES.between(instant, Instant.now());
            return elapseMinute + " minutes";
        } else if (elapseSeconds < 60 * 60 * 24) {
            long elapseHour = ChronoUnit.HOURS.between(instant, Instant.now());
            return elapseHour + " hours";
        }
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_DATE;
        return localDateTime.format(dateTimeFormatter);
    }
}
