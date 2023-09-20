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
# Objectos Way
#

MODULE := objectos.way
VERSION := 0.1.0-SNAPSHOT

## Deps versions

TESTNG_VERSION := 7.7.1
JCOMMANDER_VERSION := 1.82
SLF4J_VERSION := 1.7.36

## Compile options

WAY_JAVA_RELEASE = 21
SGEN_JAVA_RELEASE = 21
SGEN_ENABLE_PREVIEW = 1

## Test options
TEST_JAVAX_EXPORTS := objectos.lang
TEST_JAVAX_EXPORTS += objectos.util

# Delete the default suffixes
.SUFFIXES:

#
# Tool used in this build
#

## java home
ifdef JAVA_HOME
JAVA_HOME_BIN := $(JAVA_HOME)/bin
else
JAVA_HOME_BIN :=
endif

## java common options
JAVA := $(JAVA_HOME_BIN)/java

## javac common options
JAVAC := $(JAVA_HOME_BIN)/javac

## jar common options
JAR := $(JAVA_HOME_BIN)/jar

## javadoc common options
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
# objectos.way jar options
#

## module
WAY = objectos.way

## where to find .java files
WAY_MAIN = $(WAY)/main

## the .java files
WAY_SOURCES = $(shell find ${WAY_MAIN} -type f -name '*.java' -print)

## .java files modified since last compilation
WAY_DIRTY :=

## main work dir
WAY_WORK = $(WAY)/work

## main class output path
WAY_CLASS_OUTPUT = $(WAY_WORK)/main

## main compiled classes
WAY_CLASSES = $(WAY_SOURCES:$(WAY_MAIN)/%.java=$(WAY_CLASS_OUTPUT)/%.class)

## META-INF
WAY_METAINF = $(WAY_CLASS_OUTPUT)/META-INF

## license 'artifact'
WAY_LICENSE = $(WAY_METAINF)/LICENSE

## way javac command
WAY_JAVACX = $(JAVAC)
WAY_JAVACX += -d $(WAY_CLASS_OUTPUT)
WAY_JAVACX += -g
WAY_JAVACX += -Xlint:all
WAY_JAVACX += -Xpkginfo:always
WAY_JAVACX += --module-version $(VERSION)
WAY_JAVACX += --release $(WAY_JAVA_RELEASE)
WAY_JAVACX += $(WAY_DIRTY)

## way jar path
WAY_JAR = $(WAY_WORK)/$(WAY)-$(VERSION).jar

## way jar command
WAY_JARX = $(JAR)
WAY_JARX += --create
WAY_JARX += --file $(WAY_JAR)
WAY_JARX += --module-version $(VERSION)
WAY_JARX += -C $(WAY_CLASS_OUTPUT)
WAY_JARX += .

#
# objectos.way test options
#

## test base dir
WAY_TEST = $(WAY)/test

## test source files 
WAY_TEST_SOURCES = $(shell find ${WAY_TEST} -type f -name '*.java' -print)

## test source files modified since last compilation
WAY_TEST_DIRTY :=

## test class output path
WAY_TEST_CLASS_OUTPUT = $(WAY_WORK)/test

## test compiled classes
WAY_TEST_CLASSES = $(WAY_TEST_SOURCES:$(WAY_TEST)/%.java=$(WAY_TEST_CLASS_OUTPUT)/%.class)

## test compile-time dependencies
WAY_TEST_COMPILE_DEPS = $(WAY_JAR)
WAY_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test javac command
WAY_TEST_JAVACX = $(JAVAC)
WAY_TEST_JAVACX += -d $(WAY_TEST_CLASS_OUTPUT)
WAY_TEST_JAVACX += -g
WAY_TEST_JAVACX += -Xlint:all
WAY_TEST_JAVACX += --class-path $(call class-path,$(WAY_TEST_COMPILE_DEPS))
WAY_TEST_JAVACX += --release $(WAY_JAVA_RELEASE)
WAY_TEST_JAVACX += $(WAY_TEST_DIRTY)

