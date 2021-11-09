all: java.policy compile-server-rmi

java.policy: FORCE
	@echo "Creating server java policy"
	@echo "grant codebase \"file:`pwd`/\" {" > java.policy
	@echo "permission java.security.AllPermission;" >> java.policy
	@echo "};" >> java.policy

FORCE: ;

RMIInterface.jar: Server/Interface/*.java
	@echo "Compiling RMI server interface"
	javac Server/Interface/*.java
	jar cvf RMIInterface.jar Server/Interface/*.class

ResourceStub.jar: Server/TCP/ResourceStub.java
	@echo "Compiling Resource Stub"
	javac Server/TCP/ResourceStub.java
	jar cvf ResourceStub.jar Server/TCP/ResourceStub.class

compile-server-rmi: Server/RMI/*.java Server/Interface/IResourceManager.java Server/Common/*.java Server/TCP/*.java Server/Transaction/*.java
	javac Server/RMI/*.java Server/Interface/IResourceManager.java Server/Common/*.java Server/TCP/*.java Server/Transaction/*.java

clean:
	rm -f Server/Interface/*.class Server/Common/*.class Server/RMI/*.class Server/TCP/*.class Server/Transaction/*.class
	rm -f RMIInterface.jar
	rm -f ResourceStub.jar
	rm -f InvalidTransactionException.jar
	rm -f TransactionAbortedException.jar
	rm -f java.policy
