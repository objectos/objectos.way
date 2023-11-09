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

import java.io.InputStream;
import objectos.http.RequestParser;
import objectos.lang.NoOpNoteSink;
import objectos.lang.NoteSink;
import objectox.lang.Check;

public final class RequestParserFacade implements RequestParser {

	public non-sealed static abstract class OptionValue implements RequestParser.Option {

		public abstract void accept(RequestParserFacade facade);

	}

	private NoteSink noteSink = NoOpNoteSink.of();

	public RequestParserFacade() {
	}

	public final void noteSink(NoteSink noteSink) {
		this.noteSink = noteSink;
	}

	@Override
	public final Result parse(InputStream inputStream) {
		Check.notNull(inputStream, "inputStream == null");

		InputStreamRequestParser parser;
		parser = new InputStreamRequestParser(noteSink, inputStream);

		return parser.parse();
	}

}
