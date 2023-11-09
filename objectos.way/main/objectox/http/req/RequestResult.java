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
package objectox.http.req;

import java.util.Map;
import objectos.http.Http.Header.Name;
import objectos.http.Http.Header.Value;
import objectos.http.req.GetRequest;
import objectos.http.req.HeadRequest;
import objectos.http.req.Request;
import objectox.http.HeaderName;
import objectox.http.HeaderValue;
import objectox.http.HttpRequestPath;
import objectox.lang.Check;

public sealed abstract class RequestResult implements Request {

	public static final class GetRequestResult extends RequestResult implements GetRequest {

		public GetRequestResult(boolean keepAlive, Map<HeaderName, HeaderValue> requestHeaders, HttpRequestPath requestPath) {
			super(keepAlive, requestHeaders, requestPath);
		}

	}

	public static final class HeadRequestResult extends RequestResult implements HeadRequest {

		public HeadRequestResult(boolean keepAlive, Map<HeaderName, HeaderValue> requestHeaders, HttpRequestPath requestPath) {
			super(keepAlive, requestHeaders, requestPath);
		}

	}

	private final boolean keepAlive;

	private final Map<HeaderName, HeaderValue> requestHeaders;

	private final HttpRequestPath requestPath;

	public RequestResult(boolean keepAlive, Map<HeaderName, HeaderValue> requestHeaders, HttpRequestPath requestPath) {
		this.keepAlive = keepAlive;

		this.requestHeaders = requestHeaders;

		this.requestPath = requestPath;
	}

	@Override
	public final String header(Name name) {
		Check.notNull(name, "name == null");

		Value value;
		value = requestHeaders.get(name);

		if (value == null) {
			return null;
		} else {
			return value.toString();
		}
	}

	@Override
	public final boolean keepAlive() {
		return keepAlive;
	}

	@Override
	public final String path() {
		return requestPath.toString();
	}

}