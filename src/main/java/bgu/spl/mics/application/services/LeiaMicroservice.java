package bgu.spl.mics.application.services;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.*;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Attack;
/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }
    @Override
    protected void initialize() { }

    protected void initialize(JsonArray list) {
    	attacks = new Attack[list.size()];
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

    }
}
