#!/bin/sh

# creates a new project using version 0.7 of jacamo

curl http://jacamo.sourceforge.net/nps/np07.gradle -s -o np.gradle \
  && gradle -b np.gradle --console=plain

rm np.gradle
