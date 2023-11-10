/*
 * Copyright (C) 2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.http;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import objectos.lang.NoteSink;
import objectos.lang.TestingNoteSink;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MarketingSiteTest implements SocketTaskFactory {

	private RequestParser requestParser;

	@SuppressWarnings("unused")
	private Thread server;

	private ServerSocket serverSocket;

	@BeforeClass
	public void beforeClass() throws IOException, InterruptedException {
		NoteSink noteSink;
		noteSink = TestingNoteSink.INSTANCE;

		requestParser = RequestParser.of(
				RequestParser.Option.noteSink(noteSink)
		);

		int randomPort;
		randomPort = 0;

		int backlogDefaultValue;
		backlogDefaultValue = 50;

		InetAddress address;
		address = InetAddress.getLoopbackAddress();

		serverSocket = new ServerSocket(randomPort, backlogDefaultValue, address);

		server = new TestingServer(noteSink, serverSocket, this);

		server.start();

		synchronized (this) {
			TimeUnit.SECONDS.timedWait(this, 2);
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		server.interrupt();
	}

	@Override
	public final Runnable createTask(Socket socket) {
		NoteSink noteSink;
		noteSink = TestingNoteSink.INSTANCE;

		return new MarketingSiteTask(noteSink, requestParser, socket);
	}

	@Test(enabled = false, description = """
	it should redirect '/' to '/index.html'
	""")
	public void testCase01() throws IOException {
		try (Socket socket = newSocket()) {
			req(socket, """
			GET / HTTP/1.1
			Host: www.example.com
			Connection: close

			""".replace("\n", "\r\n"));

			resp(socket, """
      HTTP/1.1 200 OK<CRLF>
      Content-Type: text/plain; charset=utf-8<CRLF>
      Content-Length: 13<CRLF>
      Date: Wed, 28 Jun 2023 12:08:43 GMT<CRLF>
      <CRLF>
      Hello World!
			""".replace("<CRLF>\n", "\r\n"));
		}
	}

	private Socket newSocket() throws IOException {
		return new Socket(serverSocket.getInetAddress(), serverSocket.getLocalPort());
	}

	private void req(Socket socket, String string) throws IOException {
		OutputStream out;
		out = socket.getOutputStream();

		byte[] bytes;
		bytes = string.getBytes(StandardCharsets.UTF_8);

		out.write(bytes);
	}

	private void resp(Socket socket, String expected) throws IOException {
		byte[] expectedBytes;
		expectedBytes = expected.getBytes(StandardCharsets.UTF_8);

		InputStream in;
		in = socket.getInputStream();

		byte[] bytes;
		bytes = in.readNBytes(expectedBytes.length);

		String res;
		res = new String(bytes, StandardCharsets.UTF_8);

		assertEquals(res, expected);
	}

}