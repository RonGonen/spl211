package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TestBroadcast;
import bgu.spl.mics.application.messages.TestEvent;
import bgu.spl.mics.application.services.TestMicroService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    MessageBusImpl mb = MessageBusImpl.getInstance();
    TestEvent testEvent = new TestEvent();
    Broadcast testBroadcast = new TestBroadcast();
    MicroService m = new TestMicroService();

    @BeforeEach
    void setUp() {
        mb.register(m);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void complete() {
        mb.subscribeEvent(testEvent.getClass(), m);
        m.complete(testEvent,true);
        assertTrue(testEvent.getVal());
    }

    @Test
    void sendBroadcast() {
        mb.subscribeBroadcast(testBroadcast.getClass(), m);
        mb.sendBroadcast(testBroadcast);
        try {
            Message mes = mb.awaitMessage(m);
            assertSame(mes, testBroadcast);
        } catch (InterruptedException e) {
        }
    }

    @Test
    void sendEvent() {
        mb.subscribeEvent(testEvent.getClass(), m);
        mb.sendEvent(testEvent);
        try {
            Message mes = mb.awaitMessage(m);
            assertSame(mes, testEvent);
        } catch (InterruptedException e) {
        }
    }

    @Test
    void awaitMessage() {
        mb.subscribeEvent(testEvent.getClass(), m);
        mb.sendEvent(testEvent);
        Message mes = null;
        try {
            mes = mb.awaitMessage(m);
        } catch (InterruptedException e) {
        }
        assertSame(testEvent, mes);
    }
}