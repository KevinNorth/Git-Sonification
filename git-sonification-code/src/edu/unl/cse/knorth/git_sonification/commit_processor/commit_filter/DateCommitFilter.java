package edu.unl.cse.knorth.git_sonification.commit_processor.commit_filter;

import edu.unl.cse.knorth.git_sonification.git_caller.PartialCommit;
import java.util.Date;

/**
 * Filters commits based on when they were made, selecting the commits that fall
 * in-between two specified dates.
 * @author knorth
 */
public class DateCommitFilter extends CommitFilter {
    private final Date since;
    private final Date until;

    /**
     * Creates a DateCommitFilter that selects commits made in-between
     * <code>until</code> and <code>since</code>, inclusively.
     * @param since The earliest timestamp that a commit can have been made on
     * and still be selected.
     * @param until The latest timestamp that a commit can have been made on and
     * still be selected.
     */
    public DateCommitFilter(Date since, Date until) {
        this.since = since;
        this.until = until;
    }
    
    /**
     * Determines whether the given commit was made in-between the two specified
     * dates passed to this <code>DateCommitFilter</code>'s constructor.
     * @param commit The commit to check
     * @return <code>true</code> if the date the commit was made falls in-
     * between <code>since</code> and <code>until</code> (inclusively).
     * <code>false</code> otherwise.
     */
    @Override
    protected boolean shouldSelectCommit(PartialCommit commit) {
        Date commitDate = commit.getDatetime();
        
        if((commitDate.after(since)) && (commitDate.before(until))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return The earliest <code>Date</code> that a commit can have been made
     * on and still be selected by this <code>DateCommitFilter</code>.
     */
    public Date getSince() {
        return since;
    }

    /**
     * @return The latest <code>Date</code> that a commit can have been made
     * on and still be selected by this <code>DateCommitFilter</code>.
     */
    public Date getUntil() {
        return until;
    }
}