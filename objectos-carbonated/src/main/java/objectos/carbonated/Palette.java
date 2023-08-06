/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.carbonated;

import objectos.carbonated.internal.Namespace;
import objectos.css.tmpl.Api.ColorValue;
import objectos.css.util.Color;

public sealed interface Palette permits Namespace {

  ColorValue BLACK = Color.ofHex("#000000");
  ColorValue BLACK_100 = BLACK;
  ColorValue BLACK_HOVER = Color.ofHex("#212121");

  ColorValue WHITE = Color.ofHex("#ffffff");
  ColorValue WHITE_0 = WHITE;
  ColorValue WHITE_HOVER = Color.ofHex("#e8e8e8");

  ColorValue YELLOW_10 = Color.ofHex("#fcf4d6");
  ColorValue YELLOW_20 = Color.ofHex("#fddc69");
  ColorValue YELLOW_30 = Color.ofHex("#f1c21b");
  ColorValue YELLOW_40 = Color.ofHex("#d2a106");
  ColorValue YELLOW_50 = Color.ofHex("#b28600");
  ColorValue YELLOW_60 = Color.ofHex("#8e6a00");
  ColorValue YELLOW_70 = Color.ofHex("#684e00");
  ColorValue YELLOW_80 = Color.ofHex("#483700");
  ColorValue YELLOW_90 = Color.ofHex("#302400");
  ColorValue YELLOW_100 = Color.ofHex("#1c1500");

  ColorValue YELLOW_10_HOVER = Color.ofHex("#f8e6a0");
  ColorValue YELLOW_20_HOVER = Color.ofHex("#fccd27");
  ColorValue YELLOW_30_HOVER = Color.ofHex("#ddb00e");
  ColorValue YELLOW_40_HOVER = Color.ofHex("#bc9005");
  ColorValue YELLOW_50_HOVER = Color.ofHex("#9e7700");
  ColorValue YELLOW_60_HOVER = Color.ofHex("#755800");
  ColorValue YELLOW_70_HOVER = Color.ofHex("#806000");
  ColorValue YELLOW_80_HOVER = Color.ofHex("#5c4600");
  ColorValue YELLOW_90_HOVER = Color.ofHex("#3d2e00");
  ColorValue YELLOW_100_HOVER = Color.ofHex("#332600");

  ColorValue ORANGE_10 = Color.ofHex("#fff2e8");
  ColorValue ORANGE_20 = Color.ofHex("#ffd9be");
  ColorValue ORANGE_30 = Color.ofHex("#ffb784");
  ColorValue ORANGE_40 = Color.ofHex("#ff832b");
  ColorValue ORANGE_50 = Color.ofHex("#eb6200");
  ColorValue ORANGE_60 = Color.ofHex("#ba4e00");
  ColorValue ORANGE_70 = Color.ofHex("#8a3800");
  ColorValue ORANGE_80 = Color.ofHex("#5e2900");
  ColorValue ORANGE_90 = Color.ofHex("#3e1a00");
  ColorValue ORANGE_100 = Color.ofHex("#231000");

  ColorValue ORANGE_10_HOVER = Color.ofHex("#ffe2cc");
  ColorValue ORANGE_20_HOVER = Color.ofHex("#ffc69e");
  ColorValue ORANGE_30_HOVER = Color.ofHex("#ff9d57");
  ColorValue ORANGE_40_HOVER = Color.ofHex("#fa6800");
  ColorValue ORANGE_50_HOVER = Color.ofHex("#cc5500");
  ColorValue ORANGE_60_HOVER = Color.ofHex("#9e4200");
  ColorValue ORANGE_70_HOVER = Color.ofHex("#a84400");
  ColorValue ORANGE_80_HOVER = Color.ofHex("#753300");
  ColorValue ORANGE_90_HOVER = Color.ofHex("#522200");
  ColorValue ORANGE_100_HOVER = Color.ofHex("#421e00");

