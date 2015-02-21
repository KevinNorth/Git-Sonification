package edu.unl.cse.knorth.git_sonification;

import edu.unl.cse.knorth.git_sonification.commit_processor.CommitProcessor;
import edu.unl.cse.knorth.git_sonification.conflict_data.Conflict;
import edu.unl.cse.knorth.git_sonification.conflict_data.ConflictDataParser;
import edu.unl.cse.knorth.git_sonification.git_caller.GitCaller;
import edu.unl.cse.knorth.git_sonification.git_caller.PartialCommit;
import edu.unl.cse.knorth.git_sonification.intermediate_data.Commit;
import java.io.IOException;
import java.util.List;
import org.joda.time.DateTime;

public class Main {
    public static void main(String[] args) throws IOException {
//        GregorianCalendar cal = new GregorianCalendar();
//        cal.set(2009, 11, 23);
        DateTime since = new DateTime(2009, 12, 23, 0, 0);
//        cal.set(2010, 0, 6);
        DateTime until = new DateTime(2010, 1, 6, 0, 0);
                
        List<PartialCommit> partialCommits;
        try(GitCaller gitCaller = new GitCaller("../../voldemort/.git")) {
            partialCommits = gitCaller.getPartialCommits();
        }
        
        List<Conflict> conflicts = new ConflictDataParser()
                .parseConflictData("data/conflict_data.dat");
        
        List<Commit> commits = new CommitProcessor()
                .processCommits(partialCommits, conflicts, since, until);
        
        for(Commit commit : commits) {
            System.out.println(commit);
        }
    }
}
