/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.select;

import java.util.Objects;
import objectos.css.tmpl.Api;
import objectos.html.tmpl.Api.ExternalAttribute;
import objectos.lang.object.Check;

public record IdSelector(String id) implements ExternalAttribute.Id, Api.ExternalIdSelector {

	public IdSelector {
		Objects.requireNonNull(id, "id == null");

		Check.argument(!id.isBlank(), "id must not be blank");
	}

	public static IdSelector of(String id) {
		return new IdSelector(id);
	}

	/**
	 * Returns a new distinct id selector whose value is 4 characters in
	 * length. Each returned value is distinct from any of the previously returned
	 * values.
	 *
	 * @return a newly created id selector
	 */
	public static IdSelector next() {
		String id;
		id = SeqIdHolder.INSTANCE.next();

		return new IdSelector(id);
	}

	private static class SeqIdHolder {
		static final SeqId INSTANCE = new SeqId();
	}

	@Override
	public final String id() {
		return id;
	}

	@Override
	public final String toString() {
		return "#" + id;
	}

}
