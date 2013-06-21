#!/bin/sh

###
# #%L
# ARC Header Extractor
# %%
# Copyright (C) 2013 State and University Library, Denmark
# %%
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# #L%
###
# If JAVA_HOME is not set, use the java in the execution path
if [ ${JAVA_HOME} ] ; then
  JAVA="$JAVA_HOME/bin/java"
else
  JAVA=java
fi

# home directory of install.
PRG="$0"

# need this for relative symlinks
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
      PRG="$link"
    else
      PRG="`dirname "$PRG"`/$link"
    fi
done

INSTALL_HOME=`dirname "$PRG"`

# make it fully qualified
INSTALL_HOME=`cd "$INSTALL_HOME" && pwd`

# CP must contain a colon-separated list of resources used.
CP=$INSTALL_HOME/
for i in `ls ${INSTALL_HOME}/lib/*.jar`
do
  CP=${CP}:${i}
done
#echo $CP
