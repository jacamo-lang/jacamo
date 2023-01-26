# Extending a JaCaMo component
This document contains some (initial) tips for someone aiming at developing some part of JaCaMo.

For instance, suppose you want to extend Moise and test it in a JaCaMo application. The following steps can be used for that.

## 1. Clone Moise

```
git clone https://github.com/moise-lang/moise.git
```

## 2. Create a JaCaMo application

to test the new feature

```
curl -LO http://jacamo.sourceforge.net/nps/npss.zip
unzip npss.zip
./gradlew
```

* I am using JaCaMo SNAPSHOT version here
* The name of the application is app-t

Output:
```
> Task :run
JaCaMo 1.2-SNAPSHOT built on Fri Jan 20 13:51:24 CET 2023

Enter the identification of the new application: app-t

...
```

## 3. Edit the application project file

so that an organisation is create. For instance, the content of `app-t/app-t.jcm` could be:

```
mas app_t {

    agent bob: sample_agent.asl

    organisation o : org.xml {
       group g : group1 {
         players: bob role1
         debug: inspector_gui(on)
       }
    }

}
```


##  4. Run the application

```
cd app-t
./gradlew
```

The Moise window shows that Bob plays `role1`.

## 5. Change your application dependencies

to use the Moise just cloned.

Create a file `settings.gradle` with the  following content:

```
includeBuild '../moise'
```

Here I added the `moise` project following the example, but, of course, any other JaCaMo component could be added as well.


## 6. Implement the new feature

For instance, change the operation `adoptRole` of `moise/src/main/java/ora4mas/nopl/GroupBoard.java` so that a debugging message is printed:

```
@OPERATION public void adoptRole(String role)  {
    logger.info("****** "+getOpUserName()+" adopting "+role);

    adoptRole(getOpUserName(), role);
}

```

When you run the application again, a new message appears in the MASConsole.
