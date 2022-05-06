package seedu.address.model;

import java.util.ArrayList;


public class OldAddressBook extends AddressBook {
    private ArrayList<AddressBook> addressBookList;
    private int currentState;

    /**
     * Initializes a OldAddressBook
     */
    public OldAddressBook() {
        super();
        this.currentState = 0;
    }

    /**
     * OldAddressBook
     */
    public OldAddressBook(ReadOnlyAddressBook toBeCopied) {
        super(toBeCopied);
        this.currentState = 0;
        AddressBook newCopy = new AddressBook(toBeCopied);
        this.addressBookList = new ArrayList<>();
        this.addressBookList.add(newCopy);
    }

    /**
     * Commit to OldAddressBook
     */
    public void commit(ReadOnlyAddressBook toBeCommitted) {
        int newPointer = this.currentState;
        if (newPointer + 1 < addressBookList.size()) {
            newPointer += 1;
            this.addressBookList.subList(newPointer, this.addressBookList.size()).clear();
        }
        this.addressBookList.add(new AddressBook(toBeCommitted));
        this.currentState = this.addressBookList.size() - 1;
    }

    /**
     * Undo of OldAddressBook
     */
    public boolean undo() {
        if (currentState == 0) {
            return false;
        } else {
            super.resetData(this.addressBookList.get(this.currentState - 1));
            this.currentState -= 1;
            return true;
        }

    }
}
