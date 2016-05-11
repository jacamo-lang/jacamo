USER=jomifred

asciidoctor readme.adoc
cp readme.html index.html

asciidoctor shell-based.adoc

scp -r *.html $USER,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world

#scp -r *.css $USER,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
#scp -r screens $USER,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
#scp -r solutions $USER,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
#scp -r code $USER,jacamo@web.sf.net:/home/project-web/jacamo/htdocs/tutorial/hello-world
