/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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
package objectos.lang;

/**
 * Provides {@code static} convenience methods for computing the hash
 * code of an object. It is useful for implementing the
 * {@link Object#hashCode()} method when one has to consider more than one
 * field.
 *
 * <p>
 * For {@code hashCode()} method implementations that uses only object
 * references, i.e., it does not use any primitive values, usage is like so
 *
 * <pre>
 * &#64;Override
 * public final int hashCode() {
 *   return HashCode.hashCode(a, b, c);
 * }</pre>
 *
 * <p>
 * Where {@code a}, {@code b} and {@code c} are the fields used for hash code
 * computation.
 *
 * <p>
 * For method implementations that uses object references <i>and</i> primitive
 * values, usage is like so
 *
 * <pre>
 * &#64;Override
 * public final int hashCode() {
 *   return HashCode.hashCode(
 *       HashCode.hashCode(a),
 *       HashCode.hashCode(b),
 *       HashCode.hashCode(c)
 *   );
 * }</pre>
 *
 * <p>
 * In the first example, if any of the fields is a primitive value, invoking
 * the method incurs in a auto-boxing operation. The second example avoids this
 * cost.
 *
 * <p>
 * This class uses the algorithm described in Item 9 from the book
 * Effective Java (Second Edition) by Joshua Bloch.
 *
 * @since 0.2
 */
public final class HashCode {

  static final int HASH_CODE_MULTIPLIER = 31;

  static final int HASH_CODE_START = 17;

  private HashCode() {}

  /**
   * Returns a hash code for a {@code boolean} value.
   *
   * @param b
   *        a {@code boolean} value
   *
   * @return the hash code
   */
  public static int hashCode(boolean b) {
    return b ? 1 : 0;
  }

  /**
   * Returns a hash code for a {@code byte} value.
   *
   * @param b
   *        a {@code byte} value
   *
   * @return the hash code
   */
  public static int hashCode(byte b) {
    return b;
  }

  /**
   * Returns a hash code for a {@code double} value.
   *
   * @param d
   *        a {@code double} value
   *
   * @return the hash code
   */
  public static int hashCode(double d) {
    long l;
    l = Double.doubleToLongBits(d);

    return hashCode(l);
  }

  /**
   * Returns a hash code for a {@code float} value.
   *
   * @param f
   *        a {@code float} value
   *
   * @return the hash code
   */
  public static int hashCode(float f) {
    return Float.floatToIntBits(f);
  }

  /**
   * Returns a hash code for a {@code int} value.
   *
   * @param i
   *        an {@code int} value
   *
   * @return the hash code
   */
  public static int hashCode(int i) {
    return i;
  }

  /**
   * Returns the computed hash code from the two individual hash code values.
   *
   * @param hc1
   *        the first hash code value
   * @param hc2
   *        the second hash code value
   *
   * @return the computed hash code
   */
  public static int hashCode(int hc1, int hc2) {
    int result;
    result = HASH_CODE_START;

    result = hashCodeUpdate(result, hc1);

    result = hashCodeUpdate(result, hc2);

    return result;
  }

  /**
   * Returns the computed hash code from the three individual hash code values.
   *
   * @param hc1
   *        the first hash code value
   * @param hc2
   *        the second hash code value
   * @param hc3
   *        the third hash code value
   *
   * @return the computed hash code
   */
  public static int hashCode(int hc1, int hc2, int hc3) {
    int result;
    result = HASH_CODE_START;

    result = hashCodeUpdate(result, hc1);

    result = hashCodeUpdate(result, hc2);

    result = hashCodeUpdate(result, hc3);

    return result;
  }

  /**
   * Returns the computed hash code from the four individual hash code values.
   *
   * @param hc1
   *        the first hash code value
   * @param hc2
   *        the second hash code value
   * @param hc3
   *        the third hash code value
   * @param hc4
   *        the fourth hash code value
   *
   * @return the computed hash code
   */
  public static int hashCode(int hc1, int hc2, int hc3, int hc4) {
    int result;
    result = HASH_CODE_START;

    result = hashCodeUpdate(result, hc1);

    result = hashCodeUpdate(result, hc2);

    result = hashCodeUpdate(result, hc3);

    result = hashCodeUpdate(result, hc4);

    return result;
  }

  /**
   * Returns the computed hash code from the five individual hash code values.
   *
   * @param hc1
   *        the first hash code value
   * @param hc2
   *        the second hash code value
   * @param hc3
   *        the third hash code value
   * @param hc4
   *        the fourth hash code value
   * @param hc5
   *        the fifth hash code value
   *
   * @return the computed hash code
   */
  public static int hashCode(int hc1, int hc2, int hc3, int hc4, int hc5) {
    int result;
    result = HASH_CODE_START;

    result = hashCodeUpdate(result, hc1);

    result = hashCodeUpdate(result, hc2);

    result = hashCodeUpdate(result, hc3);

    result = hashCodeUpdate(result, hc4);

    result = hashCodeUpdate(result, hc5);

    return result;
  }

