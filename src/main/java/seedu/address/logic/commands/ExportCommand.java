package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.collections.ObservableList;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.post.Post;
import seedu.address.storage.StorageManager;

public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_SUCCESS = "Data exported";
    public static final String MESSAGE_FAIL = "No record. Please ensure there is a record in the system";

    private final Path path = Paths.get("").toAbsolutePath();
    private final Path txtStoragePath = Paths.get(path + "/data");

    private ObservableList<Person> personList;
    private ObservableList<Post> postList;

    public ExportCommand() {
    }

    @Override
    public CommandResult execute(Model model) throws IOException {
        requireNonNull(model);
        this.personList = model.getAddressBook().getPersonList();
        this.postList = model.getAddressBook().getPostList();

        String dataToTxt = "";

        for (int i = 0; i < personList.size(); i++) {
            String userInfo = "Name: " + personList.get(i).getName() + "\n"
                + "Phone: " + personList.get(i).getPhone() + "\n"
                + "Email: " + personList.get(i).getEmail() + "\n"
                + "Address: " + personList.get(i).getAddress() + "\n"
                + "Tag(s): " + personList.get(i).getTags().toString() + "\n"
                + "\n\n";
            dataToTxt += userInfo;
        }

        for (int i = 0; i < postList.size(); i++) {
            String postInfo = "Title: " + postList.get(i).getTitle() + "\n"
                + "Content: " + postList.get(i).getContent() + "\n"
                + "Category: " + postList.get(i).getCategory() + "\n"
                + "Notes: " + postList.get(i).getNotes() + "\n"
                + "PostDate: " + postList.get(i).getPostDate() + "\n"
                + "\n\n";
            dataToTxt += postInfo;
        }


        if (dataToTxt.equals("")) {

            return new CommandResult(MESSAGE_FAIL);
        }

        StorageManager.exportAddressBookToTxt(dataToTxt, txtStoragePath);

        return new CommandResult(MESSAGE_SUCCESS);
    }
}

