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
# This file was generated. DO NOT EDIT!
#

#
# Objectos Way
#

GROUP_ID := br.com.objectos
ARTIFACT_ID := objectos.way
VERSION := 0.2.0-SNAPSHOT

JAVA_RELEASE := 21

## Deps versions

SLF4J_VERSION := 1.7.36
TESTNG_VERSION := 7.7.1

## Deps artifacts

SLF4J_NOP := org.slf4j/slf4j-nop/$(SLF4J_VERSION)
TESTNG := org.testng/testng/$(TESTNG_VERSION)

EXTERNAL_DEPS := $(SLF4J_NOP) $(TESTNG) 

# Delete the default suffixes
.SUFFIXES:

#
# Default target
#

.PHONY: all
all: resolve-external-deps test install

.PHONY: jar
jar: way@jar

print-%::
	@echo $* = $($*)


#
# pom template
#

# $(1) = copyright years
# $(2) = group id
# $(3) = artifact id
# $(4) = version
# $(5) = description
# $(6) = deps
define POM_TMPL
<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (C) $(1) Objectos Software LTDA.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

	<modelVersion>4.0.0</modelVersion>
	
	<groupId>$(2)</groupId>
	<artifactId>$(3)</artifactId>
	<version>$(4)</version>
	<name>$(2):$(3)</name>

	<description>
	$(5)
	</description>

	<url>https://www.objectos.com.br/</url>

	<inceptionYear>2022</inceptionYear>

	<licenses>
		<license>
			<name>The Apache License, Version 2.0</name>
			<url>https://www.apache.org/licenses/LICENSE-2.0</url>
			<distribution>repo</distribution>
		</license>
	</licenses>

	<scm>
		<connection>scm:git:git://github.com/objectos/objectos.way.git</connection>
		<developerConnection>scm:git:ssh://git@github.com/objectos/objectos.way.git</developerConnection>
		<url>https://github.com/objectos/objectos.way</url>
		<tag>HEAD</tag>
	</scm>

	<organization>
		<name>Objectos Software LTDA</name>
		<url>https://www.objectos.com.br/</url>
	</organization>

	<developers>
		<developer>
			<id>objectos</id>
			<name>Objectos Software LTDA</name>
			<email>opensource@objectos.com.br</email>
			<organization>Objectos Software LTDA</organization>
			<organizationUrl>https://www.objectos.com.br/</organizationUrl>
		</developer>
	</developers>
	
	<dependencies>
$(6)
	</dependencies>
	
</project>
endef

#
# mk-pom function
#

mk-pom = $(call POM_TMPL,$($(1)COPYRIGHT_YEARS),$($(1)GROUP_ID),$($(1)ARTIFACT_ID),$($(1)VERSION),$($(1)DESCRIPTION),$($(1)POM_DEPENDENCIES))

#
# Tools and global options
#

## configures JAVA_HOME_BIN
ifdef JAVA_HOME
JAVA_HOME_BIN := $(JAVA_HOME)/bin
else
JAVA_HOME_BIN :=
endif

## local objectos dir
ifndef OBJECTOS_DIR
OBJECTOS_DIR := $(HOME)/.cache/objectos
endif

## local repository path
ifndef LOCAL_REPO_PATH
LOCAL_REPO_PATH := $(OBJECTOS_DIR)/repository
endif

## local resolution dir
ifndef RESOLUTION_DIR
RESOLUTION_DIR := $(OBJECTOS_DIR)/resolution
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

## cat common options
CAT := cat

## curl common options
CURL := curl
CURL += --fail

## gpg common options
GPG := gpg

## jq common options
JQ := jq

## sed common options
SED := sed

## tr common options
TR := tr

## mvn command
MVN := mvn
MVN += --define maven.repo.local=$(LOCAL_REPO_PATH)

#
# Dependencies related options & functions
#

## remote repository URL
REMOTE_REPO_URL := https://repo.maven.apache.org/maven2

## remote repository curl
REMOTE_REPO_CURLX := $(CURL)
REMOTE_REPO_CURLX += --create-dirs

## dependency function
## 
## syntax:
## $(call dependency,[GROUP_ID],[ARTIFACT_ID],[VERSION])
colon := :
dot := .
solidus := /