  ColorValue RED_10 = Color.ofHex("#fff1f1");
  ColorValue RED_20 = Color.ofHex("#ffd7d9");
  ColorValue RED_30 = Color.ofHex("#ffb3b8");
  ColorValue RED_40 = Color.ofHex("#ff8389");
  ColorValue RED_50 = Color.ofHex("#fa4d56");
  ColorValue RED_60 = Color.ofHex("#da1e28");
  ColorValue RED_70 = Color.ofHex("#a2191f");
  ColorValue RED_80 = Color.ofHex("#750e13");
  ColorValue RED_90 = Color.ofHex("#520408");
  ColorValue RED_100 = Color.ofHex("#2d0709");

  ColorValue RED_100_HOVER = Color.ofHex("#540d11");
  ColorValue RED_90_HOVER = Color.ofHex("#66050a");
  ColorValue RED_80_HOVER = Color.ofHex("#921118");
  ColorValue RED_70_HOVER = Color.ofHex("#c21e25");
  ColorValue RED_60_HOVER = Color.ofHex("#b81922");
  ColorValue RED_50_HOVER = Color.ofHex("#ee0713");
  ColorValue RED_40_HOVER = Color.ofHex("#ff6168");
  ColorValue RED_30_HOVER = Color.ofHex("#ff99a0");
  ColorValue RED_20_HOVER = Color.ofHex("#ffc2c5");
  ColorValue RED_10_HOVER = Color.ofHex("#ffe0e0");

  ColorValue MAGENTA_10 = Color.ofHex("#fff0f7");
  ColorValue MAGENTA_20 = Color.ofHex("#ffd6e8");
  ColorValue MAGENTA_30 = Color.ofHex("#ffafd2");
  ColorValue MAGENTA_40 = Color.ofHex("#ff7eb6");
  ColorValue MAGENTA_50 = Color.ofHex("#ee5396");
  ColorValue MAGENTA_60 = Color.ofHex("#d02670");
  ColorValue MAGENTA_70 = Color.ofHex("#9f1853");
  ColorValue MAGENTA_80 = Color.ofHex("#740937");
  ColorValue MAGENTA_90 = Color.ofHex("#510224");
  ColorValue MAGENTA_100 = Color.ofHex("#2a0a18");

  ColorValue MAGENTA_100_HOVER = Color.ofHex("#53142f");
  ColorValue MAGENTA_90_HOVER = Color.ofHex("#68032e");
  ColorValue MAGENTA_80_HOVER = Color.ofHex("#8e0b43");
  ColorValue MAGENTA_70_HOVER = Color.ofHex("#bf1d63");
  ColorValue MAGENTA_60_HOVER = Color.ofHex("#b0215f");
  ColorValue MAGENTA_50_HOVER = Color.ofHex("#e3176f");
  ColorValue MAGENTA_40_HOVER = Color.ofHex("#ff57a0");
  ColorValue MAGENTA_30_HOVER = Color.ofHex("#ff94c3");
  ColorValue MAGENTA_20_HOVER = Color.ofHex("#ffbdda");
  ColorValue MAGENTA_10_HOVER = Color.ofHex("#ffe0ef");

  ColorValue PURPLE_10 = Color.ofHex("#f6f2ff");
  ColorValue PURPLE_20 = Color.ofHex("#e8daff");
  ColorValue PURPLE_30 = Color.ofHex("#d4bbff");
  ColorValue PURPLE_40 = Color.ofHex("#be95ff");
  ColorValue PURPLE_50 = Color.ofHex("#a56eff");
  ColorValue PURPLE_60 = Color.ofHex("#8a3ffc");
  ColorValue PURPLE_70 = Color.ofHex("#6929c4");
  ColorValue PURPLE_80 = Color.ofHex("#491d8b");
  ColorValue PURPLE_90 = Color.ofHex("#31135e");
  ColorValue PURPLE_100 = Color.ofHex("#1c0f30");

