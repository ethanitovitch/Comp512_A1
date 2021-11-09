package Server.Transaction;

import Server.Interface.IResourceManager;
import Server.Interface.InvalidTransactionException;
import Server.LockManager.DeadlockException;
import Server.LockManager.LockManager;
import Server.LockManager.TransactionLockObject;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionManager {

    private Map<Integer, Transaction> transactions;
    private AtomicInteger transactionIdCount;
    private LockManager lockManager;

    public TransactionManager() {
        this.transactions = new HashMap<>();
        this.transactionIdCount = new AtomicInteger(0);
        this.lockManager = new LockManager();
    }

    public int start() throws RemoteException {
        int transactionId = transactionIdCount.incrementAndGet();
        Transaction transaction = new Transaction(transactionId);
        transactions.put(transactionId, transaction);
        return transactionId;
    }

    public boolean commit(int xid) throws RemoteException, InvalidTransactionException {
        if (!transactions.containsKey(xid)) {
            throw new InvalidTransactionException();
        }
        Transaction transaction = transactions.get(xid);
        boolean result = transaction.commit();
        transactions.remove(xid);
        lockManager.UnlockAll(xid);
        return result;
    }

    public boolean abort(int xid) throws RemoteException, InvalidTransactionException {
        if (!transactions.containsKey(xid)) {
            throw new InvalidTransactionException();
        }
        Transaction transaction = transactions.get(xid);
        boolean result = transaction.abort();
        transactions.remove(xid);
        lockManager.UnlockAll(xid);
        return result;
    }

    public boolean getLock(int xid, String data, IResourceManager resourceManager, TransactionLockObject.LockType lockType) throws RemoteException,  InvalidTransactionException{
        if (!transactions.containsKey(xid)) {
            return false;
        }
        Transaction transaction = transactions.get(xid);

        synchronized (transaction)
        {
            try {
                boolean locked = lockManager.Lock(xid, data, lockType);
                if (!locked) {
                    throw new InvalidTransactionException();
                }
            } catch (DeadlockException e) {
                return false;
            }

            transaction.addResourceManager(resourceManager);
            return true;
        }
    }
}
