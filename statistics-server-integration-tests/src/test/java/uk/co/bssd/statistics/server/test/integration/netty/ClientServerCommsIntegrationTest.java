package uk.co.bssd.statistics.server.test.integration.netty;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.bssd.netty.client.RpcClient;
import uk.co.bssd.netty.server.RpcServer;
import uk.co.bssd.statistics.server.api.dto.StartTimingPointMessage;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointMessage;
import uk.co.bssd.statistics.server.api.service.TimingPointService;
import uk.co.bssd.statistics.server.application.rpc.handler.TimingPointServiceHandlers;
import uk.co.bssd.statistics.server.application.service.StatisticsServerFacade;
import uk.co.statistics.server.client.service.ClientTimingPointService;

@RunWith(MockitoJUnitRunner.class)
public class ClientServerCommsIntegrationTest {

	private static final String HOSTNAME = "127.0.0.1";
	private static final int PORT = 6363;

	private static final int CLIENT_CONNECT_TIMEOUT_MILLIS = 1000;
	private static final int SERVER_RECEIVE_MESSAGE_TIMEOUT_MILLIS = 1000;

	private static final UUID TIMING_POINT_ID = UUID.randomUUID();
	private static final String TIMING_POINT_NAME = "timing_point";

	private RpcClient rpcClient;
	private TimingPointService clientService;

	private RpcServer rpcServer;

	@Mock
	private StatisticsServerFacade mockServerFacade;

	@Before
	public void before() {
		this.rpcClient = new RpcClient();
		this.clientService = new ClientTimingPointService(this.rpcClient);

		this.rpcServer = new RpcServer();
		TimingPointServiceHandlers.bindHandlers(this.rpcServer,
				this.mockServerFacade);

		this.rpcServer.start(HOSTNAME, PORT);
		this.rpcClient.start(HOSTNAME, PORT, CLIENT_CONNECT_TIMEOUT_MILLIS);
	}

	@After
	public void after() {
		this.rpcClient.stop();
		this.rpcServer.stop();
	}

	@Test
	public void testStartingATimingPointOnTheClientInvokesTheStartTimingPointMethodOnTheServer() {
		this.clientService.startTimingPoint(TIMING_POINT_ID, TIMING_POINT_NAME);
		verify(this.mockServerFacade,
				timeout(SERVER_RECEIVE_MESSAGE_TIMEOUT_MILLIS))
				.startTimingPoint(any(StartTimingPointMessage.class));
	}

	@Test
	public void testStoppingATimingPointOnTheClientInvokesTheStopTimingPointMethodOnTheServer() {
		this.clientService.stopTimingPoint(TIMING_POINT_ID, TIMING_POINT_NAME);
		verify(this.mockServerFacade,
				timeout(SERVER_RECEIVE_MESSAGE_TIMEOUT_MILLIS))
				.stopTimingPoint(any(StopTimingPointMessage.class));
	}
}