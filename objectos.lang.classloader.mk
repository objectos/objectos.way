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
# objectos.lang.classloader options
#

## module directory
LANG_CLASSLOADER = objectos.lang.classloader

## module
LANG_CLASSLOADER_MODULE = $(LANG_CLASSLOADER)

## module version
LANG_CLASSLOADER_VERSION = $(VERSION)

## javac --release option
LANG_CLASSLOADER_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
LANG_CLASSLOADER_ENABLE_PREVIEW = 0

## compile deps
LANG_CLASSLOADER_COMPILE_DEPS = $(LANG_OBJECT_JAR_FILE)
LANG_CLASSLOADER_COMPILE_DEPS += $(NOTES_JAR_FILE)
LANG_CLASSLOADER_COMPILE_DEPS += $(UTIL_ARRAY_JAR_FILE)
LANG_CLASSLOADER_COMPILE_DEPS += $(UTIL_COLLECTION_JAR_FILE)
LANG_CLASSLOADER_COMPILE_DEPS += $(UTIL_LIST_JAR_FILE)
LANG_CLASSLOADER_COMPILE_DEPS += $(UTIL_MAP_JAR_FILE)

## jar name
LANG_CLASSLOADER_JAR_NAME = $(LANG_CLASSLOADER)

## test compile deps
LANG_CLASSLOADER_TEST_COMPILE_DEPS = $(LANG_CLASSLOADER_COMPILE_DEPS)
LANG_CLASSLOADER_TEST_COMPILE_DEPS += $(NOTES_BASE_JAR_FILE)
LANG_CLASSLOADER_TEST_COMPILE_DEPS += $(NOTES_CONSOLE_JAR_FILE)
LANG_CLASSLOADER_TEST_COMPILE_DEPS += $(LANG_CLASSLOADER_JAR_FILE)
LANG_CLASSLOADER_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
LANG_CLASSLOADER_TEST_RUNTIME_DEPS = $(LANG_CLASSLOADER_TEST_COMPILE_DEPS)
LANG_CLASSLOADER_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
LANG_CLASSLOADER_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
LANG_CLASSLOADER_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime modules
LANG_CLASSLOADER_TEST_JAVAX_MODULES = org.testng
LANG_CLASSLOADER_TEST_JAVAX_MODULES += java.compiler
LANG_CLASSLOADER_TEST_JAVAX_MODULES += objectos.notes.console

## way test runtime reads
LANG_CLASSLOADER_TEST_JAVAX_READS = java.compiler
LANG_CLASSLOADER_TEST_JAVAX_READS += objectos.notes.console

## test runtime exports
LANG_CLASSLOADER_TEST_JAVAX_EXPORTS = objectox.lang.classloader

## install coordinates
LANG_CLASSLOADER_GROUP_ID = $(GROUP_ID)
LANG_CLASSLOADER_ARTIFACT_ID = $(LANG_CLASSLOADER_MODULE)

## copyright years for javadoc
LANG_CLASSLOADER_COPYRIGHT_YEARS := 2022-2023

## pom description
LANG_CLASSLOADER_DESCRIPTION = Utilities for java.lang.ClassLoader

#
# eval tasks
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),LANG_CLASSLOADER_)))

#
# objectos.lang.classloader targets
#

.PHONY: lang.classloader@clean
lang.classloader@clean:
	rm -rf $(LANG_CLASSLOADER_WORK)/*

.PHONY: lang.classloader@compile
lang.classloader@compile: $(LANG_CLASSLOADER_SELFGEN_MARKER) $(LANG_CLASSLOADER_COMPILE_MARKER)

.PHONY: lang.classloader@jar
lang.classloader@jar: $(LANG_CLASSLOADER_JAR_FILE)

.PHONY: lang.classloader@test
lang.classloader@test: $(LANG_CLASSLOADER_TEST_RUN_MARKER)

.PHONY: lang.classloader@install
lang.classloader@install: $(LANG_CLASSLOADER_INSTALL)

.PHONY: lang.classloader@source-jar
lang.classloader@source-jar: $(LANG_CLASSLOADER_SOURCE_JAR_FILE)

.PHONY: lang.classloader@javadoc
lang.classloader@javadoc: $(LANG_CLASSLOADER_JAVADOC_JAR_FILE)

.PHONY: lang.classloader@pom
lang.classloader@pom: $(LANG_CLASSLOADER_POM_FILE)

.PHONY: lang.classloader@ossrh-prepare
lang.classloader@ossrh-prepare: $(LANG_CLASSLOADER_OSSRH_PREPARE)
