
TESTS = $(wildcard tests/*.test)

test : $(TESTS)
	@echo all passed!

parser : lex.yy.c parser.tab.c *.c
	g++ *.c -o parser -Wno-free-nonheap-object

lex.yy.c : *.h lexer.l
	lex lexer.l

parser.tab.c : *.h parser.y
	bison -d parser.y

%.test : %.out parser phony
	@cat $@ | ./parser
	@diff -b lexer.log $<
	@echo $@ passed!

mktest% :
	@touch tests/test$*.test tests/test$*.out

clean : phony
	rm parser *.yy.c *.tab.c *.tab.h

phony : ;

.PHONY : phony
