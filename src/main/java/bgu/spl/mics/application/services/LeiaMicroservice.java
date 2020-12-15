package bgu.spl.mics.application.services;

import java.util.LinkedList;
import java.util.List;

import bgu.spl.mics.Future;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import com.google.gson.*;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Attack;
/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private List<Future> attackFuture;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }
    @Override
    protected void initialize() { }

    protected void initialize(JsonArray list) {
    	attacks = new Attack[list.size()];
    	attackFuture = new LinkedList<>();
    	for (int i=0; i<attacks.length;i++) {
    	    JsonObject attack = list.get(i).getAsJsonObject();
    	    List<Integer> serials = new LinkedList<>();
    	    int duration = attack.get("duration").getAsInt();
    	    JsonArray arr = attack.get("serials").getAsJsonArray();
    	    for (int j=0; j<arr.size();j++) {
    	        serials.add(arr.get(j).getAsInt());
            }
    	    Attack a = new Attack(serials, duration);
    	    attacks[i] = a;
        }
//    	LeiaMicroservice leia = new LeiaMicroservice(attacks);
		for (int i=0; i<attacks.length;i++) {
			AttackEvent e = new AttackEvent(attacks[i]);
			attackFuture.add(sendEvent(e));
		}
		for (int i=0; i<attackFuture.size();i++) {
			if (!attackFuture.get(i).isDone())
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
		}
		DeactivationEvent d = new DeactivationEvent();
		Future deactivationFuture = sendEvent(d);
		if (!deactivationFuture.isDone())
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		BombDestroyerEvent bomb = new BombDestroyerEvent();
		Future bombFuture = sendEvent(bomb);
    }

}
