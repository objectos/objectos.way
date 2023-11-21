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
# objectos.util.array options
#

## module directory
UTIL_ARRAY = objectos.util.array

## module
UTIL_ARRAY_MODULE = $(UTIL_ARRAY)

## module version
UTIL_ARRAY_GROUP_ID = $(GROUP_ID)
UTIL_ARRAY_ARTIFACT_ID = $(UTIL_ARRAY)
UTIL_ARRAY_VERSION = $(VERSION)

## javac --release option
UTIL_ARRAY_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
UTIL_ARRAY_ENABLE_PREVIEW = 0

## compile deps
UTIL_ARRAY_COMPILE_DEPS = $(call module-gav,$(LANG_OBJECT))

## test compile deps
UTIL_ARRAY_TEST_COMPILE_DEPS  = $(UTIL_ARRAY_COMPILE_DEPS)
UTIL_ARRAY_TEST_COMPILE_DEPS += $(call module-gav,$(UTIL_ARRAY))
UTIL_ARRAY_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
UTIL_ARRAY_TEST_RUNTIME_DEPS  = $(UTIL_ARRAY_TEST_COMPILE_DEPS)
UTIL_ARRAY_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## copyright years for javadoc
UTIL_ARRAY_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# UTIL_ARRAY_JAVADOC_SNIPPET_PATH := UTIL_ARRAY_TEST

## pom description
UTIL_ARRAY_DESCRIPTION = Utilities for working with arrays of primitives and references

#
# objectos.util.array targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),UTIL_ARRAY_,util.array@)))

.PHONY: util.array@source-jar
util.array@source-jar: $(UTIL_ARRAY_SOURCE_JAR_FILE)

.PHONY: util.array@javadoc
util.array@javadoc: $(UTIL_ARRAY_JAVADOC_JAR_FILE)

.PHONY: util.array@pom
util.array@pom: $(UTIL_ARRAY_POM_FILE)

.PHONY: util.array@ossrh-prepare
util.array@ossrh-prepare: $(UTIL_ARRAY_OSSRH_PREPARE)

