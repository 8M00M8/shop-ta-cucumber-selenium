package lv.m8008m.steps;

import lv.m8008m.driver.WDriver;

public class SharedContext {

    public static ThreadLocal<String> scenarioID = ThreadLocal.withInitial(() -> "");
    public static ThreadLocal<String> scenarioName = ThreadLocal.withInitial(() -> "");

    public WDriver extraDriver;
}
