package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSTDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TITLE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_POSTS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.post.Category;
import seedu.address.model.post.Content;
import seedu.address.model.post.Notes;
import seedu.address.model.post.Post;
import seedu.address.model.post.PostDate;
import seedu.address.model.post.Title;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " c: Edits the details of the person identified "
        + "by the index number used in the displayed person list. "
        + "Existing values will be overwritten by the input values.\n"
        + "Parameters: INDEX (must be a positive integer) "
        + "[" + PREFIX_NAME + "NAME] "
        + "[" + PREFIX_PHONE + "PHONE] "
        + "[" + PREFIX_EMAIL + "EMAIL] "
        + "[" + PREFIX_ADDRESS + "ADDRESS] "
        + "[" + PREFIX_TAG + "TAG]...\n"
        + "Example: " + COMMAND_WORD + " c 1 "
        + PREFIX_PHONE + "91234567 "
        + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_USAGE_2 = COMMAND_WORD + " p: Edits the details of the post identified "
        + "by the index number used in the displayed content list. "
        + "Existing values will be overwritten by the input values.\n"
        + "Parameters: INDEX (must be a positive integer) "
        + "[" + PREFIX_TITLE + "TITLE OF CONTENT] "
        + "[" + PREFIX_CONTENT + "CONTENT INFORMATION] "
        + "[" + PREFIX_POSTDATE + "POSTING DATE IN yyyyMMdd HHmm] "
        + PREFIX_CATEGORY + "CATEGORY "
        + PREFIX_NOTES + "NOTES\n"
        + "Example: " + COMMAND_WORD + " p 1 "
        + PREFIX_TITLE + "a dummy title two "
        + PREFIX_CONTENT + "a dummy example content two ";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_EDIT_POST_SUCCESS = "Edited Post: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private EditPersonDescriptor editPersonDescriptor;
    private EditContentDescriptor editContentDescriptor;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    /**
     * @param index                 of the post in the filtered content list to edit
     * @param editContentDescriptor details to edit the content with
     */
    public EditCommand(Index index, EditContentDescriptor editContentDescriptor) {
        requireNonNull(index);
        requireNonNull(editContentDescriptor);

        this.index = index;
        this.editContentDescriptor = new EditContentDescriptor(editContentDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (editPersonDescriptor != null) {
            List<Person> lastShownList = model.getFilteredPersonList();

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person personToEdit = lastShownList.get(index.getZeroBased());
            Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

            if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }

            model.setPerson(personToEdit, editedPerson);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            model.commitAddressBook(model.getAddressBook());
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedPerson));

        } else if (editContentDescriptor != null) {
            List<Post> lastShownList = model.getFilteredPostList();
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_POST_DISPLAYED_INDEX);
            }

            Post postToEdit = lastShownList.get(index.getZeroBased());
            Post editedPost = createEditedPost(postToEdit, editContentDescriptor);

            if (!postToEdit.isSamePost(editedPost) && model.hasPost(editedPost)) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }

            model.setPost(postToEdit, editedPost);
            model.updateFilteredPostList(PREDICATE_SHOW_ALL_POSTS);
            model.commitAddressBook(model.getAddressBook());
            return new CommandResult(String.format(MESSAGE_EDIT_POST_SUCCESS, editedPost));
        }
        return new CommandResult("Something went wrong, check your inputs again!");
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    /**
     * Creates and returns a {@code Post} with the details of {@code postToEdit}
     * edited with {@code editContentDescriptor}.
     */
    private static Post createEditedPost(Post postToEdit, EditContentDescriptor editContentDescriptor) {
        assert postToEdit != null;

        Title updatedTitle = editContentDescriptor.getTitle().orElse(postToEdit.getTitle());
        Content updatedContent = editContentDescriptor.getContent().orElse(postToEdit.getContent());
        PostDate updatedPostDate = editContentDescriptor.getPostDate().orElse(postToEdit.getPostDate());
        Category updatedCategory = editContentDescriptor.getCategory().orElse(postToEdit.getCategory());
        Notes updatedNotes = editContentDescriptor.getNotes().orElse(postToEdit.getNotes());

        return new Post(updatedTitle, updatedContent, updatedPostDate, updatedCategory, updatedNotes);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
            && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                && getPhone().equals(e.getPhone())
                && getEmail().equals(e.getEmail())
                && getAddress().equals(e.getAddress())
                && getTags().equals(e.getTags());
        }
    }

    /**
     * Stores the details to edit the post with. Each non-empty field value will replace the
     * corresponding field value of the content.
     */
    public static class EditContentDescriptor {
        private Title title;
        private Content content;
        private PostDate postDate;
        private Category category;
        private Notes notes;

        public EditContentDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditContentDescriptor(EditContentDescriptor toCopy) {
            setTitle(toCopy.title);
            setContent(toCopy.content);
            setPostDate(toCopy.postDate);
            setCategory(toCopy.category);
            setNotes(toCopy.notes);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(title, content, postDate, category, notes);
        }

        public void setTitle(Title title) {
            this.title = title;
        }

        public Optional<Title> getTitle() {
            return Optional.ofNullable(title);
        }

        public void setContent(Content content) {
            this.content = content;
        }

        public Optional<Content> getContent() {
            return Optional.ofNullable(content);
        }

        public void setPostDate(PostDate postDate) {
            this.postDate = postDate;
        }

        public Optional<PostDate> getPostDate() {
            return Optional.ofNullable(postDate);
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public Optional<Category> getCategory() {
            return Optional.ofNullable(category);
        }

        public void setNotes(Notes notes) {
            this.notes = notes;
        }

        public Optional<Notes> getNotes() {
            return Optional.ofNullable(notes);
        }


        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditContentDescriptor)) {
                return false;
            }

            // state check
            EditContentDescriptor e = (EditContentDescriptor) other;

            return getTitle().equals(e.getTitle())
                && getContent().equals(e.getContent())
                && getPostDate().equals(e.getPostDate())
                && getCategory().equals(e.getCategory())
                && getNotes().equals(e.getNotes());
        }
    }
}
