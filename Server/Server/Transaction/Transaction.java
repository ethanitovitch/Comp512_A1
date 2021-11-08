package Server.Transaction;

import Server.Interface.IResourceManager;

import java.rmi.RemoteException;
import java.util.Vector;

public class Transaction {
    int transactionId;
    private Vector<IResourceManager> resourceManagers;

    public Transaction(int transactionId) {
        this.transactionId = transactionId;
    }

    public boolean commit() throws RemoteException, InvalidTransactionException, TransactionAbortedException {
        boolean result = true;
        for (IResourceManager resourceManager : resourceManagers) {
            result &= resourceManager.commit(this.transactionId);
        }
        return result;
    }

    public boolean abort() throws RemoteException, InvalidTransactionException {
        boolean result = true;
        for (IResourceManager resourceManager : resourceManagers) {
            result &= resourceManager.abort(this.transactionId);
        }
        return result;
    }

    public void addResourceManager(IResourceManager resourceManager) {
        this.resourceManagers.add(resourceManager);
    }
}
