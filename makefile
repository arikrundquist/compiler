
JAVA = $(wildcard src/*.java) $(wildcard src/*/*.java) $(wildcard src/*/*/*.java)
CLASS = $(patsubst src/%.java,bin/%.class,$(JAVA))

JAVAC_FLAGS = --release 8 -d bin -Xlint:unchecked

all : bin

bin : $(CLASS)

$(CLASS) : $(JAVA)
	@javac $(JAVA) $(JAVAC_FLAGS)

%.run :
	-@make bin
	-@java -cp bin $*

#run : bin/SynReader.class
#	@java -cp bin SynReader test.syn

clean :
	-@rm -rf bin/*
	-@clear

save :
	git add .
	git commit -m "saving"
	git push
