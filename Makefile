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
VERSION := 0.1.5.5-SNAPSHOT

## Deps versions

TESTNG_VERSION := 7.7.1
JCOMMANDER_VERSION := 1.82
SLF4J_VERSION := 1.7.36

JAVA_RELEASE := 21

# Delete the default suffixes
.SUFFIXES:

## common module tasks
MODULE_TASKS = COMPILE_TASK
MODULE_TASKS += JAR_TASK
MODULE_TASKS += TEST_COMPILE_TASK
MODULE_TASKS += TEST_RUN_TASK
MODULE_TASKS += INSTALL_TASK
MODULE_TASKS += SOURCE_JAR_TASK
MODULE_TASKS += JAVADOC_TASK
MODULE_TASKS += POM_TASK
MODULE_TASKS += OSSRH_PREPARE_TASK

#
# Default target
#

.PHONY: all
all: jar

.PHONY: jar
jar: way@jar

print-%::
	@echo $* = $($*)

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
# compilation options
#

define COMPILE_TASK

## source directory
$(1)MAIN = $$($(1)MODULE)/main

## source files
$(1)SOURCES = $$(shell find $${$(1)MAIN} -type f -name '*.java' -print)

## source files modified since last compilation
$(1)DIRTY :=

## work dir
$(1)WORK = $$($(1)MODULE)/work

## class output path
$(1)CLASS_OUTPUT = $$($(1)WORK)/main

## compiled classes
$(1)CLASSES = $$($(1)SOURCES:$$($(1)MAIN)/%.java=$$($(1)CLASS_OUTPUT)/%.class)

## compile-time dependencies
# $(1)COMPILE_DEPS = 

## compile-time module-path
$(1)COMPILE_MODULE_PATH = $$(call module-path,$$($(1)COMPILE_DEPS))
 
## javac command
$(1)JAVACX = $$(JAVAC)
$(1)JAVACX += -d $$($(1)CLASS_OUTPUT)
$(1)JAVACX += -g
$(1)JAVACX += -Xlint:all
$(1)JAVACX += -Xpkginfo:always
ifeq ($$($(1)ENABLE_PREVIEW),1)
$(1)JAVACX += --enable-preview
endif
ifneq ($$($(1)COMPILE_MODULE_PATH),)
$(1)JAVACX += --module-path $$($(1)COMPILE_MODULE_PATH)
endif
$(1)JAVACX += --module-version $$($(1)VERSION)
$(1)JAVACX += --release $$($(1)JAVA_RELEASE)
$(1)JAVACX += $$($(1)DIRTY)

## resources
# $(1)RESOURCES =

## compilation marker
$(1)COMPILE_MARKER = $$($(1)WORK)/compile-marker

#
# compilation targets
#

$$($(1)COMPILE_MARKER): $$($(1)COMPILE_DEPS) $$($(1)CLASSES) $$($(1)RESOURCES)
	if [ -n "$$($(1)DIRTY)" ]; then \
		$$($(1)JAVACX); \
	fi
	touch $$@

$$($(1)CLASSES): $$($(1)CLASS_OUTPUT)/%.class: $$($(1)MAIN)/%.java
	$$(eval $(1)DIRTY += $$$$<)

endef

#
# jar options
#

define JAR_TASK

## license 'artifact'
$(1)LICENSE = $$($(1)CLASS_OUTPUT)/META-INF/LICENSE

## jar file path
$(1)JAR_FILE = $$($(1)WORK)/$$($(1)JAR_NAME)-$$($(1)VERSION).jar

## jar command
$(1)JARX = $$(JAR)
$(1)JARX += --create
$(1)JARX += --file $$($(1)JAR_FILE)
$(1)JARX += --module-version $$($(1)VERSION)
$(1)JARX += -C $$($(1)CLASS_OUTPUT)
$(1)JARX += .

## requirements of the $(1)JAR_FILE target
$(1)JAR_FILE_REQS = $$($(1)COMPILE_MARKER)
$(1)JAR_FILE_REQS += $$($(1)LICENSE)
ifdef $(1)JAR_FILE_REQS_MORE
$(1)JAR_FILE_REQS += $$($(1)JAR_FILE_REQS_MORE)
endif

#
# jar targets
#

$$($(1)JAR_FILE): $$($(1)JAR_FILE_REQS)
	$$($(1)JARX)

