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
# objectos.git
#

## module directory
GIT = objectos.git

## module
GIT_MODULE = $(GIT)

## module version
GIT_GROUP_ID = $(GROUP_ID)
GIT_ARTIFACT_ID = $(GIT)
GIT_VERSION = $(VERSION)

## javac --release option
GIT_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
GIT_ENABLE_PREVIEW = 0

## compile deps
GIT_COMPILE_DEPS  = $(call module-gav,$(CONCURRENT))
GIT_COMPILE_DEPS += $(call module-gav,$(FS))
GIT_COMPILE_DEPS += $(call module-gav,$(NOTES))
GIT_COMPILE_DEPS += $(call module-gav,$(UTIL_LIST))
GIT_COMPILE_DEPS += $(call module-gav,$(UTIL_MAP))
GIT_COMPILE_DEPS += $(call module-gav,$(UTIL_SET))

## test compile deps
GIT_TEST_COMPILE_DEPS  = $(GIT_COMPILE_DEPS)
GIT_TEST_COMPILE_DEPS += $(call module-gav,$(GIT))
GIT_TEST_COMPILE_DEPS += $(call module-gav,$(CORE_SERVICE))
GIT_TEST_COMPILE_DEPS += $(call module-gav,$(FS_TESTING))
GIT_TEST_COMPILE_DEPS += $(TESTNG)

## test resources
GIT_TEST_RESOURCES := $(GIT)/test-resources

## test runtime dependencies
GIT_TEST_RUNTIME_DEPS  = $(GIT_TEST_COMPILE_DEPS)
GIT_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## runtime modules
GIT_TEST_JAVAX_MODULES  = org.testng
GIT_TEST_JAVAX_MODULES += objectos.core.service
GIT_TEST_JAVAX_MODULES += objectos.fs.testing

## test runtime reads
GIT_TEST_JAVAX_READS  = objectos.core.service
GIT_TEST_JAVAX_READS += objectos.fs.testing

## copyright years for javadoc
GIT_COPYRIGHT_YEARS := 2020-2023

## pom description
GIT_DESCRIPTION = Objectos Git is a pure Java library that provides a limited set of GIT operations.

#
# objectos.git targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),GIT_,git@)))

.PHONY: git@source-jar
git@source-jar: $(GIT_SOURCE_JAR_FILE)

.PHONY: git@javadoc
git@javadoc: $(GIT_JAVADOC_JAR_FILE)

.PHONY: git@ossrh-prepare
git@ossrh-prepare: $(GIT_OSSRH_PREPARE)
