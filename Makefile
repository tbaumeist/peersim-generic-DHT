.PHONY: all clean doc compile

PEERSIM_JARS=""
LIB_JARS=`find -L lib/ -name "*.jar" | tr [:space:] :`

compile:
	mkdir -p classes
	javac -g -sourcepath src -classpath $(LIB_JARS):$(PEERSIM_JARS) -d classes `find -L -name "*.java"`

doc:
	mkdir -p doc
	javadoc -docletpath lib/peersim-doclet.jar -doclet peersim.tools.doclets.standard.Standard \
		-sourcepath src -classpath $(LIB_JARS):$(PEERSIM_JARS) -d doc -subpackages peersim.dht

run:
	java -cp $(LIB_JARS):$(PEERSIM_JARS):classes peersim.Simulator example.cfg

all: compile doc run

clean: 
	rm -fr classes doc