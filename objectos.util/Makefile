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

# Objectos Util

MODULE = objectos.util
VERSION = 0.9.0-SNAPSHOT

# Compile Options

JAVA_RELEASE = 21

COMPILE_DEPS = objectos.lang/work/objectos.lang-0.9.0-SNAPSHOT.jar

## Test options

TEST_COMPILE_DEPS = $(COMPILE_DEPS) 
TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,7.7.1)

TEST_RUNTIME_DEPS = $(TEST_COMPILE_DEPS)
TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,1.82)
TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,1.7.36)
TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,1.7.36)

## POM generation options

POM_VARIABLES = DESCRIPTION

.PHONY: all
all: jar

.PHONY: clean
clean:
	rm -rf $(WORK)/*

# Delete the default suffixes
.SUFFIXES:

#
# Tool options
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
JAVAC += -g
JAVAC += -Xpkginfo:always

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
# Gets the dependency from the remote repository
#
$(LOCAL_REPO_PATH)/%.jar:	
	$(REMOTE_REPO_CURLX) --output $@ $(@:$(LOCAL_REPO_PATH)/%.jar=$(REMOTE_REPO_URL)/%.jar)

#
# main artifact options
#

## main base dir
MAIN = main

## main source path
SOURCE_PATH = $(MAIN)

## main source files
SOURCES = $(shell find ${SOURCE_PATH} -type f -name '*.java' -print)

## main source files modified since last compilation
MODIFIED_SOURCES :=

## main work dir
WORK = work

## main class output path
CLASS_OUTPUT = $(WORK)/main

## META-INF
META_INF_DIR = $(CLASS_OUTPUT)/META-INF

## license 'artifact'
LICENSE_ARTIFACT = $(META_INF_DIR)/LICENSE

## main compiled classes
CLASSES = $(SOURCES:$(SOURCE_PATH)/%.java=$(CLASS_OUTPUT)/%.class)

## main compile-time dependencies
# COMPILE_DEPS = 

## main compile-time module-path
COMPILE_MODULE_PATH = $(call module-path,$(COMPILE_DEPS))
 
## main javac command
## - do no set the module-path for artifacts that have no compile-time deps
JAVACX = $(JAVAC)
JAVACX += -d $(CLASS_OUTPUT)
JAVACX += -Xlint:all
ifdef ENABLE_PREVIEW
JAVACX += --enable-preview
endif
ifneq ($(COMPILE_MODULE_PATH),)
JAVACX += --module-path $(COMPILE_MODULE_PATH)
endif
JAVACX += --module-version $(VERSION)
JAVACX += --release $(JAVA_RELEASE)
JAVACX += $(MODIFIED_SOURCES)

## main generated artifact
ARTIFACT = $(WORK)/$(MODULE)-$(VERSION).jar

## main jar command
JARX = $(JAR)
JARX += --create
JARX += --file $(ARTIFACT)
JARX += --module-version $(VERSION)
JARX += -C $(CLASS_OUTPUT)
JARX += .

#
# jar target
#
.PHONY: jar
jar: $(ARTIFACT)

$(ARTIFACT): $(COMPILE_DEPS) $(CLASSES) $(LICENSE_ARTIFACT)
	if [ -n "$(MODIFIED_SOURCES)" ]; then \
		$(JAVACX); \
	fi
	$(JARX)

$(CLASSES): $(CLASS_OUTPUT)/%.class: $(SOURCE_PATH)/%.java
	$(eval MODIFIED_SOURCES += $$<)

$(LICENSE_ARTIFACT): LICENSE
	mkdir -p $(META_INF_DIR)
	cp LICENSE $(META_INF_DIR)
#
# test options
#

## test base dir
TEST = test

## test source path
TEST_SOURCE_PATH = $(TEST)

## test source files 
TEST_SOURCES = $(shell find ${TEST_SOURCE_PATH} -type f -name '*.java' -print)

## test source files modified since last compilation
TEST_MODIFIED_SOURCES :=

## test class output path
TEST_CLASS_OUTPUT = $(WORK)/test

## test compiled classes
TEST_CLASSES = $(TEST_SOURCES:$(TEST_SOURCE_PATH)/%.java=$(TEST_CLASS_OUTPUT)/%.class)

## test compile-time dependencies
# TEST_COMPILE_DEPS =

## test compile-time class path
TEST_COMPILE_CLASS_PATH = $(call class-path,$(TEST_COMPILE_DEPS)) 

## test javac command
TEST_JAVACX = $(JAVAC)
TEST_JAVACX += -d $(TEST_CLASS_OUTPUT)
TEST_JAVACX += -Xlint:all
TEST_JAVACX += --class-path $(CLASS_OUTPUT)$(CLASS_PATH_SEPARATOR)$(TEST_COMPILE_CLASS_PATH)
ifdef ENABLE_PREVIEW
TEST_JAVACX += --enable-preview
endif
TEST_JAVACX += --release $(JAVA_RELEASE)
TEST_JAVACX += $(TEST_MODIFIED_SOURCES)

## test runtime dependencies
# TEST_RUNTIME_DEPS =

## test runtime module-path
TEST_RUNTIME_MODULE_PATH = $(call module-path,$(TEST_RUNTIME_DEPS))

## test runtime output path
TEST_RUNTIME_OUTPUT = $(WORK)/test-output

## test main class
ifndef TEST_MAIN
TEST_MAIN = $(MODULE).RunTests
endif

## test java command
TEST_JAVAX = $(JAVA)
TEST_JAVAX += --module-path $(CLASS_OUTPUT)$(MODULE_PATH_SEPARATOR)$(TEST_RUNTIME_MODULE_PATH)
TEST_JAVAX += --add-modules org.testng
TEST_JAVAX += --add-reads $(MODULE)=org.testng
ifdef TEST_JAVAX_EXPORTS
TEST_JAVAX += $(foreach pkg,$(TEST_JAVAX_EXPORTS),--add-exports $(MODULE)/$(pkg)=org.testng)
endif
ifdef ENABLE_PREVIEW
TEST_JAVAX += --enable-preview
endif
TEST_JAVAX += --patch-module $(MODULE)=$(TEST_CLASS_OUTPUT)
TEST_JAVAX += --module $(MODULE)/$(TEST_MAIN)
TEST_JAVAX += $(TEST_RUNTIME_OUTPUT)

#
# test target
#
.PHONY: test
test: $(ARTIFACT) $(TEST_COMPILE_DEPS) $(TEST_CLASSES) $(TEST_RUNTIME_DEPS) 
	if [ -n "$(TEST_MODIFIED_SOURCES)" ]; then \
		$(TEST_JAVACX); \
	fi
	$(TEST_JAVAX)

$(TEST_CLASSES): $(TEST_CLASS_OUTPUT)/%.class: $(TEST_SOURCE_PATH)/%.java
	$(eval TEST_MODIFIED_SOURCES += $$<)
