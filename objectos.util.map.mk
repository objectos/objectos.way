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
# objectos.util.map options
#

## module directory
UTIL_MAP = objectos.util.map

## module
UTIL_MAP_MODULE = $(UTIL_MAP)

## module version
UTIL_MAP_VERSION = $(VERSION)

## javac --release option
UTIL_MAP_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
UTIL_MAP_ENABLE_PREVIEW = 0

## compile deps
UTIL_MAP_COMPILE_DEPS = $(LANG_OBJECT_JAR_FILE)
UTIL_MAP_COMPILE_DEPS += $(UTIL_ARRAY_JAR_FILE)
UTIL_MAP_COMPILE_DEPS += $(UTIL_COLLECTION_JAR_FILE)

## jar name
UTIL_MAP_JAR_NAME = $(UTIL_MAP)

## test compile deps
UTIL_MAP_TEST_COMPILE_DEPS = $(UTIL_MAP_COMPILE_DEPS)
UTIL_MAP_TEST_COMPILE_DEPS += $(UTIL_MAP_JAR_FILE)
UTIL_MAP_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
UTIL_MAP_TEST_RUNTIME_DEPS = $(UTIL_MAP_TEST_COMPILE_DEPS)
UTIL_MAP_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
UTIL_MAP_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
UTIL_MAP_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## install coordinates
UTIL_MAP_GROUP_ID = $(GROUP_ID)
UTIL_MAP_ARTIFACT_ID = $(UTIL_MAP_MODULE)

## copyright years for javadoc
UTIL_MAP_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# UTIL_MAP_JAVADOC_SNIPPET_PATH := UTIL_MAP_TEST

## pom description
UTIL_MAP_DESCRIPTION = Special-purpose java.util.Set implementations

#
# objectos.util.map targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),UTIL_MAP_)))

.PHONY: util.map@clean
util.map@clean:
	rm -rf $(UTIL_MAP_WORK)/*

.PHONY: util.map@compile
util.map@compile: $(UTIL_MAP_COMPILE_MARKER)

.PHONY: util.map@jar
util.map@jar: $(UTIL_MAP_JAR_FILE)

.PHONY: util.map@test
util.map@test: $(UTIL_MAP_TEST_RUN_MARKER)

.PHONY: util.map@install
util.map@install: $(UTIL_MAP_INSTALL)

.PHONY: util.map@source-jar
util.map@source-jar: $(UTIL_MAP_SOURCE_JAR_FILE)

.PHONY: util.map@javadoc
util.map@javadoc: $(UTIL_MAP_JAVADOC_JAR_FILE)

.PHONY: util.map@pom
util.map@pom: $(UTIL_MAP_POM_FILE)

.PHONY: util.map@ossrh-prepare
util.map@ossrh-prepare: $(UTIL_MAP_OSSRH_PREPARE)