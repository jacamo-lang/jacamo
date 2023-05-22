#scp np* $USERSF,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/nps

rm *.zip

cd jacamo-project-1.2
rm -rf .gradle
zip -r ../np1.2.zip *
cd ..

cd jacamo-project-ss
rm -rf .gradle
zip -r ../npss.zip *
cd ..

#git checkout site
#scp *.zip $USERSF,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/nps
cp *.zip ../../../../doc/nps

#git commit -m "new project script" ../../../../docs/nps/np1.1.zip

git add ../../../../doc/nps/npss.zip
git commit -m "new project script" ../../../../doc/nps/npss.zip
git push

