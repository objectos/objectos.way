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
# objectos.util.set options
#

## module directory
UTIL_SET = objectos.util.set

## module
UTIL_SET_MODULE = $(UTIL_SET)

## module version
UTIL_SET_VERSION = $(VERSION)

## javac --release option
UTIL_SET_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
UTIL_SET_ENABLE_PREVIEW = 0

## compile deps
UTIL_SET_COMPILE_DEPS = $(LANG_OBJECT_JAR_FILE)
UTIL_SET_COMPILE_DEPS += $(UTIL_ARRAY_JAR_FILE)
UTIL_SET_COMPILE_DEPS += $(UTIL_COLLECTION_JAR_FILE)

## jar name
UTIL_SET_JAR_NAME = $(UTIL_SET)

## test compile deps
UTIL_SET_TEST_COMPILE_DEPS = $(UTIL_SET_COMPILE_DEPS)
UTIL_SET_TEST_COMPILE_DEPS += $(UTIL_SET_JAR_FILE)
UTIL_SET_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
UTIL_SET_TEST_RUNTIME_DEPS = $(UTIL_SET_TEST_COMPILE_DEPS)
UTIL_SET_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
UTIL_SET_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
UTIL_SET_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## install coordinates
UTIL_SET_GROUP_ID = $(GROUP_ID)
UTIL_SET_ARTIFACT_ID = $(UTIL_SET_MODULE)

## copyright years for javadoc
UTIL_SET_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# UTIL_SET_JAVADOC_SNIPPET_PATH := UTIL_SET_TEST

## pom description
UTIL_SET_DESCRIPTION = Special-purpose java.util.Set implementations

#
# objectos.util.set targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),UTIL_SET_)))

.PHONY: util.set@clean
util.set@clean:
	rm -rf $(UTIL_SET_WORK)/*

.PHONY: util.set@compile
util.set@compile: $(UTIL_SET_COMPILE_MARKER)

.PHONY: util.set@jar
util.set@jar: $(UTIL_SET_JAR_FILE)

.PHONY: util.set@test
util.set@test: $(UTIL_SET_TEST_RUN_MARKER)

.PHONY: util.set@install
util.set@install: $(UTIL_SET_INSTALL)

.PHONY: util.set@source-jar
util.set@source-jar: $(UTIL_SET_SOURCE_JAR_FILE)

.PHONY: util.set@javadoc
util.set@javadoc: $(UTIL_SET_JAVADOC_JAR_FILE)

.PHONY: util.set@pom
util.set@pom: $(UTIL_SET_POM_FILE)

.PHONY: util.set@ossrh-prepare
util.set@ossrh-prepare: $(UTIL_SET_OSSRH_PREPARE)
