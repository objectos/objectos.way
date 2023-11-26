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
# objectos.fs.zip
#

## module directory
FS_ZIP = objectos.fs.zip

## module
FS_ZIP_MODULE = $(FS_ZIP)

## module version
FS_ZIP_GROUP_ID = $(GROUP_ID)
FS_ZIP_ARTIFACT_ID = $(FS_ZIP)
FS_ZIP_VERSION = $(VERSION)

## javac --release option
FS_ZIP_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
FS_ZIP_ENABLE_PREVIEW = 0

## compile deps
FS_ZIP_COMPILE_DEPS = $(call module-gav,$(FS))

## test compile deps
FS_ZIP_TEST_COMPILE_DEPS  = $(FS_ZIP_COMPILE_DEPS)
FS_ZIP_TEST_COMPILE_DEPS += $(call module-gav,$(CORE_TESTING))
FS_ZIP_TEST_COMPILE_DEPS += $(call module-gav,$(FS_ZIP))
FS_ZIP_TEST_COMPILE_DEPS += $(call module-gav,$(FS_TESTING))
FS_ZIP_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
FS_ZIP_TEST_RUNTIME_DEPS  = $(FS_ZIP_TEST_COMPILE_DEPS)
FS_ZIP_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## runtime modules
FS_ZIP_TEST_JAVAX_MODULES  = org.testng
FS_ZIP_TEST_JAVAX_MODULES += objectos.core.testing
FS_ZIP_TEST_JAVAX_MODULES += objectos.fs.testing

## test runtime reads
FS_ZIP_TEST_JAVAX_READS  = objectos.core.testing
FS_ZIP_TEST_JAVAX_READS += objectos.fs.testing

## copyright years for javadoc
FS_ZIP_COPYRIGHT_YEARS := 2021-2023

## javadoc snippet path
# FS_ZIP_JAVADOC_SNIPPET_PATH := FS_ZIP_TEST

## pom description
FS_ZIP_DESCRIPTION = $(FS_ZIP)

#
# objectos.fs.zip targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),FS_ZIP_,fs.zip@)))

.PHONY: fs.zip@source-jar
fs.zip@source-jar: $(FS_ZIP_SOURCE_JAR_FILE)

.PHONY: fs.zip@javadoc
fs.zip@javadoc: $(FS_ZIP_JAVADOC_JAR_FILE)

.PHONY: fs.zip@ossrh-prepare
fs.zip@ossrh-prepare: $(FS_ZIP_OSSRH_PREPARE)
