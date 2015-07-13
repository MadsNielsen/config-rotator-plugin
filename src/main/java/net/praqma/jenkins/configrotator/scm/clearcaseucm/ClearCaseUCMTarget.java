package net.praqma.jenkins.configrotator.scm.clearcaseucm;

import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.jenkins.configrotator.AbstractTarget;
import net.praqma.jenkins.configrotator.Incrementor;
import org.kohsuke.stapler.DataBoundConstructor;

public class ClearCaseUCMTarget extends AbstractTarget {

    private String component;
    private String baselineName;
    private Project.PromotionLevel level;
    
    @Deprecated
    private transient boolean fixed;
    
    private Incrementor incrementor;

    public ClearCaseUCMTarget() {
    }

    /**
     * Warning: Only one databound constructor per component. Figured this out
     * the hard way.
     *
     * @param component
     */
    public ClearCaseUCMTarget(String component) {
        this.component = component;
    }

    /**
     * New constructor. Builds a correct component string for backwards
     * compatability.
     *
     * @param baselineName
     * @param level
     * @param fixed
     */
    
    @Deprecated
    public ClearCaseUCMTarget(String baselineName, Project.PromotionLevel level, boolean fixed) {
        this.component = baselineName + ", " + level + ", " + fixed;
        this.baselineName = baselineName;
        this.level = level;
        this.fixed = fixed;
        this.incrementor = fixed ? Incrementor.FIXED : Incrementor.NEXT;
    }
    
    @DataBoundConstructor
    public ClearCaseUCMTarget(String baselineName, Project.PromotionLevel level, String incrementor) {
        this.component = baselineName + ", " + level + ", " + fixed;
        this.baselineName = baselineName;
        this.level = level;
        this.incrementor = Incrementor.valueOf(incrementor.toUpperCase());
        this.fixed = this.incrementor == Incrementor.FIXED;
    }
    
    public ClearCaseUCMTarget(String baselineName, Project.PromotionLevel level, Incrementor incrementor) {
        this.component = baselineName + ", " + level + ", " + fixed;
        this.baselineName = baselineName;
        this.level = level;
        this.incrementor = incrementor;
        this.fixed = this.incrementor == Incrementor.FIXED;
    }
    

    public String getComponent() {
        return component;
    }

    public String getBaselineName() {
        return baselineName;
    }

    public void setBaselineName(String baselineName) {
        this.baselineName = baselineName;
    }

    public Project.PromotionLevel getLevel() {
        return level;
    }

    public void setLevel(Project.PromotionLevel level) {
        this.level = level;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean getFixed() {
        if(incrementor != null) {
            return incrementor == Incrementor.FIXED; 
        }  
        return fixed;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    @Override
    public String toString() {
        return String.format("%s", component);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other instanceof ClearCaseUCMTarget) {
            ClearCaseUCMTarget o = (ClearCaseUCMTarget) other;

            return component.equals(o.getComponent());
        } else {
            return false;
        }
    }

    /**
     * @return the incrementor
     */
    public Incrementor getIncrementor() {
        return incrementor;
    }

    /**
     * @param incrementor the incrementor to set
     */
    public void setIncrementor(Incrementor incrementor) {
        this.incrementor = incrementor;
    }
}
