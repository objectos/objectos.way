/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.css.type;

import br.com.objectos.css.keyword.StandardKeyword;

public interface Marker {

  void addColor(ColorName color);

  void addKeyword(StandardKeyword keyword);

  void addZero();

  void markColor(ColorKind kind);

  void markDouble();

  void markDoubleAngle();

  void markDoubleLength();

  void markDoublePercentage();

  void markFunction();

  void markInt();

  void markIntAngle();

  void markIntLength();

  void markIntPercentage();

  void markKeyword();

  void markString();

  void markUri();

}