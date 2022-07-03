package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Repository.*;
import static gitlet.Utils.*;
import static gitlet.Blob.*;

public class Stage implements Serializable {

    private static HashMap<String, Blob> addition;
    private static HashSet<String> removal;

    public Stage() {
        addition = new HashMap<>();
        removal = new HashSet<>();
    }

    public Stage(HashMap<String, Blob> add, HashSet<String> remove) {
        addition = add;
        removal = remove;
    }

    public static void addStaging(String fileName) {

        // failure case
        File file = join(CWD, fileName);
        if (!file.exists() || file.isDirectory()) {
            exitWithError("File does not exist.");
        } else {
            Blob blob = new Blob(fileName);
            String blobID = blob.getBlobID();

            // check commit track
            if (checkBlob(fileName, blobID)) {
                Stage stage = readObject(STAGING, Stage.class);
                HashMap<String, Blob> addStage = stage.getAddition();
                HashSet<String> remove = stage.getRemoval();

                // check removal
                if (remove.contains(fileName)) {
                    remove.remove(fileName);
                    Stage newStage = new Stage(addStage, remove);
                    writeObject(STAGING, newStage);

                } else {

                    // add stage
                    addStage.put(fileName, blob);
                    Stage newStage = new Stage(addStage, remove);
                    writeObject(STAGING, newStage);
                }
            }
        }
    }

    public static void removeStaging(String fileName) {

        Stage stage = readObject(STAGING, Stage.class);
        HashMap<String, Blob> addStage = stage.getAddition();
        HashSet<String> remove = stage.getRemoval();

        if (addStage.containsKey(fileName)) {

            // check addition
            addStage.remove(fileName);
            Stage newStage = new Stage(addStage, remove);
            writeObject(STAGING, newStage);

        } else {

            // check commit track
            Commit commit = getHEADcommit();
            if (commit.getBlob().containsKey(fileName)) {
                remove.add(fileName);
                Stage newStage = new Stage(addStage, remove);
                writeObject(STAGING, newStage);

            } else {

                // failure case
                exitWithError("No reason to remove the file.");
            }
        }
    }

    public HashMap<String, Blob> getAddition() {
        return addition;
    }

    public HashSet<String> getRemoval() {
        return removal;
    }

    public void clean() {
        HashMap<String, Blob> addition = new HashMap<>();
        HashSet<String> removal = this.getRemoval();
        if (!removal.isEmpty()) {
            for (String fileName : removal) {
                File file = join(CWD, fileName);
                restrictedDelete(file);
            }
        }
        Stage stage = new Stage(addition, removal);
        writeObject(STAGING, stage);
    }
}
