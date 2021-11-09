all: java.policy compile-client

java.policy: FORCE
	@echo "Creating client java policy"
	@echo "grant codebase \"file:`pwd`/\" {" > java.policy
	@echo "permission java.security.AllPermission;" >> java.policy
	@echo "};" >> java.policy

FORCE: ;

../Server/InvalidTransactionException.jar: ../Server/Server/Transaction/InvalidTransactionException.java
	make -C ../Server/ InvalidTransactionException.jar

../Server/TransactionAbortedException.jar: ../Server/Server/Transaction/TransactionAbortedException.java
	make -C ../Server/ TransactionAbortedException.jar

../Server/RMIInterface.jar: ../Server/Server/Interface/IResourceManager.java
	make -C ../Server/ RMIInterface.jar

../Server/ResourceStub.jar: ../Server/Server/TCP/ResourceStub.java
	make -C ../Server/ ResourceStub.jar

compile-client: ../Server/RMIInterface.jar ../Server/ResourceStub.jar ../Server/InvalidTransactionException.jar ../Server/TransactionAbortedException.jar
	javac -cp ../Server/RMIInterface.jar:../Server/ResourceStub.jar:../Server/InvalidTransactionException.jar:../Server/TransactionAbortedException.jar Client/*.java

clean:
	rm -f Client/*.class
	rm -f ../Server/RMIInterface.jar
	rm -f ../Server/ResourceStub.jar
	rm -f ../Server/InvalidTransactionException.jar
	rm -f ../Server/TransactionAbortedException.jar
	rm -f java.policy
