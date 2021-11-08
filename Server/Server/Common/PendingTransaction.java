package Server.Common;

import java.util.HashSet;
import java.util.Set;

public class PendingTransaction {
    public int xid;
    public RMHashMap updatedItems;
    public Set<String> deletedItems;
    public RMHashMap data;

    public PendingTransaction(int xid, RMHashMap data) {
        this.xid = xid;
        this.updatedItems = new RMHashMap();
        this.deletedItems = new HashSet<>();
        this.data = data;
    }

    public synchronized RMItem readItem(String key) {
        if (deletedItems.contains(key)) {
            return null;
        }
        if (updatedItems.containsKey(key)) {
            return (RMItem)updatedItems.get(key);
        } else if (data.containsKey(key)) {
            return (RMItem)data.get(key).clone();
        } else {
            return null;
        }
    }

    public synchronized void updateOrCreate(String key, RMItem item) {
        updatedItems.put(key, item);
        deletedItems.remove(key);
    }

    public synchronized void deleteItem(String key) {
        if (updatedItems.containsKey(key)) {
            deletedItems.add(key);
            updatedItems.remove(key);
        }
    }

    public synchronized boolean commit() {
        synchronized (data) {
            for (String deletedItem : deletedItems) {
                data.remove(deletedItem);
            }

            data.putAll(updatedItems);
        }
        return true;
    }
}
