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

@generated-msg@

#
# Objectos Way
#

GROUP_ID := br.com.objectos
ARTIFACT_ID := objectos.way
VERSION := 0.1.5.5-SNAPSHOT

## Deps versions

TESTNG_VERSION := 7.7.1
JCOMMANDER_VERSION := 1.82
SLF4J_VERSION := 1.7.36

JAVA_RELEASE := 21

#
# objectos.core.object options
#

## directory
CORE_OBJECT := objectos.core.object

## module
CORE_OBJECT_MODULE := $(CORE_OBJECT)

## module version
CORE_OBJECT_VERSION := $(VERSION)

## javac --release option
CORE_OBJECT_JAVA_RELEASE := $(JAVA_RELEASE)

## --enable-preview ?
CORE_OBJECT_ENABLE_PREVIEW := 0

## jar name
CORE_OBJECT_JAR_NAME := $(CORE_OBJECT)

## test compile deps
CORE_OBJECT_TEST_COMPILE_DEPS = $(CORE_OBJECT_JAR_FILE)
CORE_OBJECT_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
CORE_OBJECT_TEST_RUNTIME_DEPS = $(CODE_TEST_COMPILE_DEPS)
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

#
# objectos.way options
# 

## way directory
WAY := objectos.way

## way module
WAY_MODULE := $(WAY)

## way module version
WAY_VERSION := $(VERSION)

## way javac --release option
WAY_JAVA_RELEASE := 21

## way --enable-preview ?
WAY_ENABLE_PREVIEW := 0

## way jar name
WAY_JAR_NAME := $(WAY)

## way JS source
WAY_JS_SRC = $(WAY)/js/objectos.way.js

## way JS artifact
WAY_JS_ARTIFACT = $(WAY_CLASS_OUTPUT)/objectos/js/objectos.way.js

## way jar file reqs
WAY_JAR_FILE_REQS_MORE = $(WAY_JS_ARTIFACT)

## way test compile-time dependencies
WAY_TEST_COMPILE_DEPS = $(WAY_JAR_FILE)
WAY_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## way test runtime dependencies
WAY_TEST_RUNTIME_DEPS = $(WAY_TEST_COMPILE_DEPS)
WAY_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
WAY_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
WAY_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## way test runtime reads
WAY_TEST_JAVAX_READS := java.compiler

## way test runtime exports
WAY_TEST_JAVAX_EXPORTS := objectos.html.internal
WAY_TEST_JAVAX_EXPORTS += objectos.util
WAY_TEST_JAVAX_EXPORTS += objectox.css
WAY_TEST_JAVAX_EXPORTS += objectox.css.util
WAY_TEST_JAVAX_EXPORTS += objectox.http
WAY_TEST_JAVAX_EXPORTS += objectox.lang

## way install coordinates
WAY_GROUP_ID := $(GROUP_ID)
WAY_ARTIFACT_ID := $(ARTIFACT_ID)

## way copyright years for javadoc
WAY_COPYRIGHT_YEARS := 2022-2023

## way javadoc snippet path
WAY_JAVADOC_SNIPPET_PATH := WAY_TEST

# Delete the default suffixes
.SUFFIXES:

#
# Default target
#

.PHONY: all
all: jar

.PHONY: jar
jar: way@jar
