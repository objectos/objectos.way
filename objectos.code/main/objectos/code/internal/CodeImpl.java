/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

import java.util.List;
import objectos.code.Code;

public final class CodeImpl implements Code {

  private final CodeImportList importList = new CodeImportList();

  @Override
  public final ImportList importList(String packageName) {
    Check.notNull(packageName, "packageName == null");
    JavaModel.checkPackageName(packageName);

    importList.set(packageName);

    return importList;
  }

  @Override
  public final String process(StringTemplate template) throws IllegalArgumentException {
    List<String> fragments;
    fragments = template.fragments();

    int fragmentsSize;
    fragmentsSize = fragments.size();

    if (fragmentsSize == 1) {
      return fragments.get(0);
    }

    Object[] values;
    values = processValues(template);

    StringBuilder sb;
    sb = new StringBuilder();

    for (int i = 0, len = values.length; i < len; i++) {
      String fragment;
      fragment = fragments.get(i);

      sb.append(fragment);

      Object value;
      value = values[i];

      sb.append(value);
    }

    String lastFragment;
    lastFragment = fragments.getLast();

    sb.append(lastFragment);

    return sb.toString();
  }

  private Object[] processValues(StringTemplate template) {
    List<Object> values;
    values = template.values();

    int size;
    size = values.size();

    Object[] result;
    result = new Object[size];

    for (int i = 0; i < size; i++) {
      Object value;
      value = values.get(i);

      if (value instanceof InternalClassName className) {
        value = importList.process(className);
      }

      result[i] = value;
    }

    return result;
  }

}