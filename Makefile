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

GROUP_ID := br.com.objectos
ARTIFACT_ID := objectos.way
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
WAY_TEST_JAVAX_EXPORTS += objectos.http.internal
WAY_TEST_JAVAX_EXPORTS += objectos.lang
WAY_TEST_JAVAX_EXPORTS += objectos.util

## install coordinates
WAY_GROUP_ID := $(GROUP_ID)
WAY_ARTIFACT_ID := $(ARTIFACT_ID)

# Delete the default suffixes
.SUFFIXES:

#
# Default target
#

.PHONY: all
all: jar

.PHONY: jar
jar: way@jar

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
# Dependencies related options & functions
#

## local repository path
LOCAL_REPO_PATH := $(HOME)/.cache/objectos

## remote repository URL
REMOTE_REPO_URL := https://repo.maven.apache.org/maven2

## remote repository curl
REMOTE_REPO_CURLX = $(CURL)
REMOTE_REPO_CURLX += --create-dirs

## dependency function
## 
## syntax:
## $(call dependency,[GROUP_ID],[ARTIFACT_ID],[VERSION])
dot := .
solidus := /

dependency = $(LOCAL_REPO_PATH)/$(subst $(dot),$(solidus),$(1))/$(2)/$(3)/$(2)-$(3).jar

## class-path function
##
## syntax:
## $(call class-path,[list of deps])
ifeq ($(OS),Windows_NT)
CLASS_PATH_SEPARATOR := ;
else
CLASS_PATH_SEPARATOR := :
endif
empty :=
space := $(empty) $(empty)

class-path = $(subst $(space),$(CLASS_PATH_SEPARATOR),$(1))

## module-path function
##
## syntax:
## $(call module-path,[list of deps])
MODULE_PATH_SEPARATOR := :

module-path = $(subst $(space),$(MODULE_PATH_SEPARATOR),$(1))

#
# Gets the dependency from the remote repository
#

$(LOCAL_REPO_PATH)/%.jar:	
	$(REMOTE_REPO_CURLX) --output $@ $(@:$(LOCAL_REPO_PATH)/%.jar=$(REMOTE_REPO_URL)/%.jar)

#
# objectos.code compilation options
#

## objectos.code source directory
CODE_MAIN = $(CODE_MODULE)/main

## objectos.code source files
CODE_SOURCES = $(shell find ${CODE_MAIN} -type f -name '*.java' -print)

## objectos.code source files modified since last compilation
CODE_DIRTY :=

## objectos.code work dir
CODE_WORK = $(CODE_MODULE)/work

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
ifeq ($(CODE_ENABLE_PREVIEW),1)
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
	touch $(CODE_COMPILE_MARKER); \

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
# objectos.code test compilation options
#

## objectos.code test source directory
CODE_TEST = $(CODE_MODULE)/test

## objectos.code test source files 
CODE_TEST_SOURCES = $(shell find ${CODE_TEST} -type f -name '*.java' -print)

## objectos.code test source files modified since last compilation
CODE_TEST_DIRTY :=

## objectos.code test class output path
CODE_TEST_CLASS_OUTPUT = $(CODE_WORK)/test

## objectos.code test compiled classes
CODE_TEST_CLASSES = $(CODE_TEST_SOURCES:$(CODE_TEST)/%.java=$(CODE_TEST_CLASS_OUTPUT)/%.class)

## objectos.code test compile-time dependencies
# CODE_TEST_COMPILE_DEPS =

## objectos.code test javac command
CODE_TEST_JAVACX = $(JAVAC)
CODE_TEST_JAVACX += -d $(CODE_TEST_CLASS_OUTPUT)
CODE_TEST_JAVACX += -g
CODE_TEST_JAVACX += -Xlint:all
CODE_TEST_JAVACX += --class-path $(call class-path,$(CODE_TEST_COMPILE_DEPS))
ifeq ($(CODE_ENABLE_PREVIEW),1)
CODE_TEST_JAVACX += --enable-preview
endif
CODE_TEST_JAVACX += --release $(CODE_JAVA_RELEASE)
CODE_TEST_JAVACX += $(CODE_TEST_DIRTY)

