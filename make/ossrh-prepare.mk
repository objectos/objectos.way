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
