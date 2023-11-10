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

import java.io.InputStream;
import objectos.lang.LongNote;
import objectos.lang.NoteSink;
import objectox.http.RequestParserFacade;
import objectox.http.RequestParserFacade.OptionValue;
import objectox.lang.Check;

/**
 * A parser for HTTP requests.
 *
 * <p>
 * Instances of this type are immutable and are safe to be used concurrently by
 * multiple threads.
 */
public sealed interface RequestParser permits RequestParserFacade {

	LongNote READ_BYTES = LongNote.debug(RequestParser.class, "Read bytes");

	/**
	 * Configures the creation of a {@link RequestParser} instance.
	 */
	sealed interface Option permits OptionValue {

		/**
		 * Defines the note sink instance to be used by the parser.
		 *
		 * @param noteSink
		 *        the note sink instance
		 *
		 * @return an option instance
		 */
		static Option noteSink(NoteSink noteSink) {
			Check.notNull(noteSink, "noteSink == null");

			return new OptionValue() {
				@Override
				public final void accept(RequestParserFacade facade) {
					facade.noteSink(noteSink);
				}
			};
		}

	}

	/**
	 * Represents the result of a parse operation.
	 */
	sealed interface Result permits Request {}

	/**
	 * Creates a request parser with the specified option.
	 *
	 * @param option
	 *        the option
	 *
	 * @return a request parser instance configured with the specified option
	 */
	static RequestParser of(Option option) {
		Check.notNull(option, "option == null");

		RequestParserFacade facade;
		facade = new RequestParserFacade();

		apply(facade, option);

		return facade;
	}

	private static void apply(RequestParserFacade facade, Option option) {
		OptionValue safe;
		safe = (OptionValue) option;

		safe.accept(facade);
	}

	/**
	 * Parses an HTTP request from the bytes provided by specified input stream.
	 *
	 * @param inputStream
	 *        the input stream
	 *
	 * @return a result instance possibly representing an HTTP request
	 */
	Result parse(InputStream inputStream);

}