package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    private List<Integer> resources;
    private int duration;

    public AttackEvent(Attack a){
        resources = a.getSerials();
        duration = a.getDuration();
    }
}
