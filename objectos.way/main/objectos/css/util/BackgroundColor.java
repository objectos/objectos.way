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
package objectos.css.util;

import objectos.html.tmpl.Api.ExternalAttribute.StyleClass;
import objectox.css.ClassSelectorSeqId;

/**
 * Utility classes for the {@code background-color} CSS property.
 */
// Generated by selfgen.css.CssUtilSpec. Do not edit!
public enum BackgroundColor implements StyleClass {

  INHERIT("inherit"),

  CURRENT("currentColor"),

  TRANSPARENT("transparent"),

  BLACK("rgb(0 0 0)"),

  WHITE("rgb(255 255 255)"),

  SLATE_050("rgb(248 250 252)"),

  SLATE_100("rgb(241 245 249)"),

  SLATE_200("rgb(226 232 240)"),

  SLATE_300("rgb(203 213 225)"),

  SLATE_400("rgb(148 163 184)"),

  SLATE_500("rgb(100 116 139)"),

  SLATE_600("rgb(71 85 105)"),

  SLATE_700("rgb(51 65 85)"),

  SLATE_800("rgb(30 41 59)"),

  SLATE_900("rgb(15 23 42)"),

  GRAY_050("rgb(249 250 251)"),

  GRAY_100("rgb(243 244 246)"),

  GRAY_200("rgb(229 231 235)"),

  GRAY_300("rgb(209 213 219)"),

  GRAY_400("rgb(156 163 175)"),

  GRAY_500("rgb(107 114 128)"),

  GRAY_600("rgb(75 85 99)"),

  GRAY_700("rgb(55 65 81)"),

  GRAY_800("rgb(31 41 55)"),

  GRAY_900("rgb(17 24 39)"),

  ZINC_050("rgb(250 250 250)"),

  ZINC_100("rgb(244 244 245)"),

  ZINC_200("rgb(228 228 231)"),

  ZINC_300("rgb(212 212 216)"),

  ZINC_400("rgb(161 161 170)"),

  ZINC_500("rgb(113 113 122)"),

  ZINC_600("rgb(82 82 91)"),

  ZINC_700("rgb(63 63 70)"),

  ZINC_800("rgb(39 39 42)"),

  ZINC_900("rgb(24 24 27)"),

  NEUTRAL_050("rgb(250 250 250)"),

  NEUTRAL_100("rgb(245 245 245)"),

  NEUTRAL_200("rgb(229 229 229)"),

  NEUTRAL_300("rgb(212 212 212)"),

  NEUTRAL_400("rgb(163 163 163)"),

  NEUTRAL_500("rgb(115 115 115)"),

  NEUTRAL_600("rgb(82 82 82)"),

  NEUTRAL_700("rgb(64 64 64)"),

  NEUTRAL_800("rgb(38 38 38)"),

  NEUTRAL_900("rgb(23 23 23)"),

  STONE_050("rgb(250 250 249)"),

  STONE_100("rgb(245 245 244)"),

  STONE_200("rgb(231 229 228)"),

  STONE_300("rgb(214 211 209)"),

  STONE_400("rgb(168 162 158)"),

  STONE_500("rgb(120 113 108)"),

  STONE_600("rgb(87 83 78)"),

  STONE_700("rgb(68 64 60)"),

  STONE_800("rgb(41 37 36)"),

  STONE_900("rgb(28 25 23)"),

  RED_050("rgb(254 242 242)"),

  RED_100("rgb(254 226 226)"),

  RED_200("rgb(254 202 202)"),

  RED_300("rgb(252 165 165)"),

  RED_400("rgb(248 113 113)"),

  RED_500("rgb(239 68 68)"),

  RED_600("rgb(220 38 38)"),

  RED_700("rgb(185 28 28)"),

  RED_800("rgb(153 27 27)"),

  RED_900("rgb(127 29 29)"),

  ORANGE_050("rgb(255 247 237)"),

  ORANGE_100("rgb(255 237 213)"),

  ORANGE_200("rgb(254 215 170)"),

  ORANGE_300("rgb(253 186 116)"),

  ORANGE_400("rgb(251 146 60)"),

  ORANGE_500("rgb(249 115 22)"),

  ORANGE_600("rgb(234 88 12)"),

  ORANGE_700("rgb(194 65 12)"),

  ORANGE_800("rgb(154 52 18)"),

