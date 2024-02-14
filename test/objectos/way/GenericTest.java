/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;

public class GenericTest {

  @Test
  public void runtime() {
    List<String> list = listOfStrings();

    @SuppressWarnings("rawtypes")
    Class<? extends List> c = list.getClass();

    Type type = c;

    if (type instanceof ParameterizedType param) {
      System.out.println(param.getActualTypeArguments());
    } else {
      System.out.println("No runtime info");
    }
  }

  public List<String> listOfStrings() {
    return new ArrayList<>();
  }

  @Test
  public void compile() throws NoSuchMethodException, SecurityException {
    Class<?> c = getClass();

    Method m = c.getMethod("listOfStrings");

    Type type = m.getGenericReturnType();

    if (type instanceof ParameterizedType param) {
      Type[] typeArgs = param.getActualTypeArguments();
      for (Type typeArg : typeArgs) {
        System.out.println(typeArg.getTypeName());
      }
    } else {
      System.out.println("No runtime info");
    }
  }

}
