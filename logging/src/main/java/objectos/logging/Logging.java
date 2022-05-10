/*
 * Copyright (C) 2021-2022 Objectos Software LTDA.
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
package objectos.logging;

final class Logging {

  /*
  @startuml
  
  ' config
  
  hide empty members
  ' left to right direction
  skinparam genericDisplay old
  ' skinparam monochrome true
  skinparam shadowing false
  ' skinparam style strictuml
  
  ' classes
  
  class DoubleEvent extends Event
  
  abstract class Event {
    + int getKey()
    + Level getLevel()
    + Class<?> getSource()
  }
  
  class Event0 extends Event
  
  class Event1<V1> extends Event {
    + String formatValue(V1 value)
  }
  
  class Event2<V1, V2> extends Event {
    + String formatValue1(V1 value)
    + String formatValue2(V2 value)
  }
  
  class IntEvent extends Event
  
  enum Level {
    TRACE
    DEBUG
    INFO
    WARNING
    ERROR
  }
  
  interface Logger {
    + void log(Event0 m)
    + <V1> void log(Event1<V1> m, V1 v1)
    + <V1, V2> void log(Event2<V1, V2> m, V1 v1, V2 v2)
    + void log(EventDouble m, double v)
    + void log(EventInt m, int v)
    + void log(EventLong m, long v)
  }
  
  class Logging {
    + {static} Event0 info(\lClass<?> source, String key)
    + {static} <V1> Event1<V1> info(\lClass<?> source,\lString key,\lClass<V1> v1)
    + {static} <V1> Event1<V1> info(\lClass<?> source,\lString key,\lSerializer<V1> v1)
    + {static} <V1, V2> Event2<V1, V2> info(\lClass<?> source,\lString key,\lClass<V1> v1,\lClass<V2> v2)
    + {static} <V1, V2> Event2<V1, V2> info(\lClass<?> source,\lString key,\lTypeHint<V1> v1,\lTypeHint<V2> v2)
  }
  
  class LongEvent extends Event
  
  ' ordering
  
  Logging -[hidden]d- Level
  Level -[hidden]d- Logger
  Logger -[hidden]d- Event
  Event -[hidden]d- Event0
  Event0 -[hidden]d- Event1
  Event1 -[hidden]d- Event2
  DoubleEvent -[hidden]d- IntEvent
  IntEvent -[hidden]d- LongEvent
  
  @enduml
  */

  private Logging() {}

  static void abbreviate(StringBuilder out, String source, int length) {
    String result;
    result = source;

    int resultLength;
    resultLength = result.length();

    if (resultLength > length) {
      int start;
      start = resultLength - length;

      result = result.substring(start, resultLength);
    }

    out.append(result);

    int pad;
    pad = length - result.length();

    for (int i = 0; i < pad; i++) {
      out.append(' ');
    }
  }

  static String format(Object o) {
    if (o != null) {
      return o.toString();
    } else {
      return "null";
    }
  }

  static void pad(StringBuilder out, String source, int length) {
    String result;
    result = source;

    if (result.length() > length) {
      result = result.substring(0, length);
    }

    out.append(result);

    int pad;
    pad = length - result.length();

    for (int i = 0; i < pad; i++) {
      out.append(' ');
    }
  }

}
