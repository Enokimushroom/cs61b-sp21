package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;
import static gitlet.Utils.readContentsAsString;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * CWD
     * GITLET_DIR
     * initCommit()
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static File GITLET_DIR = join(CWD, ".gitlet");
    public static File STAGING = join(GITLET_DIR, "staging");
    public static File OBJECT_DIR = join(GITLET_DIR, "objects");
    public static File COMMIT_DIR = join(OBJECT_DIR, "commits");
    public static File BLOB_DIR = join(OBJECT_DIR, "blobs");
    public static File REFS_DIR = join(GITLET_DIR, "refs");
    public static File HEAD_DIR = join(REFS_DIR, "heads");
    public static File HEAD = join(GITLET_DIR, "HEAD");

    /**
     * Creates a new Gitlet version-control system in the current directory.
     */
    public static void initial() {
        if (GITLET_DIR.exists() && GITLET_DIR.isDirectory()) {
            // failure case
            exitWithError("A Gitlet version-control system already exists in the current directory.");
        } else {
            // make directory
            makeDir();

            // initial commit
            Commit initialCommit = new Commit();
            String commitID = initialCommit.getCommitID();
            initialCommit.saveCommit();

            // create branch
            String branchName = "master";
            File branch = join(HEAD_DIR, branchName);
            writeContents(branch, commitID);

            // create HEAD
            writeContents(HEAD, branchName);
        }
    }

    private static void makeDir() {
        GITLET_DIR.mkdir();
        STAGING.mkdir();
        writeObject(STAGING, new Stage());
        OBJECT_DIR.mkdir();
        COMMIT_DIR.mkdir();
        BLOB_DIR.mkdir();
        REFS_DIR.mkdir();
        HEAD_DIR.mkdir();
    }

    // TODO: 遇到分支的情况
    public static void printLog() {
        Commit commit = getHEADcommit();
        printCommit(commit);
        LinkedList<String> parent = commit.getParents();
        for (String parentID : parent) {
            Commit parentCommit = getCommitObject(parentID);
            printCommit(parentCommit);
        }

    }

    public static void printGlobalLog() {
        List<String> allFiles = plainFilenamesIn(COMMIT_DIR);
        if (allFiles != null) {
            for (String fileName : allFiles) {
                Commit commit = getCommitObject(fileName);
                printCommit(commit);
            }
        }
    }

    private static void printCommit(Commit commit) {
        System.out.println("===");
        System.out.println("Commit " + commit.getCommitID());
        System.out.println("Date: " + commit.getTime());
        System.out.println(commit.getMessage());
        System.out.println();
    }

    public static void findCommitID(String message) {
        List<String> allFiles = plainFilenamesIn(COMMIT_DIR);
        int foundID = 0;
        if (allFiles != null) {
            for (String fileName : allFiles) {
                Commit commit = getCommitObject(fileName);
                if (commit.getMessage().equals(message)) {
                    foundID++;
                    System.out.println(commit.getCommitID());
                }
            }
        } if (allFiles == null || foundID == 0) {
            exitWithError("Found no commit with that message.");
        }
    }

    public static void printStatus() {
        printBranch();
        printStagingFile();
    }

    private static void printBranch() {
        System.out.println("=== Branches ===");
        List<String> allFiles = plainFilenamesIn(HEAD_DIR);
        String curBranch = readContentsAsString(HEAD);
        if (allFiles != null) {
            for (String fileName : allFiles) {
                if (curBranch.equals(fileName)) {
                    System.out.println("*" + fileName);
                } else {
                    System.out.println(fileName);
                }
            }
        }
        System.out.println();
    }

    private static void printStagingFile() {
        Stage stage = readObject(STAGING, Stage.class);
        HashMap<String, Blob> add = stage.getAddition();
        HashSet<String> remove = stage.getRemoval();
        printAdd(add);
        printRemove(remove);
    }

    private static void printAdd(HashMap<String, Blob> add) {
        System.out.println("=== Staged Files ===");
        if (!add.isEmpty()) {
            for (Map.Entry<String, Blob> entry : add.entrySet()) {
                String fileName = entry.getKey();
                System.out.println(fileName);
            }
        }
        System.out.println();
    }

    private static void printRemove(HashSet<String> remove) {
        System.out.println("=== Removed Files ===");
        if (!remove.isEmpty()) {
            for (String fileName : remove) {
                System.out.println(fileName);
            }
        }
        System.out.println();
    }


    public static void checkout(String[] args) {
        Commit commit = getHEADcommit();
        String HEADid = commit.getCommitID();
        HashMap<String, String> blob = commit.getBlob();

        // checkout –- [file name]
        if (args.length == 3 && args[1].equals("--")) {
            String fileName = args[2];
            checkout(fileName, blob);

        // checkout [commit id] –- [file name]
        } else if (args.length == 4 && args[2].equals("--")) {
            String commitID = getCompleteID(args[1]);  // 解决缩写问题
            String fileName = args[3];
            if (commitID.equals(HEADid)) {
                checkout(fileName, blob);
            } else {
                LinkedList<String> parent = commit.getParents();
                if (parent.contains(commitID)) {
                    Commit foundCommit = getCommitObject(commitID);
                    HashMap<String, String> foundBlob = foundCommit.getBlob();
                    checkout(fileName, foundBlob);
                } else {
                    exitWithError("No commit with that id exists.");
                }
            }

        // checkout [branch name]
        } else if (args.length == 2) {
            String branchName = args[1];
            File branchFile = join(HEAD_DIR, branchName);
            if (!branchFile.exists()) {
                exitWithError("No such branch exists.");
            }
            String commitID = readContentsAsString(branchFile);
            String curBranch = readContentsAsString(HEAD);
            if (branchName.equals(curBranch)) {
                exitWithError("No need to checkout the current branch.");
            }
            Commit checkedCommit = getCommitObject(commitID);
            HashMap<String, String> checkedBlob = checkedCommit.getBlob();
            for (Map.Entry<String, String> entry : checkedBlob.entrySet()) {
                String fileName = entry.getKey();
                String blobID = entry.getValue();
                File file = join(CWD, fileName);
                if (blob.containsKey(fileName)) {
                    writeFile(file, blobID);
                    blob.remove(fileName);
                } else {
                    if (!file.exists()) {
                        writeFile(file, blobID);
                    } else {
                        exitWithError("There is an untracked file in the way; delete it, or add and commit it first.");
                    }
                }
            }
            if (!blob.isEmpty()) {
                for (Map.Entry<String, String> entry : blob.entrySet()) {
                    String fileName = entry.getKey();
                    File file = join(CWD, fileName);
                    restrictedDelete(file);
                }
            }
            // update HEAD
            writeContents(HEAD, branchName);

            Stage stage = readObject(STAGING, Stage.class);
            stage.clean();
        }
    }

    private static void checkout(String fileName, HashMap<String, String> blob) {
        if (!blob.containsKey(fileName)) {
            exitWithError("File does not exist in that commit.");
        } else {
            String blobID = blob.get(fileName);
            File file = join(CWD, fileName);
            writeFile(file, blobID);
        }
    }

    private static String getCompleteID(String ID) {
        if (ID.length() == UID_LENGTH) {
            return ID;
        } else {
            List<String> allFiles = plainFilenamesIn(COMMIT_DIR);
            if (allFiles != null) {
                for (String fileName : allFiles) {
                    if (fileName.startsWith(ID)) {
                        return fileName;
                    }
                }
            }
            return null;
        }
    }

    private static void writeFile(File file, String blobID) {
        Blob blob = getBlobObject(blobID);
        byte[] content = blob.getBlobContent();
        writeContents(file, content);
    }

    public static void makeBranch(String branchName) {
        File file = join(HEAD_DIR, branchName);
        Commit commit = getHEADcommit();
        writeContents(file, commit.getCommitID());
        writeContents(HEAD, branchName);
    }

    public static void removeBranch(String branchName) {
        File file = join(HEAD_DIR, branchName);
        if (!file.exists()) {
            exitWithError("A branch with that name does not exist.");
        }
        String commitID = readContentsAsString(file);
        Commit commit = getHEADcommit();
        if (commitID.equals(commit.getCommitID())) {
            exitWithError("Cannot remove the current branch.");
        }
        restrictedDelete(file);
    }

    public static void reset(String ID) {
        String commitID = getCompleteID(ID);

        Commit HEADcommit = getHEADcommit();
        LinkedList<String> parent = HEADcommit.getParents();
        if (commitID == null || (!commitID.equals(HEADcommit.getCommitID()) && !parent.contains(commitID)) ) {
            exitWithError("No commit with that id exists.");
        }

        Commit commit = getCommitObject(commitID);
        HashMap<String, String> blob = commit.getBlob();
        for (Map.Entry<String, String> entry : blob.entrySet()) {
            String fileName = entry.getKey();
            String blobID = entry.getValue();
            File file = join(CWD, fileName);
            if (blob.containsKey(fileName)) {
                writeFile(file, blobID);
                blob.remove(fileName);
            } else {
                if (!file.exists()) {
                    writeFile(file, blobID);
                } else {
                    exitWithError("There is an untracked file in the way; delete it, or add and commit it first.");
                }
            }
        }
        if (!blob.isEmpty()) {
            for (Map.Entry<String, String> entry : blob.entrySet()) {
                String fileName = entry.getKey();
                File file = join(CWD, fileName);
                restrictedDelete(file);
            }
        }
        Stage stage = readObject(STAGING, Stage.class);
        stage.clean();
    }

    public static byte[] getContent(String fileName) {
        File file = join(CWD, fileName);
        return readContents(file);
    }

    public static Commit getCommitObject(String fileName) {
        File file = join(COMMIT_DIR, fileName);
        return readObject(file, Commit.class);
    }

    public static Blob getBlobObject(String fileName) {
        File file = join(BLOB_DIR, fileName);
        return readObject(file, Blob.class);
    }

    public static Commit getHEADcommit() {
        String branchName = readContentsAsString(HEAD);
        File file = join(HEAD_DIR, branchName);
        String commitID = readContentsAsString(file);
        return getCommitObject(commitID);
    }

    public static void validateArgs(String firstArg, int argsLength, int n) {
        switch (firstArg) {
            case "commit":
                if (argsLength == 1) {
                    exitWithError("Please enter a commit message.");
                }
            case "checkout":
                if (argsLength > 1 && argsLength <= 4) {
                    break;
                }
            default:
                if (argsLength != n) {
                    exitWithError("Incorrect operands.");
            }
        }
    }

    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(0);
    }
}
