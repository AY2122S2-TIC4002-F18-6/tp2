package seedu.address.model.content;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Content {

    // Identity fields
    private final Tweet tweet;
    private final Date date;
    /**
     */
    public Content(Tweet tweet, Date date) {
        requireAllNonNull(tweet, date);
        this.tweet = tweet;
        this.date = date;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public Date getDate() {
        return date;
    }
    /**
     */
    public boolean isSameContent(Content otherContent) {
        if (otherContent == this) {
            return true;
        }

        return otherContent != null
            && otherContent.getTweet().equals(getTweet());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Content)) {
            return false;
        }

        Content otherPerson = (Content) other;
        return otherPerson.getTweet().equals(getTweet())
            && otherPerson.getDate().equals(getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tweet, date);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTweet())
            .append("; Date: ")
            .append(getDate());
        return builder.toString();
    }

}

