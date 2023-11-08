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
VERSION := 0.1.6-SNAPSHOT

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
SELFGEN_TEST_JAVAX_EXPORTS += selfgen.css.util

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

#
# Defines the tools
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

## curl common options
CURL := curl
CURL += --fail

## gpg common options
GPG := gpg

## jq common options
JQ := jq

## sed common options
SED := sed

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

## objectos.code resources
# CODE_RESOURCES =

## objectos.code compilation marker
CODE_COMPILE_MARKER = $(CODE_WORK)/compile-marker

#
# objectos.code compilation targets
#

$(CODE_COMPILE_MARKER): $(CODE_COMPILE_DEPS) $(CODE_CLASSES) $(CODE_RESOURCES)
	if [ -n "$(CODE_DIRTY)" ]; then \
		$(CODE_JAVACX); \
	fi
	touch $@

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

## requirements of the CODE_JAR_FILE target
CODE_JAR_FILE_REQS = $(CODE_COMPILE_MARKER)
CODE_JAR_FILE_REQS += $(CODE_LICENSE)
ifdef CODE_JAR_FILE_REQS_MORE
CODE_JAR_FILE_REQS += $(CODE_JAR_FILE_REQS_MORE)
endif

#
# objectos.code jar targets
#

$(CODE_JAR_FILE): $(CODE_JAR_FILE_REQS)
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
	fi
	touch $@

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
ifdef CODE_TEST_JAVAX_READS
CODE_TEST_JAVAX += $(foreach mod,$(CODE_TEST_JAVAX_READS),--add-reads $(CODE_MODULE)=$(mod))
endif
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

## objectos.selfgen resources
# SELFGEN_RESOURCES =

## objectos.selfgen compilation marker
SELFGEN_COMPILE_MARKER = $(SELFGEN_WORK)/compile-marker

#
# objectos.selfgen compilation targets
#

$(SELFGEN_COMPILE_MARKER): $(SELFGEN_COMPILE_DEPS) $(SELFGEN_CLASSES) $(SELFGEN_RESOURCES)
	if [ -n "$(SELFGEN_DIRTY)" ]; then \
		$(SELFGEN_JAVACX); \
	fi
	touch $@

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

## requirements of the SELFGEN_JAR_FILE target
SELFGEN_JAR_FILE_REQS = $(SELFGEN_COMPILE_MARKER)
SELFGEN_JAR_FILE_REQS += $(SELFGEN_LICENSE)
ifdef SELFGEN_JAR_FILE_REQS_MORE
SELFGEN_JAR_FILE_REQS += $(SELFGEN_JAR_FILE_REQS_MORE)
endif

#
# objectos.selfgen jar targets
#

$(SELFGEN_JAR_FILE): $(SELFGEN_JAR_FILE_REQS)
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
	fi
	touch $@

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
ifdef SELFGEN_TEST_JAVAX_READS
SELFGEN_TEST_JAVAX += $(foreach mod,$(SELFGEN_TEST_JAVAX_READS),--add-reads $(SELFGEN_MODULE)=$(mod))
endif
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

## include ossrh config
## - OSSRH_GPG_KEY
## - OSSRH_GPG_PASSPHRASE
## - OSSRH_USERNAME
## - OSSRH_PASSWORD
-include $(HOME)/.config/objectos/ossrh-config.mk

## include GH config
## - GH_TOKEN
-include $(HOME)/.config/objectos/gh-config.mk

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

## objectos.way resources
# WAY_RESOURCES =

## objectos.way compilation marker
WAY_COMPILE_MARKER = $(WAY_WORK)/compile-marker

#
# objectos.way compilation targets
#

$(WAY_COMPILE_MARKER): $(WAY_COMPILE_DEPS) $(WAY_CLASSES) $(WAY_RESOURCES)
	if [ -n "$(WAY_DIRTY)" ]; then \
		$(WAY_JAVACX); \
	fi
	touch $@

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

