package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private String commitID;
    private LinkedList<String> parents;
    private HashMap<String, String> blob;

    public Commit() {
        message = "initialCommit";
        timestamp = new Date(0);
        parents = new LinkedList<>();
        blob = new HashMap<>();
        commitID = sha1(message, timestamp.toString(), parents.toString(), blob.toString());
    }

    public Commit(String commitment, Date date, String ID, LinkedList<String> parent, HashMap<String, String> blobs) {
        message = commitment;
        timestamp = date;
        parents = parent;
        blob = blobs;
        commitID = ID;
    }

    public static void makeCommit(String commitment) {

        // failure case
        Stage stage = readObject(STAGING, Stage.class);
        HashMap<String, Blob> addStage = stage.getAddition();
        HashSet<String> removStage = stage.getRemoval();
        if (addStage.isEmpty() && removStage.isEmpty()) {
            exitWithError("No changes added to the commit.");
        }

        // update timestamp
        Date timestamp = new Date();

        // update parent
        Commit commit= getHEADcommit();
        LinkedList<String> parent = commit.getParents();
        String ID = commit.getCommitID();
        parent.addFirst(ID);

        // update blob
        HashMap<String, String> blob = commit.getBlob();
        for (Map.Entry<String, Blob> entry : addStage.entrySet()){
            String fileName = entry.getKey();
            Blob stageBlob = entry.getValue();
            String blobID = stageBlob.getBlobID();
            stageBlob.saveBlob();
            blob.put(fileName, blobID);
        }

        // update ID
        String newID = sha1(commitment, timestamp.toString(), parent.toString(), blob.toString());

        // update commit
        Commit newCommit = new Commit(commitment, timestamp, newID, parent, blob);
        newCommit.saveCommit();

        // update branch
        String branchName = readContentsAsString(HEAD);
        File file = join(HEAD_DIR, branchName);
        writeContents(file, newID);

        // update staging area
        stage.clean();
    }

    public void saveCommit() {
        File file = join(COMMIT_DIR, commitID);
        writeObject(file, this);
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return timestamp.toString();
    }

    public String getCommitID() {
        return commitID;
    }

    public LinkedList<String> getParents() {
        return parents;
    }

    public HashMap<String, String> getBlob() {
        return blob;
    }
}
