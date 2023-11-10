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

import java.nio.file.Path;
import objectos.http.Http.Header.Name;

public sealed interface Request extends RequestParser.Result
		permits
		GetRequest,
		HeadRequest {

	String header(Name name);

	boolean keepAlive();

	boolean matches(Segment seg);

	boolean matches(Segment seg1, Segment seg2);

	/**
	 * Returns the decoded path component of this request's target.
	 *
	 * @return the decoded path component of this request's target.
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

	String segment(int index);

	Path segmentsAsPath();

}