package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBusImpl;

import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private LinkedList<Ewok> ewoks;
    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }

    private Ewoks() {

    }

    public void initialize (int size) {
        ewoks = new LinkedList<>();
        for (int i=1; i<=size; i++) {
            Ewok newEwok = new Ewok(i);
            ewoks.add(newEwok);
        }
    }
    public static Ewoks getInstance() {
        return SingletonHolder.instance;
    }
}
