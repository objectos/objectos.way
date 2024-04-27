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
package objectos.notes.impl;

import java.time.LocalDate;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.Note3;
import objectos.notes.NoteSink;

final class TestingNotes {

	static final Note0 TRACE0;

	static final Note1<String> TRACE1;

	static final Note2<String, LocalDate> TRACE2;

	static final Note3<String, LocalDate, String> TRACE3;

	static final Note0 DEBUG0;

	static final Note1<String> DEBUG1;

	static final Note2<String, LocalDate> DEBUG2;

	static final Note3<String, LocalDate, String> DEBUG3;

	static final Note0 INFO0;

	static final Note1<String> INFO1;

	static final Note2<String, LocalDate> INFO2;

	static final Note3<String, LocalDate, String> INFO3;

	static final Note0 WARN0;

	static final Note1<String> WARN1;

	static final Note2<String, LocalDate> WARN2;

	static final Note3<String, LocalDate, String> WARN3;

	static final Note0 ERROR0;

	static final Note1<String> ERROR1;

	static final Note2<String, LocalDate> ERROR2;

	static final Note3<String, LocalDate, String> ERROR3;

	static {
		Class<?> s;
		s = TestingNotes.class;

		TRACE0 = Note0.trace(s, "TRACE0");
		TRACE1 = Note1.trace(s, "TRACE1");
		TRACE2 = Note2.trace(s, "TRACE2");
		TRACE3 = Note3.trace(s, "TRACE3");

		DEBUG0 = Note0.debug(s, "DEBUG0");
		DEBUG1 = Note1.debug(s, "DEBUG1");
		DEBUG2 = Note2.debug(s, "DEBUG2");
		DEBUG3 = Note3.debug(s, "DEBUG3");

		INFO0 = Note0.info(s, "INFO0");
		INFO1 = Note1.info(s, "INFO1");
		INFO2 = Note2.info(s, "INFO2");
		INFO3 = Note3.info(s, "INFO3");

		WARN0 = Note0.warn(s, "WARN0");
		WARN1 = Note1.warn(s, "WARN1");
		WARN2 = Note2.warn(s, "WARN2");
		WARN3 = Note3.warn(s, "WARN3");

		ERROR0 = Note0.error(s, "ERROR0");
		ERROR1 = Note1.error(s, "ERROR1");
		ERROR2 = Note2.error(s, "ERROR2");
		ERROR3 = Note3.error(s, "ERROR3");
	}

	private TestingNotes() {}

    public static void sendAll0(NoteSink noteSink) {
      noteSink.send(TRACE0);

      noteSink.send(DEBUG0);

      noteSink.send(INFO0);

      noteSink.send(WARN0);

      noteSink.send(ERROR0);
    }

}
