# Extending a JaCaMo component

This document contains some (initial) tips for someone aiming at developing some part of JaCaMo.

For example in this document, we consider a developer who wants to extend Moise with a new feature and test it in a JaCaMo application. The following steps can be used for that.

## 1. Moise Clone

```
git clone https://github.com/moise-lang/moise.git
```

## 2. JaCaMo test application

In order to test the new Moise feature, we will create a JaCaMo application:

```
curl -LO http://jacamo.sourceforge.net/nps/npss.zip
unzip npss.zip
./gradlew
```

* I am using JaCaMo SNAPSHOT version here
* The name of the application is `app-t`

Output:
```
> Task :run
JaCaMo 1.2-SNAPSHOT built on Fri Jan 20 13:51:24 CET 2023

Enter the identification of the new application: app-t

...
```

## 3. Application organisation

Edit the application project file so that an organisation is created. For instance, the content of `app-t/app-t.jcm` could be:

```
mas app_t {

    agent bob: sample_agent.asl

    organisation o : org.xml {
       group g : group1 {
         players: bob role1
       }
    }
}
```


##  4. Application execution

```
cd app-t
./gradlew
```

You can inspect at http://127.0.0.1:3271 that Bob plays `role1`.

## 5. Application dependencies

We need to use the cloned local copy of Moise instead of the Moise jar that comes from a remote repository.
For that, create a `settings.gradle` file with the following content:

```
includeBuild '../moise'
```

Here I added the `moise` project following the example, but, of course, any other JaCaMo component could be added as well.

This  `includeBuild` implies that the Moise being used in the application is not the one listed in the `build.gradle` dependencies (transitively by the dependency of JaCaMo), but the one we cloned (located at  `../moise`). 

## 6. Implementation of the new feature

For instance, change the operation `adoptRole` of `moise/src/main/java/ora4mas/nopl/GroupBoard.java` so that a debugging message is printed:

```
@OPERATION public void adoptRole(String role)  {
    logger.info("****** "+getOpUserName()+" adopting "+role); // ADDED LINE

    adoptRole(getOpUserName(), role);
}

```

When you run the application again, a new message appears in the MASConsole, meaning that the above method is being used.