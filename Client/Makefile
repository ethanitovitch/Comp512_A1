all: java.policy compile-client

java.policy: FORCE
	@echo "Creating client java policy"
	@echo "grant codebase \"file:`pwd`/\" {" > java.policy
	@echo "permission java.security.AllPermission;" >> java.policy
	@echo "};" >> java.policy

FORCE: ;

../Server/RMIInterface.jar: ../Server/Server/Interface/*.java
	make -C ../Server/ RMIInterface.jar

../Server/ResourceStub.jar: ../Server/Server/TCP/ResourceStub.java
	make -C ../Server/ ResourceStub.jar

compile-client: ../Server/RMIInterface.jar ../Server/ResourceStub.jar
	javac -cp ../Server/RMIInterface.jar:../Server/ResourceStub.jar: Client/*.java

clean:
	rm -f Client/*.class
	rm -f ../Server/RMIInterface.jar
	rm -f ../Server/ResourceStub.jar
	rm -f java.policy
