# check user at sourceforge
if [ -z $USERSF ] ; then
    echo the var USERSF must be set with the username at source forge
    exit
fi

DEST=/home/project-web/jacamo/htdocs/tutorial/coordination
find . -name .DS_Store -exec rm {} \;

asciidoctor -r ../../../src/main/resources/pygments_init.rb readme.adoc
mv readme.html index.html

scp -r *.html $USERSF,jacamo@web.sf.net:$DEST
#scp -r *.org $USERSF,jacamo@web.sf.net:$DEST

#scp -r ../jcm.css $USERSF,jacamo@web.sf.net:$DEST/..
#scp -r screens $USERSF,jacamo@web.sf.net:$DEST
#scp -r solutions $USERSF,jacamo@web.sf.net:$DEST
#scp -r code $USERSF,jacamo@web.sf.net:$DEST
