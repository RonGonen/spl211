package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TestBroadcast implements Broadcast {
    boolean testVal;
    public TestBroadcast() {
        testVal = false;
    }
    public boolean getVal() {
        return testVal;
    }
}
