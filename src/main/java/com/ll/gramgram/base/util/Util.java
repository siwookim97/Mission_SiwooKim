package com.ll.gramgram.base.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class Util {

    public static String calculateRemainTime(LocalDateTime targetTime) {
        String remain = "";
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, targetTime);

        long durationSecond = duration.getSeconds();
        long hours = durationSecond / 3600;
        long minutes = (durationSecond % 3600) / 60;
        if (durationSecond % 60 != 0) {
            minutes++;
            if (minutes == 60) {
                minutes = 0;
                hours++;
            }
        }
        remain = hours + "시간 " + minutes + "분";

        return remain;
    }
}
