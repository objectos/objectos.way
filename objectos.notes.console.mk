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
NOTES_CONSOLE = objectos.notes.console

## module
NOTES_CONSOLE_MODULE = $(NOTES_CONSOLE)

## module version
NOTES_CONSOLE_VERSION = $(VERSION)

## javac --release option
NOTES_CONSOLE_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
NOTES_CONSOLE_ENABLE_PREVIEW = 0

## compile deps
NOTES_CONSOLE_COMPILE_DEPS = $(call module-gav,$(NOTES_BASE))

## jar name
NOTES_CONSOLE_JAR_NAME = $(NOTES_CONSOLE)

## test compile deps
NOTES_CONSOLE_TEST_COMPILE_DEPS = $(NOTES_CONSOLE_COMPILE_DEPS)
NOTES_CONSOLE_TEST_COMPILE_DEPS += $(NOTES_CONSOLE_JAR_FILE)
NOTES_CONSOLE_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
NOTES_CONSOLE_TEST_RUNTIME_DEPS = $(NOTES_CONSOLE_TEST_COMPILE_DEPS)
NOTES_CONSOLE_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
NOTES_CONSOLE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
NOTES_CONSOLE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## install coordinates
NOTES_CONSOLE_GROUP_ID = $(GROUP_ID)
NOTES_CONSOLE_ARTIFACT_ID = $(NOTES_CONSOLE_MODULE)

## copyright years for javadoc
NOTES_CONSOLE_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# NOTES_CONSOLE_JAVADOC_SNIPPET_PATH := NOTES_CONSOLE_TEST

## pom description
NOTES_CONSOLE_DESCRIPTION = NoteSink implementation that writes out notes to the system console.

#
# objectos.notes targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),NOTES_CONSOLE_,notes.console@)))

.PHONY: notes.console@jar
notes.console@jar: $(NOTES_CONSOLE_JAR_FILE)

.PHONY: notes.console@test
notes.console@test: $(NOTES_CONSOLE_TEST_RUN_MARKER)

.PHONY: notes.console@install
notes.console@install: $(NOTES_CONSOLE_INSTALL)

.PHONY: notes.console@source-jar
notes.console@source-jar: $(NOTES_CONSOLE_SOURCE_JAR_FILE)

.PHONY: notes.console@javadoc
notes.console@javadoc: $(NOTES_CONSOLE_JAVADOC_JAR_FILE)

.PHONY: notes.console@pom
notes.console@pom: $(NOTES_CONSOLE_POM_FILE)

.PHONY: notes.console@ossrh-prepare
notes.console@ossrh-prepare: $(NOTES_CONSOLE_OSSRH_PREPARE)
