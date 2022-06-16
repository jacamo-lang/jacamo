#
# by Jomi
#

cd ..
./gradlew renderAsciidoc
./gradlew javadoc
cd doc
cp readme.html index.html
scp -r *  $USERSF,jacamo@web.sf.net:/home/groups/j/ja/jacamo/htdocs/doc
