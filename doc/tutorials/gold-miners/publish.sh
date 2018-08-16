asciidoctor readme.adoc
mv readme.html index.html

rm -rf initial-gold-miners/bin
rm -rf initial-gold-miners/log/*
#rm initial-gold-miners.zip
#zip -r initial-gold-miners.zip initial-gold-miners

#scp -r *.zip jomifred,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/gold-miners

scp -r *.html $USERSF,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/gold-miners

#scp -r ../jcm.css jomifred,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial
#scp -r screens jomifred,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/gold-miners
#scp -r solutions jomifred,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/gold-miners
