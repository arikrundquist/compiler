
#JAVA = $(wildcard *.java)
#CLASS = $(patsubst %.java,%.class,$(JAVA))

all : bin

bin :
	@javac compiler/*.java --release 8 -d bin

%.run : bin
	@java -cp bin $*

#run : bin/SynReader.class
#	@java -cp bin SynReader test.syn

clean :
	-@rm -rf bin/*

save :
	git add .
	git commit -m "saving"
	git push

.PHONY : bin