## requirements of the WAY_JAR_FILE target
WAY_JAR_FILE_REQS = $(WAY_COMPILE_MARKER)
WAY_JAR_FILE_REQS += $(WAY_LICENSE)
ifdef WAY_JAR_FILE_REQS_MORE
WAY_JAR_FILE_REQS += $(WAY_JAR_FILE_REQS_MORE)
endif

#
# objectos.way jar targets
#

$(WAY_JAR_FILE): $(WAY_JAR_FILE_REQS)
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
	fi
	touch $@

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
ifdef WAY_TEST_JAVAX_READS
WAY_TEST_JAVAX += $(foreach mod,$(WAY_TEST_JAVAX_READS),--add-reads $(WAY_MODULE)=$(mod))
endif
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
	cp $< $@

#
# objectos.way source-jar options
#

## objectos.way source-jar file
WAY_SOURCE_JAR_FILE = $(WAY_WORK)/$(WAY_JAR_NAME)-$(WAY_VERSION)-sources.jar

## objectos.way source-jar command
WAY_SOURCE_JARX = $(JAR)
WAY_SOURCE_JARX += --create
WAY_SOURCE_JARX += --file $(WAY_SOURCE_JAR_FILE)
WAY_SOURCE_JARX += -C $(WAY_MAIN)
WAY_SOURCE_JARX += .

#
# objectos.way source-jar targets
#

$(WAY_SOURCE_JAR_FILE): $(WAY_SOURCES)
	$(WAY_SOURCE_JARX)
	
#
# objectos.way javadoc options
#

## objectos.way javadoc output path
WAY_JAVADOC_OUTPUT = $(WAY_WORK)/javadoc

## objectos.way javadoc marker
WAY_JAVADOC_MARKER = $(WAY_JAVADOC_OUTPUT)/index.html

## objectos.way javadoc command
WAY_JAVADOCX = $(JAVADOC)
WAY_JAVADOCX += -d $(WAY_JAVADOC_OUTPUT)
ifeq ($(WAY_ENABLE_PREVIEW),1)
WAY_JAVADOCX += --enable-preview
endif
WAY_JAVADOCX += --module $(WAY_MODULE)
ifneq ($(WAY_COMPILE_MODULE_PATH),)
WAY_JAVADOCX += --module-path $(WAY_COMPILE_MODULE_PATH)
endif
WAY_JAVADOCX += --module-source-path "./*/main"
WAY_JAVADOCX += --release $(WAY_JAVA_RELEASE)
WAY_JAVADOCX += --show-module-contents api
WAY_JAVADOCX += --show-packages exported
ifdef WAY_JAVADOC_SNIPPET_PATH
WAY_JAVADOCX += --snippet-path $($(WAY_JAVADOC_SNIPPET_PATH))
endif 
WAY_JAVADOCX += -bottom 'Copyright &\#169; $(WAY_COPYRIGHT_YEARS) <a href="https://www.objectos.com.br/">Objectos Software LTDA</a>. All rights reserved.'
WAY_JAVADOCX += -charset 'UTF-8'
WAY_JAVADOCX += -docencoding 'UTF-8'
WAY_JAVADOCX += -doctitle '$(WAY_GROUP_ID):$(WAY_ARTIFACT_ID) $(WAY_VERSION) API'
WAY_JAVADOCX += -encoding 'UTF-8'
WAY_JAVADOCX += -use
WAY_JAVADOCX += -version
WAY_JAVADOCX += -windowtitle '$(WAY_GROUP_ID):$(WAY_ARTIFACT_ID) $(WAY_VERSION) API'

## objectos.way javadoc jar file
WAY_JAVADOC_JAR_FILE = $(WAY_WORK)/$(WAY_ARTIFACT_ID)-$(WAY_VERSION)-javadoc.jar

