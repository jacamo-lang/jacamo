#scp np* $USERSF,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/nps

rm *.zip

cd jacamo-project-1.0
rm -rf .gradle
zip -r ../np1.0.zip *
cd ..

cd jacamo-project-ss
rm -rf .gradle
zip -r ../npss.zip *
cd ..

scp *.zip $USERSF,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/nps