## test runtime dependencies
WAY_TEST_RUNTIME_DEPS = $(WAY_TEST_COMPILE_DEPS)
WAY_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
WAY_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
WAY_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime exports
WAY_TEST_JAVAX_EXPORTS := objectos.css.internal
WAY_TEST_JAVAX_EXPORTS += objectos.html.internal
WAY_TEST_JAVAX_EXPORTS += objectos.http.internal
WAY_TEST_JAVAX_EXPORTS += objectos.lang
WAY_TEST_JAVAX_EXPORTS += objectos.util

## test runtime output path
WAY_TEST_RUNTIME_OUTPUT = $(WAY_WORK)/test-output

## test java command
WAY_TEST_JAVAX = $(JAVA)
WAY_TEST_JAVAX += --module-path $(call module-path,$(WAY_TEST_RUNTIME_DEPS))
WAY_TEST_JAVAX += --add-modules org.testng
WAY_TEST_JAVAX += --add-reads $(WAY)=org.testng
WAY_TEST_JAVAX += $(foreach pkg,$(WAY_TEST_JAVAX_EXPORTS),--add-exports $(WAY)/$(pkg)=org.testng)
WAY_TEST_JAVAX += --patch-module $(WAY)=$(WAY_TEST_CLASS_OUTPUT)
WAY_TEST_JAVAX += --module $(WAY)/$(WAY).RunTests
WAY_TEST_JAVAX += $(WAY_TEST_RUNTIME_OUTPUT)

#
# objectos.code jar options
#

## code module
CODE = objectos.code

## code base dir
CODE_MAIN = $(CODE)/main

## code source files
CODE_SOURCES = $(shell find ${CODE_MAIN} -type f -name '*.java' -print)

## main source files modified since last compilation
CODE_DIRTY :=

## main work dir
CODE_WORK = $(CODE)/work

## main class output path
CODE_CLASS_OUTPUT = $(CODE_WORK)/main

## META-INF
CODE_METAINF = $(CODE_CLASS_OUTPUT)/META-INF

## license 'artifact'
CODE_LICENSE = $(CODE_METAINF)/LICENSE

## code compiled classes
CODE_CLASSES = $(CODE_SOURCES:$(CODE_MAIN)/%.java=$(CODE_CLASS_OUTPUT)/%.class)

## code javac command
CODE_JAVACX = $(JAVAC)
CODE_JAVACX += -d $(CODE_CLASS_OUTPUT)
CODE_JAVACX += -g
CODE_JAVACX += -Xlint:all
CODE_JAVACX += -Xpkginfo:always
CODE_JAVACX += --enable-preview
CODE_JAVACX += --module-version $(VERSION)
CODE_JAVACX += --release $(WAY_JAVA_RELEASE)
CODE_JAVACX += $(CODE_DIRTY)

## code jar path
CODE_JAR := $(CODE_WORK)/$(CODE)-$(VERSION).jar

## code jar command
CODE_JARX = $(JAR)
CODE_JARX += --create
CODE_JARX += --file $(CODE_JAR)
CODE_JARX += --module-version $(VERSION)
CODE_JARX += -C $(CODE_CLASS_OUTPUT)
CODE_JARX += .

#
# objectos.code test options
#

## test base dir
CODE_TEST = $(CODE)/test

## test source files 
CODE_TEST_SOURCES = $(shell find ${CODE_TEST} -type f -name '*.java' -print)

## test source files modified since last compilation
CODE_TEST_DIRTY :=

## test class output path
CODE_TEST_CLASS_OUTPUT = $(CODE_WORK)/test

## test compiled classes
CODE_TEST_CLASSES = $(CODE_TEST_SOURCES:$(CODE_TEST)/%.java=$(CODE_TEST_CLASS_OUTPUT)/%.class)

