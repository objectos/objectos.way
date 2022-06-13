/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.util;

/**
 * An object that can produce a string by joining together the string
 * representations from of each of its elements.
 */
public interface Joinable {

  /**
   * Returns a new string by joining together the string representation of
   * each of its elements.
   *
   * @return a new string resulting from joining together the elements
   */
  String join();

  /**
   * Returns a new string by joining together the string representation of
   * each of its elements separated by the specified {@code delimiter}.
   *
   * @param delimiter
   *        the separator to use between each element's string representation
   *
   * @return a new string resulting from joining together the elements separated
   *         by the specified {@code delimiter}
   */
  String join(String delimiter);

  /**
   * Returns a new string by joining together the string representations of
   * each of its elements separated by the specified {@code delimiter} and with
   * the specified {@code prefix} and {@code suffix}.
   *
   * @param delimiter
   *        the separator to use between each element's string representation
   * @param prefix
   *        the value to be used as the first part of the result string
   * @param suffix
   *        the value to be used as the last part of the result string
   *
   * @return a new string resulting from joining together the elements separated
   *         by the specified {@code delimiter} and with the specified
   *         {@code prefix} and {@code suffix}.
   */
  String join(String delimiter, String prefix, String suffix);

}