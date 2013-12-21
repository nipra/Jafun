legacyTest:
	cd legacy; javac Name-legacy.java; java -ea Client

generifiedTest: legacyTest
	cd generified; cp ../legacy/ExtendedName.class .; javac Name-generified.java; java -ea Test

clean:
	rm legacy/*.class
	rm generified/*.class