$$($(1)LICENSE): LICENSE
	mkdir --parents $$(@D)
	cp LICENSE $$(@D)

endef

#
# test compilation options
#

define TEST_COMPILE_TASK

## test source directory
$(1)TEST = $$($(1)MODULE)/test

## test source files 
$(1)TEST_SOURCES = $$(shell find $${$(1)TEST} -type f -name '*.java' -print)

## test source files modified since last compilation
$(1)TEST_DIRTY :=

## test class output path
$(1)TEST_CLASS_OUTPUT = $$($(1)WORK)/test

## test compiled classes
$(1)TEST_CLASSES = $$($(1)TEST_SOURCES:$$($(1)TEST)/%.java=$$($(1)TEST_CLASS_OUTPUT)/%.class)

## test compile-time dependencies
# $(1)TEST_COMPILE_DEPS =

## test javac command
$(1)TEST_JAVACX = $$(JAVAC)
$(1)TEST_JAVACX += -d $$($(1)TEST_CLASS_OUTPUT)
$(1)TEST_JAVACX += -g
$(1)TEST_JAVACX += -Xlint:all
$(1)TEST_JAVACX += --class-path $$(call class-path,$$($(1)TEST_COMPILE_DEPS))
ifeq ($$($(1)ENABLE_PREVIEW),1)
$(1)TEST_JAVACX += --enable-preview
endif
$(1)TEST_JAVACX += --release $$($(1)JAVA_RELEASE)
$(1)TEST_JAVACX += $$($(1)TEST_DIRTY)

## test compilation marker
$(1)TEST_COMPILE_MARKER = $$($(1)WORK)/test-compile-marker

#
# test compilation targets
#

$$($(1)TEST_COMPILE_MARKER): $$($(1)TEST_COMPILE_DEPS) $$($(1)TEST_CLASSES) 
	if [ -n "$$($(1)TEST_DIRTY)" ]; then \
		$$($(1)TEST_JAVACX); \
	fi
	touch $$@

$$($(1)TEST_CLASSES): $$($(1)TEST_CLASS_OUTPUT)/%.class: $$($(1)TEST)/%.java
	$$(eval $(1)TEST_DIRTY += $$$$<)

endef

#
# test execution options
#

define TEST_RUN_TASK

## test runtime dependencies
# $(1)TEST_RUNTIME_DEPS =

## test main class
ifndef $(1)TEST_MAIN
$(1)TEST_MAIN = $$($(1)MODULE).RunTests
endif

## test runtime output path
$(1)TEST_RUNTIME_OUTPUT = $$($(1)WORK)/test-output

## test java command
$(1)TEST_JAVAX = $$(JAVA)
$(1)TEST_JAVAX += --module-path $$(call module-path,$$($(1)TEST_RUNTIME_DEPS))
$(1)TEST_JAVAX += --add-modules org.testng
$(1)TEST_JAVAX += --add-reads $$($(1)MODULE)=org.testng
ifdef $(1)TEST_JAVAX_READS
$(1)TEST_JAVAX += $$(foreach mod,$$($(1)TEST_JAVAX_READS),--add-reads $$($(1)MODULE)=$$(mod))
endif
ifdef $(1)TEST_JAVAX_EXPORTS
$(1)TEST_JAVAX += $$(foreach pkg,$$($(1)TEST_JAVAX_EXPORTS),--add-exports $$($(1)MODULE)/$$(pkg)=org.testng)
endif
ifeq ($$($(1)ENABLE_PREVIEW),1)
$(1)TEST_JAVAX += --enable-preview
endif
$(1)TEST_JAVAX += --patch-module $$($(1)MODULE)=$$($(1)TEST_CLASS_OUTPUT)
$(1)TEST_JAVAX += --module $$($(1)MODULE)/$$($(1)TEST_MAIN)
$(1)TEST_JAVAX += $$($(1)TEST_RUNTIME_OUTPUT)

## test execution marker
$(1)TEST_RUN_MARKER = $$($(1)TEST_RUNTIME_OUTPUT)/index.html

#
# test execution targets
#

$$($(1)TEST_RUN_MARKER): $$($(1)TEST_COMPILE_MARKER) 
	$$($(1)TEST_JAVAX)

endef

#
# install task
#

define INSTALL_TASK

## install location
$(1)INSTALL = $$(call dependency,$$($(1)GROUP_ID),$$($(1)ARTIFACT_ID),$$($(1)VERSION))

