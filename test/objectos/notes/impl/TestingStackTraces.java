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

final class TestingStackTraces {

	private TestingStackTraces() {}

	public static Throwable ignore() {
		return new Throwable();
	}

	public static Throwable throwable1() {
		return new Throwable();
	}

	public static Throwable throwable2() {
		return new Throwable();
	}

	public static Throwable throwable3() {
		return new Throwable();
	}

}
