package uk.co.bssd.statistics.server.monitoring;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;

import uk.co.bssd.statistics.server.api.dto.AggregatedStatistics;
import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;
import uk.co.bssd.vesta.client.RpcClient;
import uk.co.bssd.vesta.server.websocket.WebSocketServer;
import uk.co.bssd.vesta.server.websocket.WebSocketServerPipelineFactory;
import uk.co.statistics.server.client.AggregatedStatisticsListener;
import uk.co.statistics.server.client.StatisticsClient;
import uk.co.statistics.server.client.tcp.TcpStatisticsClient;

public class StatisticsMonitoringServer implements AggregatedStatisticsListener {

	private static final InternalLogger logger = InternalLoggerFactory
			.getInstance(WebSocketServer.class);

	private final int port;
	private final ChannelGroup channelGroup;
	private final StatisticsClient statisticsClient;

	public StatisticsMonitoringServer(int port) {
		this.port = port;
		this.channelGroup = new DefaultChannelGroup(getClass().getName());
		this.statisticsClient = new TcpStatisticsClient(new RpcClient(), "localhost", 8081);
	}

	public void start() {
		this.statisticsClient.start();
		this.statisticsClient.registerAggregatedStatisticsListener(this);
		
		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Set up the event pipeline factory.
		bootstrap.setPipelineFactory(new WebSocketServerPipelineFactory(this.channelGroup));

		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(port));

		logger.info("Web socket server started at port " + port + '.');
		logger.info("Open your browser and navigate to http://localhost:"
				+ port + '/');
	}
	
	@Override
	public void onMessage(AggregatedStatisticsMessage message) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			
			List<Map<String, Object>> aggregatedStats = new ArrayList<Map<String,Object>>();
			
			for (AggregatedStatistics s : message) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", s.timingPointName());
				map.put("count", s.timingPoingCount());
				map.put("average", s.averageMillisecondTime());
				map.put("max", s.maximumMillisecondTime());;
				map.put("min", s.minimumMillisecondTime());
				aggregatedStats.add(map);
			}
			
			Map<String, List<Map<String, Object>>> rootMap = new HashMap<String, List<Map<String,Object>>>();
			rootMap.put("statistics", aggregatedStats);
			String s = mapper.writeValueAsString(rootMap);
			broadcast(s);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void broadcast(String message) {
		this.channelGroup.write(new TextWebSocketFrame(message));
	}

	public static void main(String[] args) {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		StatisticsMonitoringServer server = new StatisticsMonitoringServer(port);
		server.start();
	}
}