  ORANGE_900("rgb(124 45 18)"),

  AMBER_050("rgb(255 251 235)"),

  AMBER_100("rgb(254 243 199)"),

  AMBER_200("rgb(253 230 138)"),

  AMBER_300("rgb(252 211 77)"),

  AMBER_400("rgb(251 191 36)"),

  AMBER_500("rgb(245 158 11)"),

  AMBER_600("rgb(217 119 6)"),

  AMBER_700("rgb(180 83 9)"),

  AMBER_800("rgb(146 64 14)"),

  AMBER_900("rgb(120 53 15)"),

  YELLOW_050("rgb(254 252 232)"),

  YELLOW_100("rgb(254 249 195)"),

  YELLOW_200("rgb(254 240 138)"),

  YELLOW_300("rgb(253 224 71)"),

  YELLOW_400("rgb(250 204 21)"),

  YELLOW_500("rgb(234 179 8)"),

  YELLOW_600("rgb(202 138 4)"),

  YELLOW_700("rgb(161 98 7)"),

  YELLOW_800("rgb(133 77 14)"),

  YELLOW_900("rgb(113 63 18)"),

  LIME_050("rgb(247 254 231)"),

  LIME_100("rgb(236 252 203)"),

  LIME_200("rgb(217 249 157)"),

  LIME_300("rgb(190 242 100)"),

  LIME_400("rgb(163 230 53)"),

  LIME_500("rgb(132 204 22)"),

  LIME_600("rgb(101 163 13)"),

  LIME_700("rgb(77 124 15)"),

  LIME_800("rgb(63 98 18)"),

  LIME_900("rgb(54 83 20)"),

  GREEN_050("rgb(240 253 244)"),

  GREEN_100("rgb(220 252 231)"),

  GREEN_200("rgb(187 247 208)"),

  GREEN_300("rgb(134 239 172)"),

  GREEN_400("rgb(74 222 128)"),

  GREEN_500("rgb(34 197 94)"),

  GREEN_600("rgb(22 163 74)"),

  GREEN_700("rgb(21 128 61)"),

  GREEN_800("rgb(22 101 52)"),

  GREEN_900("rgb(20 83 45)"),

  EMERALD_050("rgb(236 253 245)"),

  EMERALD_100("rgb(209 250 229)"),

  EMERALD_200("rgb(167 243 208)"),

  EMERALD_300("rgb(110 231 183)"),

  EMERALD_400("rgb(52 211 153)"),

  EMERALD_500("rgb(16 185 129)"),

  EMERALD_600("rgb(5 150 105)"),

  EMERALD_700("rgb(4 120 87)"),

  EMERALD_800("rgb(6 95 70)"),

  EMERALD_900("rgb(6 78 59)"),

  TEAL_050("rgb(240 253 250)"),

  TEAL_100("rgb(204 251 241)"),

  TEAL_200("rgb(153 246 228)"),

  TEAL_300("rgb(94 234 212)"),

  TEAL_400("rgb(45 212 191)"),

  TEAL_500("rgb(20 184 166)"),

  TEAL_600("rgb(13 148 136)"),

  TEAL_700("rgb(15 118 110)"),

  TEAL_800("rgb(17 94 89)"),

  TEAL_900("rgb(19 78 74)"),

  CYAN_050("rgb(236 254 255)"),

  CYAN_100("rgb(207 250 254)"),

  CYAN_200("rgb(165 243 252)"),

  CYAN_300("rgb(103 232 249)"),

  CYAN_400("rgb(34 211 238)"),

  CYAN_500("rgb(6 182 212)"),

  CYAN_600("rgb(8 145 178)"),

  CYAN_700("rgb(14 116 144)"),

  CYAN_800("rgb(21 94 117)"),

  CYAN_900("rgb(22 78 99)"),

  SKY_050("rgb(240 249 255)"),

  SKY_100("rgb(224 242 254)"),

  SKY_200("rgb(186 230 253)"),

  SKY_300("rgb(125 211 252)"),

  SKY_400("rgb(56 189 248)"),

  SKY_500("rgb(14 165 233)"),

  SKY_600("rgb(2 132 199)"),

  SKY_700("rgb(3 105 161)"),

  SKY_800("rgb(7 89 133)"),

  SKY_900("rgb(12 74 110)"),