mk-dependency = $(subst $(dot),$(solidus),$(1))/$(2)/$(3)/$(2)-$(3).jar

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

ifndef RESOLUTION_DIR
$(error The required variable RESOLUTION_DIR was not defined)
endif

## to-resolutions

mk-resolution = $(RESOLUTION_DIR)/$(1)

to-resolutions = $(foreach dep,$(1),$(call mk-resolution,$(dep)))

## to-jars

to-jars-paths = $(foreach res,$(call to-resolutions,$(1)),$(file < $(res)))

to-jars = $(sort $(foreach jar,$(call to-jars-paths,$(1)),$(LOCAL_REPO_PATH)/$(jar)))

#
# Gets the dependency from the remote repository
#

$(LOCAL_REPO_PATH)/%.jar:	
	$(REMOTE_REPO_CURLX) --output $@ $(@:$(LOCAL_REPO_PATH)/%.jar=$(REMOTE_REPO_URL)/%.jar)

#
# Dependencies related options & functions
#

define RESOLVER_SRC
/*
 * Copyright (C) 2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.AbstractRepositoryListener;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositoryEvent;
import org.eclipse.aether.RepositoryListener;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.supplier.RepositorySystemSupplier;
import org.eclipse.aether.util.artifact.JavaScopes;

public class Resolver {

  Path localRepositoryPath;

  Path resolutionPath;

  String dependency;

  Artifact requestedArtifact;

  Resolver() {}

  public static void main(String[] args) {
    try {
      Resolver resolver;
      resolver = new Resolver();

      resolver.parseArgs(args);

      resolver.resolve();
    } catch (Exception e) {
      e.printStackTrace();

      System.exit(1);
    }
  }

  final void parseArgs(String[] args) {
    int index;
    index = 0;

    int length;
    length = args.length;

    int requestedCount;
    requestedCount = 0;

    while (index < length) {
      String arg;
      arg = args[index++];

      switch (arg) {
        case "--local-repo" -> {
          if (index < length) {
            String name;
            name = args[index++];

            localRepositoryPath = Path.of(name);
          }
        }

        case "--resolution-dir" -> {
          if (index < length) {
            String name;
            name = args[index++];

            resolutionPath = Path.of(name);
          }
        }

        default -> {
          requestedCount++;

          dependency = arg;
        }
      }
    }

    List<String> errors;
    errors = new ArrayList<>();

    if (localRepositoryPath == null) {
      errors.add("[ERROR] missing required option --local-repo [dir]");
    }

    if (resolutionPath == null) {
      errors.add("[ERROR] missing required option --resolution-dir [dir]");
    }

    if (requestedCount == 0) {
      errors.add("[ERROR] missing required option groupId/artifactId/version");
    }

    if (requestedCount > 1) {
      errors.add("[ERROR] multiple artifacts requested. Resolving only one artifact is supported.");
    }

    if (!errors.isEmpty()) {
      String msg;
      msg = errors.stream().collect(Collectors.joining("\n"));

      throw new IllegalArgumentException(msg);
    }

    String gav;
    gav = dependency.replace('/', ':');

    requestedArtifact = new DefaultArtifact(gav);
  }

  final void resolve() throws DependencyResolutionException, IOException {
    // RepositorySystem

    RepositorySystem repositorySystem;
    repositorySystem = newRepositorySystem();

    // RepositorySystemSession

    RepositorySystemSession session;
    session = newRepositorySystemSession(repositorySystem);

    // CollectRequest

    CollectRequest collectRequest;
    collectRequest = new CollectRequest();

    List<Dependency> dependencies;
    dependencies = createDependencies();

    collectRequest.setDependencies(dependencies);

    RemoteRepository central;
    central = new RemoteRepository.Builder("central", "default", "https://repo.maven.apache.org/maven2/").build();

    List<RemoteRepository> repositories;
    repositories = List.of(central);

    collectRequest.setRepositories(repositories);

    // DependencyRequest

    DependencyRequest dependencyRequest;
    dependencyRequest = new DependencyRequest(collectRequest, null);

    DependencyResult dependencyResult;
    dependencyResult = repositorySystem.resolveDependencies(session, dependencyRequest);

    List<ArtifactResult> artifacts;
    artifacts = dependencyResult.getArtifactResults();

    String contents = artifacts.stream()
        .map(ArtifactResult::getArtifact)
        .map(Artifact::getFile)
        .map(File::toPath)
        .map(path -> localRepositoryPath.relativize(path))
        .map(Path::toString)
        .sorted()
        .collect(Collectors.joining("\n", "", "\n"));

    Path targetFile;
    targetFile = resolutionPath.resolve(dependency);

    Files.createDirectories(targetFile.getParent());

    Files.writeString(
        targetFile, contents, StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
    );
  }

  private RepositorySystem newRepositorySystem() {
    RepositorySystemSupplier repositorySystemSupplier;
    repositorySystemSupplier = new RepositorySystemSupplier();

    return repositorySystemSupplier.get();
  }

  private RepositorySystemSession newRepositorySystemSession(RepositorySystem repositorySystem) {
    DefaultRepositorySystemSession session;
    session = MavenRepositorySystemUtils.newSession();

    File localRepositoryFile;
    localRepositoryFile = localRepositoryPath.toFile();

    LocalRepository localRepository;
    localRepository = new LocalRepository(localRepositoryFile);

    LocalRepositoryManager localRepositoryManager;
    localRepositoryManager = repositorySystem.newLocalRepositoryManager(session, localRepository);

    session.setLocalRepositoryManager(localRepositoryManager);

    RepositoryListener repositoryListener;
    repositoryListener = new ThisRepositoryListener();

    session.setRepositoryListener(repositoryListener);

    return session;
  }

  private List<Dependency> createDependencies() {
    String scope;
    scope = JavaScopes.COMPILE;

    Dependency dependency;
    dependency = new Dependency(requestedArtifact, scope);

    return List.of(dependency);
  }

}

final class ThisRepositoryListener extends AbstractRepositoryListener {

  @Override
  public final void artifactDownloading(RepositoryEvent event) {
    Artifact artifact;
    artifact = event.getArtifact();

    log("Downloading", artifact);
  }

  private void log(String action, Artifact artifact) {
    System.out.println(action + " " + artifact);
  }

}
endef

## Resolver.java path
RESOLVER_JAVA = $(OBJECTOS_DIR)/Resolver.java

## Resolver.java deps
RESOLVER_DEPS  = commons-codec/commons-codec/1.16.0
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

## dep-to-jar
word-solidus = $(word $(2), $(subst $(solidus),$(space),$(1)))
mk-resolved-jar = $(call mk-dependency,$(call word-solidus,$(1),1),$(call word-solidus,$(1),2),$(call word-solidus,$(1),3))
dep-to-jar = $(foreach dep,$(1),$(LOCAL_REPO_PATH)/$(call mk-resolved-jar,$(dep)))

## Resolver.java jars
RESOLVER_DEPS_JARS = $(call dep-to-jar,$(RESOLVER_DEPS))

## resolve java command
RESOLVEX  = $(JAVA)
RESOLVEX += --class-path $(call class-path,$(RESOLVER_DEPS_JARS))
RESOLVEX += $(RESOLVER_JAVA)
RESOLVEX += --local-repo $(LOCAL_REPO_PATH)
RESOLVEX += --resolution-dir $(RESOLUTION_DIR)

#
# resolver rules
#

$(RESOLVER_JAVA): Makefile
	mkdir --parents $(@D)
	$(file > $@,$(RESOLVER_SRC))

#
# clean task
#

define CLEAN_TASK

## work dir
$(1)WORK = $$($(1)MODULE)/work

## targets

.PHONY: $(2)clean
$(2)clean:
ifneq ($$($(1)WORK),)
	rm -rf $$($(1)WORK)/*
else
	@echo "Cannot clean: $(1)WORK was not defined!"
endif
	
endef

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

## class output path
$(1)CLASS_OUTPUT = $$($(1)WORK)/main

## compiled classes
$(1)CLASSES = $$($(1)SOURCES:$$($(1)MAIN)/%.java=$$($(1)CLASS_OUTPUT)/%.class)

## compile-time dependencies
# $(1)COMPILE_DEPS = 

## compile-time required resolutions
$(1)COMPILE_RESOLUTIONS = $$(call to-resolutions,$$($(1)COMPILE_DEPS))

## compile-time required jars
$(1)COMPILE_JARS = $$(call to-jars,$$($(1)COMPILE_DEPS))

## compile-time module-path
$(1)COMPILE_MODULE_PATH = $$(call module-path,$$($(1)COMPILE_JARS))
 
## javac command
$(1)JAVACX = $$(JAVAC)
$(1)JAVACX += -d $$($(1)CLASS_OUTPUT)
$(1)JAVACX += -g
$(1)JAVACX += -Xlint:all
$(1)JAVACX += -Xpkginfo:always
ifeq ($$($(1)ENABLE_PREVIEW),1)
$(1)JAVACX += --enable-preview
endif
ifneq ($$($(1)COMPILE_DEPS),)
$(1)JAVACX += --module-path $$($(1)COMPILE_MODULE_PATH)
endif
$(1)JAVACX += --module-version $$($(1)VERSION)
$(1)JAVACX += --release $$($(1)JAVA_RELEASE)
$(1)JAVACX += $$($(1)DIRTY)

## resources
# $(1)RESOURCES =

## compilation marker
$(1)COMPILE_MARKER = $$($(1)WORK)/compile-marker

## compilation requirements
$(1)COMPILE_REQS  = $$($(1)COMPILE_RESOLUTIONS)
$(1)COMPILE_REQS += $$($(1)CLASSES)
ifdef $(1)RESOURCES
$(1)COMPILE_REQS += $$($(1)RESOURCES)
endif
ifdef $(1)COMPILE_REQS_MORE
$(1)COMPILE_REQS += $$($(1)COMPILE_REQS_MORE)
endif

#
# compilation deps generation
#

## resolution cache file
$(1)RESOLUTION = $$(RESOLUTION_DIR)/$$($(1)GROUP_ID)/$$($(1)ARTIFACT_ID)/$$($(1)VERSION)

## resolution cache file reqs
ifndef $(1)RESOLUTION_REQS
$(1)RESOLUTION_REQS = $$($(1)MODULE).mk
endif

$(1)RESOLUTION_DEPS  = $$($(1)GROUP_ID)/$$($(1)ARTIFACT_ID)/$$($(1)VERSION)
$(1)RESOLUTION_DEPS += $$($(1)COMPILE_DEPS)

$(1)RESOLUTION_JARS  = $$(call mk-dependency,$$($(1)GROUP_ID),$$($(1)ARTIFACT_ID),$$($(1)VERSION))
$(1)RESOLUTION_JARS += $$(call to-jars-paths,$$($(1)COMPILE_DEPS))

$$($(1)RESOLUTION): $$($(1)RESOLUTION_REQS)
	mkdir --parents $$(@D)
	echo "$$(sort $$($(1)RESOLUTION_JARS))" | $(TR) ' ' '\n' > $$($(1)RESOLUTION)

#
# compilation targets
#

.PHONY: $(2)compile
$(2)compile: $$($(1)COMPILE_MARKER)

.PHONY: $(2)compile-jars
$(2)compile-jars: $$($(1)COMPILE_JARS) $$($(1)RESOLUTION)

$$($(1)COMPILE_MARKER): $$($(1)COMPILE_REQS)
	if [ -n "$$($(1)DIRTY)" ]; then \
		$(MAKE) $(2)compile-jars; \
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
$(1)JAR_FILE = $$($(1)WORK)/$$($(1)ARTIFACT_ID)-$$($(1)VERSION).jar

## jar command
$(1)JARX = $$(JAR)
$(1)JARX += --create
$(1)JARX += --file $$($(1)JAR_FILE)
$(1)JARX += --module-version $$($(1)VERSION)
$(1)JARX += -C $$($(1)CLASS_OUTPUT)
$(1)JARX += .

## requirements of the $(1)JAR_FILE target
$(1)JAR_FILE_REQS  = $$($(1)COMPILE_MARKER)
$(1)JAR_FILE_REQS += $$($(1)LICENSE)
ifdef $(1)JAR_FILE_REQS_MORE
$(1)JAR_FILE_REQS += $$($(1)JAR_FILE_REQS_MORE)
endif

#
# jar targets
#

.PHONY: $(2)jar
$(2)jar: $$($(1)JAR_FILE)

$$($(1)JAR_FILE): $$($(1)JAR_FILE_REQS)
	$$($(1)JARX)

$$($(1)LICENSE): LICENSE
	mkdir --parents $$(@D)
	cp LICENSE $$(@D)

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

.PHONY: $(2)install
$(2)install: $$($(1)INSTALL)

.PHONY: $(2)clean-install
$(2)clean-install:
	rm -f $$($(1)INSTALL)

$$($(1)INSTALL): $$($(1)JAR_FILE)
	mkdir --parents $$(@D)
	cp $$< $$@
	
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

## test compile-time required resolutions
$(1)TEST_COMPILE_RESOLUTIONS = $$(call to-resolutions,$$($(1)TEST_COMPILE_DEPS))

## test compile-time required jars
$(1)TEST_COMPILE_JARS = $$(call to-jars,$$($(1)TEST_COMPILE_DEPS))

## test compile-time class-path
$(1)TEST_COMPILE_CLASS_PATH = $$(call class-path,$$($(1)TEST_COMPILE_JARS))

## test javac command
$(1)TEST_JAVACX = $$(JAVAC)
$(1)TEST_JAVACX += -d $$($(1)TEST_CLASS_OUTPUT)
$(1)TEST_JAVACX += -g
$(1)TEST_JAVACX += -Xlint:all
$(1)TEST_JAVACX += --class-path $$($(1)TEST_COMPILE_CLASS_PATH)
ifeq ($$($(1)ENABLE_PREVIEW),1)
$(1)TEST_JAVACX += --enable-preview
endif
$(1)TEST_JAVACX += --release $$($(1)JAVA_RELEASE)
$(1)TEST_JAVACX += $$($(1)TEST_DIRTY)

## test resources directory
# $(1)TEST_RESOURCES = $$($(1)MODULE)/test-resources

ifdef $(1)TEST_RESOURCES
## test resources "source"
$(1)TEST_RESOURCES_SRC = $$(shell find $${$(1)TEST_RESOURCES} -type f -print)

## test resources "output"
$(1)TEST_RESOURCES_OUT = $$($(1)TEST_RESOURCES_SRC:$$($(1)TEST_RESOURCES)/%=$$($(1)TEST_CLASS_OUTPUT)/%)

## target to copy test resources
$$($(1)TEST_RESOURCES_OUT): $$($(1)TEST_CLASS_OUTPUT)/%: $$($(1)TEST_RESOURCES)/%
	mkdir --parents $$(@D)
	cp $$< $$@
endif

## test compilation marker
$(1)TEST_COMPILE_MARKER = $$($(1)WORK)/test-compile-marker

## test compilation requirements
$(1)TEST_COMPILE_REQS  = $$($(1)TEST_COMPILE_RESOLUTIONS)
$(1)TEST_COMPILE_REQS += $$($(1)TEST_CLASSES)
$(1)TEST_COMPILE_REQS += $$($(1)TEST_RESOURCES_OUT)

#
# test compilation targets
#

.PHONY: $(2)test-compile
$(2)test-compile: $$($(1)TEST_COMPILE_MARKER)

.PHONY: $(2)test-compile-jars
$(2)test-compile-jars: $$($(1)TEST_COMPILE_JARS)

$$($(1)TEST_COMPILE_MARKER): $$($(1)TEST_COMPILE_REQS) 
	if [ -n "$$($(1)TEST_DIRTY)" ]; then \
		$(MAKE) $(2)test-compile-jars; \
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

## test runtime required resolutions
$(1)TEST_RUNTIME_RESOLUTIONS = $$(call to-resolutions,$$($(1)TEST_RUNTIME_DEPS))

## test runtime required jars
$(1)TEST_RUNTIME_JARS = $$(call to-jars,$$($(1)TEST_RUNTIME_DEPS))

## test runtime module-path
$(1)TEST_RUNTIME_MODULE_PATH = $$(call module-path,$$($(1)TEST_RUNTIME_JARS))

## test main class
ifndef $(1)TEST_MAIN
$(1)TEST_MAIN = $$($(1)MODULE).RunTests
endif

## test runtime output path
$(1)TEST_RUNTIME_OUTPUT = $$($(1)WORK)/test-output

## test system properties
#$(1)TEST_RUNTIME_SYSPROPS

## test java command
$(1)TEST_JAVAX  = $$(JAVA)
ifdef $(1)TEST_RUNTIME_SYSPROPS
$(1)TEST_JAVAX += $$(foreach v,$$($(1)TEST_RUNTIME_SYSPROPS),-D$$(v))
endif
$(1)TEST_JAVAX += --module-path $$($(1)TEST_RUNTIME_MODULE_PATH)
ifdef $(1)TEST_JAVAX_MODULES
$(1)TEST_JAVAX += $$(foreach mod,$$($(1)TEST_JAVAX_MODULES),--add-modules $$(mod))
else
$(1)TEST_JAVAX += --add-modules org.testng
endif
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

## test execution requirements
$(1)TEST_RUNTIME_REQS  = $$($(1)TEST_RUNTIME_RESOLUTIONS)
$(1)TEST_RUNTIME_REQS += $$($(1)TEST_COMPILE_MARKER)

#
# test execution targets
#

.PHONY: $(2)test
$(2)test: $$($(1)TEST_RUN_MARKER)

.PHONY: $(2)test-runtime-jars
$(2)test-runtime-jars: $$($(1)TEST_RUNTIME_JARS)

$$($(1)TEST_RUN_MARKER): $$($(1)TEST_RUNTIME_REQS)
	$$(MAKE) $(2)test-runtime-jars 
	$$($(1)TEST_JAVAX)

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

define POM_DEPENDENCY
		<dependency>
			<groupId>$(1)</groupId>
			<artifactId>$(2)</artifactId>
			<version>$(3)</version>
		</dependency>

endef

mk-pom-dep = $(call POM_DEPENDENCY,$(call word-solidus,$(1),1),$(call word-solidus,$(1),2),$(call word-solidus,$(1),3))

define POM_TASK

## pom source
ifndef mk-pom
$$(error The required mk-pom function was not defined)
endif

## pom file
$(1)POM_FILE = $$($(1)WORK)/$$($(1)ARTIFACT_ID)-$$($(1)VERSION).pom

## deps
$(1)POM_DEPENDENCIES = $$(foreach dep,$$($(1)COMPILE_DEPS),$$(call mk-pom-dep,$$(dep)))

## contents
$(1)POM_CONTENTS = $$(call mk-pom,$(1))

#
# Targets
#

.PHONY: $(2)pom
$(2)pom: $$($(1)POM_FILE)

$$($(1)POM_FILE): Makefile
	$$(file > $$@,$$($(1)POM_CONTENTS))

endef

#
# install task
#

define INSTALL_POM_TASK

## pom install location
$(1)INSTALL_POM = $$(basename $$($(1)INSTALL)).pom

#
# install target
#

.PHONY: $(2)install-pom
$(2)install-pom: $$($(1)INSTALL_POM)

.PHONY: $(2)clean-install-pom
$(2)clean-install-pom:
	rm -f $$($(1)INSTALL_POM)

$$($(1)INSTALL_POM): $$($(1)POM_FILE)
	mkdir --parents $$(@D)
	cp $$< $$@
	
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

define OSSRH_BUNDLE_TASK

## ossrh bundle jar file
$(1)OSSRH_BUNDLE = $$($(1)WORK)/$$($(1)ARTIFACT_ID)-$$($(1)VERSION)-bundle.jar

## ossrh bundle contents
#$(1)OSSRH_BUNDLE_CONTENTS =

## ossrh bundle jar command
$(1)OSSRH_JARX = $$(JAR)
$(1)OSSRH_JARX += --create
$(1)OSSRH_JARX += --file $$($(1)OSSRH_BUNDLE)
$(1)OSSRH_JARX += $$(foreach file,$$($(1)OSSRH_BUNDLE_CONTENTS), -C $$(dir $$(file)) $$(notdir $$(file)))

#
# ossrh bundle targets
#

$$($(1)OSSRH_BUNDLE): $$($(1)OSSRH_BUNDLE_CONTENTS)
	$$($(1)OSSRH_JARX)

endef

## include GH config
## - GH_TOKEN
-include $(HOME)/.config/objectos/gh-config.mk

#
# Dependency mgmt
#

.PHONY: resolve-external-deps
resolve-external-deps: export deps = $(EXTERNAL_DEPS) 
resolve-external-deps: $(RESOLVER_JAVA) $(RESOLVER_DEPS_JARS)
	for dep in $${deps}; do $(RESOLVEX) $${dep}; done

#
# objectos.way modules section
#

## all of the modules
MODULES := objectos.lang.object
MODULES += objectos.notes
MODULES += objectos.notes.base
MODULES += objectos.notes.console
MODULES += objectos.notes.file
MODULES += objectos.util.array
MODULES += objectos.util.collection
MODULES += objectos.util.list
MODULES += objectos.util.set
MODULES += objectos.util.map
MODULES += objectos.core.io
MODULES += objectos.fs
MODULES += objectos.code
MODULES += objectos.selfgen
MODULES += objectos.html.tmpl
MODULES += objectos.html
MODULES += objectos.html.icon
MODULES += objectos.html.script
MODULES += objectos.html.style
MODULES += objectos.css
MODULES += objectos.http
MODULES += objectos.http.server
MODULES += objectos.lang.classloader
MODULES += objectos.lang.runtime

## common module tasks
MODULE_TASKS  = CLEAN_TASK
MODULE_TASKS += COMPILE_TASK
MODULE_TASKS += JAR_TASK
MODULE_TASKS += TEST_COMPILE_TASK
MODULE_TASKS += TEST_RUN_TASK
MODULE_TASKS += INSTALL_TASK
MODULE_TASKS += SOURCE_JAR_TASK
MODULE_TASKS += JAVADOC_TASK
MODULE_TASKS += POM_TASK
MODULE_TASKS += INSTALL_POM_TASK
MODULE_TASKS += OSSRH_PREPARE_TASK

## test-related tasks
TEST_TASKS  = TEST_COMPILE_TASK
TEST_TASKS += TEST_RUN_TASK

## @ names
AT_MODULES := $(foreach mod,$(MODULES),$(subst objectos.,,$(mod)))
AT_MODULES += way

## generate module gav
module-gav = $(GROUP_ID)/$(1)/$(VERSION)

## generate common module values
LOWER_MODULES := $(foreach mod,$(AT_MODULES),$(subst .,_,$(mod)_))
UPPER_MODULES := $(shell echo $(LOWER_MODULES) | tr a-z A-Z)

$(foreach mod,$(UPPER_MODULES),$(eval $(mod)POM_SOURCE := pom.xml.tmpl))

## include each modules's makefile

include $(foreach mod,$(MODULES),$(mod).mk)

#
# objectos.way options
# 

## way directory
WAY := objectos.way

## way module
WAY_MODULE := $(WAY)

## way module version
WAY_GROUP_ID = $(GROUP_ID)
WAY_ARTIFACT_ID = $(ARTIFACT_ID)
WAY_VERSION = $(VERSION)

## way javac --release option
WAY_JAVA_RELEASE := 21

## way --enable-preview ?
WAY_ENABLE_PREVIEW := 0

## way compile deps
WAY_COMPILE_DEPS  = $(call module-gav,$(CSS))
WAY_COMPILE_DEPS += $(call module-gav,$(HTML))
WAY_COMPILE_DEPS += $(call module-gav,$(NOTES))
WAY_COMPILE_DEPS += $(call module-gav,$(UTIL_LIST))
WAY_COMPILE_DEPS += $(call module-gav,$(UTIL_SET))

## way resolution reqs
WAY_RESOLUTION_REQS = Makefile

## way test compile-time dependencies
WAY_TEST_COMPILE_DEPS  = $(WAY_COMPILE_DEPS)
WAY_TEST_COMPILE_DEPS += $(call module-gav,$(WAY))
WAY_TEST_COMPILE_DEPS += $(call module-gav,$(NOTES_CONSOLE))
WAY_TEST_COMPILE_DEPS += $(TESTNG)

## way test runtime dependencies
WAY_TEST_RUNTIME_DEPS  = $(WAY_TEST_COMPILE_DEPS)
WAY_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## way test runtime modules
WAY_TEST_JAVAX_MODULES = org.testng
WAY_TEST_JAVAX_MODULES += objectos.notes.console

## way test runtime reads
WAY_TEST_JAVAX_READS = java.compiler
WAY_TEST_JAVAX_READS += objectos.notes.console

## way test runtime exports
WAY_TEST_JAVAX_EXPORTS = objectox.css
WAY_TEST_JAVAX_EXPORTS += objectox.css.util
WAY_TEST_JAVAX_EXPORTS += objectox.http
WAY_TEST_JAVAX_EXPORTS += objectox.lang

## way copyright years for javadoc
WAY_COPYRIGHT_YEARS := 2022-2023

## way javadoc snippet path
WAY_JAVADOC_SNIPPET_PATH := WAY_TEST

## way sub modules
WAY_SUBMODULES = lang.object
WAY_SUBMODULES += notes
WAY_SUBMODULES += notes.base
WAY_SUBMODULES += notes.console
WAY_SUBMODULES += notes.file
WAY_SUBMODULES += util.array
WAY_SUBMODULES += util.collection
WAY_SUBMODULES += util.list
WAY_SUBMODULES += util.set
WAY_SUBMODULES += util.map
WAY_SUBMODULES += html.tmpl
WAY_SUBMODULES += html
WAY_SUBMODULES += html.icon
WAY_SUBMODULES += html.script
WAY_SUBMODULES += html.style
WAY_SUBMODULES += css
WAY_SUBMODULES += http
WAY_SUBMODULES += http.server
WAY_SUBMODULES += lang.classloader
WAY_SUBMODULES += lang.runtime

## way bundle contents
WAY_OSSRH_BUNDLE_CONTENTS = $(LANG_OBJECT_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(NOTES_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(NOTES_BASE_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(NOTES_CONSOLE_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(NOTES_FILE_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(UTIL_ARRAY_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(UTIL_COLLECTION_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(UTIL_LIST_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(UTIL_SET_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(UTIL_MAP_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTML_TMPL_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTML_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTML_ICON_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTML_SCRIPT_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTML_STYLE_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(CSS_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTTP_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTTP_SERVER_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(LANG_CLASSLOADER_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(LANG_RUNTIME_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(WAY_OSSRH_PREPARE)

#
# objectos.way targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),WAY_,way@)))
$(eval $(call OSSRH_BUNDLE_TASK,WAY_))

#
# Targets section
#

.PHONY: clean
clean: $(foreach mod,$(AT_MODULES),$(mod)@clean)

.PHONY: clean-install
clean-install: $(foreach mod,$(AT_MODULES),$(foreach t,clean-install clean-install-pom,$(mod)@$(t)))

.PHONY: compile
compile: $(foreach mod,$(AT_MODULES),$(mod)@compile)

.PHONY: jar
jar: $(foreach mod,$(AT_MODULES),$(mod)@jar)

.PHONY: test-compile
test-compile: $(foreach mod,$(AT_MODULES),$(mod)@test-compile)

.PHONY: test
test: $(foreach mod,$(AT_MODULES),$(mod)@test)

.PHONY: install
install: $(foreach mod,$(AT_MODULES),$(foreach t,install install-pom,$(mod)@$(t)))

.PHONY: source-jar
source-jar: $(foreach mod,$(WAY_SUBMODULES),$(mod)@source-jar) way@source-jar 

.PHONY: javadoc
javadoc: $(foreach mod,$(WAY_SUBMODULES),$(mod)@javadoc) way@javadoc 

.PHONY: pom
pom: $(foreach mod,$(AT_MODULES),$(mod)@pom) 

.PHONY: ossrh-prepare
ossrh-prepare: $(foreach mod,$(WAY_SUBMODULES),$(mod)@ossrh-prepare) way@ossrh-prepare

.PHONY: ossrh-bundle
ossrh-bundle: way@ossrh-bundle

.PHONY: ossrh
ossrh: way@ossrh

.PHONY: gh-release
gh-release: way@gh-release

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
