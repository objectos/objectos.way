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
# objectos.notes options
#

## module directory
NOTES = objectos.notes

## module
NOTES_MODULE = $(NOTES)

## module version
NOTES_VERSION = $(VERSION)

## javac --release option
NOTES_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
NOTES_ENABLE_PREVIEW = 1

## compile deps
NOTES_COMPILE_DEPS = $(LANG_OBJECT_JAR_FILE)

## jar name
NOTES_JAR_NAME = $(NOTES)

## test compile deps
NOTES_TEST_COMPILE_DEPS = $(NOTES_COMPILE_DEPS)
NOTES_TEST_COMPILE_DEPS += $(NOTES_JAR_FILE)
NOTES_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
NOTES_TEST_RUNTIME_DEPS = $(NOTES_TEST_COMPILE_DEPS)
NOTES_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
NOTES_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
NOTES_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime exports
NOTES_TEST_JAVAX_EXPORTS := objectos.notes.internal

## install coordinates
NOTES_GROUP_ID = $(GROUP_ID)
NOTES_ARTIFACT_ID = $(NOTES_MODULE)

## copyright years for javadoc
NOTES_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# NOTES_JAVADOC_SNIPPET_PATH := NOTES_TEST

## pom description
NOTES_DESCRIPTION = Type-safe note sink API

#
# objectos.notes targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),NOTES_)))

.PHONY: notes@clean
notes@clean:
	rm -rf $(NOTES_WORK)/*

.PHONY: notes@compile
notes@compile: $(NOTES_COMPILE_MARKER)

.PHONY: notes@jar
notes@jar: $(NOTES_JAR_FILE)

.PHONY: notes@test
notes@test: $(NOTES_TEST_RUN_MARKER)

.PHONY: notes@install
notes@install: $(NOTES_INSTALL)

.PHONY: notes@source-jar
notes@source-jar: $(NOTES_SOURCE_JAR_FILE)

.PHONY: notes@javadoc
notes@javadoc: $(NOTES_JAVADOC_JAR_FILE)

.PHONY: notes@pom
notes@pom: $(NOTES_POM_FILE)

.PHONY: notes@ossrh-prepare
notes@ossrh-prepare: $(NOTES_OSSRH_PREPARE)

