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
VERSION := 0.1.1-SNAPSHOT

## Deps versions

TESTNG_VERSION := 7.7.1
JCOMMANDER_VERSION := 1.82
SLF4J_VERSION := 1.7.36

#
# objectos.code options
#

## code directory
CODE := objectos.code

## code module
CODE_MODULE := $(CODE)

## code module version
CODE_VERSION := $(VERSION)

## code javac --release option
CODE_JAVA_RELEASE := 21

## code --enable-preview ?
CODE_ENABLE_PREVIEW := 1

## code jar name
CODE_JAR_NAME := $(CODE)

## code test compile deps
CODE_TEST_COMPILE_DEPS = $(CODE_JAR_FILE)
CODE_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## code test runtime dependencies
CODE_TEST_RUNTIME_DEPS = $(CODE_TEST_COMPILE_DEPS)
CODE_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
CODE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
CODE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime exports
CODE_TEST_JAVAX_EXPORTS := objectos.code.internal

#
# objectos.selfgen options
#

## selfgen directory
SELFGEN := objectos.selfgen

## selfgen module
SELFGEN_MODULE := $(SELFGEN)

## selfgen module version
SELFGEN_VERSION := $(VERSION)

## selfgen javac --release option
SELFGEN_JAVA_RELEASE := 21

## selfgen --enable-preview ?
SELFGEN_ENABLE_PREVIEW := 1

## selfgen compile deps
SELFGEN_COMPILE_DEPS = $(CODE_JAR_FILE) 

## selfgen jar name
SELFGEN_JAR_NAME := $(SELFGEN)

## selfgen test compile deps
SELFGEN_TEST_COMPILE_DEPS = $(CODE_JAR_FILE)
SELFGEN_TEST_COMPILE_DEPS += $(SELFGEN_JAR_FILE)
SELFGEN_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## selfgen test runtime dependencies
SELFGEN_TEST_RUNTIME_DEPS = $(SELFGEN_TEST_COMPILE_DEPS)
SELFGEN_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
SELFGEN_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
SELFGEN_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## seflgen test runtime exports
SELFGEN_TEST_JAVAX_EXPORTS := objectos.selfgen.css
SELFGEN_TEST_JAVAX_EXPORTS += objectos.selfgen.html

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

## way test runtime exports
WAY_TEST_JAVAX_EXPORTS := objectos.css.internal
WAY_TEST_JAVAX_EXPORTS += objectos.html.internal
WAY_TEST_JAVAX_EXPORTS += objectos.lang
WAY_TEST_JAVAX_EXPORTS += objectos.util
WAY_TEST_JAVAX_EXPORTS += objectox.http

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
