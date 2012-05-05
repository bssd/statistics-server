package uk.co.statistics.server.client;

import java.util.HashSet;
import java.util.Set;

public class Listeners<M, L extends Listener<M>> {

	private final Set<L> listeners;

	public Listeners() {
		this.listeners = new HashSet<L>();
	}

	public synchronized void registerListener(L listener) {
		this.listeners.add(listener);
	}

	public synchronized void unregisterListener(L listener) {
		this.listeners.remove(listener);
	}

	public synchronized int countListeners() {
		return this.listeners.size();
	}

	public synchronized void notifyListeners(M message) {
		for (L listener : this.listeners) {
			try {
				listener.onMessage(message);
			} catch (RuntimeException e) {
				// TODO : log
			}
		}
	}
}