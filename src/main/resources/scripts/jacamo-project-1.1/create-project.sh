#!/bin/bash
# 
# create JaCaMo application

curl -LO http://jacamo.sourceforge.net/nps/np1.1.zip
unzip np1.1.zip
unset JACAMO_HOME
./gradlew --console=plain