## test compile-time dependencies
CODE_TEST_COMPILE_DEPS = $(CODE_JAR)
CODE_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test javac command
CODE_TEST_JAVACX = $(JAVAC)
CODE_TEST_JAVACX += -d $(CODE_TEST_CLASS_OUTPUT)
CODE_TEST_JAVACX += -g
CODE_TEST_JAVACX += -Xlint:all
CODE_TEST_JAVACX += --class-path $(call class-path,$(CODE_TEST_COMPILE_DEPS))
CODE_TEST_JAVACX += --release $(WAY_JAVA_RELEASE)
CODE_TEST_JAVACX += --enable-preview
CODE_TEST_JAVACX += $(CODE_TEST_DIRTY)

## test runtime dependencies
CODE_TEST_RUNTIME_DEPS = $(CODE_TEST_COMPILE_DEPS)
CODE_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
CODE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
CODE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime exports
CODE_TEST_JAVAX_EXPORTS := objectos.code.internal

## test runtime output path
CODE_TEST_RUNTIME_OUTPUT = $(CODE_WORK)/test-output

## test java command
CODE_TEST_JAVAX = $(JAVA)
CODE_TEST_JAVAX += --module-path $(call module-path,$(CODE_TEST_RUNTIME_DEPS))
CODE_TEST_JAVAX += --add-modules org.testng
CODE_TEST_JAVAX += --add-reads $(CODE)=org.testng
CODE_TEST_JAVAX += $(foreach pkg,$(CODE_TEST_JAVAX_EXPORTS),--add-exports $(CODE)/$(pkg)=org.testng)
CODE_TEST_JAVAX += --patch-module $(CODE)=$(CODE_TEST_CLASS_OUTPUT)
CODE_TEST_JAVAX += --enable-preview
CODE_TEST_JAVAX += --module $(CODE)/$(CODE).RunTests
CODE_TEST_JAVAX += $(CODE_TEST_RUNTIME_OUTPUT)

#
# objectos.selfgen options
#

## selfgen module
SGEN = objectos.selfgen

## selfgen source dir
SGEN_MAIN = $(SGEN)/main

## code source files
SGEN_SOURCES = $(shell find ${SGEN_MAIN} -type f -name '*.java' -print)

## main source files modified since last compilation
SGEN_DIRTY :=

## main work dir
SGEN_WORK = $(SGEN)/work

## main class output path
SGEN_CLASS_OUTPUT = $(SGEN_WORK)/main

## selfgen compiled classes
SGEN_CLASSES = $(SGEN_SOURCES:$(SGEN_MAIN)/%.java=$(SGEN_CLASS_OUTPUT)/%.class)

## selfgen compile deps
SGEN_COMPILE_DEPS = $(CODE_JAR)

## selfgen javac command
SGEN_JAVACX = $(JAVAC)
SGEN_JAVACX += -d $(SGEN_CLASS_OUTPUT)
SGEN_JAVACX += -g
SGEN_JAVACX += -Xlint:all
SGEN_JAVACX += -Xpkginfo:always
ifeq ($(SGEN_ENABLE_PREVIEW), 1)
SGEN_JAVACX += -Xmaxwarns 500
SGEN_JAVACX += --enable-preview
endif
SGEN_JAVACX += --module-path $(call module-path,$(SGEN_COMPILE_DEPS))
SGEN_JAVACX += --module-version $(VERSION)
SGEN_JAVACX += --release $(SGEN_JAVA_RELEASE)
SGEN_JAVACX += $(SGEN_DIRTY)

## selfgen jar path
SGEN_JAR := $(SGEN_WORK)/$(SGEN)-$(VERSION).jar

## code jar command
SGEN_JARX = $(JAR)
SGEN_JARX += --create
SGEN_JARX += --file $(SGEN_JAR)
SGEN_JARX += --module-version $(VERSION)
SGEN_JARX += -C $(SGEN_CLASS_OUTPUT)
SGEN_JARX += .

## marker to indicate when selfgen was last run
SGEN_MARKER = $(WAY_WORK)/selfgen-marker

