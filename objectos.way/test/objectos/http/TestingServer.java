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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import objectos.lang.Note0;
import objectos.lang.Note1;
import objectos.lang.NoteSink;

final class TestingServer extends Thread {

	private final NoteSink noteSink;

	private final ServerSocket serverSocket;

	private final SocketTaskFactory taskFactory;

	public TestingServer(NoteSink noteSink, ServerSocket serverSocket, SocketTaskFactory taskFactory) {
		this.noteSink = noteSink;

		this.serverSocket = serverSocket;

		this.taskFactory = taskFactory;
	}

	@Override
	public final void run() {
		Note1<ServerSocket> startNote;
		startNote = Note1.info(getClass(), "Start");

		noteSink.send(startNote, serverSocket);

		try (serverSocket) {
			ThreadFactory factory;
			factory = Thread.ofVirtual().name("http-", 1).factory();

			try (ExecutorService executor = Executors.newThreadPerTaskExecutor(factory)) {
				while (!isInterrupted()) {
					Socket socket;
					socket = serverSocket.accept();

					Runnable task;
					task = taskFactory.createTask(socket);

					executor.submit(task);
				}
			}
		} catch (IOException e) {
			Note1<IOException> errorNote;
			errorNote = Note1.info(getClass(), "I/O error");

			noteSink.send(errorNote, e);
		}

		Note0 stopNote;
		stopNote = Note0.info(getClass(), "Stop");

		noteSink.send(stopNote);
	}

}