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
package objectos.lang;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import org.testng.annotations.Test;

public class HashCodeTest {

  @Test
  public void hashCodeTest() {
    Object o1 = "one";
    Object o2 = Long.MAX_VALUE;
    Object o3 = Integer.valueOf(3);
    Object o4 = Arrays.asList(1, 2, 3, 4);
    Object o5 = new File("abc.txt");
    Object o6 = "six";
    Object o7 = "seven";

    assertEquals(
        HashCode.of(o1, o2),
        hc0(
            hc0(
                HashCode.START, hc(o1)
            ), hc(o2)
        )
    );

    assertEquals(
        HashCode.of(o1, o2, o3),
        hc0(
            hc0(
                hc0(
                    HashCode.START, hc(o1)
                ), hc(o2)),
            hc(o3)
        )
    );

    assertEquals(
        HashCode.of(o1, o2, o3, o4),
        hc0(
            hc0(
                hc0(
                    hc0(
                        HashCode.START, hc(o1)
                    ), hc(o2)
                ), hc(o3)
            ), hc(o4)
        )
    );

    assertEquals(
        HashCode.of(o1, o2, o3, o4, o5),
        hc0(
            hc0(
                hc0(
                    hc0(
                        hc0(
                            HashCode.START, hc(o1)
                        ), hc(o2)
                    ), hc(o3)
                ), hc(o4)
            ), hc(o5)
        )
    );

    assertEquals(
        HashCode.of(o1, o2, o3, o4, o5, o6),
        hc0(
            hc0(
                hc0(
                    hc0(
                        hc0(
                            hc0(
                                HashCode.START, hc(o1)
                            ), hc(o2)
                        ), hc(o3)
                    ), hc(o4)
                ), hc(o5)
            ), hc(o6)
        )
    );

    assertEquals(
        HashCode.of(o1, o2, o3, o4, o5, o6, o7),
        hc0(
            hc0(
                hc0(
                    hc0(
                        hc0(
                            hc0(
                                hc0(
                                    HashCode.START, hc(o1)
                                ), hc(o2)
                            ), hc(o3)
                        ), hc(o4)
                    ), hc(o5)
                ), hc(o6)
            ), hc(o7)
        )
    );
  }

  private int hc(Object o) {
    return HashCode.of(o);
  }

  private int hc0(int partial, int hashCode) {
    return HashCode.update(partial, hashCode);
  }

}