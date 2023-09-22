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

# This file was generated. Do not edit!

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

## code directory/module name
CODE := objectos.code

## code module version
CODE_VERSION := $(VERSION)

## code javac --release option
CODE_JAVA_RELEASE := 21

## code --enable-preview ?
CODE_ENABLE_PREVIEW := 1

## code jar name
CODE_JAR_NAME := $(CODE)

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

#
# Defines the java tools
#

## configures JAVA_HOME_BIN
ifdef JAVA_HOME
JAVA_HOME_BIN := $(JAVA_HOME)/bin
else
JAVA_HOME_BIN :=
endif

## java command
JAVA := $(JAVA_HOME_BIN)/java

## javac command
JAVAC := $(JAVA_HOME_BIN)/javac
JAVAC += -g

## jar command
JAR := $(JAVA_HOME_BIN)/jar

## javadoc command
JAVADOC := $(JAVA_HOME_BIN)/javadoc

#
# objectos.code compilation options
#

## objectos.code source directory
CODE_MAIN = $(CODE)/main

## objectos.code source files
CODE_SOURCES = $(shell find ${CODE_MAIN} -type f -name '*.java' -print)

## objectos.code source files modified since last compilation
CODE_DIRTY :=

## objectos.code work dir
CODE_WORK = $(CODE)/work

## objectos.code class output path
CODE_CLASS_OUTPUT = $(CODE_WORK)/main

## objectos.code compiled classes
CODE_CLASSES = $(CODE_SOURCES:$(CODE_MAIN)/%.java=$(CODE_CLASS_OUTPUT)/%.class)

## objectos.code compile-time dependencies
# CODE_COMPILE_DEPS = 

## objectos.code compile-time module-path
CODE_COMPILE_MODULE_PATH = $(call module-path,$(CODE_COMPILE_DEPS))
 
## objectos.code javac command
CODE_JAVACX = $(JAVAC)
CODE_JAVACX += -d $(CODE_CLASS_OUTPUT)
CODE_JAVACX += -g
CODE_JAVACX += -Xlint:all
CODE_JAVACX += -Xpkginfo:always
ifdef CODE_ENABLE_PREVIEW
CODE_JAVACX += --enable-preview
endif
ifneq ($(CODE_COMPILE_MODULE_PATH),)
CODE_JAVACX += --module-path $(CODE_COMPILE_MODULE_PATH)
endif
CODE_JAVACX += --module-version $(CODE_VERSION)
CODE_JAVACX += --release $(CODE_JAVA_RELEASE)
CODE_JAVACX += $(CODE_DIRTY)

## objectos.code compilation marker
CODE_COMPILE_MARKER = $(CODE_WORK)/compile-marker

#
# objectos.code compilation targets
#

$(CODE_COMPILE_MARKER): $(CODE_COMPILE_DEPS) $(CODE_CLASSES)
	if [ -n "$(CODE_DIRTY)" ]; then \
		$(CODE_JAVACX); \
	fi
	touch $(CODE_COMPILE_MARKER)

$(CODE_CLASSES): $(CODE_CLASS_OUTPUT)/%.class: $(CODE_MAIN)/%.java
	$(eval CODE_DIRTY += $$<)

#
# objectos.code jar options
#

## objectos.code license 'artifact'
CODE_LICENSE = $(CODE_CLASS_OUTPUT)/META-INF/LICENSE

## objectos.code jar file path
CODE_JAR_FILE = $(CODE_WORK)/$(CODE_JAR_NAME)-$(CODE_VERSION).jar

## objectos.code jar command
CODE_JARX = $(JAR)
CODE_JARX += --create
CODE_JARX += --file $(CODE_JAR_FILE)
CODE_JARX += --module-version $(CODE_VERSION)
CODE_JARX += -C $(CODE_CLASS_OUTPUT)
CODE_JARX += .

#
# objectos.code jar targets
#

$(CODE_JAR_FILE): $(CODE_COMPILE_MARKER) $(CODE_LICENSE)
	$(CODE_JARX)

$(CODE_LICENSE): LICENSE
	mkdir --parents $(@D)
	cp LICENSE $(@D)

#
# objectos.way compilation options
#

## objectos.way source directory
WAY_MAIN = $(WAY)/main

## objectos.way source files
WAY_SOURCES = $(shell find ${WAY_MAIN} -type f -name '*.java' -print)

## objectos.way source files modified since last compilation
WAY_DIRTY :=

## objectos.way work dir
WAY_WORK = $(WAY)/work

## objectos.way class output path
WAY_CLASS_OUTPUT = $(WAY_WORK)/main

## objectos.way compiled classes
WAY_CLASSES = $(WAY_SOURCES:$(WAY_MAIN)/%.java=$(WAY_CLASS_OUTPUT)/%.class)

## objectos.way compile-time dependencies
# WAY_COMPILE_DEPS = 

## objectos.way compile-time module-path
WAY_COMPILE_MODULE_PATH = $(call module-path,$(WAY_COMPILE_DEPS))
 
## objectos.way javac command
WAY_JAVACX = $(JAVAC)
WAY_JAVACX += -d $(WAY_CLASS_OUTPUT)
WAY_JAVACX += -g
WAY_JAVACX += -Xlint:all
WAY_JAVACX += -Xpkginfo:always
ifdef WAY_ENABLE_PREVIEW
WAY_JAVACX += --enable-preview
endif
ifneq ($(WAY_COMPILE_MODULE_PATH),)
WAY_JAVACX += --module-path $(WAY_COMPILE_MODULE_PATH)
endif
WAY_JAVACX += --module-version $(WAY_VERSION)
WAY_JAVACX += --release $(WAY_JAVA_RELEASE)
WAY_JAVACX += $(WAY_DIRTY)

## objectos.way compilation marker
WAY_COMPILE_MARKER = $(WAY_WORK)/compile-marker

#
# objectos.way compilation targets
#

$(WAY_COMPILE_MARKER): $(WAY_COMPILE_DEPS) $(WAY_CLASSES)
	if [ -n "$(WAY_DIRTY)" ]; then \
		$(WAY_JAVACX); \
	fi
	touch $(WAY_COMPILE_MARKER)

$(WAY_CLASSES): $(WAY_CLASS_OUTPUT)/%.class: $(WAY_MAIN)/%.java
	$(eval WAY_DIRTY += $$<)

#
# Targets
#

.PHONY: clean
clean: code@clean way@clean

.PHONY: test
test:

# maybe use eval for module@target targets?

.PHONY: code@clean
code@clean:
	rm -rf $(CODE_WORK)/*

.PHONY: way@clean
way@clean:
	rm -rf $(WAY_WORK)/*

.PHONY: code@compile
code@compile: $(CODE_COMPILE_MARKER)

.PHONY: code@jar
code@jar: $(CODE_JAR_FILE)

.PHONY: way@compile
way@compile: $(WAY_COMPILE_MARKER)