#
# install target
#

$$($(1)INSTALL): $$($(1)JAR_FILE)
	mkdir --parents $$(@D)
	cp $$< $$@

endef

#
# source-jar task
#

define SOURCE_JAR_TASK

## source-jar file
$(1)SOURCE_JAR_FILE = $$($(1)WORK)/$$($(1)JAR_NAME)-$$($(1)VERSION)-sources.jar

## source-jar command
$(1)SOURCE_JARX = $$(JAR)
$(1)SOURCE_JARX += --create
$(1)SOURCE_JARX += --file $$($(1)SOURCE_JAR_FILE)
$(1)SOURCE_JARX += -C $$($(1)MAIN)
$(1)SOURCE_JARX += .

#
# source-jar targets
#

$$($(1)SOURCE_JAR_FILE): $$($(1)SOURCES)
	$$($(1)SOURCE_JARX)
	
endef

#
# javadoc task
#

define JAVADOC_TASK

## javadoc output path
$(1)JAVADOC_OUTPUT = $$($(1)WORK)/javadoc

## javadoc marker
$(1)JAVADOC_MARKER = $$($(1)JAVADOC_OUTPUT)/index.html

## javadoc command
$(1)JAVADOCX = $$(JAVADOC)
$(1)JAVADOCX += -d $$($(1)JAVADOC_OUTPUT)
ifeq ($$($(1)ENABLE_PREVIEW),1)
$(1)JAVADOCX += --enable-preview
endif
$(1)JAVADOCX += --module $$($(1)MODULE)
ifneq ($$($(1)COMPILE_MODULE_PATH),)
$(1)JAVADOCX += --module-path $$($(1)COMPILE_MODULE_PATH)
endif
$(1)JAVADOCX += --module-source-path "./*/main"
$(1)JAVADOCX += --release $$($(1)JAVA_RELEASE)
$(1)JAVADOCX += --show-module-contents api
$(1)JAVADOCX += --show-packages exported
ifdef $(1)JAVADOC_SNIPPET_PATH
$(1)JAVADOCX += --snippet-path $$($$($(1)JAVADOC_SNIPPET_PATH))
endif 
$(1)JAVADOCX += -bottom 'Copyright &\#169; $$($(1)COPYRIGHT_YEARS) <a href="https://www.objectos.com.br/">Objectos Software LTDA</a>. All rights reserved.'
$(1)JAVADOCX += -charset 'UTF-8'
$(1)JAVADOCX += -docencoding 'UTF-8'
$(1)JAVADOCX += -doctitle '$$($(1)GROUP_ID):$$($(1)ARTIFACT_ID) $$($(1)VERSION) API'
$(1)JAVADOCX += -encoding 'UTF-8'
$(1)JAVADOCX += -use
$(1)JAVADOCX += -version
$(1)JAVADOCX += -windowtitle '$$($(1)GROUP_ID):$$($(1)ARTIFACT_ID) $$($(1)VERSION) API'

## javadoc jar file
$(1)JAVADOC_JAR_FILE = $$($(1)WORK)/$$($(1)ARTIFACT_ID)-$$($(1)VERSION)-javadoc.jar

## javadoc jar command
$(1)JAVADOC_JARX = $$(JAR)
$(1)JAVADOC_JARX += --create
$(1)JAVADOC_JARX += --file $$($(1)JAVADOC_JAR_FILE)
$(1)JAVADOC_JARX += -C $$($(1)JAVADOC_OUTPUT)
$(1)JAVADOC_JARX += .

#
# javadoc targets
#

$$($(1)JAVADOC_JAR_FILE): $$($(1)JAVADOC_MARKER)
	$$($(1)JAVADOC_JARX)

$$($(1)JAVADOC_MARKER): $$($(1)SOURCES)
	$$($(1)JAVADOCX)

endef

#
# Provides the pom target:
#
# - generates a pom.xml suitable for deploying to a maven repository
# 
# Requirements:
#
# - you must provide the pom template $$(MODULE)/pom.xml.tmpl

define POM_TASK

## pom source
$(1)POM_SOURCE = $$($(1)MODULE)/pom.xml.tmpl

## pom file
$(1)POM_FILE = $$($(1)WORK)/pom.xml

## pom external variables
# $(1)POM_VARIABLES = 

