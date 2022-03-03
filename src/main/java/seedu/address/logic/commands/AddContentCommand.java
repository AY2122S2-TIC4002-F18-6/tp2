package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TWEET;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.content.Content;

/**
 * Adds content to the address book.
 */
public class AddContentCommand extends Command {

    public static final String COMMAND_WORD = "addContent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds content to the address book. "
        + "Parameters: "
        + PREFIX_TWEET + "CONTENT "
        + PREFIX_DATE + "DATE "
        + "Example: " + COMMAND_WORD + " "
        + PREFIX_TWEET + "Singapore GST Budget "
        + PREFIX_DATE + "02022022 ";

    public static final String MESSAGE_SUCCESS = "New Content added: %1$s";
    public static final String MESSAGE_DUPLICATE_CONTENT = "This content already exists in the address book";

    private final Content toAddContent;

    /**
     * Creates an AddContentCommand to add the specified {@code Content}
     */
    public AddContentCommand(Content content) {
        requireNonNull(content);
        toAddContent = content;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasContent(toAddContent)) {
            throw new CommandException(MESSAGE_DUPLICATE_CONTENT);
        }

        model.addContent(toAddContent);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAddContent));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof AddContentCommand // instanceof handles nulls
            && toAddContent.equals(((AddContentCommand) other).toAddContent));
    }
}
