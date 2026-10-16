IMAGE=jomifred/adoc
docker run --rm -i --user="$(id -u):$(id -g)" --net=none -v "$PWD":/app "$IMAGE" asciidoctor -r /pygments_init.rb readme.adoc
docker run --rm -i --user="$(id -u):$(id -g)" --net=none -v "$PWD":/app "$IMAGE" asciidoctor -r /pygments_init.rb shell-based.adoc

cp readme.html index.html
cp ./code/helloworld/helloworld.jcm ./code/helloworld/helloworld.jcm.txt

#asciidoctor shell-based.adoc

scp -r *.html $USERSF,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world

#scp -r *.css $USERSF,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
#scp -r screens $USERSF,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
#scp -r solutions $USERSF,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
scp -r code $USERSF,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
