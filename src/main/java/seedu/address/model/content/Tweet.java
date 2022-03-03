package seedu.address.model.content;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Tweet {

    public static final String MESSAGE_CONSTRAINTS =
            "Tweet should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullTweet;
    /**
     */
    public Tweet(String tweet) {
        requireNonNull(tweet);
        checkArgument(isValidTweet(tweet), MESSAGE_CONSTRAINTS);
        fullTweet = tweet;
    }

    public static boolean isValidTweet(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullTweet;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tweet // instanceof handles nulls
                && fullTweet.equals(((Tweet) other).fullTweet)); // state check
    }

    @Override
    public int hashCode() {
        return fullTweet.hashCode();
    }

}
