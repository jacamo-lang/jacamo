USER=jomifred

asciidoctor -r ../../../src/main/resources/pygments_init.rb readme.adoc
cp readme.html index.html
cp ./code/helloworld/helloworld.jcm ./code/helloworld/helloworld.jcm.txt

asciidoctor shell-based.adoc

scp -r *.html $USER,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world

#scp -r *.css $USER,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
#scp -r screens $USER,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
#scp -r solutions $USER,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
scp -r code $USER,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
