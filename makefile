
TESTS = $(wildcard tests/*.test)

test : $(TESTS)
	@echo all passed!

parser : lex.yy.c parser.tab.c *.c
	gcc *.c -o parser

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

phony : ;

.PHONY : phony
