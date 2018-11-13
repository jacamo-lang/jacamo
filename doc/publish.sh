#
# by Jomi
#

cd ..
gradle renderAsciidoc
gradle javadoc
cd doc
cp readme.html index.html
scp -r *  jomifred,jacamo@web.sf.net:/home/groups/j/ja/jacamo/htdocs/doc