  ColorValue PURPLE_100_HOVER = Color.ofHex("#341c59");
  ColorValue PURPLE_90_HOVER = Color.ofHex("#40197b");
  ColorValue PURPLE_80_HOVER = Color.ofHex("#5b24ad");
  ColorValue PURPLE_70_HOVER = Color.ofHex("#7c3dd6");
  ColorValue PURPLE_60_HOVER = Color.ofHex("#7822fb");
  ColorValue PURPLE_50_HOVER = Color.ofHex("#9352ff");
  ColorValue PURPLE_40_HOVER = Color.ofHex("#ae7aff");
  ColorValue PURPLE_30_HOVER = Color.ofHex("#c5a3ff");
  ColorValue PURPLE_20_HOVER = Color.ofHex("#dcc7ff");
  ColorValue PURPLE_10_HOVER = Color.ofHex("#ede5ff");

  ColorValue BLUE_10 = Color.ofHex("#edf5ff");
  ColorValue BLUE_20 = Color.ofHex("#d0e2ff");
  ColorValue BLUE_30 = Color.ofHex("#a6c8ff");
  ColorValue BLUE_40 = Color.ofHex("#78a9ff");
  ColorValue BLUE_50 = Color.ofHex("#4589ff");
  ColorValue BLUE_60 = Color.ofHex("#0f62fe");
  ColorValue BLUE_70 = Color.ofHex("#0043ce");
  ColorValue BLUE_80 = Color.ofHex("#002d9c");
  ColorValue BLUE_90 = Color.ofHex("#001d6c");
  ColorValue BLUE_100 = Color.ofHex("#001141");

  ColorValue BLUE_100_HOVER = Color.ofHex("#001f75");
  ColorValue BLUE_90_HOVER = Color.ofHex("#00258a");
  ColorValue BLUE_80_HOVER = Color.ofHex("#0039c7");
  ColorValue BLUE_70_HOVER = Color.ofHex("#0053ff");
  ColorValue BLUE_60_HOVER = Color.ofHex("#0050e6");
  ColorValue BLUE_50_HOVER = Color.ofHex("#1f70ff");
  ColorValue BLUE_40_HOVER = Color.ofHex("#5c97ff");
  ColorValue BLUE_30_HOVER = Color.ofHex("#8ab6ff");
  ColorValue BLUE_20_HOVER = Color.ofHex("#b8d3ff");
  ColorValue BLUE_10_HOVER = Color.ofHex("#dbebff");

  ColorValue CYAN_10 = Color.ofHex("#e5f6ff");
  ColorValue CYAN_20 = Color.ofHex("#bae6ff");
  ColorValue CYAN_30 = Color.ofHex("#82cfff");
  ColorValue CYAN_40 = Color.ofHex("#33b1ff");
  ColorValue CYAN_50 = Color.ofHex("#1192e8");
  ColorValue CYAN_60 = Color.ofHex("#0072c3");
  ColorValue CYAN_70 = Color.ofHex("#00539a");
  ColorValue CYAN_80 = Color.ofHex("#003a6d");
  ColorValue CYAN_90 = Color.ofHex("#012749");
  ColorValue CYAN_100 = Color.ofHex("#061727");

  ColorValue CYAN_10_HOVER = Color.ofHex("#cceeff");
  ColorValue CYAN_20_HOVER = Color.ofHex("#99daff");
  ColorValue CYAN_30_HOVER = Color.ofHex("#57beff");
  ColorValue CYAN_40_HOVER = Color.ofHex("#059fff");
  ColorValue CYAN_50_HOVER = Color.ofHex("#0f7ec8");
  ColorValue CYAN_60_HOVER = Color.ofHex("#005fa3");
  ColorValue CYAN_70_HOVER = Color.ofHex("#0066bd");
  ColorValue CYAN_80_HOVER = Color.ofHex("#00498a");
  ColorValue CYAN_90_HOVER = Color.ofHex("#013360");
  ColorValue CYAN_100_HOVER = Color.ofHex("#0b2947");

