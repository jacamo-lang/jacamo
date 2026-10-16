# Publishing in the JaCaMo Maven Repository

The repository is managed at https://github.com/jacamo-lang/mvn-repo and used, in gradle, as

    repositories {
       maven { url "https://raw.githubusercontent.com/jacamo-lang/mvn-repo/master" }
    }

To publish, do the following once:

    cd parent_directory_of_your_project
    git clone https://github.com/jacamo-lang/mvn-repo.git

So that `../mvn-repo` is a valid path from your project directory.

Then, every time you want to publish a maven package:

    ./gradlew publishMavenGitHub

`publishMavenGitHub` is a task available in many JaCaMo projects (Moise, NPL, Jason, ...), see the file `build.gradle` of these projects for more details.    