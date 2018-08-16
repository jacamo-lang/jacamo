#!/bin/sh

curl http://jacamo.sourceforge.net/lv/np.gradle -s -o np.gradle \
  && gradle -b np.gradle --console=plain

rm np.gradle
