/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectos.lang.object;

/**
 * Provides {@code static} convenience methods for implementing
 * {@link Object#equals(Object)} methods.
 */
public final class Equals {

	private Equals() {}

	/**
	 * Checks if two objects are equal to each other in a null-safe manner.
	 *
	 * <p>
	 * Returns:
	 *
	 * <ul>
	 * <li>{@code true} if {@code a} is null and {@code b} is null;</li>
	 * <li>{@code true} if {@code a} is not null and {@code a.equals(b)} evaluates
	 * to {@code true}; and</li>
	 * <li>{@code false} otherwise</li>
	 * </ul>
	 *
	 * @param a
	 *        the first reference to check for equality
	 * @param b
	 *        the second reference to check for equality
	 *
	 * @return {@code true} if the arguments are equal to each other and
	 *         {@code false} otherwise
	 */
	public static boolean of(Object a, Object b) {
		if (a == b) {
			return true;
		}

		if (a != null) {
			return a.equals(b);
		}

		return false;
	}

	/**
	 * Checks if objects are equal to each other in pairs. It checks, in a
	 * null-safe manner, if {@code a.equals(b) && c.equals(d)}.
	 *
	 * <p>
	 * An invocation of this method of the form
	 *
	 * <pre>{@code Equals.of(a, b, c, d)}</pre>
	 *
	 * <p>
	 * behaves in exactly the same way as the expression
	 *
	 * <pre>{@code Equals.of(a, b) && Equals.of(c, d)}</pre>
	 *
	 * <p>
	 * meaning that if the equality test fails for the first pair, the second pair
	 * is not checked.
	 *
	 * @param a
	 *        the first reference of the first pair to check for equality
	 * @param b
	 *        the second reference of the first pair to check for equality
	 * @param c
	 *        the first reference of the second pair to check for equality
	 * @param d
	 *        the second reference of the second pair to check for equality
	 *
	 * @return {@code true} if
	 *         {@code Equals.of(a, b) && Equals.of(c, d)}
	 */
	public static boolean of(
			Object a, Object b,
			Object c, Object d) {
		return of(a, b)
				&& of(c, d);
	}

	/**
	 * Checks if objects are equal to each other in pairs. It checks, in a
	 * null-safe manner, if {@code a.equals(b) && c.equals(d) && e.equals(f)}.
	 *
	 * <p>
	 * An invocation of this method of the form
	 *
	 * <pre>{@code Equals.of(a, b, c, d, e, f)}</pre>
	 *
	 * <p>
	 * behaves in exactly the same way as the expression
	 *
	 * <pre>{@code Equals.of(a, b) && Equals.of(c, d) &&  Equals.of(e, f)}</pre>
	 *
	 * <p>
	 * meaning that if the equality test fails for the first pair, the second pair
	 * is not checked.
	 *
	 * @param a
	 *        the first reference of the first pair to check for equality
	 * @param b
	 *        the second reference of the first pair to check for equality
	 * @param c
	 *        the first reference of the second pair to check for equality
	 * @param d
	 *        the second reference of the second pair to check for equality
	 * @param e
	 *        the first reference of the third pair to check for equality
	 * @param f
	 *        the second reference of the third pair to check for equality
	 *
	 * @return {@code true} if
	 *         {@code Equals.of(a, b) && Equals.of(c, d) && Equals.of(e, f)}
	 */
	public static boolean of(
			Object a, Object b,
			Object c, Object d,
			Object e, Object f) {
		return of(a, b)
				&& of(c, d)
				&& of(e, f);
	}

}