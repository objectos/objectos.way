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

## requirements
## - OSSRH_USERNAME
## - OSSRH_PASSWORD

ifndef JAR_FILE
$(error Required java-jar.mk was not included)
endif

ifndef POM_FILE
$(error Required java-pom.mk was not included)
endif

# the OSSRH server to use
ifndef OSSRH_SERVER
OSSRH_SERVER := https://s01.oss.sonatype.org
endif

# prefix
OSSRH_SNAPSHOTS_SLUG := $(OSSRH_SERVER)/content/repositories/snapshots/$(call mk-dependency,$(GROUP_ID),$(ARTIFACT_ID),$(VERSION),)

.PHONY: ossrh-snapshots
ossrh-snapshots: $(JAR_FILE) $(POM_FILE)
	@curl -u $(OSSRH_USERNAME):$(OSSRH_PASSWORD) -T $(JAR_FILE) $(OSSRH_SNAPSHOTS_SLUG)jar
	@curl -u $(OSSRH_USERNAME):$(OSSRH_PASSWORD) -T $(POM_FILE) $(OSSRH_SNAPSHOTS_SLUG)pom
