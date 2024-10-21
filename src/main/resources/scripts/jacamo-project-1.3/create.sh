# create JaCaMo application

curl -LO http://jacamo.sourceforge.net/nps/np1.3.zip
unzip -o np1.3.zip
unset JACAMO_HOME
./gradlew --console=plain
