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
package objectox.http;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import objectos.lang.NoteSink;
import objectos.lang.TestingNoteSink;
import objectox.http.RequestParser.RequestResult;
import org.testng.annotations.Test;

public class RequestParserTest {

	@Test(description = "It should parse a GET request")
	public void testCase01() {
		String s = """
		GET / HTTP/1.1
		Host: www.example.com
		Connection: close

		""".replace("\n", "\r\n");

		RequestResult result;
		result = parse(s);

		assertEquals(result instanceof GetRequestResult, true);
	}

	private RequestParser.RequestResult parse(String s) {
		NoteSink noteSink;
		noteSink = TestingNoteSink.INSTANCE;

		InputStream in;
		in = TestableInputStream.of(s);

		RequestParser parser;
		parser = new RequestParser(noteSink, in);

		return parser.parse();
	}

}