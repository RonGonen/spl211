package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class TestEvent implements Event<Boolean> {
    boolean testVal;
    public TestEvent() {
        testVal = false;
    }
    public boolean getVal() {
        return testVal;
    }

}
