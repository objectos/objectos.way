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
# objectos.util.list options
#

## module directory
UTIL_LIST = objectos.util.list

## module
UTIL_LIST_MODULE = $(UTIL_LIST)

## module version
UTIL_LIST_VERSION = $(VERSION)

## javac --release option
UTIL_LIST_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
UTIL_LIST_ENABLE_PREVIEW = 0

## compile deps
UTIL_LIST_COMPILE_DEPS = $(LANG_OBJECT_JAR_FILE)
UTIL_LIST_COMPILE_DEPS += $(UTIL_ARRAY_JAR_FILE)
UTIL_LIST_COMPILE_DEPS += $(UTIL_COLLECTION_JAR_FILE)

## jar name
UTIL_LIST_JAR_NAME = $(UTIL_LIST)

## test compile deps
UTIL_LIST_TEST_COMPILE_DEPS = $(UTIL_LIST_COMPILE_DEPS)
UTIL_LIST_TEST_COMPILE_DEPS += $(UTIL_LIST_JAR_FILE)
UTIL_LIST_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
UTIL_LIST_TEST_RUNTIME_DEPS = $(UTIL_LIST_TEST_COMPILE_DEPS)
UTIL_LIST_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
UTIL_LIST_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
UTIL_LIST_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## install coordinates
UTIL_LIST_GROUP_ID = $(GROUP_ID)
UTIL_LIST_ARTIFACT_ID = $(UTIL_LIST_MODULE)

## copyright years for javadoc
UTIL_LIST_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# UTIL_LIST_JAVADOC_SNIPPET_PATH := UTIL_LIST_TEST

## pom description
UTIL_LIST_DESCRIPTION = Special-purpose java.util.List implementations

#
# objectos.util.list targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),UTIL_LIST_)))

.PHONY: util.list@clean
util.list@clean:
	rm -rf $(UTIL_LIST_WORK)/*

.PHONY: util.list@compile
util.list@compile: $(UTIL_LIST_COMPILE_MARKER)

.PHONY: util.list@jar
util.list@jar: $(UTIL_LIST_JAR_FILE)

.PHONY: util.list@test
util.list@test: $(UTIL_LIST_TEST_RUN_MARKER)

.PHONY: util.list@install
util.list@install: $(UTIL_LIST_INSTALL)

.PHONY: util.list@source-jar
util.list@source-jar: $(UTIL_LIST_SOURCE_JAR_FILE)

.PHONY: util.list@javadoc
util.list@javadoc: $(UTIL_LIST_JAVADOC_JAR_FILE)

.PHONY: util.list@pom
util.list@pom: $(UTIL_LIST_POM_FILE)

.PHONY: util.list@ossrh-prepare
util.list@ossrh-prepare: $(UTIL_LIST_OSSRH_PREPARE)