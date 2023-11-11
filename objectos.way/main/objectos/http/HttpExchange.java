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
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Set;
import objectos.http.Http.Method;
import objectos.http.server.Body;
import objectos.lang.Note1;
import objectos.lang.NoteSink;
import objectox.lang.CharWritable;
import objectox.lang.Check;

/**
 * Represents the server-side view of an HTTP exchange. This class allows for
 * writing an HTTP server.
 */
public sealed interface HttpExchange extends AutoCloseable permits objectox.http.HttpExchange {

	/**
	 * Note indicating an I/O read error occurred.
	 */
	Note1<IOException> IO_READ_ERROR = Note1.error(HttpExchange.class, "I/O read error");

	Note1<Processed> PROCESSED = Note1.trace(HttpExchange.class, "Processed");

	Note1<Set<String>> UKNOWN_HEADER_NAMES = Note1.trace(HttpExchange.class, "Unknown header names");

	sealed interface Processed permits objectox.http.HttpExchange.ProcessedRecord {
		SocketAddress remoteAddress();

		Method method();

		String target();

		Http.Status status();

		long processingTime();
	}

	/**
	 * Configures the creation of a {@link HttpExchange} instance.
	 */
	sealed interface Option permits objectox.http.HttpExchange.OptionValue {

		/**
		 * Defines the size in bytes of the buffer.
		 *
		 * @param size
		 *        the buffer size
		 *
		 * @return an option instance
		 */
		static Option bufferSize(int size) {
			Check.argument(size > 128, "buffer size must be > 128");

			return objectox.http.HttpExchange.OptionValue.bufferSize(size);
		}

		/**
		 * Defines the note sink to be used by the http exchange instance.
		 *
		 * @param noteSink
		 *        the note sink instance
		 *
		 * @return an option instance
		 */
		static Option noteSink(NoteSink noteSink) {
			Check.notNull(noteSink, "noteSink == null");

			return objectox.http.HttpExchange.OptionValue.noteSink(noteSink);
		}

	}

	@SuppressWarnings("resource")
	static HttpExchange of(Socket socket) {
		Check.notNull(socket, "socket == null");

		objectox.http.HttpExchange instance;
		instance = new objectox.http.HttpExchange(socket);

		return instance.init();
	}

	static HttpExchange of(Socket socket, Option option) {
		Check.notNull(socket, "socket == null");
		Check.notNull(option, "option == null");

		objectox.http.HttpExchange instance;
		instance = new objectox.http.HttpExchange(socket);

		((objectox.http.HttpExchange.OptionValue) option).accept(instance);

		return instance.init();
	}

	static HttpExchange of(Socket socket, Option option1, Option option2) {
		Check.notNull(socket, "socket == null");
		Check.notNull(option1, "option1 == null");
		Check.notNull(option2, "option2 == null");

		objectox.http.HttpExchange instance;
		instance = new objectox.http.HttpExchange(socket);

		((objectox.http.HttpExchange.OptionValue) option1).accept(instance);

		((objectox.http.HttpExchange.OptionValue) option2).accept(instance);

		return instance.init();
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

	Http.Header.Value header(Http.Header.Name name);

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

	String segment(int index);

	/**
	 * Returns all of the segments that makes up the path component of this
	 * request's target as a relative {@link Path} instance. The returned is path
	 * instance is {@linkplain Path#normalize() normalized}.
	 *
	 * <p>
	 * Examples:
	 *
	 * <pre>
	 * GET / HTTP/1.1                   => Path.of("")
	 * POST /foo HTTP/1.1               => Path.of("foo")
	 * DELETE /foo/ HTTP/1.1            => Path.of("foo", "")
	 * HEAD /dir/foo.html HTTP/1.1      => Path.of("dir", "foo.html")
	 * GET /dir/foo.html?q=abc HTTP/1.1 => Path.of("dir", "foo.html")
	 * GET /dir/../foo.html HTTP/1.1    => Path.of("foo.html")
	 * </pre>
	 *
	 * @return a newly created and normalized {@link Path} instance
	 */
	Path segmentsAsPath();

	// Response methods

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
