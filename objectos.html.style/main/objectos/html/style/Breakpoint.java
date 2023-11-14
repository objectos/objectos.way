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
package objectos.html.style;

/**
 * Defines the standard breakpoints for responsive design.
 */
public final class Breakpoint {

	/**
	 * The small breakpoint {@code min-width: 640px}.
	 */
	public static final int SM = 640;

	/**
	 * The medium breakpoint {@code min-width: 768px}.
	 */
	public static final int MD = 768;

	/**
	 * The large breakpoint {@code min-width: 1024px}.
	 */
	public static final int LG = 1024;

	/**
	 * The extra large breakpoint {@code min-width: 1280px}.
	 */
	public static final int XL = 1280;

	/**
	 * The extra extra large breakpoint {@code min-width: 1536px}.
	 */
	public static final int X2L = 1536;

	private Breakpoint() {}

}