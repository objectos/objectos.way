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

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import com.google.common.collect.testing.AbstractCollectionTestSuiteBuilder;
import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.TestListGenerator;
import com.google.common.collect.testing.TestStringListGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.testers.CollectionClearTester;
import com.google.common.collect.testing.testers.CollectionSerializationEqualTester;
import com.google.common.collect.testing.testers.ListAddAllAtIndexTester;
import com.google.common.collect.testing.testers.ListAddAllTester;
import com.google.common.collect.testing.testers.ListAddAtIndexTester;
import com.google.common.collect.testing.testers.ListAddTester;
import com.google.common.collect.testing.testers.ListCreationTester;
import com.google.common.collect.testing.testers.ListEqualsTester;
import com.google.common.collect.testing.testers.ListGetTester;
import com.google.common.collect.testing.testers.ListIndexOfTester;
import com.google.common.collect.testing.testers.ListLastIndexOfTester;
import com.google.common.collect.testing.testers.ListRemoveAllTester;
import com.google.common.collect.testing.testers.ListRemoveAtIndexTester;
import com.google.common.collect.testing.testers.ListRemoveTester;
import com.google.common.collect.testing.testers.ListReplaceAllTester;
import com.google.common.collect.testing.testers.ListRetainAllTester;
import com.google.common.collect.testing.testers.ListSetTester;
import com.google.common.collect.testing.testers.ListToArrayTester;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GrowableListGuavaTestlibTest extends TestCase {

  @SuppressWarnings("exports")
  public static Test suite() {
    var suite = new TestSuite();

    add(
      suite, "GrowableList, add method",
      new TestStringListGenerator() {
        @Override
        protected List<String> create(String[] elements) {
          var list = new GrowableList<String>();

          for (String e : elements) {
            list.add(e);
          }

          return list;
        }
      }
    );

    add(
      suite, "GrowableList, addAll a Collection that is not a List",
      new TestStringListGenerator() {
        @Override
        protected List<String> create(String[] elements) {
          // not RandomAccess
          var coll = new LinkedList<String>();

          for (String e : elements) {
            coll.add(e);
          }

          var list = new GrowableList<String>();

          list.addAll(coll);

          return list;
        }
      }
    );

    add(
      suite, "GrowableList, addAll a Collection that is a List",
      new TestStringListGenerator() {
        @Override
        protected List<String> create(String[] elements) {
          // not RandomAccess
          var coll = new ArrayList<String>(elements.length);

          for (String e : elements) {
            coll.add(e);
          }

          var list = new GrowableList<String>();

          list.addAll(coll);

          return list;
        }
      }
    );

    add(
      suite, "GrowableList, addAllIterable method",
      new TestStringListGenerator() {
        @Override
        protected List<String> create(String[] elements) {
          var iterable = new ArrayBackedIterable<>(elements);

          var list = new GrowableList<String>();

          list.addAllIterable(iterable);

          return list;
        }
      }
    );

    return suite;
  }

  private static void add(TestSuite suite, String name, TestStringListGenerator gen) {
    suite.addTest(
      ThisListTestSuiteBuilder
          .using(gen)
          .named(name)
          .withFeatures(
            CollectionSize.ANY,

            CollectionFeature.ALLOWS_NULL_QUERIES,
            CollectionFeature.NON_STANDARD_TOSTRING,
            CollectionFeature.SUPPORTS_ADD
          )
          .createTestSuite()
    );
  }

  private static class ThisListTestSuiteBuilder<E>
      extends AbstractCollectionTestSuiteBuilder<ThisListTestSuiteBuilder<E>, E> {
    private static final Set<Class<?>> REMOVE_SET = Set.of(
      CollectionClearTester.class
    );

    public static <E> ThisListTestSuiteBuilder<E> using(TestListGenerator<E> generator) {
      return new ThisListTestSuiteBuilder<E>().usingGenerator(generator);
    }

    @Override
    public TestSuite createTestSuite() {
      withFeatures(KNOWN_ORDER);

      return super.createTestSuite();
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected List<Class<? extends AbstractTester>> getTesters() {
      List<Class<? extends AbstractTester>> testers = super.getTesters()
          .stream()
          .filter(c -> !REMOVE_SET.contains(c))
          .collect(Collectors.toList());

      testers.add(CollectionSerializationEqualTester.class);
      testers.add(ThisCollectionClearTester.class);
      testers.add(ListAddAllAtIndexTester.class);
      testers.add(ListAddAllTester.class);
      testers.add(ListAddAtIndexTester.class);
      testers.add(ListAddTester.class);
      testers.add(ListCreationTester.class);
      testers.add(ListEqualsTester.class);
      testers.add(ListGetTester.class);
      testers.add(ThisListHashCodeTester.class);
      testers.add(ListIndexOfTester.class);
      testers.add(ListLastIndexOfTester.class);
      //testers.add(ListListIteratorTester.class);
      testers.add(ListRemoveAllTester.class);
      testers.add(ListRemoveAtIndexTester.class);
      testers.add(ListRemoveTester.class);
      testers.add(ListReplaceAllTester.class);
      testers.add(ListRetainAllTester.class);
      testers.add(ListSetTester.class);
      //testers.add(ListSubListTester.class);
      testers.add(ListToArrayTester.class);

      return testers;
    }
  }
}