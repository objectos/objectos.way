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
# objectos.util.collection options
#

## module directory
UTIL_COLLECTION = objectos.util.collection

## module
UTIL_COLLECTION_MODULE = $(UTIL_COLLECTION)

## module version
UTIL_COLLECTION_VERSION = $(VERSION)

## javac --release option
UTIL_COLLECTION_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
UTIL_COLLECTION_ENABLE_PREVIEW = 0

## compile deps
UTIL_COLLECTION_COMPILE_DEPS = $(LANG_OBJECT_JAR_FILE)

## jar name
UTIL_COLLECTION_JAR_NAME = $(UTIL_COLLECTION)

## test compile deps
UTIL_COLLECTION_TEST_COMPILE_DEPS = $(UTIL_COLLECTION_COMPILE_DEPS)
UTIL_COLLECTION_TEST_COMPILE_DEPS += $(UTIL_COLLECTION_JAR_FILE)
UTIL_COLLECTION_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
UTIL_COLLECTION_TEST_RUNTIME_DEPS = $(UTIL_COLLECTION_TEST_COMPILE_DEPS)
UTIL_COLLECTION_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
UTIL_COLLECTION_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
UTIL_COLLECTION_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## install coordinates
UTIL_COLLECTION_GROUP_ID = $(GROUP_ID)
UTIL_COLLECTION_ARTIFACT_ID = $(UTIL_COLLECTION_MODULE)

## copyright years for javadoc
UTIL_COLLECTION_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# UTIL_COLLECTION_JAVADOC_SNIPPET_PATH := UTIL_COLLECTION_TEST

## pom description
UTIL_COLLECTION_DESCRIPTION = Utilities for working with arrays of primitives and references

#
# objectos.util.collection targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),UTIL_COLLECTION_)))

.PHONY: util.collection@clean
util.collection@clean:
	rm -rf $(UTIL_COLLECTION_WORK)/*

.PHONY: util.collection@compile
util.collection@compile: $(UTIL_COLLECTION_COMPILE_MARKER)

.PHONY: util.collection@jar
util.collection@jar: $(UTIL_COLLECTION_JAR_FILE)

.PHONY: util.collection@test
util.collection@test: $(UTIL_COLLECTION_TEST_RUN_MARKER)

.PHONY: util.collection@install
util.collection@install: $(UTIL_COLLECTION_INSTALL)

.PHONY: util.collection@source-jar
util.collection@source-jar: $(UTIL_COLLECTION_SOURCE_JAR_FILE)

.PHONY: util.collection@javadoc
util.collection@javadoc: $(UTIL_COLLECTION_JAVADOC_JAR_FILE)

.PHONY: util.collection@pom
util.collection@pom: $(UTIL_COLLECTION_POM_FILE)

.PHONY: util.collection@ossrh-prepare
util.collection@ossrh-prepare: $(UTIL_COLLECTION_OSSRH_PREPARE)
