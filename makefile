
all : run

%.run : bin
	@java -cp bin $*

bin : *.java
	-@javac *.java --release 8
	-@mv *.class bin

run : bin
	@java -cp bin SynReader test.syn
