JAVAC=	jikes +E
#JAVAC=	javac

all:	SimpleTextDriver.jar

SimpleTextDriver.jar:
	$(JAVAC) -d . *.java
	jar cvf $@ jdbc/SimpleText

clean:
	rm -r jdbc

clobber:	clean
	rm -f *.jar
