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

import java.io.InputStream;
import objectos.http.RequestParser.Result;
import objectos.http.req.GetRequest;
import objectos.http.req.HeadRequest;
import objectos.lang.NoteSink;
import objectos.lang.TestingNoteSink;
import objectos.way.HappyPath;
import objectox.http.TestableInputStream;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RequestParserTest {

	private RequestParser parser;

	@BeforeClass
	public void beforeClass() {
		NoteSink noteSink;
		noteSink = TestingNoteSink.INSTANCE;

		parser = RequestParser.of(
				RequestParser.Option.noteSink(noteSink)
		);
	}

	@Test(description = "It should parse a GET request")
	@HappyPath
	public void testCase01() {
		String s = """
		GET / HTTP/1.1
		Host: www.example.com
		Connection: close

		""".replace("\n", "\r\n");

		InputStream in;
		in = TestableInputStream.of(s);

		Result result;
		result = parser.parse(in);

		assertEquals(result instanceof GetRequest, true);

		GetRequest res;
		res = (GetRequest) result;

		assertEquals(res.header(Http.Header.ACCEPT_ENCODING), null);
		assertEquals(res.header(Http.Header.HOST), "www.example.com");
		assertEquals(res.header(Http.Header.CONNECTION), "close");
		assertEquals(res.keepAlive(), false);
		assertEquals(res.path(), "/");
	}

	@Test(description = "It should parse a HEAD request")
	@HappyPath
	public void testCase02() {
		String s = """
		HEAD /index.html HTTP/1.1
		Host: www.example.com
		Connection: close

		""".replace("\n", "\r\n");

		InputStream in;
		in = TestableInputStream.of(s);

		Result result;
		result = parser.parse(in);

		assertEquals(result instanceof HeadRequest, true);

		HeadRequest res;
		res = (HeadRequest) result;

		assertEquals(res.header(Http.Header.ACCEPT_ENCODING), null);
		assertEquals(res.header(Http.Header.HOST), "www.example.com");
		assertEquals(res.header(Http.Header.CONNECTION), "close");
		assertEquals(res.keepAlive(), false);
		assertEquals(res.path(), "/index.html");
	}

}