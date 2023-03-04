/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.html;

public class StringDiff {

  public static void test(String a, String b) {
    int length = Math.min(a.length(), b.length());

    for (int i = 0; i < length; i++) {
      var ca = a.charAt(i);
      var cb = b.charAt(i);

      if (ca != cb) {
        System.out.println("Diff at index=" + i + ";char a = " + ca + ";charb = " + cb);
      }
    }
  }

}