## objectos.code test compilation marker
CODE_TEST_COMPILE_MARKER = $(CODE_WORK)/test-compile-marker

#
# objectos.code test compilation targets
#

$(CODE_TEST_COMPILE_MARKER): $(CODE_TEST_COMPILE_DEPS) $(CODE_TEST_CLASSES) 
	if [ -n "$(CODE_TEST_DIRTY)" ]; then \
		$(CODE_TEST_JAVACX); \
		touch $(CODE_TEST_COMPILE_MARKER); \
	fi

$(CODE_TEST_CLASSES): $(CODE_TEST_CLASS_OUTPUT)/%.class: $(CODE_TEST)/%.java
	$(eval CODE_TEST_DIRTY += $$<)

#
# objectos.code test execution options
#

## objectos.code test runtime dependencies
# CODE_TEST_RUNTIME_DEPS =

## objectos.code test main class
ifndef CODE_TEST_MAIN
CODE_TEST_MAIN = $(CODE_MODULE).RunTests
endif

## objectos.code test runtime output path
CODE_TEST_RUNTIME_OUTPUT = $(CODE_WORK)/test-output

## objectos.code test java command
CODE_TEST_JAVAX = $(JAVA)
CODE_TEST_JAVAX += --module-path $(call module-path,$(CODE_TEST_RUNTIME_DEPS))
CODE_TEST_JAVAX += --add-modules org.testng
CODE_TEST_JAVAX += --add-reads $(CODE_MODULE)=org.testng
ifdef CODE_TEST_JAVAX_EXPORTS
CODE_TEST_JAVAX += $(foreach pkg,$(CODE_TEST_JAVAX_EXPORTS),--add-exports $(CODE_MODULE)/$(pkg)=org.testng)
endif
ifeq ($(CODE_ENABLE_PREVIEW),1)
CODE_TEST_JAVAX += --enable-preview
endif
CODE_TEST_JAVAX += --patch-module $(CODE_MODULE)=$(CODE_TEST_CLASS_OUTPUT)
CODE_TEST_JAVAX += --module $(CODE_MODULE)/$(CODE_TEST_MAIN)
CODE_TEST_JAVAX += $(CODE_TEST_RUNTIME_OUTPUT)

## objectos.code test execution marker
CODE_TEST_RUN_MARKER = $(CODE_TEST_RUNTIME_OUTPUT)/index.html

#
# objectos.code test execution targets
#

$(CODE_TEST_RUN_MARKER): $(CODE_TEST_COMPILE_MARKER) 
	$(CODE_TEST_JAVAX)

#
# objectos.selfgen compilation options
#

## objectos.selfgen source directory
SELFGEN_MAIN = $(SELFGEN_MODULE)/main

## objectos.selfgen source files
SELFGEN_SOURCES = $(shell find ${SELFGEN_MAIN} -type f -name '*.java' -print)

## objectos.selfgen source files modified since last compilation
SELFGEN_DIRTY :=

## objectos.selfgen work dir
SELFGEN_WORK = $(SELFGEN_MODULE)/work

## objectos.selfgen class output path
SELFGEN_CLASS_OUTPUT = $(SELFGEN_WORK)/main

## objectos.selfgen compiled classes
SELFGEN_CLASSES = $(SELFGEN_SOURCES:$(SELFGEN_MAIN)/%.java=$(SELFGEN_CLASS_OUTPUT)/%.class)

## objectos.selfgen compile-time dependencies
# SELFGEN_COMPILE_DEPS = 

## objectos.selfgen compile-time module-path
SELFGEN_COMPILE_MODULE_PATH = $(call module-path,$(SELFGEN_COMPILE_DEPS))
 
