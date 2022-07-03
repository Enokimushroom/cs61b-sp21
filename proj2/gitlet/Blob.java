package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class Blob implements Serializable {

    private byte[] content;
    private String blobID;

    public Blob(String filename) {
        content = getContent(filename);
        blobID = sha1(filename, Arrays.toString(content));
    }

    public String getBlobID() {
        return blobID;
    }

    public byte[] getBlobContent() {
        return content;
    }

    public void saveBlob() {
        File file = join(BLOB_DIR, blobID);
        writeObject(file, this);
    }

    public static boolean checkBlob(String filename, String blobID) {
        Commit commit = getHEADcommit();
        String trackedBlobID = commit.getBlob().get(filename);
        return !blobID.equals(trackedBlobID);
    }
}
