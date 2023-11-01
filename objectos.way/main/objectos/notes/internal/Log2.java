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
package objectos.notes.internal;

import java.time.Clock;
import objectos.lang.Note2;

public final class Log2 extends Log {

	final Object value1;

	final Object value2;

	Log2(Clock clock, Note2<?, ?> note, Object value1, Object value2) {
		super(clock, note);

		this.value1 = value1;

		this.value2 = value2;
	}

	@Override
	final String format(Layout layout) {
		return layout.formatLog2(this);
	}

}