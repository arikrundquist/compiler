
all : run

%.run : bin/%.class
	@java -cp bin $*

bin/%.class : *.java
	@javac *.java --release 8
	-@mv *.class bin

run : bin/SynReader.class
	@java -cp bin SynReader test.syn

clean :
	-@rm bin/*
