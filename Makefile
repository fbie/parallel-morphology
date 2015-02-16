JPFCORE := jpf-core/build/jpf.jar
JPFAPROP := jpf-aprop/build/jpf-aprop.jar
JPFSYMBC := jpf-symbc/build/jpf-symbc-classes.jar
JPF = $(JPFCORE):$(JPFAPROP):$(JPFSYMBC)

JUNIT := /usr/share/java/junit4.jar
CP := $(CLASSPATH):parallel-union-find/src:parallel-morphology/src:parallel-experiments/src:lib/multiverse-core-0.7.0.jar:
BIN := bin

JAVAC := javac -verbose

all: experiments tests

experiments:
	mkdir -p $(BIN)
	$(JAVAC) -g:none -cp $(CP) -d $(BIN) parallel-experiments/src/dk/itu/parallel/experiments/morphology/MorphologyRunner.java parallel-experiments/src/dk/itu/parallel/experiments/unionfind/UnionFindRunner.java

tests: morph-test union-test

morph-test:
	mkdir -p $(BIN)
	$(JAVAC) -cp $(CP):$(JPF):$(JUNIT):parallel-morphology/test -d $(BIN) parallel-morphology/test/dk/itu/parallel/morphology/filter/FilterSuite.java

union-test:
	mkdir -p $(BIN)
	$(JAVAC) -cp $(CP):$(JPF):$(JUNIT):parallel-union-find/test -d $(BIN) parallel-union-find/test/dk/itu/parallel/unionfind/UnionFindSuite.java

verify:
	mkdir -p $(BIN)
	$(JAVAC) -g -cp $(CP):$(JPF):parallel-morphology/verify -d $(BIN) parallel-morphology/verify/dk/itu/parallel/morphology/verify/FilterDriver.java parallel-morphology/verify/dk/itu/parallel/morphology/verify/predicate/MonotonicDecreasingGrayValue.java

clean:
	rm -rf $(BIN)
