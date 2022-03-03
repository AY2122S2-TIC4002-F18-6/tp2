package seedu.address.model.content;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UniqueContentList implements Iterable<Content> {

    private final ObservableList<Content> internalList = FXCollections.observableArrayList();
    private final ObservableList<Content> internalUnmodifiableList =
        FXCollections.unmodifiableObservableList(internalList);

    /**
     *
     */
    public boolean contains(Content toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameContent);
    }

    /**
     *
     */
    public void add(Content toAddContent) {
        requireNonNull(toAddContent);
        if (contains(toAddContent)) {
            //throw new DuplicateContentException();
        }
        internalList.add(toAddContent);
    }

    public void setContent(Content target, Content editedContent) {
        requireAllNonNull(target, editedContent);

        int index = internalList.indexOf(target);
        if (index == -1) {
            //throw new ContentNotFoundException();
        }

        if (!target.isSameContent(editedContent) && contains(editedContent)) {
            //throw new DuplicateContentException();
        }

        internalList.set(index, editedContent);
    }

    /**
     *
     */
    public void remove(Content toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            //throw new ContentNotFoundException();
        }
    }

    public void setContents(UniqueContentList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    public void setContents(List<Content> contents) {
        requireAllNonNull(contents);
        if (!contentsAreUnique(contents)) {
            //throw new DuplicateContentException();
        }

        internalList.setAll(contents);
    }

    public ObservableList<Content> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Content> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof UniqueContentList // instanceof handles nulls
            && internalList.equals(((UniqueContentList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    private boolean contentsAreUnique(List<Content> contents) {
        for (int i = 0; i < contents.size() - 1; i++) {
            for (int j = i + 1; j < contents.size(); j++) {
                if (contents.get(i).isSameContent(contents.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
