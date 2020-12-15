package bgu.spl.mics;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private ConcurrentHashMap<Event, Future> futureMap;
	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> mb;
	private ConcurrentHashMap<Class<? extends Event>, ArrayList<MicroService>> eventSub;
	private ConcurrentHashMap<Class<? extends Broadcast>, ArrayList<MicroService>> broadcastSub;
	private AtomicInteger count = new AtomicInteger(0);


	private static MessageBusImpl instance = null;

	public static MessageBusImpl getInstance() {
		if (instance==null){
			instance = new MessageBusImpl();
		}
		return instance;
	}

	private MessageBusImpl() {
		mb = new ConcurrentHashMap<>();
		eventSub = new ConcurrentHashMap<>();
		broadcastSub = new ConcurrentHashMap<>();
		futureMap = new ConcurrentHashMap<>();
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if (!eventSub.containsKey(type))
			eventSub.put(type, new ArrayList<>());
		eventSub.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (!broadcastSub.containsKey(type))
			broadcastSub.put(type, new ArrayList<>());
		broadcastSub.get(type).add(m);
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		futureMap.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (b.getClass()) {
			ArrayList<MicroService> temp = broadcastSub.get(b.getClass());
			for (int i = 0; i < temp.size(); i++) {
				MicroService m = temp.get(i);
				mb.get(m).add(b);
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = new Future<>();
		synchronized (e.getClass()) {
			ArrayList<MicroService> temp = eventSub.get(e.getClass());
			if (count.intValue() < temp.size()) {
				mb.get(this).add(e);
				count.incrementAndGet();
				if (!futureMap.containsKey(e))
					futureMap.put(e, future);
			}
			else {
				count = new AtomicInteger(0);
				 future = sendEvent(e);
			}
		}
		return future;
	}


	@Override
	public void register(MicroService m) {
		if (!mb.containsKey(m))
			mb.put(m, new LinkedBlockingDeque<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		mb.get(m).remove();
		for (Class<? extends Event> key:eventSub.keySet()) {
			if (eventSub.get(key).contains(m))
				eventSub.get(key).remove(m);
		}
		for (Class<? extends Broadcast> key:broadcastSub.keySet()) {
			if (broadcastSub.get(key).contains(m))
				broadcastSub.get(key).remove(m);
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		return mb.get(m).remove();
	}

}
