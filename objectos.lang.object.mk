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

## module version
LANG_OBJECT_VERSION = $(VERSION)

## javac --release option
LANG_OBJECT_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
LANG_OBJECT_ENABLE_PREVIEW = 0

## jar name
LANG_OBJECT_JAR_NAME = $(LANG_OBJECT)

## test compile deps
LANG_OBJECT_TEST_COMPILE_DEPS = $(LANG_OBJECT_JAR_FILE)
LANG_OBJECT_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
LANG_OBJECT_TEST_RUNTIME_DEPS = $(LANG_OBJECT_TEST_COMPILE_DEPS)
LANG_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
LANG_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
LANG_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## install coordinates
LANG_OBJECT_GROUP_ID = $(GROUP_ID)
LANG_OBJECT_ARTIFACT_ID = $(LANG_OBJECT_MODULE)

## copyright years for javadoc
LANG_OBJECT_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# LANG_OBJECT_JAVADOC_SNIPPET_PATH := LANG_OBJECT_TEST

## pom description
LANG_OBJECT_DESCRIPTION = Utilities for java.lang.Object instances

#
# objectos.lang.object targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),LANG_OBJECT_)))

.PHONY: core.object@clean
core.object@clean:
	rm -rf $(LANG_OBJECT_WORK)/*

.PHONY: core.object@compile
core.object@compile: $(LANG_OBJECT_COMPILE_MARKER)

.PHONY: core.object@jar
core.object@jar: $(LANG_OBJECT_JAR_FILE)

.PHONY: core.object@test
core.object@test: $(LANG_OBJECT_TEST_RUN_MARKER)

.PHONY: core.object@install
core.object@install: $(LANG_OBJECT_INSTALL)

.PHONY: core.object@source-jar
core.object@source-jar: $(LANG_OBJECT_SOURCE_JAR_FILE)

.PHONY: core.object@javadoc
core.object@javadoc: $(LANG_OBJECT_JAVADOC_JAR_FILE)

.PHONY: core.object@pom
core.object@pom: $(LANG_OBJECT_POM_FILE)

.PHONY: core.object@ossrh-prepare
core.object@ossrh-prepare: $(LANG_OBJECT_OSSRH_PREPARE)
