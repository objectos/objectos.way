#
# Copyright (C) 2025 Objectos Software LTDA.
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
# way@mysql7 (by mysql7 we mean mysql-5.7
#

## base dir
MYSQL7_BASEDIR = /opt/mysql-5.7

## mysql-config-editor command
MYSQL7_CONFIG_ED := $(MYSQL7_BASEDIR)/bin/mysql_config_editor

## mysql dir
MYSQL7 := $(abspath $(WORK)/mysql-5.7)

## mysql port
MYSQL7_PORT := 17003

## mysqld cnf file
MYSQL7_SERVER_CNF := $(MYSQL7)/conf/server.cnf 

## mysqld command
MYSQL7_SERVER := $(MYSQL7_BASEDIR)/bin/mysqld
MYSQL7_SERVER += --defaults-file=$(MYSQL7_SERVER_CNF)

## mysqld log files
MYSQL7_SERVER_GENERAL_LOG := $(MYSQL7)/mysqld-general.log
MYSQL7_SERVER_ERROR_LOG := $(MYSQL7)/mysqld-error.log
MYSQL7_SERVER_SLOW_QUERY_LOG := $(MYSQL7)/mysqld-slow-query.log

## mysqld socket file
MYSQL7_SERVER_SOCKET := $(MYSQL7)/mysqld.sock

## mysqld pid file
MYSQL7_SERVER_PID := $(abspath $(WORK)/mysql-5.7.pid)

## mysql client cnf file
MYSQL7_CLIENT_CNF := $(MYSQL7)/conf/client.cnf

## mysql client command
MYSQL7_CLIENT := $(MYSQL7_BASEDIR)/bin/mysql
MYSQL7_CLIENT += --defaults-file=$(MYSQL7_CLIENT_CNF)

## mysql login cnf file
MYSQL7_LOGIN_CNF := $(MYSQL7)/conf/login.cnf

## mysql resources dir
MYSQL7_RESOURCES := test-mysql

## mysql server.cnf contents
define MYSQL7_SERVER_CNF_CONTENTS
[mysqld]
basedir=$(MYSQL7_BASEDIR)
bind-address=127.0.0.1
binlog-format=ROW
character-set-server=utf8mb4
datadir=$(MYSQL7)/data
disabled-storage-engines=ARCHIVE,BLACKHOLE,CSV,EXAMPLE,FEDERATED,MEMORY,MERGE,MRG_MYISAM,PERFORMANCE_SCHEMA
explicit-defaults-for-timestamp=ON
general-log=OFF
general-log-file=$(MYSQL7_SERVER_GENERAL_LOG)
innodb-buffer-pool-chunk-size=96MB
innodb-buffer-pool-size=96MB
log-bin=$(MYSQL7)/binlog
log-error=$(MYSQL7_SERVER_ERROR_LOG)
max-binlog-size=128MB
max-connections=30
read-buffer-size=256KB
read-rnd-buffer-size=512KB
performance-schema=OFF
pid-file=$(MYSQL7_SERVER_PID)
port=$(MYSQL7_PORT)
secure-file-priv=$(MYSQL7)/tmp
server-id=1
skip-ssl
slow-query-log-file=$(MYSQL7_SERVER_SLOW_QUERY_LOG)
socket=$(MYSQL7_SERVER_SOCKET)
sql-mode=NO_ENGINE_SUBSTITUTION
sync-binlog=1
tmpdir=$(MYSQL7)/tmp
endef

#
# v001: Creates directory hierarchy
#

$(MYSQL7_SERVER_PID): $(MYSQL7)/v001
$(MYSQL7)/v001:
	mkdir --parents $(MYSQL7)
	mkdir $(MYSQL7)/conf
	mkdir $(MYSQL7)/tmp 
	touch $@

#
# v002: create server.cnf file
#

$(MYSQL7_SERVER_PID): $(MYSQL7)/v002
$(MYSQL7)/v002: export CONTENTS = $(MYSQL7_SERVER_CNF_CONTENTS)
	@echo "$$CONTENTS" > $(MYSQL7_SERVER_CNF)
	touch $@

#
# v003: Initialize data dir
#

$(MYSQL7_SERVER_PID): $(MYSQL7)/v003
$(MYSQL7)/v003:
	$(MYSQL7_SERVER) --initialize-insecure --skip-log-bin
	cat $(MYSQL7_SERVER_ERROR_LOG)
	touch $@

#
# MySQL Targets
#

.PHONY: mysql7-start
mysql7-start: $(MYSQL7_SERVER_PID)

$(MYSQL7_SERVER_PID):
	$(MYSQL7_SERVER) & echo "$$!" > $@

.PHONY: mysql7-clean
mysql7-clean:
	rm -rf $(MYSQL7)

.PHONY: mysql7-stop
mysql7-stop:
	kill `cat $(MYSQL7_SERVER_PID)`
	rm $(MYSQL7_SERVER_PID)

.PHONY: mysql7-zap
mysql7-zap:
	rm -f $(MYSQL7_SERVER_PID)
