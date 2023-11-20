#
# Copyright (C) 2023 Objectos Software LTDA.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# objectos.html.icon options
#

## module directory
HTML_ICON = objectos.html.icon

## module
HTML_ICON_MODULE = $(HTML_ICON)

## module version
HTML_ICON_VERSION = $(VERSION)

## javac --release option
HTML_ICON_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
HTML_ICON_ENABLE_PREVIEW = 0

## compile deps
HTML_ICON_COMPILE_DEPS  = $(call module-gav,$(HTML))
HTML_ICON_COMPILE_DEPS += $(call module-gav,$(UTIL_MAP))

## jar name
HTML_ICON_JAR_NAME = $(HTML_ICON)

## install coordinates
HTML_ICON_GROUP_ID = $(GROUP_ID)
HTML_ICON_ARTIFACT_ID = $(HTML_ICON_MODULE)

## copyright years for javadoc
HTML_ICON_COPYRIGHT_YEARS := 2022-2023

## pom description
HTML_ICON_DESCRIPTION = Defines the types of the Objectos HTML domain specific language

#
# eval tasks
#

HTML_ICON_TASKS = $(filter-out $(TEST_TASKS), $(MODULE_TASKS))

$(foreach task,$(HTML_ICON_TASKS),$(eval $(call $(task),HTML_ICON_,html.icon@)))

#
# objectos.html.icon targets
#

.PHONY: html.icon@jar
html.icon@jar: $(HTML_ICON_JAR_FILE)

.PHONY: html.icon@test-compile
html.icon@test-compile:

.PHONY: html.icon@test
html.icon@test:

.PHONY: html.icon@install
html.icon@install: $(HTML_ICON_INSTALL)

.PHONY: html.icon@source-jar
html.icon@source-jar: $(HTML_ICON_SOURCE_JAR_FILE)

.PHONY: html.icon@javadoc
html.icon@javadoc: $(HTML_ICON_JAVADOC_JAR_FILE)

.PHONY: html.icon@pom
html.icon@pom: $(HTML_ICON_POM_FILE)

.PHONY: html.icon@ossrh-prepare
html.icon@ossrh-prepare: $(HTML_ICON_OSSRH_PREPARE)
