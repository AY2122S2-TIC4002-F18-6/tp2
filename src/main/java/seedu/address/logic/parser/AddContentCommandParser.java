package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TWEET;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddContentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.content.Content;
import seedu.address.model.content.Date;
import seedu.address.model.content.Tweet;



public class AddContentCommandParser implements Parser<AddContentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddContentCommand
     * and returns an AddContentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddContentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_TWEET, PREFIX_DATE);

        if (!arePrefixesPresent(argMultimap, PREFIX_TWEET, PREFIX_DATE)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddContentCommand.MESSAGE_USAGE));
        }

        Tweet tweet = ParserUtil.parseTweet(argMultimap.getValue(PREFIX_TWEET).get());
        Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());

        Content content = new Content(tweet, date);

        return new AddContentCommand(content);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
