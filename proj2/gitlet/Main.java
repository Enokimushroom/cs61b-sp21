package gitlet;

import static gitlet.Commit.*;
import static gitlet.Stage.*;
import static gitlet.Utils.*;
import static gitlet.Repository.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            exitWithError("Please enter a command.");
        }
        String firstArg = args[0];
        int argsLength = args.length;
        switch (firstArg) {
            case "init":
                validateArgs(firstArg, argsLength, 1);
                initial();
                break;
            case "add":
                validateArgs(firstArg, argsLength, 2);
                addStaging(args[1]);
                break;
            case "commit":
                validateArgs(firstArg, argsLength, 2);
                makeCommit(args[1]);
                break;
            case "rm":
                validateArgs(firstArg, argsLength, 2);
                removeStaging(args[1]);
                break;
            case "log":
                validateArgs(firstArg, argsLength, 1);
                printLog();
                break;
            case "global-log":
                validateArgs(firstArg, argsLength, 1);
                printGlobalLog();
                break;
            case "find":
                validateArgs(firstArg, argsLength, 2);
                findCommitID(args[1]);
                break;
            case "status":
                validateArgs(firstArg, argsLength, 1);
                printStatus();
                break;
            case "checkout":
                validateArgs(firstArg, argsLength, 2);
                checkout(args);
                break;
            case "branch":
                validateArgs(firstArg, argsLength, 2);
                makeBranch(args[1]);
                break;
            case "rm-branch":
                validateArgs(firstArg, argsLength, 2);
                removeBranch(args[1]);
                break;
            case "reset":
                validateArgs(firstArg, argsLength, 2);
                reset(args[1]);
                break;
            case "merge":
                // TODO: handle the `add [filename]` command
                break;
            default:
                exitWithError("No command with that name exists.");
        }
    }
}