  ColorValue TEAL_10 = Color.ofHex("#d9fbfb");
  ColorValue TEAL_20 = Color.ofHex("#9ef0f0");
  ColorValue TEAL_30 = Color.ofHex("#3ddbd9");
  ColorValue TEAL_40 = Color.ofHex("#08bdba");
  ColorValue TEAL_50 = Color.ofHex("#009d9a");
  ColorValue TEAL_60 = Color.ofHex("#007d79");
  ColorValue TEAL_70 = Color.ofHex("#005d5d");
  ColorValue TEAL_80 = Color.ofHex("#004144");
  ColorValue TEAL_90 = Color.ofHex("#022b30");
  ColorValue TEAL_100 = Color.ofHex("#081a1c");

  ColorValue TEAL_10_HOVER = Color.ofHex("#acf6f6");
  ColorValue TEAL_20_HOVER = Color.ofHex("#57e5e5");
  ColorValue TEAL_30_HOVER = Color.ofHex("#25cac8");
  ColorValue TEAL_40_HOVER = Color.ofHex("#07aba9");
  ColorValue TEAL_50_HOVER = Color.ofHex("#008a87");
  ColorValue TEAL_60_HOVER = Color.ofHex("#006b68");
  ColorValue TEAL_70_HOVER = Color.ofHex("#007070");
  ColorValue TEAL_80_HOVER = Color.ofHex("#005357");
  ColorValue TEAL_90_HOVER = Color.ofHex("#033940");
  ColorValue TEAL_100_HOVER = Color.ofHex("#0f3034");

  ColorValue GREEN_10 = Color.ofHex("#defbe6");
  ColorValue GREEN_20 = Color.ofHex("#a7f0ba");
  ColorValue GREEN_30 = Color.ofHex("#6fdc8c");
  ColorValue GREEN_40 = Color.ofHex("#42be65");
  ColorValue GREEN_50 = Color.ofHex("#24a148");
  ColorValue GREEN_60 = Color.ofHex("#198038");
  ColorValue GREEN_70 = Color.ofHex("#0e6027");
  ColorValue GREEN_80 = Color.ofHex("#044317");
  ColorValue GREEN_90 = Color.ofHex("#022d0d");
  ColorValue GREEN_100 = Color.ofHex("#071908");

  ColorValue GREEN_10_HOVER = Color.ofHex("#b6f6c8");
  ColorValue GREEN_20_HOVER = Color.ofHex("#74e792");
  ColorValue GREEN_30_HOVER = Color.ofHex("#36ce5e");
  ColorValue GREEN_40_HOVER = Color.ofHex("#3bab5a");
  ColorValue GREEN_50_HOVER = Color.ofHex("#208e3f");
  ColorValue GREEN_60_HOVER = Color.ofHex("#166f31");
  ColorValue GREEN_70_HOVER = Color.ofHex("#11742f");
  ColorValue GREEN_80_HOVER = Color.ofHex("#05521c");
  ColorValue GREEN_90_HOVER = Color.ofHex("#033b11");
  ColorValue GREEN_100_HOVER = Color.ofHex("#0d300f");

  ColorValue COOL_GRAY_10 = Color.ofHex("#f2f4f8");
  ColorValue COOL_GRAY_20 = Color.ofHex("#dde1e6");
  ColorValue COOL_GRAY_30 = Color.ofHex("#c1c7cd");
  ColorValue COOL_GRAY_40 = Color.ofHex("#a2a9b0");
  ColorValue COOL_GRAY_50 = Color.ofHex("#878d96");
  ColorValue COOL_GRAY_60 = Color.ofHex("#697077");
  ColorValue COOL_GRAY_70 = Color.ofHex("#4d5358");
  ColorValue COOL_GRAY_80 = Color.ofHex("#343a3f");
  ColorValue COOL_GRAY_90 = Color.ofHex("#21272a");
  ColorValue COOL_GRAY_100 = Color.ofHex("#121619");

