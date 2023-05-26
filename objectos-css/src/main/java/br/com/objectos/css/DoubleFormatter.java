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
package br.com.objectos.css;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DoubleFormatter {

  private static final DecimalFormatSymbols US = new DecimalFormatSymbols(Locale.US);

  private DoubleFormatter() {}

  public static String format(double value) {
    DecimalFormat formatter = new DecimalFormat(".##", US);
    formatter.setDecimalSeparatorAlwaysShown(false);
    formatter.setMinimumFractionDigits(0);
    return formatter.format(value);
  }

}