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
LANG_CLASSLOADER_COMPILE_DEPS  = $(call module-gav,$(NOTES))
LANG_CLASSLOADER_COMPILE_DEPS += $(call module-gav,$(UTIL_LIST))
LANG_CLASSLOADER_COMPILE_DEPS += $(call module-gav,$(UTIL_MAP))

## test compile deps
LANG_CLASSLOADER_TEST_COMPILE_DEPS  = $(LANG_CLASSLOADER_COMPILE_DEPS)
LANG_CLASSLOADER_TEST_COMPILE_DEPS += $(call module-gav,$(LANG_CLASSLOADER))
LANG_CLASSLOADER_TEST_COMPILE_DEPS += $(call module-gav,$(NOTES_CONSOLE))
LANG_CLASSLOADER_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
LANG_CLASSLOADER_TEST_RUNTIME_DEPS  = $(LANG_CLASSLOADER_TEST_COMPILE_DEPS)
LANG_CLASSLOADER_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

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

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),LANG_CLASSLOADER_,lang.classloader@)))

#
# objectos.lang.classloader targets
#

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