## ossrh pom sed command
$(1)POM_SEDX = $$(SED)
$(1)POM_SEDX += $$(foreach var,$$(POM_VARIABLES),--expression='s/@$$(var)@/$$($$(var))/g')
$(1)POM_SEDX += --expression='s/@COPYRIGHT_YEARS@/$$($(1)COPYRIGHT_YEARS)/g'
$(1)POM_SEDX += --expression='s/@ARTIFACT_ID@/$$($(1)ARTIFACT_ID)/g'
$(1)POM_SEDX += --expression='s/@GROUP_ID@/$$($(1)GROUP_ID)/g'
$(1)POM_SEDX += --expression='s/@VERSION@/$$($(1)VERSION)/g'
$(1)POM_SEDX += --expression='s/@DESCRIPTION@/$$($(1)DESCRIPTION)/g'
$(1)POM_SEDX += --expression='w $$($(1)POM_FILE)'
$(1)POM_SEDX += $$($(1)POM_SOURCE)

#
# Targets
#

$$($(1)POM_FILE): $$($(1)POM_SOURCE) Makefile
	$$($(1)POM_SEDX)

endef
## include ossrh config
## - OSSRH_GPG_KEY
## - OSSRH_GPG_PASSPHRASE
## - OSSRH_USERNAME
## - OSSRH_PASSWORD
-include $(HOME)/.config/objectos/ossrh-config.mk

## gpg command
GPGX = $(GPG)
GPGX += --armor
GPGX += --batch
GPGX += --default-key $(OSSRH_GPG_KEY)
GPGX += --detach-sign
GPGX += --passphrase $(OSSRH_GPG_PASSPHRASE)
GPGX += --pinentry-mode loopback
GPGX += --yes

%.asc: %
	@$(GPGX) $<

define OSSRH_PREPARE_TASK

## ossrh bundle contents
$(1)OSSRH_CONTENTS = $$($(1)POM_FILE)
$(1)OSSRH_CONTENTS += $$($(1)JAR_FILE)
$(1)OSSRH_CONTENTS += $$($(1)SOURCE_JAR_FILE)
$(1)OSSRH_CONTENTS += $$($(1)JAVADOC_JAR_FILE)

## ossrh sigs
$(1)OSSRH_SIGS = $$($(1)OSSRH_CONTENTS:%=%.asc)

## contents + sigs
$(1)OSSRH_PREPARE = $$($(1)OSSRH_CONTENTS)
$(1)OSSRH_PREPARE += $$($(1)OSSRH_SIGS)

endef

## include GH config
## - GH_TOKEN
-include $(HOME)/.config/objectos/gh-config.mk

#
# objectos.core.object options
#

## module directory
CORE_OBJECT = objectos.core.object

## module
CORE_OBJECT_MODULE = $(CORE_OBJECT)

## module version
CORE_OBJECT_VERSION = $(VERSION)

## javac --release option
CORE_OBJECT_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
CORE_OBJECT_ENABLE_PREVIEW = 1

## jar name
CORE_OBJECT_JAR_NAME = $(CORE_OBJECT)

## test compile deps
CORE_OBJECT_TEST_COMPILE_DEPS = $(CORE_OBJECT_JAR_FILE)
CORE_OBJECT_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
CORE_OBJECT_TEST_RUNTIME_DEPS = $(CORE_OBJECT_TEST_COMPILE_DEPS)
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
CORE_OBJECT_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime exports
CORE_OBJECT_TEST_JAVAX_EXPORTS := objectos.core.object.internal

## install coordinates
CORE_OBJECT_GROUP_ID = $(GROUP_ID)
CORE_OBJECT_ARTIFACT_ID = $(CORE_OBJECT_MODULE)

## copyright years for javadoc
CORE_OBJECT_COPYRIGHT_YEARS := 2022-2023

## javadoc snippet path
# CORE_OBJECT_JAVADOC_SNIPPET_PATH := CORE_OBJECT_TEST

## pom description
CORE_OBJECT_DESCRIPTION = Utilities for java.lang.Object instances

#
# objectos.core.object targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),CORE_OBJECT_)))

