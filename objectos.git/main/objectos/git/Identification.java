/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import objectos.lang.object.Check;
import objectos.lang.object.HashCode;
import objectos.lang.object.ToString;

/**
 * The author or the commiter field of a commit object. Consists of the
 * combination of the name + e-mail + time + time zone.
 *
 * @since 1
 */
public final class Identification implements ToString.Formattable {

  private String email;

  private final String gitTimeZone;

  private String name;

  private final long seconds;

  Identification(IdentificationBuilder mutable) {
    email = mutable.email;

    gitTimeZone = mutable.gitTimeZone;

    name = mutable.name;

    seconds = mutable.seconds;
  }

  Identification(String name, String email, long seconds, String gitTimeZone) {
    this.email = email;
    this.gitTimeZone = gitTimeZone;
    this.name = name;
    this.seconds = seconds;
  }

  /**
   * Compares the specified object with this identification for equality.
   *
   * <p>
   * Returns {@code true} if and only if the specified object is also a
   * {@code Identification} instance and if both instances have the same email,
   * time zone, name and timestamp.
   *
   * @param obj
   *        the object to be compared for equality with this identification
   *
   * @return {@code true} if the specified object is equal to this
   *         identification
   */
  @Override
  public final boolean equals(Object obj) {
    return obj == this
        || obj instanceof Identification && equals0((Identification) obj);
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} a string representation of this id.
   *
   * <p>
   * The string representation <i>may</i> contain:
   *
   * <ul>
   * <li>the simple name of this class;</li>
   * <li>the name of the author/committer;</li>
   * <li>the e-mail address;</li>
   * <li>the timestamp; and</li>
   * <li>the Git timezone.</li>
   * </ul>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   */
  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "", name,
        "", email,
        "", Long.toString(seconds),
        "", gitTimeZone
    );
  }

  /**
   * Returns the e-mail addresss associated with this identification.
   *
   * @return the e-mail addresss associated with this identification
   */
  public final String getEmail() {
    return email;
  }

  /**
   * Returns the string representation of the timezone associated with this
   * identification.
   *
   * @return the timezone associated with this identification
   */
  public final String getGitTimeZone() {
    return gitTimeZone;
  }

  /**
   * Returns the name associated with this identification.
   *
   * @return the name associated with this identification
   */
  public final String getName() {
    return name;
  }

  /**
   * Returns the date, as seconds since UNIX epoch, associated with this
   * identification.
   *
   * @return the date, as seconds since UNIX epoch, associated with this
   *         identification
   */
  public final long getSeconds() {
    return seconds;
  }

  /**
   * Returns the hash code value of this identification.
   *
   * @return the hash code value of this identification
   */
  @Override
  public final int hashCode() {
    return HashCode.of(email, gitTimeZone, name, seconds);
  }

  /**
   * Returns a string representation of the form
   * {@code name <email> seconds timezone}.
   *
   * @return a string representation
   */
  public final String print() {
    return name + " <" + email + "> " + Long.toString(seconds) + ' ' + gitTimeZone;
  }

  /**
   * Sets the e-mail address value.
   *
   * @param newEmail
   *        the new e-mail address value
   *
   * @deprecated this class will be immutable in a future release. Please use
   *             {@link #withIdentity(String, String)}.
   */
  @Deprecated
  public final void setEmail(String newEmail) {
    email = Check.notNull(newEmail, "newEmail == null");
  }

  /**
   * Sets the name value.
   *
   * @param newName
   *        the new name value
   *
   * @deprecated this class will be immutable in a future release. Please use
   *             {@link #withIdentity(String, String)}.
   */
  @Deprecated
  public final void setName(String newName) {
    name = Check.notNull(newName, "newName == null");
  }

  /**
   * Returns the string representation of this identification as defined by the
   * {@link ToString.Formattable#formatToString(StringBuilder, int)} method.
   *
   * @return the string representation of this identification
   */
  @Override
  public final String toString() {
    return ToString.of(this);
  }

  /**
   * Returns a copy of this identification with both the name and the e-mail
   * altered.
   *
   * @param newName
   *        the name of the returned copy
   * @param newEmail
   *        the e-mail address of the returned copy
   *
   * @return a copy of this identification with both the name and the e-mail
   *         altered
   *
   * @since 3
   */
  public final Identification withIdentity(String newName, String newEmail) {
    Check.notNull(newName, "newName == null");
    Check.notNull(newEmail, "newEmail == null");

    return new Identification(newName, newEmail, seconds, gitTimeZone);
  }

  private boolean equals0(Identification that) {
    return email.equals(that.email)
        && gitTimeZone.equals(that.gitTimeZone)
        && name.equals(that.name)
        && seconds == that.seconds;
  }

}