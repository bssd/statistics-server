package uk.co.bssd.statistics.server.monitoring;

import java.util.Random;
import java.util.UUID;

import uk.co.bssd.vesta.client.RpcClient;
import uk.co.statistics.server.client.TimingPointClient;
import uk.co.statistics.server.client.tcp.TcpTimingPointClient;

public class TimingPointInjector {

	private static final String TIMING_POINT = "timing_point";
	
	public static void main(String[] args) {
		RpcClient rpcClient = new RpcClient();
		rpcClient.start("localhost", 8081, 1000);
		
		TimingPointClient timingPointClient = new TcpTimingPointClient(rpcClient);
		
		while(true) {
			UUID uuid = UUID.randomUUID();
			timingPointClient.startTimingPoint(uuid, TIMING_POINT);
			Random random = new Random(System.currentTimeMillis());
			int sleepTime = random.nextInt(30);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			timingPointClient.stopTimingPoint(uuid, TIMING_POINT);
		}
	}
}