## objectos.way javadoc jar command
WAY_JAVADOC_JARX = $(JAR)
WAY_JAVADOC_JARX += --create
WAY_JAVADOC_JARX += --file $(WAY_JAVADOC_JAR_FILE)
WAY_JAVADOC_JARX += -C $(WAY_JAVADOC_OUTPUT)
WAY_JAVADOC_JARX += .

#
# objectos.way javadoc targets
#

$(WAY_JAVADOC_JAR_FILE): $(WAY_JAVADOC_MARKER)
	$(WAY_JAVADOC_JARX)

$(WAY_JAVADOC_MARKER): $(WAY_SOURCES)
	$(WAY_JAVADOCX)

#
# Provides the pom target:
#
# - generates a pom.xml suitable for deploying to a maven repository
# 
# Requirements:
#
# - you must provide the pom template $(MODULE)/pom.xml.tmpl

## objectos.way pom source
WAY_POM_SOURCE = $(WAY_MODULE)/pom.xml.tmpl

## objectos.way pom file
WAY_POM_FILE = $(WAY_WORK)/pom.xml

## objectos.way pom external variables
# WAY_POM_VARIABLES = 

## objectos.way ossrh pom sed command
WAY_POM_SEDX = $(SED)
WAY_POM_SEDX += $(foreach var,$(POM_VARIABLES),--expression='s/@$(var)@/$($(var))/g')
WAY_POM_SEDX += --expression='s/@COPYRIGHT_YEARS@/$(WAY_COPYRIGHT_YEARS)/g'
WAY_POM_SEDX += --expression='s/@ARTIFACT_ID@/$(WAY_ARTIFACT_ID)/g'
WAY_POM_SEDX += --expression='s/@GROUP_ID@/$(WAY_GROUP_ID)/g'
WAY_POM_SEDX += --expression='s/@VERSION@/$(WAY_VERSION)/g'
WAY_POM_SEDX += --expression='w $(WAY_POM_FILE)'
WAY_POM_SEDX += $(WAY_POM_SOURCE)

#
# Targets
#

$(WAY_POM_FILE): $(WAY_POM_SOURCE) Makefile
	$(WAY_POM_SEDX)

## objectos.way gpg command
WAY_GPGX = $(GPG)
WAY_GPGX += --armor
WAY_GPGX += --batch
WAY_GPGX += --default-key $(OSSRH_GPG_KEY)
WAY_GPGX += --detach-sign
WAY_GPGX += --passphrase $(OSSRH_GPG_PASSPHRASE)
WAY_GPGX += --pinentry-mode loopback
WAY_GPGX += --yes

## objectos.way ossrh bundle jar file
WAY_OSSRH_BUNDLE = $(WAY_WORK)/$(WAY_ARTIFACT_ID)-$(WAY_VERSION)-bundle.jar

## objectos.way ossrh bundle contents
WAY_OSSRH_CONTENTS = $(WAY_POM_FILE)
WAY_OSSRH_CONTENTS += $(WAY_JAR_FILE)
WAY_OSSRH_CONTENTS += $(WAY_SOURCE_JAR_FILE)
WAY_OSSRH_CONTENTS += $(WAY_JAVADOC_JAR_FILE)

## objectos.way ossrh sigs
WAY_OSSRH_SIGS = $(WAY_OSSRH_CONTENTS:%=%.asc)

## objectos.way ossrh bundle jar command
WAY_OSSRH_JARX = $(JAR)
WAY_OSSRH_JARX += --create
WAY_OSSRH_JARX += --file $(WAY_OSSRH_BUNDLE)
WAY_OSSRH_JARX += $(WAY_OSSRH_CONTENTS:$(WAY_WORK)/%=-C $(WAY_WORK) %)
WAY_OSSRH_JARX += $(WAY_OSSRH_SIGS:$(WAY_WORK)/%=-C $(WAY_WORK) %)

#
# objectos.way ossrh bundle targets
#

