USER=jomifred
DEST=/home/project-web/jacamo/htdocs/tutorial/coordination
find . -name .DS_Store -exec rm {} \;

asciidoctor readme.adoc
mv readme.html index.html

scp -r *.html $USER,jacamo@web.sf.net:$DEST
#scp -r *.org $USER,jacamo@web.sf.net:$DEST

#scp -r ../jcm.css $USER,jacamo@web.sf.net:$DEST/..
#scp -r screens $USER,jacamo@web.sf.net:$DEST
#scp -r solutions $USER,jacamo@web.sf.net:$DEST
#scp -r code $USER,jacamo@web.sf.net:$DEST
