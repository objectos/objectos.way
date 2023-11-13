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
package objectos.notes.base;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

final class TestingClock extends Clock {

	private final ZonedDateTime startTime;

	private int minutes;

	public TestingClock(int year, int month, int day) {
		LocalDateTime dateTime;
		dateTime = LocalDateTime.of(year, month, day, 10, 0);

		this.startTime = dateTime.atZone(ZoneId.systemDefault());
	}

	public TestingClock(ZonedDateTime startTime) {
		this.startTime = startTime;
	}

	@Override
	public final Instant instant() {
		ZonedDateTime instant;
		instant = startTime.plusMinutes(minutes++);

		return Instant.from(instant);
	}

	@Override
	public final ZoneId getZone() {
		return startTime.getZone();
	}

	@Override
	public Clock withZone(ZoneId zone) {
		throw new UnsupportedOperationException();
	}

}