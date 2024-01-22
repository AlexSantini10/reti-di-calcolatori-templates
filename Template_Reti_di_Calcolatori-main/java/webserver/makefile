compile:
	javac -sourcepath src -d bin $$(tree src -ivf | grep ".java" | xargs)
debug:
	make
	jdb -sourcepath src -cp lib:bin Main
run:
	java -cp lib:bin Main
all:
	make compile
	make run
