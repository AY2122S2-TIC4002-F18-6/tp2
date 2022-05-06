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

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.post.Post;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " c: Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " c "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_USAGE_2 = COMMAND_WORD + " p: Adds a post to content list. "
            + "Parameters: "
            + PREFIX_TITLE + "TITLE OF CONTENT "
            + PREFIX_CONTENT + "CONTENT INFORMATION "
            + PREFIX_POSTDATE + "POSTING DATE IN yyyyMMdd HHmm "
            + PREFIX_CATEGORY + "CATEGORY "
            + PREFIX_NOTES + "NOTES\n"
            + "Example: " + COMMAND_WORD + " p "
            + PREFIX_TITLE + "a dummy title two "
            + PREFIX_CONTENT + "a dummy example content two "
            + PREFIX_POSTDATE + "20220805 0900 "
            + PREFIX_CATEGORY + "notice "
            + PREFIX_NOTES + "remember to send to all VIP1";

    public static final String MESSAGE_SUCCESS = "New person/post added: %1$s"; //TODO : separate person and post

    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";
    public static final String MESSAGE_DUPLICATE_POST = "This post already exists in the post list";

    private Person toAdd;
    private Post postToAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    /**
     * Creates an AddCommand to add the specified {@code Post}
     */
    public AddCommand(Post post) {
        requireNonNull(post);
        postToAdd = post;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        String outputMsg = "";

        if (toAdd != null) {
            if (model.hasPerson(toAdd)) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }
            model.addPerson(toAdd);
            model.commitAddressBook(model.getAddressBook());
            outputMsg += String.format(MESSAGE_SUCCESS, toAdd);
        } else if (postToAdd != null) {
            if (model.hasPost(postToAdd)) {
                throw new CommandException(MESSAGE_DUPLICATE_POST);
            }
            model.addPost(postToAdd);
            outputMsg += String.format(MESSAGE_SUCCESS, postToAdd);
        } else {
            outputMsg += "something went wrong, you must add a client or a post.";
        }

        return new CommandResult(outputMsg);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddCommand)) {
            return false;
        }
        //return toAdd.equals(((AddCommand) other).toAdd)
        //        && postToAdd.equals(((AddCommand) other).postToAdd);
        return toAdd != null ? toAdd.equals(((AddCommand) other).toAdd)
                : postToAdd != null ? postToAdd.equals(((AddCommand) other).postToAdd) : false;
        //return true;
    }
}
