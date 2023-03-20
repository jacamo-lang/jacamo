# create JaCaMo application

curl -LO http://jacamo.sourceforge.net/nps/np1.1.zip
unzip -o np1.1.zip
unset JACAMO_HOME
./gradlew --console=plain
