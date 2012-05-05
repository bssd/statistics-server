package uk.co.statistics.server.client;

public interface Listener<M> {

	void onMessage(M message);
}