$(WAY_OSSRH_BUNDLE): $(WAY_OSSRH_SIGS)
	$(WAY_OSSRH_JARX)

%.asc: %
	@$(WAY_GPGX) $<

## objectos.way ossrh cookies
WAY_OSSRH_COOKIES = $(WAY_WORK)/ossrh-cookies.txt 

## objectos.way ossrh login curl command
WAY_OSSRH_LOGIN_CURLX = $(CURL)
WAY_OSSRH_LOGIN_CURLX += --cookie-jar $(WAY_OSSRH_COOKIES)
WAY_OSSRH_LOGIN_CURLX += --output /dev/null
WAY_OSSRH_LOGIN_CURLX += --request GET
WAY_OSSRH_LOGIN_CURLX += --silent
WAY_OSSRH_LOGIN_CURLX += --url https://oss.sonatype.org/service/local/authentication/login
WAY_OSSRH_LOGIN_CURLX += --user $(OSSRH_USERNAME):$(OSSRH_PASSWORD)

## objectos.way ossrh response json
WAY_OSSRH_UPLOAD_JSON = $(WAY_WORK)/ossrh-upload.json

## objectos.way ossrh upload curl command
WAY_OSSRH_UPLOAD_CURLX = $(CURL)
WAY_OSSRH_UPLOAD_CURLX += --cookie $(WAY_OSSRH_COOKIES)
WAY_OSSRH_UPLOAD_CURLX += --header 'Content-Type: multipart/form-data'
WAY_OSSRH_UPLOAD_CURLX += --form file=@$(WAY_OSSRH_BUNDLE)
WAY_OSSRH_UPLOAD_CURLX += --output $(WAY_OSSRH_UPLOAD_JSON)
WAY_OSSRH_UPLOAD_CURLX += --request POST
WAY_OSSRH_UPLOAD_CURLX += --url https://oss.sonatype.org/service/local/staging/bundle_upload

## objectos.way ossrh repository id sed parsing
WAY_OSSRH_SEDX = $(SED)
WAY_OSSRH_SEDX += --regexp-extended
WAY_OSSRH_SEDX += --silent
WAY_OSSRH_SEDX += 's/^.*repositories\/(.*)".*/\1/p'
WAY_OSSRH_SEDX += $(WAY_OSSRH_UPLOAD_JSON)

## objectos.way repository id
WAY_OSSRH_REPOSITORY_ID = $(shell $(WAY_OSSRH_SEDX))

## objectos.way ossrh release curl command
WAY_OSSRH_RELEASE_CURLX = $(CURL)
WAY_OSSRH_RELEASE_CURLX += --cookie $(WAY_OSSRH_COOKIES)
WAY_OSSRH_RELEASE_CURLX += --data '{"data":{"autoDropAfterRelease":true,"description":"","stagedRepositoryIds":["$(WAY_OSSRH_REPOSITORY_ID)"]}}'
WAY_OSSRH_RELEASE_CURLX += --header 'Content-Type: application/json'
WAY_OSSRH_RELEASE_CURLX += --request POST
WAY_OSSRH_RELEASE_CURLX += --url https://oss.sonatype.org/service/local/staging/bulk/promote

## objectos.way ossrh marker
WAY_OSSRH_MARKER = $(WAY_WORK)/ossrh-marker

#
# objectos.way ossrh targets
#

$(WAY_OSSRH_MARKER): $(WAY_OSSRH_UPLOAD_JSON)
	@for i in 1 2 3; do \
	  echo "Waiting before release..."; \
	  sleep 45; \
	  echo "Trying to release $(WAY_OSSRH_REPOSITORY_ID)"; \
	  $(WAY_OSSRH_RELEASE_CURLX); \
	  if [ $$? -eq 0 ]; then \
	    exit 0; \
	  fi \
	done
	touch $@

