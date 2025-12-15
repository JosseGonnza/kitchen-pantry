package org.jossegonnza.kitchenpantry.infrastructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

public class LogHelper {

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void setRequestId(String requestId) {
        MDC.put("request_id", requestId);
    }

    public static String generateRequestId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static void clearMDC() {
        MDC.clear();
    }

    public static void setUserId(String userId) {
        MDC.put("user_id", userId);
    }
}