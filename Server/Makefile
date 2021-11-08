all: java.policy compile-server-rmi

java.policy: FORCE
	@echo "Creating server java policy"
	@echo "grant codebase \"file:`pwd`/\" {" > java.policy
	@echo "permission java.security.AllPermission;" >> java.policy
	@echo "};" >> java.policy

FORCE: ;

RMIInterface.jar: Server/Interface/IResourceManager.java
	@echo "Compiling RMI server interface"
	javac Server/Interface/IResourceManager.java
	jar cvf RMIInterface.jar Server/Interface/IResourceManager.class

ResourceStub.jar: Server/TCP/ResourceStub.java
	@echo "Compiling Resource Stub"
	javac Server/TCP/ResourceStub.java
	jar cvf ResourceStub.jar Server/TCP/ResourceStub.class

InvalidTransactionException.jar: Server/Transaction/InvalidTransactionException.java
	@echo "Compiling Invalid Transaction"
	javac Server/Transaction/InvalidTransactionException.java
	jar cvf InvalidTransactionException.jar Server/Transaction/InvalidTransactionException.class

TransactionAbortedException.jar: Server/Transaction/TransactionAbortedException.java
	@echo "Compiling Aborted Transaction"
	javac Server/Transaction/TransactionAbortedException.java
	jar cvf TransactionAbortedException.jar Server/Transaction/TransactionAbortedException.class

compile-server-rmi: Server/RMI/*.java Server/Interface/IResourceManager.java Server/Common/*.java Server/TCP/*.java
	javac Server/RMI/*.java Server/Interface/IResourceManager.java Server/Common/*.java Server/TCP/*.java

clean:
	rm -f Server/Interface/*.class Server/Common/*.class Server/RMI/*.class Server/TCP/*.class Server/Transaction/*.class
	rm -f RMIInterface.jar
	rm -f ResourceStub.jar
	rm -f InvalidTransactionException.jar
	rm -f TransactionAbortedException.jar
	rm -f java.policy
