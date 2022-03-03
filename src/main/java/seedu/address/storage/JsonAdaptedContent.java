package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.content.Content;
import seedu.address.model.content.Date;
import seedu.address.model.content.Tweet;

/**
 * Jackson-friendly version of {@link Content}.
 */
class JsonAdaptedContent {

    //public static final String MISSING_FIELD_MESSAGE_FORMAT = "Content's %s field is missing!";

    private final String tweet;
    private final String date;

    /**
     * Constructs a {@code JsonAdaptedContent} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedContent(@JsonProperty("tweet") String tweet, @JsonProperty("date") String date) {
        this.tweet = tweet;
        this.date = date;
    }

    /**
     * Converts a given {@code Content} into this class for Jackson use.
     */
    public JsonAdaptedContent(Content source) {
        tweet = source.getTweet().fullTweet;
        date = source.getDate().value;
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Content} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted content.
     */
    public Content toModelType() throws IllegalValueException {
        final Tweet modelTweet = new Tweet(tweet);
        final Date modelDate = new Date(date);
        return new Content(modelTweet, modelDate);
    }

}