.PHONY: core.object@clean
core.object@clean:
	rm -rf $(CORE_OBJECT_WORK)/*

.PHONY: core.object@compile
core.object@compile: $(CORE_OBJECT_COMPILE_MARKER)

.PHONY: core.object@jar
core.object@jar: $(CORE_OBJECT_JAR_FILE)

.PHONY: core.object@test
core.object@test: $(CORE_OBJECT_TEST_RUN_MARKER)

.PHONY: core.object@install
core.object@install: $(CORE_OBJECT_INSTALL)

.PHONY: core.object@source-jar
core.object@source-jar: $(CORE_OBJECT_SOURCE_JAR_FILE)

.PHONY: core.object@javadoc
core.object@javadoc: $(CORE_OBJECT_JAVADOC_JAR_FILE)

.PHONY: core.object@pom
core.object@pom: $(CORE_OBJECT_POM_FILE)

.PHONY: core.object@ossrh-prepare
core.object@ossrh-prepare: $(CORE_OBJECT_OSSRH_PREPARE)


#
# objectos.code options
#

## code directory
CODE = objectos.code

## code module
CODE_MODULE = $(CODE)

## code module version
CODE_VERSION = $(VERSION)

## code javac --release option
CODE_JAVA_RELEASE = $(JAVA_RELEASE)

## code --enable-preview ?
CODE_ENABLE_PREVIEW = 1

## code jar name
CODE_JAR_NAME = $(CODE)

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
# objectos.code targets
#

CODE_TASKS = COMPILE_TASK
CODE_TASKS += JAR_TASK
CODE_TASKS += TEST_COMPILE_TASK
CODE_TASKS += TEST_RUN_TASK

$(foreach task,$(CODE_TASKS),$(eval $(call $(task),CODE_)))

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
# objectos.selfgen options
#

## selfgen directory
SELFGEN := objectos.selfgen

## selfgen module
SELFGEN_MODULE := $(SELFGEN)

## selfgen module version
SELFGEN_VERSION := $(VERSION)

## selfgen javac --release option
SELFGEN_JAVA_RELEASE := $(JAVA_RELEASE)

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
# objectos.code targets
#

#
# objectos.selfgen targets
#

SELFGEN_TASKS = COMPILE_TASK
SELFGEN_TASKS += JAR_TASK
SELFGEN_TASKS += TEST_COMPILE_TASK
SELFGEN_TASKS += TEST_RUN_TASK

$(foreach task,$(SELFGEN_TASKS),$(eval $(call $(task),SELFGEN_)))

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
SELFGEN_MARKER = $(SELFGEN_WORK)/selfgen-marker

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

## way compile deps
WAY_COMPILE_DEPS = $(CORE_OBJECT_JAR_FILE)

## way jar name
WAY_JAR_NAME := $(WAY)

## way JS source
WAY_JS_SRC = $(WAY)/js/objectos.way.js

## way JS artifact
WAY_JS_ARTIFACT = $(WAY_CLASS_OUTPUT)/objectos/js/objectos.way.js

## way jar file reqs
WAY_JAR_FILE_REQS_MORE = $(WAY_JS_ARTIFACT)

## way test compile-time dependencies
WAY_TEST_COMPILE_DEPS = $(WAY_COMPILE_DEPS)
WAY_TEST_COMPILE_DEPS += $(WAY_JAR_FILE)
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

## way sub modules
WAY_SUBMODULES = core.object

#
# objectos.way targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),WAY_)))

#
# Targets section
#

.PHONY: clean
clean: $(foreach mod,$(WAY_SUBMODULES),$(mod)@clean) code@clean selfgen@clean way@clean 

.PHONY: test
test: $(foreach mod,$(WAY_SUBMODULES),$(mod)@test) code@test selfgen@test way@test

.PHONY: install
install: $(foreach mod,$(WAY_SUBMODULES),$(mod)@install) way@install

.PHONY: source-jar
source-jar: $(foreach mod,$(WAY_SUBMODULES),$(mod)@source-jar) way@source-jar 

.PHONY: javadoc
javadoc: $(foreach mod,$(WAY_SUBMODULES),$(mod)@javadoc) way@javadoc 

.PHONY: pom
pom: $(foreach mod,$(WAY_SUBMODULES),$(mod)@pom) way@pom 

.PHONY: ossrh-prepare
ossrh-prepare: $(foreach mod,$(WAY_SUBMODULES),$(mod)@ossrh-prepare) way@ossrh-prepare

.PHONY: ossrh
ossrh: way@ossrh

.PHONY: gh-release
gh-release: way@gh-release

.PHONY: way@clean
way@clean:
	rm -rf $(WAY_WORK)/*

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

.PHONY: way@ossrh-prepare
way@ossrh-prepare: $(WAY_OSSRH_PREPARE)

.PHONY: way@ossrh way@ossrh-bundle
way@ossrh: $(WAY_OSSRH_MARKER)

way@ossrh-bundle: $(WAY_OSSRH_BUNDLE)

.PHONY: way@gh-release way@gh-release-body
way@gh-release: $(WAY_GH_RELEASE_MARKER)

way@gh-release-body: $(WAY_GH_RELEASE_BODY)

#
# Eclipse project targets
#

define ECLIPSE_CLASSPATH
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" output="work/main" path="main"/>
	<classpathentry kind="src" output="work/test" path="test">
		<attributes>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-21">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="var" path="M2_REPO/org/testng/testng/7.7.1/testng-7.7.1.jar"/>
	<classpathentry kind="var" path="M2_REPO/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar"/>
	<classpathentry kind="var" path="M2_REPO/org/slf4j/slf4j-nop/1.7.36/slf4j-nop-1.7.36.jar"/>
	<classpathentry kind="var" path="M2_REPO/com/beust/jcommander/1.82/jcommander-1.82.jar"/>
	<classpathentry kind="output" path="eclipse-bin"/>
</classpath>
endef

define ECLIPSE_GITIGNORE
/eclipse-bin/
/work/
endef

define ECLIPSE_PROJECT
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>objectos.way:$(ECLIPSE_MODULE_NAME)</name>
	<comment></comment>
	<projects>
	</projects>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.eclipse.jdt.core.javanature</nature>
	</natures>
</projectDescription>
endef

define ECLIPSE_SETTINGS_CORE_RESOURCES
eclipse.preferences.version=1
encoding/<project>=UTF-8
endef

define ECLIPSE_SETTINGS_JDT_CORE
eclipse.preferences.version=1
org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled
org.eclipse.jdt.core.compiler.codegen.targetPlatform=21
org.eclipse.jdt.core.compiler.codegen.unusedLocal=preserve
org.eclipse.jdt.core.compiler.compliance=21
org.eclipse.jdt.core.compiler.debug.lineNumber=generate
org.eclipse.jdt.core.compiler.debug.localVariable=generate
org.eclipse.jdt.core.compiler.debug.sourceFile=generate
org.eclipse.jdt.core.compiler.problem.assertIdentifier=error
org.eclipse.jdt.core.compiler.problem.enablePreviewFeatures=disabled
org.eclipse.jdt.core.compiler.problem.enumIdentifier=error
org.eclipse.jdt.core.compiler.problem.reportPreviewFeatures=warning
org.eclipse.jdt.core.compiler.release=enabled
org.eclipse.jdt.core.compiler.source=21
endef

define ECLIPSE_SETTINGS_JDT_UI
eclipse.preferences.version=1
org.eclipse.jdt.ui.javadoc=true
org.eclipse.jdt.ui.text.custom_code_templates=<?xml version\="1.0" encoding\="UTF-8" standalone\="no"?><templates><template autoinsert\="false" context\="gettercomment_context" deleted\="false" description\="Comment for getter function" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.gettercomment" name\="gettercomment"/><template autoinsert\="false" context\="settercomment_context" deleted\="false" description\="Comment for setter function" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.settercomment" name\="settercomment"/><template autoinsert\="false" context\="constructorcomment_context" deleted\="false" description\="Comment for created constructors" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.constructorcomment" name\="constructorcomment"/><template autoinsert\="false" context\="filecomment_context" deleted\="false" description\="Comment for created Java files" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.filecomment" name\="filecomment">/*\n * Copyright (C) 2023 Objectos Software LTDA.\n *\n * Licensed under the Apache License, Version 2.0 (the "License");\n * you may not use this file except in compliance with the License.\n * You may obtain a copy of the License at\n *\n * http\://www.apache.org/licenses/LICENSE-2.0\n *\n * Unless required by applicable law or agreed to in writing, software\n * distributed under the License is distributed on an "AS IS" BASIS,\n * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n * See the License for the specific language governing permissions and\n * limitations under the License.\n */</template><template autoinsert\="false" context\="typecomment_context" deleted\="false" description\="Comment for created types" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.typecomment" name\="typecomment"/><template autoinsert\="true" context\="fieldcomment_context" deleted\="false" description\="Comment for fields" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.fieldcomment" name\="fieldcomment">/**\n * \n */</template><template autoinsert\="false" context\="methodcomment_context" deleted\="false" description\="Comment for non-overriding function" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.methodcomment" name\="methodcomment"/><template autoinsert\="true" context\="modulecomment_context" deleted\="false" description\="Comment for modules" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.modulecomment" name\="modulecomment">/**\n * ${tags}\n */</template><template autoinsert\="false" context\="overridecomment_context" deleted\="false" description\="Comment for overriding functions" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.overridecomment" name\="overridecomment"/><template autoinsert\="false" context\="delegatecomment_context" deleted\="false" description\="Comment for delegate methods" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.delegatecomment" name\="delegatecomment"/><template autoinsert\="true" context\="newtype_context" deleted\="false" description\="Newly created files" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.newtype" name\="newtype">${filecomment}\n${package_declaration}\n\n${typecomment}\n${type_declaration}</template><template autoinsert\="true" context\="classbody_context" deleted\="false" description\="Code in new class type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.classbody" name\="classbody">\n</template><template autoinsert\="true" context\="interfacebody_context" deleted\="false" description\="Code in new interface type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.interfacebody" name\="interfacebody">\n</template><template autoinsert\="true" context\="enumbody_context" deleted\="false" description\="Code in new enum type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.enumbody" name\="enumbody">\n</template><template autoinsert\="true" context\="recordbody_context" deleted\="false" description\="Code in new record type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.recordbody" name\="recordbody">\n</template><template autoinsert\="true" context\="annotationbody_context" deleted\="false" description\="Code in new annotation type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.annotationbody" name\="annotationbody">\n</template><template autoinsert\="true" context\="catchblock_context" deleted\="false" description\="Code in new catch blocks" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.catchblock" name\="catchblock">// ${todo} Auto-generated catch block\n${exception_var}.printStackTrace();</template><template autoinsert\="false" context\="methodbody_context" deleted\="false" description\="Code in created function stubs" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.methodbody" name\="methodbody">${body_statement}</template><template autoinsert\="false" context\="constructorbody_context" deleted\="false" description\="Code in created constructor stubs" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.constructorbody" name\="constructorbody">${body_statement}</template><template autoinsert\="true" context\="getterbody_context" deleted\="false" description\="Code in created getters" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.getterbody" name\="getterbody">return ${field};</template><template autoinsert\="true" context\="setterbody_context" deleted\="false" description\="Code in created setters" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.setterbody" name\="setterbody">$${field} \= $${param};</template></templates>
endef