$(WAY_OSSRH_UPLOAD_JSON): $(WAY_OSSRH_BUNDLE)
	@$(WAY_OSSRH_LOGIN_CURLX)
	$(WAY_OSSRH_UPLOAD_CURLX)

#
# objectos.way GitHub release
#

## objectos.way GitHub API
ifndef WAY_GH_API
WAY_GH_API = https://api.github.com/repos/objectos/$(WAY_ARTIFACT_ID)
endif

## objectos.way GitHub milestone title
WAY_GH_MILESTONE_TITLE = v$(WAY_VERSION)

## objectos.way GitHub milestones curl command
WAY_GH_MILESTONE_CURLX = $(CURL)
WAY_GH_MILESTONE_CURLX += '$(WAY_GH_API)/milestones'

## objectos.way GitHub milestone number parsing
WAY_GH_MILESTONE_JQX = $(JQ)
WAY_GH_MILESTONE_JQX += '.[] | select(.title == "$(WAY_GH_MILESTONE_TITLE)") | .number'

## objectos.way GitHub milestone ID
WAY_GH_MILESTONE_ID = $(shell $(WAY_GH_MILESTONE_CURLX) | $(WAY_GH_MILESTONE_JQX))

## objectos.way Issues from GitHub
WAY_GH_ISSUES_JSON = $(WAY_WORK)/gh-issues.json

## objectos.way GitHub issues curl command
WAY_GH_ISSUES_CURLX = $(CURL)
WAY_GH_ISSUES_CURLX += '$(WAY_GH_API)/issues?milestone=$(WAY_GH_MILESTONE_ID)&per_page=100&state=all'
WAY_GH_ISSUES_CURLX += >
WAY_GH_ISSUES_CURLX += $(WAY_GH_ISSUES_JSON)

##
WAY_GH_RELEASE_BODY = $(WAY_WORK)/gh-release.md

## objectos.way Filter and format issues
WAY_GH_ISSUES_JQX = $(JQ)
WAY_GH_ISSUES_JQX += --arg LABEL "$(LABEL)"
WAY_GH_ISSUES_JQX += --raw-output
WAY_GH_ISSUES_JQX += 'sort_by(.number) | [.[] | {number: .number, title: .title, label: .labels[] | select(.name) | .name}] | .[] | select(.label == $$LABEL) | "- \(.title) \#\(.number)"'
WAY_GH_ISSUES_JQX += $(WAY_GH_ISSUES_JSON)

WAY_gh_issues = $(let LABEL,$1,$(WAY_GH_ISSUES_JQX))

##
WAY_GH_RELEASE_JSON := $(WAY_WORK)/gh-release.json

## objectos.way git tag name to be generated
WAY_GH_TAG := $(WAY_GH_MILESTONE_TITLE)

##
WAY_GH_RELEASE_JQX = $(JQ)
WAY_GH_RELEASE_JQX += --raw-input
WAY_GH_RELEASE_JQX += --slurp
WAY_GH_RELEASE_JQX += '. as $$body | {"tag_name":"$(WAY_GH_TAG)","name":"Release $(WAY_GH_TAG)","body":$$body,"draft":true,"prerelease":false,"generate_release_notes":false}'
WAY_GH_RELEASE_JQX += $(WAY_GH_RELEASE_BODY)

## 
WAY_GH_RELEASE_CURLX = $(CURL)
WAY_GH_RELEASE_CURLX += --data-binary "@$(WAY_GH_RELEASE_JSON)"
WAY_GH_RELEASE_CURLX += --header "Accept: application/vnd.github+json"
WAY_GH_RELEASE_CURLX += --header "Authorization: Bearer $(GH_TOKEN)"
WAY_GH_RELEASE_CURLX += --header "X-GitHub-Api-Version: 2022-11-28"
WAY_GH_RELEASE_CURLX += --location
WAY_GH_RELEASE_CURLX += --request POST
WAY_GH_RELEASE_CURLX +=  $(WAY_GH_API)/releases