## selfgen runtime deps
SGEN_RUNTIME_DEPS = $(SGEN_JAR)
SGEN_RUNTIME_DEPS += $(SGEN_COMPILE_DEPS)

## selfgen java command
SGEN_JAVAX = $(JAVA)
SGEN_JAVAX += --module-path $(call module-path,$(SGEN_RUNTIME_DEPS))
ifeq ($(SGEN_ENABLE_PREVIEW), 1)
SGEN_JAVAX += --enable-preview
endif
SGEN_JAVAX += --module $(SGEN)/$(SGEN).Main
SGEN_JAVAX += $(WAY_MAIN)

#
# objectos.selfgen test options
#

## test base dir
SGEN_TEST = $(SGEN)/test

## test source files 
SGEN_TEST_SOURCES = $(shell find ${SGEN_TEST} -type f -name '*.java' -print)

## test source files modified since last compilation
SGEN_TEST_DIRTY :=

## test class output path
SGEN_TEST_CLASS_OUTPUT = $(SGEN_WORK)/test

## test compiled classes
SGEN_TEST_CLASSES = $(SGEN_TEST_SOURCES:$(SGEN_TEST)/%.java=$(SGEN_TEST_CLASS_OUTPUT)/%.class)

## test compile-time dependencies
SGEN_TEST_COMPILE_DEPS = $(CODE_JAR)
SGEN_TEST_COMPILE_DEPS += $(SGEN_JAR)
SGEN_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test javac command
SGEN_TEST_JAVACX = $(JAVAC)
SGEN_TEST_JAVACX += -d $(SGEN_TEST_CLASS_OUTPUT)
SGEN_TEST_JAVACX += -g
SGEN_TEST_JAVACX += -Xlint:all
SGEN_TEST_JAVACX += --class-path $(call class-path,$(SGEN_TEST_COMPILE_DEPS))
SGEN_TEST_JAVACX += --release $(SGEN_JAVA_RELEASE)
SGEN_TEST_JAVACX += --enable-preview
SGEN_TEST_JAVACX += $(SGEN_TEST_DIRTY)

## test runtime dependencies
SGEN_TEST_RUNTIME_DEPS = $(SGEN_TEST_COMPILE_DEPS)
SGEN_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
SGEN_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
SGEN_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## test runtime exports
SGEN_TEST_JAVAX_EXPORTS := objectos.selfgen.css
SGEN_TEST_JAVAX_EXPORTS += objectos.selfgen.html

## test runtime output path
SGEN_TEST_RUNTIME_OUTPUT = $(SGEN_WORK)/test-output

## test java command
SGEN_TEST_JAVAX = $(JAVA)
SGEN_TEST_JAVAX += --module-path $(call module-path,$(SGEN_TEST_RUNTIME_DEPS))
SGEN_TEST_JAVAX += --add-modules org.testng
SGEN_TEST_JAVAX += --add-reads $(SGEN)=org.testng
SGEN_TEST_JAVAX += $(foreach pkg,$(SGEN_TEST_JAVAX_EXPORTS),--add-exports $(SGEN)/$(pkg)=org.testng)
SGEN_TEST_JAVAX += --patch-module $(SGEN)=$(SGEN_TEST_CLASS_OUTPUT)
SGEN_TEST_JAVAX += --enable-preview
SGEN_TEST_JAVAX += --module $(SGEN)/$(SGEN).RunTests
SGEN_TEST_JAVAX += $(SGEN_TEST_RUNTIME_OUTPUT)

#
# Targets
#

.PHONY: all
all: jar

.PHONY: clean way@clean code@clean selfgen@clean
clean: way@clean code@clean selfgen@clean

