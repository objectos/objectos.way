#
# Copyright (C) 2023-2024 Objectos Software LTDA.
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
# Tools and global options
#

## configures JAVA_HOME_BIN
ifdef JAVA_HOME
JAVA_HOME_BIN := $(JAVA_HOME)/bin
else
$(error Required JAVA_HOME variable was not set)
endif

## java command
JAVA := $(JAVA_HOME_BIN)/java

## javac command
JAVAC := $(JAVA_HOME_BIN)/javac
JAVAC += -g

## jar command
JAR := $(JAVA_HOME_BIN)/jar

## jdeps command
JDEPS := $(JAVA_HOME_BIN)/jdeps

## jlink command
JLINK := $(JAVA_HOME_BIN)/jlink

#
# java related functions
#

## chars
empty :=
space := $(empty) $(empty)
colon := :
dot := .
solidus := /

## class path separator
ifeq ($(OS),Windows_NT)
CLASS_PATH_SEPARATOR := ;
else
CLASS_PATH_SEPARATOR := :
endif

## class-path function
##
## syntax:
## $(call class-path,[list of deps])
class-path = $(subst $(space),$(CLASS_PATH_SEPARATOR),$(1))

## module path separator
MODULE_PATH_SEPARATOR := $(colon)

## module-path function
##
## syntax:
## $(call module-path,[list of deps])
module-path = $(subst $(space),$(MODULE_PATH_SEPARATOR),$(1))

## gav-to-artifact: convert a GAV into a relative path name
##
## syntax:
## $(call gav-to-artifact,com.example/foo/1.2.3)
## => com/example/foo/1.2.3/foo-1.2.3
gav-to-artifact = $(call mk-dependency,$(call word-solidus,$(1),1),$(call word-solidus,$(1),2),$(call word-solidus,$(1),3))

## gav-to-resolution-file:
##
## syntax:
## RESOLUTION_DIR := /tmp/resolution
## $(call gav-to-resolution-file,com.example/foo/1.2.3)
## => /tmp/resolution/com/example/foo/1.2.3/foo-1.2.3
gav-to-resolution-file = $(RESOLUTION_DIR)/$(1)

## deps-to-local
##
## syntax:
## LOCAL_REPO_PATH := /tmp/repo
## DEPS := com.example/foo/1.2.3
## DEPS += br.com.objectos/bar/3.4.5
## $(call deps-to-local,$(DEPS))
## => /tmp/repo/com/example/foo/1.2.3/foo-1.2.3.jar
## => /tmp/repo/br/com/objectos/bar/3.4.5/bar-3.4.5.jar
deps-to-local = $(foreach dep,$(1),$(call gav-to-local,$(dep)))

## dependency function
## 
## syntax:
## $(call dependency,[GROUP_ID],[ARTIFACT_ID],[VERSION])
mk-dependency = $(subst $(dot),$(solidus),$(1))/$(2)/$(3)/$(2)-$(3).$(4)
dependency = $(LOCAL_REPO_PATH)/$(subst $(dot),$(solidus),$(1))/$(2)/$(3)/$(2)-$(3).jar

## dep-to-jar
word-solidus = $(word $(2), $(subst $(solidus),$(space),$(1)))
mk-resolved-jar = $(call mk-dependency,$(call word-solidus,$(1),1),$(call word-solidus,$(1),2),$(call word-solidus,$(1),3),jar)
gav-to-local = $(LOCAL_REPO_PATH)/$(call mk-resolved-jar,$(1))
dep-to-jar = $(foreach dep,$(1),$(call gav-to-local,$(dep)))

## to-resolution-files
mk-resolution-file = $(RESOLUTION_DIR)/$(1)
to-resolution-files = $(foreach dep,$(1),$(call mk-resolution-file,$(dep)))

## uniq-resolution-files
uniq-resolution-files = cat -n $(1) | sort -uk2 | sort -n | cut -f2-

#
# Dependencies related options & functions
#

## remote repository URL
ifndef REMOTE_REPO_URL
REMOTE_REPO_URL := https://repo.maven.apache.org/maven2
endif

## local repository path
ifndef LOCAL_REPO_PATH
LOCAL_REPO_PATH := $(HOME)/.cache/objectos/repository
endif

## resolution directory
ifndef RESOLUTION_DIR
RESOLUTION_DIR := $(HOME)/.cache/objectos/resolution
endif

## Resolver.java path
ifndef RESOLVER_JAVA
RESOLVER_JAVA := Resolver.java
endif

## Where to find our Resolver.java source 
RESOLVER_URL := https://raw.githubusercontent.com/objectos/objectos.mk/main/resolver/src/main/java/Resolver.java

## Resolver.java deps
RESOLVER_DEPS := commons-codec/commons-codec/1.16.0
RESOLVER_DEPS += org.apache.commons/commons-lang3/3.12.0
RESOLVER_DEPS += org.apache.httpcomponents/httpclient/4.5.14
RESOLVER_DEPS += org.apache.httpcomponents/httpcore/4.4.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-api/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-connector-basic/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-impl/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-named-locks/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-spi/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-supplier/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-transport-file/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-transport-http/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-util/1.9.16
RESOLVER_DEPS += org.apache.maven/maven-artifact/3.9.4
RESOLVER_DEPS += org.apache.maven/maven-builder-support/3.9.4
RESOLVER_DEPS += org.apache.maven/maven-model-builder/3.9.4
RESOLVER_DEPS += org.apache.maven/maven-model/3.9.4
RESOLVER_DEPS += org.apache.maven/maven-repository-metadata/3.9.4
RESOLVER_DEPS += org.apache.maven/maven-resolver-provider/3.9.4
RESOLVER_DEPS += org.codehaus.plexus/plexus-interpolation/1.26
RESOLVER_DEPS += org.codehaus.plexus/plexus-utils/3.5.1
RESOLVER_DEPS += org.slf4j/jcl-over-slf4j/1.7.36
RESOLVER_DEPS += org.slf4j/slf4j-api/1.7.36
RESOLVER_DEPS += org.slf4j/slf4j-nop/1.7.36

## Resolver.java jars
RESOLVER_DEPS_JARS := $(call dep-to-jar,$(RESOLVER_DEPS))

## resolve java command
RESOLVEX := $(JAVA)
RESOLVEX += --class-path $(call class-path,$(RESOLVER_DEPS_JARS))
RESOLVEX += $(RESOLVER_JAVA)
RESOLVEX += --local-repo $(LOCAL_REPO_PATH)
RESOLVEX += --resolution-dir $(RESOLUTION_DIR)

## remote repository curl
REMOTE_REPO_WGETX := wget
REMOTE_REPO_WGETX += --directory-prefix=$(LOCAL_REPO_PATH)
REMOTE_REPO_WGETX += --force-directories
REMOTE_REPO_WGETX += --no-host-directories
REMOTE_REPO_WGETX += --cut-dirs=1
REMOTE_REPO_WGETX += --no-verbose

#
# java dependency related rules
#

$(LOCAL_REPO_PATH)/%.jar:	
	$(REMOTE_REPO_WGETX) $(@:$(LOCAL_REPO_PATH)/%.jar=$(REMOTE_REPO_URL)/%.jar)

$(RESOLVER_JAVA):
	wget --no-verbose $(RESOLVER_URL) 

$(RESOLUTION_DIR)/%: $(RESOLVER_JAVA) $(RESOLVER_DEPS_JARS)
	$(RESOLVEX) $(@:$(RESOLUTION_DIR)/%=%)
