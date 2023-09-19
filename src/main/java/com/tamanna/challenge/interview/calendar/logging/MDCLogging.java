package com.tamanna.challenge.interview.calendar.logging;

import org.apache.logging.log4j.ThreadContext;

/**
 * @author tlferreira
 */
public class MDCLogging {

    private MDCLogging() {
        //private
    }

    public static void putObjectMDC(String objectStr) {
        ThreadContext.put("object", objectStr);
    }

    public static void putObjectMDC(String objectStr, Object... params) {
        ThreadContext.put("object", String.format(objectStr, params));
    }
}