way@clean:
	rm -rf $(WAY_WORK)/*
	
code@clean:
	rm -rf $(CODE_WORK)/*

selfgen@clean:
	rm -rf $(SGEN_WORK)/*

.PHONY: jar
jar: way@jar

.PHONY: way@jar
way@jar: $(WAY_JAR)

$(WAY_JAR): $(SGEN_MARKER) $(WAY_CLASSES) $(WAY_LICENSE)
	if [ -n "$(WAY_DIRTY)" ]; then \
		$(WAY_JAVACX); \
	fi
	$(WAY_JARX)

$(WAY_CLASSES): $(WAY_CLASS_OUTPUT)/%.class: $(WAY_MAIN)/%.java
	$(eval WAY_DIRTY += $$<)

$(WAY_LICENSE): LICENSE
	mkdir -p $(WAY_METAINF)
	cp LICENSE $(WAY_METAINF)

.PHONY: code@jar
code@jar: $(CODE_JAR)
	
$(CODE_JAR): $(CODE_CLASSES) $(CODE_LICENSE)
	if [ -n "$(CODE_DIRTY)" ]; then \
		$(CODE_JAVACX); \
	fi
	$(CODE_JARX)

$(CODE_CLASSES): $(CODE_CLASS_OUTPUT)/%.class: $(CODE_MAIN)/%.java
	$(eval CODE_DIRTY += $$<)

$(CODE_LICENSE): LICENSE
	mkdir -p $(CODE_METAINF)
	cp LICENSE $(CODE_METAINF)

.PHONY: selfgen
selfgen: $(SGEN_MARKER)

$(SGEN_MARKER): $(SGEN_JAR)
	$(SGEN_JAVAX)
	mkdir -p $(WAY_WORK)
	touch $(SGEN_MARKER)

$(SGEN_JAR): $(SGEN_COMPILE_DEPS) $(SGEN_CLASSES)
	if [ -n "$(SGEN_DIRTY)" ]; then \
		$(SGEN_JAVACX); \
	fi
	$(SGEN_JARX)

$(SGEN_CLASSES): $(SGEN_CLASS_OUTPUT)/%.class: $(SGEN_MAIN)/%.java
	$(eval SGEN_DIRTY += $$<)

## Gets the dependency from the remote repository
$(LOCAL_REPO_PATH)/%.jar:
	$(REMOTE_REPO_CURLX) --output $@ $(@:$(LOCAL_REPO_PATH)/%.jar=$(REMOTE_REPO_URL)/%.jar)

.PHONY: test
test: code@test selfgen@test way@test

.PHONY: way@test
way@test: $(WAY_TEST_CLASSES) $(WAY_TEST_RUNTIME_DEPS)  
	if [ -n "$(WAY_TEST_DIRTY)" ]; then \
		$(WAY_TEST_JAVACX); \
	fi
	$(WAY_TEST_JAVAX)
	
$(WAY_TEST_CLASSES): $(WAY_TEST_CLASS_OUTPUT)/%.class: $(WAY_TEST)/%.java
	$(eval WAY_TEST_DIRTY += $$<)

.PHONY: code@test
code@test: $(CODE_TEST_CLASSES) $(CODE_TEST_RUNTIME_DEPS)  
	if [ -n "$(CODE_TEST_DIRTY)" ]; then \
		$(CODE_TEST_JAVACX); \
	fi
	$(CODE_TEST_JAVAX)
	
$(CODE_TEST_CLASSES): $(CODE_TEST_CLASS_OUTPUT)/%.class: $(CODE_TEST)/%.java
	$(eval CODE_TEST_DIRTY += $$<)

.PHONY: selfgen@test
selfgen@test: $(SGEN_TEST_CLASSES) $(SGEN_TEST_RUNTIME_DEPS)  
	if [ -n "$(SGEN_TEST_DIRTY)" ]; then \
		$(SGEN_TEST_JAVACX); \
	fi
	$(SGEN_TEST_JAVAX)
	
$(SGEN_TEST_CLASSES): $(SGEN_TEST_CLASS_OUTPUT)/%.class: $(SGEN_TEST)/%.java
	$(eval SGEN_TEST_DIRTY += $$<)