#!/bin/sh

# creates a new application using SNAPSHOT version of jacamo

curl http://jacamo.sourceforge.net/nps/npss.gradle -s -o np.gradle \
  && gradle -b np.gradle --console=plain -Dexec.args=$*

rm np.gradle
