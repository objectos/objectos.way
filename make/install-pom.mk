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
