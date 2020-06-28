all:
	@echo "clean  - Clean all the projects"
	@echo "build  - Build all the projects"
	@echo "deploy - Deploy all the projects"

clean:
	cd java; make clean
	cd python; make clean
	cd nodejs; make clean

build:
	cd java; make build
	cd python; make build
	cd nodejs; make build

deploy:
	cd java; make deploy
	cd python; make deploy
	cd nodejs; make deploy