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
# objectos.lang.object
#

## module directory
LANG_OBJECT = objectos.lang.object

## module
LANG_OBJECT_MODULE = $(LANG_OBJECT)

## module coordinates
LANG_OBJECT_GROUP_ID = $(GROUP_ID)
LANG_OBJECT_ARTIFACT_ID = $(LANG_OBJECT_MODULE)
LANG_OBJECT_VERSION = $(VERSION)

## javac --release option
LANG_OBJECT_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
LANG_OBJECT_ENABLE_PREVIEW = 0

## compile deps
LANG_OBJECT_COMPILE_DEPS =

## jar name
LANG_OBJECT_JAR_NAME = $(LANG_OBJECT)

## test compile deps
LANG_OBJECT_TEST_COMPILE_DEPS  = $(call module-gav,$(LANG_OBJECT))
LANG_OBJECT_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
LANG_OBJECT_TEST_RUNTIME_DEPS  = $(LANG_OBJECT_TEST_COMPILE_DEPS)
LANG_OBJECT_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## copyright years for javadoc
LANG_OBJECT_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# LANG_OBJECT_JAVADOC_SNIPPET_PATH := LANG_OBJECT_TEST

## pom description
LANG_OBJECT_DESCRIPTION = Utilities for java.lang.Object instances

#
# objectos.lang.object targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),LANG_OBJECT_,lang.object@)))

.PHONY: lang.object@jar
lang.object@jar: $(LANG_OBJECT_JAR_FILE)

.PHONY: lang.object@test
lang.object@test: $(LANG_OBJECT_TEST_RUN_MARKER)

.PHONY: lang.object@install
lang.object@install: $(LANG_OBJECT_INSTALL)

.PHONY: lang.object@source-jar
lang.object@source-jar: $(LANG_OBJECT_SOURCE_JAR_FILE)

.PHONY: lang.object@javadoc
lang.object@javadoc: $(LANG_OBJECT_JAVADOC_JAR_FILE)

.PHONY: lang.object@pom
lang.object@pom: $(LANG_OBJECT_POM_FILE)

.PHONY: lang.object@ossrh-prepare
lang.object@ossrh-prepare: $(LANG_OBJECT_OSSRH_PREPARE)
