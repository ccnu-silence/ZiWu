package com.github.tinkerti.ziwu.ui.utils;

public class FormatTime {

    public static String formatTimeToNumberString(long timeMillis) {
        long timeSeconds = timeMillis / 1000;
        StringBuilder builder = new StringBuilder();
        if (timeSeconds < 60) {
            builder.append("00:00:")
                    .append(formatTime(timeSeconds));
        } else if (timeSeconds < 60 * 60) {
            long minutes = timeSeconds / 60;
            long seconds = timeSeconds % 60;
            builder.append("00:")
                    .append(formatTime(minutes))
                    .append(":")
                    .append(formatTime(seconds));
        } else if (timeSeconds < 99 * 3600) {
            long hours = timeSeconds / 3600;
            long rest = timeSeconds % 3600;
            long minutes = rest / 60;
            long seconds = rest % 60;
            builder.append(formatTime(hours))
                    .append(":")
                    .append(formatTime(minutes))
                    .append(":")
                    .append(formatTime(seconds));
        } else {
            long days = timeSeconds / (24 * 3600);
            long restTime = timeSeconds % (24 * 3600);
            long hours = restTime / 3600;
            long rest = restTime % 3600;
            long minutes = rest / 60;
            long seconds = rest % 60;
            builder.append(formatTime(days))
                    .append("天")
                    .append(formatTime(hours))
                    .append(":")
                    .append(formatTime(minutes))
                    .append(":")
                    .append(formatTime(seconds));
        }
        return builder.toString();
    }

    public static String formatTimeToUnitString(long timeMillis) {
        long timeSeconds = timeMillis / 1000;
        StringBuilder builder = new StringBuilder();
        if (timeSeconds < 60) {
            builder.append(timeSeconds)
                    .append("s ");
        } else if (timeSeconds < 60 * 60) {
            long minutes = timeSeconds / 60;
            long seconds = timeSeconds % 60;
            builder.append(minutes)
                    .append("min ")
                    .append(seconds).append("s ");
        } else if (timeSeconds < 99 * 3600) {
            long hours = timeSeconds / 3600;
            long rest = timeSeconds % 3600;
            long minutes = rest / 60;
            long seconds = rest % 60;
            builder.append(hours)
                    .append("h ")
                    .append(minutes)
                    .append("min");
        } else {
            long days = timeSeconds / (24 * 3600);
            long restTime = timeSeconds % (24 * 3600);
            long hours = restTime / 3600;
            long rest = restTime % 3600;
            long minutes = rest / 60;
            long seconds = rest % 60;
            builder.append(days)
                    .append("day ")
                    .append(hours)
                    .append("h ");
        }
        return builder.toString();
    }

    private static String formatTime(long time) {
        StringBuilder builder = new StringBuilder();
        if (time < 10) {
            builder.append("0")
                    .append(String.valueOf(time));
        } else {
            builder.append(String.valueOf(time));
        }
        return builder.toString();
    }
}