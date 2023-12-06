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

import objectos.lang.object.Equals;
import objectos.lang.object.ToString;

/**
 * @since 1
 */
abstract class GitObject implements ToString.Formattable {

  final ObjectId objectId;

  GitObject(ObjectId objectId) {
    this.objectId = objectId;
  }

  /**
   * Compares the specified object with this Git object for equality.
   *
   * <p>
   * Returns {@code true} if and only if the specified object is also a Git
   * object of the same type and if both instances have the same hash value.
   *
   * @param obj
   *        the object to be compared for equality with this Git object
   *
   * @return {@code true} if the specified object is equal to this Git object
   */
  @Override
  public final boolean equals(Object obj) {
    return obj == this
        || obj instanceof GitObject && equals0((GitObject) obj);
  }

  /**
   * Returns the hash value that uniquely identifies this Git object.
   *
   * @return the hash value that uniquely identifies this Git object
   */
  public final ObjectId getObjectId() {
    return objectId;
  }

  /**
   * Returns the hash code value of this Git object.
   *
   * @return the hash code value of this Git object
   */
  @Override
  public final int hashCode() {
    return objectId.hashCode();
  }

  /**
   * Returns the string representation of this Git object as defined by the
   * {@link ToString.Formattable#formatToString(StringBuilder, int)} method.
   *
   * @return the string representation of this Git object
   */
  @Override
  public final String toString() {
    return ToString.of(this);
  }

  private boolean equals0(GitObject obj) {
    return Equals.of(
        getClass(), obj.getClass(),

        objectId, obj.objectId
    );
  }

}