  BLUE_050("rgb(239 246 255)"),

  BLUE_100("rgb(219 234 254)"),

  BLUE_200("rgb(191 219 254)"),

  BLUE_300("rgb(147 197 253)"),

  BLUE_400("rgb(96 165 250)"),

  BLUE_500("rgb(59 130 246)"),

  BLUE_600("rgb(37 99 235)"),

  BLUE_700("rgb(29 78 216)"),

  BLUE_800("rgb(30 64 175)"),

  BLUE_900("rgb(30 58 138)"),

  INDIGO_050("rgb(238 242 255)"),

  INDIGO_100("rgb(224 231 255)"),

  INDIGO_200("rgb(199 210 254)"),

  INDIGO_300("rgb(165 180 252)"),

  INDIGO_400("rgb(129 140 248)"),

  INDIGO_500("rgb(99 102 241)"),

  INDIGO_600("rgb(79 70 229)"),

  INDIGO_700("rgb(67 56 202)"),

  INDIGO_800("rgb(55 48 163)"),

  INDIGO_900("rgb(49 46 129)"),

  VIOLET_050("rgb(245 243 255)"),

  VIOLET_100("rgb(237 233 254)"),

  VIOLET_200("rgb(221 214 254)"),

  VIOLET_300("rgb(196 181 253)"),

  VIOLET_400("rgb(167 139 250)"),

  VIOLET_500("rgb(139 92 246)"),

  VIOLET_600("rgb(124 58 237)"),

  VIOLET_700("rgb(109 40 217)"),

  VIOLET_800("rgb(91 33 182)"),

  VIOLET_900("rgb(76 29 149)"),

  PURPLE_050("rgb(250 245 255)"),

  PURPLE_100("rgb(243 232 255)"),

  PURPLE_200("rgb(233 213 255)"),

  PURPLE_300("rgb(216 180 254)"),

  PURPLE_400("rgb(192 132 252)"),

  PURPLE_500("rgb(168 85 247)"),

  PURPLE_600("rgb(147 51 234)"),

  PURPLE_700("rgb(126 34 206)"),

  PURPLE_800("rgb(107 33 168)"),

  PURPLE_900("rgb(88 28 135)"),

  FUCHSIA_050("rgb(253 244 255)"),

  FUCHSIA_100("rgb(250 232 255)"),

  FUCHSIA_200("rgb(245 208 254)"),

  FUCHSIA_300("rgb(240 171 252)"),

  FUCHSIA_400("rgb(232 121 249)"),

  FUCHSIA_500("rgb(217 70 239)"),

  FUCHSIA_600("rgb(192 38 211)"),

  FUCHSIA_700("rgb(162 28 175)"),

  FUCHSIA_800("rgb(134 25 143)"),

  FUCHSIA_900("rgb(112 26 117)"),

  PINK_050("rgb(253 242 248)"),

  PINK_100("rgb(252 231 243)"),

  PINK_200("rgb(251 207 232)"),

  PINK_300("rgb(249 168 212)"),

  PINK_400("rgb(244 114 182)"),

  PINK_500("rgb(236 72 153)"),

  PINK_600("rgb(219 39 119)"),

  PINK_700("rgb(190 24 93)"),

  PINK_800("rgb(157 23 77)"),

  PINK_900("rgb(131 24 67)"),

  ROSE_050("rgb(255 241 242)"),

  ROSE_100("rgb(255 228 230)"),

  ROSE_200("rgb(254 205 211)"),

  ROSE_300("rgb(253 164 175)"),

  ROSE_400("rgb(251 113 133)"),

  ROSE_500("rgb(244 63 94)"),

  ROSE_600("rgb(225 29 72)"),

  ROSE_700("rgb(190 18 60)"),

  ROSE_800("rgb(159 18 57)"),

  ROSE_900("rgb(136 19 55)");

  private final String className = ClassSelectorSeqId.next();

  private final String value;

  private BackgroundColor(String value) {
    this.value = value;
  }

  /**
   * Returns the CSS class name.
   *
   * @return the CSS class name
   */
  @Override
  public final String className() {
    return className;
  }

  /**
   * Returns the CSS style rule represented by this utility class.
   *
   * @return the CSS style rule
   */
  @Override
  public final String toString() {
    return "." + className + " { background-color: " + value + " }";
  }

}