## objectos.selfgen javac command
SELFGEN_JAVACX = $(JAVAC)
SELFGEN_JAVACX += -d $(SELFGEN_CLASS_OUTPUT)
SELFGEN_JAVACX += -g
SELFGEN_JAVACX += -Xlint:all
SELFGEN_JAVACX += -Xpkginfo:always
ifeq ($(SELFGEN_ENABLE_PREVIEW),1)
SELFGEN_JAVACX += --enable-preview
endif
ifneq ($(SELFGEN_COMPILE_MODULE_PATH),)
SELFGEN_JAVACX += --module-path $(SELFGEN_COMPILE_MODULE_PATH)
endif
SELFGEN_JAVACX += --module-version $(SELFGEN_VERSION)
SELFGEN_JAVACX += --release $(SELFGEN_JAVA_RELEASE)
SELFGEN_JAVACX += $(SELFGEN_DIRTY)

## objectos.selfgen compilation marker
SELFGEN_COMPILE_MARKER = $(SELFGEN_WORK)/compile-marker

#
# objectos.selfgen compilation targets
#

$(SELFGEN_COMPILE_MARKER): $(SELFGEN_COMPILE_DEPS) $(SELFGEN_CLASSES)
	if [ -n "$(SELFGEN_DIRTY)" ]; then \
		$(SELFGEN_JAVACX); \
	fi
	touch $(SELFGEN_COMPILE_MARKER); \

$(SELFGEN_CLASSES): $(SELFGEN_CLASS_OUTPUT)/%.class: $(SELFGEN_MAIN)/%.java
	$(eval SELFGEN_DIRTY += $$<)

#
# objectos.selfgen jar options
#

## objectos.selfgen license 'artifact'
SELFGEN_LICENSE = $(SELFGEN_CLASS_OUTPUT)/META-INF/LICENSE

## objectos.selfgen jar file path
SELFGEN_JAR_FILE = $(SELFGEN_WORK)/$(SELFGEN_JAR_NAME)-$(SELFGEN_VERSION).jar

## objectos.selfgen jar command
SELFGEN_JARX = $(JAR)
SELFGEN_JARX += --create
SELFGEN_JARX += --file $(SELFGEN_JAR_FILE)
SELFGEN_JARX += --module-version $(SELFGEN_VERSION)
SELFGEN_JARX += -C $(SELFGEN_CLASS_OUTPUT)
SELFGEN_JARX += .

#
# objectos.selfgen jar targets
#

$(SELFGEN_JAR_FILE): $(SELFGEN_COMPILE_MARKER) $(SELFGEN_LICENSE)
	$(SELFGEN_JARX)

$(SELFGEN_LICENSE): LICENSE
	mkdir --parents $(@D)
	cp LICENSE $(@D)

#
# objectos.selfgen test compilation options
#

## objectos.selfgen test source directory
SELFGEN_TEST = $(SELFGEN_MODULE)/test

## objectos.selfgen test source files 
SELFGEN_TEST_SOURCES = $(shell find ${SELFGEN_TEST} -type f -name '*.java' -print)

## objectos.selfgen test source files modified since last compilation
SELFGEN_TEST_DIRTY :=

## objectos.selfgen test class output path
SELFGEN_TEST_CLASS_OUTPUT = $(SELFGEN_WORK)/test

## objectos.selfgen test compiled classes
SELFGEN_TEST_CLASSES = $(SELFGEN_TEST_SOURCES:$(SELFGEN_TEST)/%.java=$(SELFGEN_TEST_CLASS_OUTPUT)/%.class)

## objectos.selfgen test compile-time dependencies
# SELFGEN_TEST_COMPILE_DEPS =

## objectos.selfgen test javac command
SELFGEN_TEST_JAVACX = $(JAVAC)
SELFGEN_TEST_JAVACX += -d $(SELFGEN_TEST_CLASS_OUTPUT)
SELFGEN_TEST_JAVACX += -g
SELFGEN_TEST_JAVACX += -Xlint:all
SELFGEN_TEST_JAVACX += --class-path $(call class-path,$(SELFGEN_TEST_COMPILE_DEPS))
ifeq ($(SELFGEN_ENABLE_PREVIEW),1)
SELFGEN_TEST_JAVACX += --enable-preview
endif
SELFGEN_TEST_JAVACX += --release $(SELFGEN_JAVA_RELEASE)
SELFGEN_TEST_JAVACX += $(SELFGEN_TEST_DIRTY)

