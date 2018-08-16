#!/bin/sh

# creates a new project using SNAPSHOT version of jacamo

curl http://jacamo.sourceforge.net/nps/np07.gradle -s -o np.gradle \
  && gradle -b np.gradle --console=plain

rm np.gradle
