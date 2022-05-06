package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ListType;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.post.Post;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_POST_SUCCESS = "Deleted Post: %1$s";

    //private final Index targetIndex;
    private final ListType listType;
    private List<Index> targetIndexes = new ArrayList<>();

    /**
     *
     * @param listType {@code Client} List or {@code Post} List from which to delete the item
     * @param targetIndexes the target indexes to be deleted from the list.
     */
    public DeleteCommand(ListType listType, List<Index> targetIndexes) {
        this.listType = listType;
        this.targetIndexes.addAll(targetIndexes);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (listType == ListType.CLIENT) {
            List<Person> lastShownList = model.getFilteredPersonList();

            for (Index targetIndex : targetIndexes) {
                if (targetIndex.getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
                }

            }

            List<Person> personToRemove = new ArrayList<>();
            String outputMsg = "";
            for (Index i : targetIndexes) {
                Person pToRemove = lastShownList.get(i.getZeroBased());
                personToRemove.add(pToRemove);
                outputMsg += String.format(MESSAGE_DELETE_PERSON_SUCCESS, pToRemove) + "\n";
            }


            model.deletePersonAll(personToRemove);

            return new CommandResult(outputMsg);

        } else {
            List<Post> lastShownList = model.getFilteredPostList();

            for (Index targetIndex : targetIndexes) {
                if (targetIndex.getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_POST_DISPLAYED_INDEX);
                }

            }

            List<Post> postToRemove = new ArrayList<>();
            String outputMsg = "";
            for (Index i : targetIndexes) {
                Post pToRemove = lastShownList.get(i.getZeroBased());
                postToRemove.add(pToRemove);
                outputMsg += String.format(MESSAGE_DELETE_POST_SUCCESS, pToRemove) + "\n";
            }


            model.deletePostAll(postToRemove);
            model.commitAddressBook(model.getAddressBook());

            return new CommandResult(outputMsg);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndexes.equals(((DeleteCommand) other).targetIndexes)); // state check
    }
}