## objectos.selfgen test compilation marker
SELFGEN_TEST_COMPILE_MARKER = $(SELFGEN_WORK)/test-compile-marker

#
# objectos.selfgen test compilation targets
#

$(SELFGEN_TEST_COMPILE_MARKER): $(SELFGEN_TEST_COMPILE_DEPS) $(SELFGEN_TEST_CLASSES) 
	if [ -n "$(SELFGEN_TEST_DIRTY)" ]; then \
		$(SELFGEN_TEST_JAVACX); \
		touch $(SELFGEN_TEST_COMPILE_MARKER); \
	fi

$(SELFGEN_TEST_CLASSES): $(SELFGEN_TEST_CLASS_OUTPUT)/%.class: $(SELFGEN_TEST)/%.java
	$(eval SELFGEN_TEST_DIRTY += $$<)

#
# objectos.selfgen test execution options
#

## objectos.selfgen test runtime dependencies
# SELFGEN_TEST_RUNTIME_DEPS =

## objectos.selfgen test main class
ifndef SELFGEN_TEST_MAIN
SELFGEN_TEST_MAIN = $(SELFGEN_MODULE).RunTests
endif

## objectos.selfgen test runtime output path
SELFGEN_TEST_RUNTIME_OUTPUT = $(SELFGEN_WORK)/test-output

## objectos.selfgen test java command
SELFGEN_TEST_JAVAX = $(JAVA)
SELFGEN_TEST_JAVAX += --module-path $(call module-path,$(SELFGEN_TEST_RUNTIME_DEPS))
SELFGEN_TEST_JAVAX += --add-modules org.testng
SELFGEN_TEST_JAVAX += --add-reads $(SELFGEN_MODULE)=org.testng
ifdef SELFGEN_TEST_JAVAX_EXPORTS
SELFGEN_TEST_JAVAX += $(foreach pkg,$(SELFGEN_TEST_JAVAX_EXPORTS),--add-exports $(SELFGEN_MODULE)/$(pkg)=org.testng)
endif
ifeq ($(SELFGEN_ENABLE_PREVIEW),1)
SELFGEN_TEST_JAVAX += --enable-preview
endif
SELFGEN_TEST_JAVAX += --patch-module $(SELFGEN_MODULE)=$(SELFGEN_TEST_CLASS_OUTPUT)
SELFGEN_TEST_JAVAX += --module $(SELFGEN_MODULE)/$(SELFGEN_TEST_MAIN)
SELFGEN_TEST_JAVAX += $(SELFGEN_TEST_RUNTIME_OUTPUT)

## objectos.selfgen test execution marker
SELFGEN_TEST_RUN_MARKER = $(SELFGEN_TEST_RUNTIME_OUTPUT)/index.html

#
# objectos.selfgen test execution targets
#

$(SELFGEN_TEST_RUN_MARKER): $(SELFGEN_TEST_COMPILE_MARKER) 
	$(SELFGEN_TEST_JAVAX)

#
# objectos.way compilation options
#

## objectos.way source directory
WAY_MAIN = $(WAY_MODULE)/main

## objectos.way source files
WAY_SOURCES = $(shell find ${WAY_MAIN} -type f -name '*.java' -print)

## objectos.way source files modified since last compilation
WAY_DIRTY :=

## objectos.way work dir
WAY_WORK = $(WAY_MODULE)/work

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
ifeq ($(WAY_ENABLE_PREVIEW),1)
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
	touch $(WAY_COMPILE_MARKER); \

$(WAY_CLASSES): $(WAY_CLASS_OUTPUT)/%.class: $(WAY_MAIN)/%.java
	$(eval WAY_DIRTY += $$<)

