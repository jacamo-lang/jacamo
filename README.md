# JaCaMo

This project aims to promote the MultiAgent Programming (MAP) approach by providing a suitable integration of tools and languages for programming the following dimensions: agents ([Jason](http://jason.sf.net)), environment ([Cartago](http://cartago.sourceforge.net/)), and organisation ([Moise](http://moise.sf.net)).

# Installation

For JaCaMo users:

- get a release at [GitHub](https://github.com/jomifred/jacamo/releases) 
- install the eclipse plugin as described [here](http://jacamo.sourceforge.net/eclipseplugin/tutorial).
- follow the [Hello World tutorial](http://jacamo.sourceforge.net/tutorial/hello-world)
- more documentation [here](http://jacamo.sf.net)
	
For JaCaMo developers:

	git clone https://github.com/jomifred/jacamo.git
	cd jacamo
	gradle jar
	gradle eclipse
	gradle config
	
The config task prints out the command to set up the JACAMO_HOME variable.
	
Examples can be run using the jacamo script, for example:

	scripts/jacamo src/examples/house-building/house.jcm

