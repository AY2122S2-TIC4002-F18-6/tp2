package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;
import seedu.address.model.post.Post;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    Predicate<Post> PREDICATE_SHOW_ALL_POSTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Returns true if a post with the same identity as {@code post} exists in the address book.
     */
    boolean hasPost(Post post);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Deletes the given persons
     * The person must exist in the content planner
     */
    void deletePersonAll(List<Person> targets);

    /**
     * Deletes the given post.
     * The post must exist in the address book.
     */
    void deletePost(Post target);

    /**
     * Deletes the given posts
     * The post must exist in the content planner
     */
    void deletePostAll(List<Post> targets);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Adds the given post.
     * {@code post} must not already exist in the address book.
     */
    void addPost(Post post);


    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Replaces the given post {@code target} with {@code editedPost}.
     * {@code target} must exist in the address book.
     * The post identity of {@code editedPost} must not be the same as another existing post in the address book.
     */
    void setPost(Post target, Post editedPost);


    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered post list */
    ObservableList<Post> getFilteredPostList();


    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Updates the filter of the filtered post list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPostList(Predicate<Post> predicate);

    /**
     *
     * @param updatedSentCid the updated client id(s) that the target post will send to
     * @param targetPost the target post that will get the sent client id updated.
     */
    void updatePostSentCid(Set<String> updatedSentCid, Post targetPost);

    /**
     * Updates the whole post list from an entire new {@code postList}
     * @param postList the source post list
     */
    void updateWholePostList(List<Post> postList);

    /**
     * Undo the latest commit
     * Throws CommandException if no previous commit
     */
    boolean undoAddressBook();

    void commitAddressBook(ReadOnlyAddressBook addressBook);
}