#
# objectos.way jar options
#

## objectos.way license 'artifact'
WAY_LICENSE = $(WAY_CLASS_OUTPUT)/META-INF/LICENSE

## objectos.way jar file path
WAY_JAR_FILE = $(WAY_WORK)/$(WAY_JAR_NAME)-$(WAY_VERSION).jar

## objectos.way jar command
WAY_JARX = $(JAR)
WAY_JARX += --create
WAY_JARX += --file $(WAY_JAR_FILE)
WAY_JARX += --module-version $(WAY_VERSION)
WAY_JARX += -C $(WAY_CLASS_OUTPUT)
WAY_JARX += .

#
# objectos.way jar targets
#

$(WAY_JAR_FILE): $(WAY_COMPILE_MARKER) $(WAY_LICENSE)
	$(WAY_JARX)

$(WAY_LICENSE): LICENSE
	mkdir --parents $(@D)
	cp LICENSE $(@D)

#
# objectos.way test compilation options
#

## objectos.way test source directory
WAY_TEST = $(WAY_MODULE)/test

## objectos.way test source files 
WAY_TEST_SOURCES = $(shell find ${WAY_TEST} -type f -name '*.java' -print)

## objectos.way test source files modified since last compilation
WAY_TEST_DIRTY :=

## objectos.way test class output path
WAY_TEST_CLASS_OUTPUT = $(WAY_WORK)/test

## objectos.way test compiled classes
WAY_TEST_CLASSES = $(WAY_TEST_SOURCES:$(WAY_TEST)/%.java=$(WAY_TEST_CLASS_OUTPUT)/%.class)

## objectos.way test compile-time dependencies
# WAY_TEST_COMPILE_DEPS =

## objectos.way test javac command
WAY_TEST_JAVACX = $(JAVAC)
WAY_TEST_JAVACX += -d $(WAY_TEST_CLASS_OUTPUT)
WAY_TEST_JAVACX += -g
WAY_TEST_JAVACX += -Xlint:all
WAY_TEST_JAVACX += --class-path $(call class-path,$(WAY_TEST_COMPILE_DEPS))
ifeq ($(WAY_ENABLE_PREVIEW),1)
WAY_TEST_JAVACX += --enable-preview
endif
WAY_TEST_JAVACX += --release $(WAY_JAVA_RELEASE)
WAY_TEST_JAVACX += $(WAY_TEST_DIRTY)

## objectos.way test compilation marker
WAY_TEST_COMPILE_MARKER = $(WAY_WORK)/test-compile-marker

#
# objectos.way test compilation targets
#

$(WAY_TEST_COMPILE_MARKER): $(WAY_TEST_COMPILE_DEPS) $(WAY_TEST_CLASSES) 
	if [ -n "$(WAY_TEST_DIRTY)" ]; then \
		$(WAY_TEST_JAVACX); \
		touch $(WAY_TEST_COMPILE_MARKER); \
	fi

$(WAY_TEST_CLASSES): $(WAY_TEST_CLASS_OUTPUT)/%.class: $(WAY_TEST)/%.java
	$(eval WAY_TEST_DIRTY += $$<)

#
# objectos.way test execution options
#

## objectos.way test runtime dependencies
# WAY_TEST_RUNTIME_DEPS =

## objectos.way test main class
ifndef WAY_TEST_MAIN
WAY_TEST_MAIN = $(WAY_MODULE).RunTests
endif

## objectos.way test runtime output path
WAY_TEST_RUNTIME_OUTPUT = $(WAY_WORK)/test-output