##
WAY_GH_RELEASE_MARKER = $(WAY_WORK)/gh-release-marker 

#
# objectos.way GitHub release targets
#

$(WAY_GH_RELEASE_MARKER): $(WAY_GH_RELEASE_JSON)
	@$(WAY_GH_RELEASE_CURLX)
	touch $@

$(WAY_GH_RELEASE_JSON): $(WAY_GH_RELEASE_BODY)
	$(WAY_GH_RELEASE_JQX) > $(WAY_GH_RELEASE_JSON) 

$(WAY_GH_ISSUES_JSON):
	$(WAY_GH_ISSUES_CURLX)

$(WAY_GH_RELEASE_BODY): $(WAY_GH_ISSUES_JSON)
	echo '## New features' > $(WAY_GH_RELEASE_BODY)
	echo '' >> $(WAY_GH_RELEASE_BODY)
	$(call WAY_gh_issues,t:feature) >> $(WAY_GH_RELEASE_BODY) 
	echo '' >> $(WAY_GH_RELEASE_BODY)
	echo '## Enhancements' >> $(WAY_GH_RELEASE_BODY)
	echo '' >> $(WAY_GH_RELEASE_BODY)
	$(call WAY_gh_issues,t:enhancement) >> $(WAY_GH_RELEASE_BODY) 
	echo '' >> $(WAY_GH_RELEASE_BODY)
	echo '## Bug Fixes' >> $(WAY_GH_RELEASE_BODY)
	echo '' >> $(WAY_GH_RELEASE_BODY)
	$(call WAY_gh_issues,t:defect) >> $(WAY_GH_RELEASE_BODY) 
	echo '' >> $(WAY_GH_RELEASE_BODY)
	echo '## Documentation' >> $(WAY_GH_RELEASE_BODY)
	echo '' >> $(WAY_GH_RELEASE_BODY)
	$(call WAY_gh_issues,t:documentation) >> $(WAY_GH_RELEASE_BODY) 
	echo '' >> $(WAY_GH_RELEASE_BODY)
	echo '## Work' >> $(WAY_GH_RELEASE_BODY)
	echo '' >> $(WAY_GH_RELEASE_BODY)
	$(call WAY_gh_issues,t:work) >> $(WAY_GH_RELEASE_BODY) 
	
#
# Targets section
#

.PHONY: clean
clean: code@clean selfgen@clean way@clean

.PHONY: test
test: code@test selfgen@test way@test

.PHONY: install
install: way@install

.PHONY: ossrh
ossrh: way@ossrh

.PHONY: gh-release
gh-release: way@gh-release

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
	rm -r $(WAY_WORK)/*

$(WAY_JS_ARTIFACT): $(WAY_JS_SRC)
	mkdir --parents $(@D)
	cp $< $@

.PHONY: way@jar
way@jar: $(SELFGEN_MARKER) $(WAY_JAR_FILE)

.PHONY: way@test
way@test: $(WAY_TEST_RUN_MARKER)

.PHONY: way@install
way@install: $(WAY_INSTALL)

.PHONY: way@source-jar
way@source-jar: $(WAY_SOURCE_JAR_FILE)

.PHONY: way@javadoc way@clean-javadoc
way@javadoc: $(WAY_JAVADOC_JAR_FILE)

way@clean-javadoc:
	rm -r $(WAY_JAVADOC_OUTPUT)

.PHONY: way@pom
way@pom: $(WAY_POM_FILE)

.PHONY: way@ossrh way@ossrh-bundle
way@ossrh: $(WAY_OSSRH_MARKER)

way@ossrh-bundle: $(WAY_OSSRH_BUNDLE)

.PHONY: way@gh-release way@gh-release-body
way@gh-release: $(WAY_GH_RELEASE_MARKER)

way@gh-release-body: $(WAY_GH_RELEASE_BODY)
