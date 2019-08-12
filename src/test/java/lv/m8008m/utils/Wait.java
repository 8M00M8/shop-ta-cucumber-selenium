package lv.m8008m.utils;

public class Wait {
    public static final long PAGE_LOAD_TIMEOUT_S = get("timeout.pageload.s");
    public static final long SCRIPT_TIMEOUT_S = get("timeout.script.s");

    public static final long IMPLICIT_WAIT_TIMEOUT_S = get("timeout.wait.implicit.s");

    public static final long EXPLICIT_WAIT_TIMEOUT_S = get("timeout.wait.explicit.s");
    public static final long POLLING_EXPLICIT_WAIT_TIMEOUT_MS = get("timeout.wait.explicit.polling.ms");

    public static final long JQUERY_EXPLICIT_WAIT_TIMEOUT_S = get("timeout.wait.explicit.jquery.s");
    public static final long JQUERY_POLLING_EXPLICIT_WAIT_TIMEOUT_MS = get("timeout.wait.explicit.polling.jquery.ms");

    public static final long ACTIONS_PAUSE_TIME_MS = get("time.pause.actions.ms");
    public static final long SLEEP_TIME_MS = get("time.sleep.ms");

    private static long get(String name) {
        if (System.getProperty(name, "").matches("^\\d+$"))
            return Long.parseLong(System.getProperty(name));
        else if (ConfigProperties.getProperty(name, "").matches("^\\d+$"))
            return Long.parseLong(ConfigProperties.getProperty(name));
        else
            return 0;
    }
}