## objectos.way test java command
WAY_TEST_JAVAX = $(JAVA)
WAY_TEST_JAVAX += --module-path $(call module-path,$(WAY_TEST_RUNTIME_DEPS))
WAY_TEST_JAVAX += --add-modules org.testng
WAY_TEST_JAVAX += --add-reads $(WAY_MODULE)=org.testng
ifdef WAY_TEST_JAVAX_EXPORTS
WAY_TEST_JAVAX += $(foreach pkg,$(WAY_TEST_JAVAX_EXPORTS),--add-exports $(WAY_MODULE)/$(pkg)=org.testng)
endif
ifeq ($(WAY_ENABLE_PREVIEW),1)
WAY_TEST_JAVAX += --enable-preview
endif
WAY_TEST_JAVAX += --patch-module $(WAY_MODULE)=$(WAY_TEST_CLASS_OUTPUT)
WAY_TEST_JAVAX += --module $(WAY_MODULE)/$(WAY_TEST_MAIN)
WAY_TEST_JAVAX += $(WAY_TEST_RUNTIME_OUTPUT)

## objectos.way test execution marker
WAY_TEST_RUN_MARKER = $(WAY_TEST_RUNTIME_OUTPUT)/index.html

#
# objectos.way test execution targets
#

$(WAY_TEST_RUN_MARKER): $(WAY_TEST_COMPILE_MARKER) 
	$(WAY_TEST_JAVAX)

#
# objectos.way install options
#

## objectos.way install location
WAY_INSTALL = $(call dependency,$(WAY_GROUP_ID),$(WAY_ARTIFACT_ID),$(WAY_VERSION))

#
# objectos.way install target
#

$(WAY_INSTALL): $(WAY_JAR_FILE)
	mkdir --parents $(@D)
	cp $(WAY_JAR_FILE) $@

#
# Targets section
#

.PHONY: clean
clean: code@clean selfgen@clean way@clean

.PHONY: test
test: code@test selfgen@test way@test

.PHONY: install
install: way@install

# maybe use eval for module targets?

#
# objectos.code targets
#

.PHONY: code@clean
code@clean:
	rm -rf $(CODE_WORK)/*

.PHONY: code@compile
code@compile: $(CODE_COMPILE_MARKER)

.PHONY: code@jar
code@jar: $(CODE_JAR_FILE)

.PHONY: code@test
code@test: $(CODE_TEST_RUN_MARKER)

#
# objectos.selfgen targets
#

.PHONY: selfgen@clean
selfgen@clean:
	rm -rf $(SELFGEN_WORK)/*

.PHONY: selfgen@compile
selfgen@compile: $(SELFGEN_COMPILE_MARKER)

.PHONY: selfgen@jar
selfgen@jar: $(SELFGEN_JAR_FILE)

.PHONY: selfgen@test
selfgen@test: $(SELFGEN_TEST_RUN_MARKER)

## marker to indicate when selfgen was last run
SELFGEN_MARKER = $(WAY_WORK)/selfgen-marker

## selfgen runtime deps
SELFGEN_RUNTIME_DEPS = $(SELFGEN_JAR_FILE)
SELFGEN_RUNTIME_DEPS += $(SELFGEN_COMPILE_DEPS)

## selfgen java command
SELFGEN_JAVAX = $(JAVA)
SELFGEN_JAVAX += --module-path $(call module-path,$(SELFGEN_RUNTIME_DEPS))
ifeq ($(SELFGEN_ENABLE_PREVIEW), 1)
SELFGEN_JAVAX += --enable-preview
endif
SELFGEN_JAVAX += --module $(SELFGEN_MODULE)/$(SELFGEN_MODULE).Main
SELFGEN_JAVAX += $(WAY_MAIN)

.PHONY: selfgen
selfgen: $(SELFGEN_MARKER)

$(SELFGEN_MARKER): $(SELFGEN_JAR_FILE)
	$(SELFGEN_JAVAX)
	mkdir --parents $(@D)
	touch $(SELFGEN_MARKER)

#
# objectos.way targets
#

.PHONY: way@clean
way@clean:
	rm -rf $(WAY_WORK)/*

.PHONY: way@jar
way@jar: $(SELFGEN_MARKER) $(WAY_JAR_FILE)

.PHONY: way@test
way@test: $(WAY_TEST_RUN_MARKER)

.PHONY: way@install
way@install: $(WAY_INSTALL)
