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

import java.util.NoSuchElementException;
import objectos.lang.object.ToString;

/**
 * Represents an {@link ObjectId} or its absence.
 *
 * @since 1
 */
public abstract class MaybeObjectId implements ToString.Formattable {

  MaybeObjectId() {}

  /**
   * Returns the instance representing the absence of a hash value.
   *
   * @return the instance representing the absence of a hash value
   */
  public static MaybeObjectId empty() {
    return EmptyObjectId.INSTANCE;
  }

  /**
   * Returns this reference if it is an instance of {@link ObjectId} or throws
   * an exception otherwise.
   *
   * @return this reference if it is an instance of {@link ObjectId}
   *
   * @throws NoSuchElementException
   *         if this object is not an {@link ObjectId}
   */
  public abstract ObjectId getObjectId();

  /**
   * Returns {@code true} if this object is an instance of {@link ObjectId}.
   *
   * @return {@code true} if this object is an instance of {@link ObjectId}
   */
  public abstract boolean isObjectId();

  /**
   * Returns the string representation of this id as defined by the
   * {@link ToString.Formattable#formatToString(StringBuilder, int)} method.
   *
   * @return the string representation of this id
   */
  @Override
  public final String toString() {
    return ToString.of(this);
  }

}