.PHONY: eclipse-gen
eclipse-gen: export CLASSPATH = $(ECLIPSE_CLASSPATH)
eclipse-gen: export GITIGNORE = $(ECLIPSE_GITIGNORE)
eclipse-gen: export PROJECT = $(ECLIPSE_PROJECT)
eclipse-gen: export SETTINGS_CORE_RESOURCES = $(ECLIPSE_SETTINGS_CORE_RESOURCES)
eclipse-gen: export SETTINGS_JDT_CORE = $(ECLIPSE_SETTINGS_JDT_CORE)
eclipse-gen: export SETTINGS_JDT_UI = $(ECLIPSE_SETTINGS_JDT_UI)
eclipse-gen:
	mkdir --parents $(ECLIPSE_MODULE_NAME)/main
	mkdir --parents $(ECLIPSE_MODULE_NAME)/test
	echo "$$CLASSPATH" > $(ECLIPSE_MODULE_NAME)/.classpath
	echo "$$GITIGNORE" > $(ECLIPSE_MODULE_NAME)/.gitignore
	echo "$$PROJECT" > $(ECLIPSE_MODULE_NAME)/.project
	mkdir --parents $(ECLIPSE_MODULE_NAME)/.settings
	echo "$$SETTINGS_CORE_RESOURCES" > $(ECLIPSE_MODULE_NAME)/.settings/org.eclipse.core.resources.prefs
	echo "$$SETTINGS_JDT_CORE" > $(ECLIPSE_MODULE_NAME)/.settings/org.eclipse.jdt.core.prefs
	echo "$$SETTINGS_JDT_UI" > $(ECLIPSE_MODULE_NAME)/.settings/org.eclipse.jdt.ui.prefs
