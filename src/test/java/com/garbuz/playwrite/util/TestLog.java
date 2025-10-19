package com.garbuz.playwrite.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class TestLog {

    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    private final List<String> log = Collections.synchronizedList(new ArrayList<>());

    public final void log(final String messageToLog) {
        final String timestamp = LocalDateTime.now().format(FORMATTER);
        log.add(String.format("[%s] %s", timestamp, messageToLog));
    }

    public final String getMessages() {
        final StringBuilder out = new StringBuilder();
        synchronized (log) {
            for (String message : log) {
                out.append(message).append(System.lineSeparator());
            }
        }
        return out.toString();
    }

    public final void clear() {
        log.clear();
    }
}
