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

VERSION := 0.1.0-SNAPSHOT

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
# objectos.way options
# 

## way directory/module name
WAY := objectos.way

## way module version
WAY_VERSION := $(VERSION)

## way javac --release option
WAY_JAVA_RELEASE := 21

## way --enable-preview ?
WAY_ENABLE_PREVIEW := 0

SGEN_JAVA_RELEASE = 21
SGEN_ENABLE_PREVIEW = 1

# Delete the default suffixes
.SUFFIXES:

#
# Default target
#
.PHONY: all
all: way@compile
