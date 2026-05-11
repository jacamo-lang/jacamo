find . -name .DS_Store -exec rm {} \;

# clean code and zip
cd code
find . -name build -exec rm -rf {} \;
find . -name .gradle -exec rm -rf {} \;
rm *.zip
zip -r auction_ag.zip auction_ag
zip -r auction_env.zip auction_env
zip -r auction_org.zip auction_org
cd ..

IMAGE=jomifred/adoc
docker run --rm -i --user="$(id -u):$(id -g)" --net=none -v "$PWD":/app "$IMAGE" asciidoctor -r /pygments_init.rb readme.adoc

mv readme.html index.html

# check user at sourceforge
#if [ -z $USERSF ] ; then
#    echo the var USERSF must be set with your username at source forge
#    exit
#fi

#DEST=/home/project-web/jacamo/htdocs/tutorial/coordination

#scp -r *.html $USERSF,jacamo@web.sf.net:$DEST
#scp -r *.org $USERSF,jacamo@web.sf.net:$DEST

#scp -r ../jcm.css $USERSF,jacamo@web.sf.net:$DEST/..
#scp -r screens $USERSF,jacamo@web.sf.net:$DEST
#scp -r solutions $USERSF,jacamo@web.sf.net:$DEST
#scp -r code $USERSF,jacamo@web.sf.net:$DEST
#scp code/es.png $USERSF,jacamo@web.sf.net:$DEST/code
