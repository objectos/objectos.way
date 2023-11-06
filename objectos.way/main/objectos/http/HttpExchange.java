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
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Path;
import objectos.http.Http.Method;
import objectos.http.server.Body;
import objectos.lang.NoOpNoteSink;
import objectos.lang.NoteSink;
import objectox.lang.CharWritable;
import objectox.lang.Check;

/**
 * Represents the server-side view of an HTTP exchange. This class allows for
 * writing an HTTP server.
 */
public sealed interface HttpExchange extends AutoCloseable
		permits objectox.http.HttpExchange {

	static HttpExchange of(Socket socket) {
		return of(socket, 1024);
	}

	static HttpExchange of(Socket socket, int bufferSize) {
		Check.notNull(socket, "socket == null");
		Check.argument(bufferSize > 128, "buffer size must be > 128");

		NoteSink noteSink;
		noteSink = NoOpNoteSink.of();

		return new objectox.http.HttpExchange(bufferSize, noteSink, socket);
	}

	/**
	 * Closes and ends this exchange by closing its underlying socket.
	 *
	 * @throws IOException
	 *         if an I/O error occurs
	 */
	@Override
	void close() throws IOException;

	boolean active();

	// Request methods

	boolean matches(Segment pat);

	boolean matches(Segment pat1, Segment pat2);

	/**
	 * Returns the request HTTP method.
	 *
	 * @return the request HTTP method
	 *
	 * @throws IllegalStateException
	 *         if the request has not been parsed or if the response has already
	 *         been sent to the client.
	 */
	Http.Method method();

	/**
	 * Tests if the request HTTP method is equal to one of the two specified
	 * methods.
	 *
	 * @param m1 the first method candidate
	 * @param m2 the second method candidate
	 *
	 * @return {@code true} if (and only if) the request method is equal to
	 *         {@code m1} or is equal to {@code m2}
	 */
	boolean methodIn(Method m1, Method m2);

	/**
	 * Returns the decoded path component of the request target.
	 *
	 * @return the decoded path component of the request target.
	 */
	String path();

	/**
	 * Compares the decoded path component of this request's target to the
	 * specified string. Returns {@code true} if the decoded path is equal to the
	 * specified string.
	 *
	 * @param s the string to compare for equality
	 *
	 * @return {@code true} if the decoded path is equal to the specified string;
	 *         {@code false} otherwise
	 */
	boolean pathEquals(String s);

	/**
	 * Tests if the decoded path component of this request's target starts with
	 * the specified prefix.
	 *
	 * @param prefix the prefix
	 *
	 * @return {@code true} if the decoded path starts with specified prefix;
	 *         {@code false} otherwise.
	 */
	boolean pathStartsWith(String prefix);

	Path toRelativePath();

	Path resolveAgainst(Path directory);

	String segment(int index);

	// Response methods

	Http.Header.Value header(Http.Header.Name name);

	Body body();

	boolean hasResponse();

	Http.Status status();

	/**
	 * Tests if a status value is present.
	 *
	 * @return {@code true} if a status value is present; {@code false} otherwise
	 */
	boolean statusPresent();

	void status(Http.Status status);

	void header(Http.Header.Name name, String value);

	void body(byte[] data);

	void body(CharWritable entity, Charset charset);

}
