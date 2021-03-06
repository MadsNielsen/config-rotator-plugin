package net.praqma.jenkins.configrotator.scm.git;

import net.praqma.jenkins.configrotator.AbstractTarget;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;

public class GitTarget extends AbstractTarget implements Serializable {

    private String name;
    private String repository;
    private String branch;
    private String commitId;
    private boolean fixed;

    public GitTarget() {
    }

    @DataBoundConstructor
    public GitTarget(String name, String repository, String branch, String commitId, boolean fixed) {
        this.name = name;
        this.repository = repository;
        this.branch = branch;
        this.commitId = commitId;
        this.fixed = fixed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean getFixed() {
        return fixed;
    }

    @Override
    public String toString() {
        return String.format("%s, %s", repository, branch);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other instanceof GitTarget) {
            GitTarget o = (GitTarget) other;

            //return repository.equals( o.repository );
            return commitId.equals(o.commitId);
        } else {
            return false;
        }
    }
}
