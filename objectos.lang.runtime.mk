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
# objectos.lang.runtime options
#

## module directory
LANG_RUNTIME = objectos.lang.runtime

## module
LANG_RUNTIME_MODULE = $(LANG_RUNTIME)

## module version
LANG_RUNTIME_GROUP_ID = $(GROUP_ID)
LANG_RUNTIME_ARTIFACT_ID = $(LANG_RUNTIME_MODULE)
LANG_RUNTIME_VERSION = $(VERSION)

## javac --release option
LANG_RUNTIME_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
LANG_RUNTIME_ENABLE_PREVIEW = 0

## compile deps
LANG_RUNTIME_COMPILE_DEPS  = $(call module-gav,$(NOTES))
LANG_RUNTIME_COMPILE_DEPS += $(call module-gav,$(UTIL_LIST))

## test compile deps
LANG_RUNTIME_TEST_COMPILE_DEPS  = $(LANG_RUNTIME_COMPILE_DEPS)
LANG_RUNTIME_TEST_COMPILE_DEPS += $(call module-gav,$(LANG_RUNTIME))
LANG_RUNTIME_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
LANG_RUNTIME_TEST_RUNTIME_DEPS  = $(LANG_RUNTIME_TEST_COMPILE_DEPS)
LANG_RUNTIME_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## test runtime exports
#LANG_RUNTIME_TEST_JAVAX_EXPORTS := objectos.lang.runtime.internal

## copyright years for javadoc
LANG_RUNTIME_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# LANG_RUNTIME_JAVADOC_SNIPPET_PATH := LANG_RUNTIME_TEST

## pom description
LANG_RUNTIME_DESCRIPTION = Utilities for the java.lang.Runtime class

#
# objectos.lang.runtime targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),LANG_RUNTIME_,lang.runtime@)))

.PHONY: lang.runtime@source-jar
lang.runtime@source-jar: $(LANG_RUNTIME_SOURCE_JAR_FILE)

.PHONY: lang.runtime@javadoc
lang.runtime@javadoc: $(LANG_RUNTIME_JAVADOC_JAR_FILE)

.PHONY: lang.runtime@ossrh-prepare
lang.runtime@ossrh-prepare: $(LANG_RUNTIME_OSSRH_PREPARE)