  /**
   * Returns the computed hash code from the six or more individual hash code
   * values.
   *
   * @param hc1
   *        the first hash code value
   * @param hc2
   *        the second hash code value
   * @param hc3
   *        the third hash code value
   * @param hc4
   *        the fourth hash code value
   * @param hc5
   *        the fifth hash code value
   * @param rest
   *        the remaining hash code values
   *
   * @return the computed hash code
   */
  public static int hashCode(int hc1, int hc2, int hc3, int hc4, int hc5, int... rest) {
    Checks.checkNotNull(rest, "rest == null");

    int result;
    result = HASH_CODE_START;

    result = hashCodeUpdate(result, hc1);

    result = hashCodeUpdate(result, hc2);

    result = hashCodeUpdate(result, hc3);

    result = hashCodeUpdate(result, hc4);

    result = hashCodeUpdate(result, hc5);

    for (int hc : rest) {
      result = hashCodeUpdate(result, hc);
    }

    return result;
  }

  /**
   * Returns a hash code for a {@code long} value.
   *
   * @param l
   *        a {@code long} value
   *
   * @return the hash code
   */
  public static int hashCode(long l) {
    return (int) (l ^ (l >>> 32));
  }

  /**
   * Returns a hash code for an object reference. It returns:
   *
   * <ul>
   * <li>{@code 0} if the reference is null; or</li>
   * <li>the result of {@code o.hashCode()} otherwise.</li>
   * </ul>
   *
   * @param o
   *        an object
   *
   * @return {@code 0} if {@code o} is null, the computed hash code otherwise
   */
  public static int hashCode(Object o) {
    if (o == null) {
      return 0;
    }

    return o.hashCode();
  }

  /**
   * Returns the computed hash code from two individual objects.
   *
   * @param o1
   *        the first object
   * @param o2
   *        the second object
   *
   * @return the computed hash code
   */
  public static int hashCode(Object o1, Object o2) {
    return hashCode(
      hashCode(o1),
      hashCode(o2)
    );
  }

  /**
   * Returns the computed hash code from three individual objects.
   *
   * @param o1
   *        the first object
   * @param o2
   *        the second object
   * @param o3
   *        the third object
   *
   * @return the computed hash code
   */
  public static int hashCode(Object o1, Object o2, Object o3) {
    return hashCode(
      hashCode(o1),
      hashCode(o2),
      hashCode(o3)
    );
  }

  /**
   * Returns the computed hash code from four individual objects.
   *
   * @param o1
   *        the first object
   * @param o2
   *        the second object
   * @param o3
   *        the third object
   * @param o4
   *        the fourth object
   *
   * @return the computed hash code
   */
  public static int hashCode(Object o1, Object o2, Object o3, Object o4) {
    return hashCode(
      hashCode(o1),
      hashCode(o2),
      hashCode(o3),
      hashCode(o4)
    );
  }

  /**
   * Returns the computed hash code from five individual objects.
   *
   * @param o1
   *        the first object
   * @param o2
   *        the second object
   * @param o3
   *        the third object
   * @param o4
   *        the fourth object
   * @param o5
   *        the fifth object
   *
   * @return the computed hash code
   */
  public static int hashCode(Object o1, Object o2, Object o3, Object o4, Object o5) {
    return hashCode(
      hashCode(o1),
      hashCode(o2),
      hashCode(o3),
      hashCode(o4),
      hashCode(o5)
    );
  }

  /**
   * Returns the computed hash code from six or more individual objects.
   *
   * @param o1
   *        the first object
   * @param o2
   *        the second object
   * @param o3
   *        the third object
   * @param o4
   *        the fourth object
   * @param o5
   *        the fifth object
   * @param rest
   *        the remaining objects
   *
   * @return the computed hash code
   */
  public static int hashCode(
      Object o1, Object o2, Object o3, Object o4, Object o5, Object... rest) {
    Checks.checkNotNull(rest, "rest == null");

    int result;
    result = HASH_CODE_START;

    result = hashCodeUpdate(result, hashCode(o1));

    result = hashCodeUpdate(result, hashCode(o2));

    result = hashCodeUpdate(result, hashCode(o3));

    result = hashCodeUpdate(result, hashCode(o4));

    result = hashCodeUpdate(result, hashCode(o5));

    for (Object o : rest) {
      result = hashCodeUpdate(result, hashCode(o));
    }

    return result;
  }

  /**
   * Returns a hash code for a {@code short} value.
   *
   * @param s
   *        a {@code short} value
   *
   * @return the hash code
   */
  public static int hashCode(short s) {
    return s;
  }

  /**
   * Returns the initial value of a hash code computation.
   *
   * @return the initial value of hash code computation
   */
  public static int hashCodeStart() {
    return HASH_CODE_START;
  }

  /**
   * Updates a partial hash code computation with an additional hash code value.
   *
   * @param partial
   *        the current partial result of a hash code computation
   * @param hashCode
   *        the additional hash code value
   *
   * @return the updated hash code value
   */
  public static int hashCodeUpdate(int partial, int hashCode) {
    return HASH_CODE_MULTIPLIER * partial + hashCode;
  }

  /**
   * Updates a partial hash code computation with the hash code of an additional
   * object.
   *
   * @param partial
   *        the current partial result of a hash code computation
   * @param o
   *        the additional object
   *
   * @return the updated hash code value
   */
  public static int hashCodeUpdate(int partial, Object o) {
    return hashCodeUpdate(partial, hashCode(o));
  }

}