  ColorValue COOL_GRAY_10_HOVER = Color.ofHex("#e4e9f1");
  ColorValue COOL_GRAY_20_HOVER = Color.ofHex("#cdd3da");
  ColorValue COOL_GRAY_30_HOVER = Color.ofHex("#adb5bd");
  ColorValue COOL_GRAY_40_HOVER = Color.ofHex("#9199a1");
  ColorValue COOL_GRAY_50_HOVER = Color.ofHex("#757b85");
  ColorValue COOL_GRAY_60_HOVER = Color.ofHex("#585e64");
  ColorValue COOL_GRAY_70_HOVER = Color.ofHex("#5d646a");
  ColorValue COOL_GRAY_80_HOVER = Color.ofHex("#434a51");
  ColorValue COOL_GRAY_90_HOVER = Color.ofHex("#2b3236");
  ColorValue COOL_GRAY_100_HOVER = Color.ofHex("#222a2f");

  ColorValue GRAY_10 = Color.ofHex("#f4f4f4");
  ColorValue GRAY_20 = Color.ofHex("#e0e0e0");
  ColorValue GRAY_30 = Color.ofHex("#c6c6c6");
  ColorValue GRAY_40 = Color.ofHex("#a8a8a8");
  ColorValue GRAY_50 = Color.ofHex("#8d8d8d");
  ColorValue GRAY_60 = Color.ofHex("#6f6f6f");
  ColorValue GRAY_70 = Color.ofHex("#525252");
  ColorValue GRAY_80 = Color.ofHex("#393939");
  ColorValue GRAY_90 = Color.ofHex("#262626");
  ColorValue GRAY_100 = Color.ofHex("#161616");

  ColorValue GRAY_10_HOVER = Color.ofHex("#e8e8e8");
  ColorValue GRAY_20_HOVER = Color.ofHex("#d1d1d1");
  ColorValue GRAY_30_HOVER = Color.ofHex("#b5b5b5");
  ColorValue GRAY_40_HOVER = Color.ofHex("#999999");
  ColorValue GRAY_50_HOVER = Color.ofHex("#7a7a7a");
  ColorValue GRAY_60_HOVER = Color.ofHex("#5e5e5e");
  ColorValue GRAY_70_HOVER = Color.ofHex("#636363");
  ColorValue GRAY_80_HOVER = Color.ofHex("#474747");
  ColorValue GRAY_90_HOVER = Color.ofHex("#333333");
  ColorValue GRAY_100_HOVER = Color.ofHex("#292929");

  ColorValue WARM_GRAY_10 = Color.ofHex("#f7f3f2");
  ColorValue WARM_GRAY_20 = Color.ofHex("#e5e0df");
  ColorValue WARM_GRAY_30 = Color.ofHex("#cac5c4");
  ColorValue WARM_GRAY_40 = Color.ofHex("#ada8a8");
  ColorValue WARM_GRAY_50 = Color.ofHex("#8f8b8b");
  ColorValue WARM_GRAY_60 = Color.ofHex("#726e6e");
  ColorValue WARM_GRAY_70 = Color.ofHex("#565151");
  ColorValue WARM_GRAY_80 = Color.ofHex("#3c3838");
  ColorValue WARM_GRAY_90 = Color.ofHex("#272525");
  ColorValue WARM_GRAY_100 = Color.ofHex("#171414");

  ColorValue WARM_GRAY_10_HOVER = Color.ofHex("#f0e8e6");
  ColorValue WARM_GRAY_20_HOVER = Color.ofHex("#d8d0cf");
  ColorValue WARM_GRAY_30_HOVER = Color.ofHex("#b9b3b1");
  ColorValue WARM_GRAY_40_HOVER = Color.ofHex("#9c9696");
  ColorValue WARM_GRAY_50_HOVER = Color.ofHex("#7f7b7b");
  ColorValue WARM_GRAY_60_HOVER = Color.ofHex("#605d5d");
  ColorValue WARM_GRAY_70_HOVER = Color.ofHex("#696363");
  ColorValue WARM_GRAY_80_HOVER = Color.ofHex("#4c4848");
  ColorValue WARM_GRAY_90_HOVER = Color.ofHex("#343232");
  ColorValue WARM_GRAY_100_HOVER = Color.ofHex("#2c2626");

}
