package net.praqma.jenkins.configrotator.scm.clearcaseucm;

import hudson.model.AbstractBuild;
import net.praqma.clearcase.exceptions.ClearCaseException;
import net.praqma.clearcase.ucm.entities.Activity;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Project.PromotionLevel;
import net.praqma.clearcase.ucm.entities.Version;
import net.praqma.html.Html;
import net.praqma.jenkins.configrotator.*;
import net.praqma.jenkins.configrotator.scm.ConfigRotatorChangeLogEntry;
import net.praqma.jenkins.configrotator.scm.ConfigRotatorVersion;
import net.praqma.util.xml.feed.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClearCaseUCMConfigurationComponent extends AbstractConfigurationComponent {

    private static Logger logger = Logger.getLogger( ClearCaseUCMConfigurationComponent.class.getName() );

    private Baseline baseline;
    private PromotionLevel plevel;

    public ClearCaseUCMConfigurationComponent( Baseline baseline, PromotionLevel plevel, boolean fixed ) {
        super( fixed );
        this.baseline = baseline;
        this.plevel = plevel;
    }

    public ClearCaseUCMConfigurationComponent( String baseline, String plevel, boolean fixed ) throws ClearCaseException {
        super( fixed );
        this.baseline = Baseline.get( baseline ).load();
        this.plevel = Project.PromotionLevel.valueOf( plevel );

    }

    @Override
    public ClearCaseUCMConfigurationComponent clone() {
        ClearCaseUCMConfigurationComponent cc = new ClearCaseUCMConfigurationComponent( this.baseline, this.plevel, this.fixed );

        return cc;
    }

    public void setBaseline( Baseline baseline ) {
        this.baseline = baseline;
    }

    public Baseline getBaseline() {
        return baseline;
    }

    public PromotionLevel getPlevel() {
        return plevel;
    }

    @Override
    public String toString() {
        return baseline.getNormalizedName() + "@" + plevel + "(" + fixed + "/" + changedLast + ")";
    }

    @Override
    public String prettyPrint() {
        return baseline.getNormalizedName() + "@" + plevel;
    }

    @Override
    public boolean equals( Object other ) {
        if( other == this ) {
            return true;
        }

        if( other instanceof ClearCaseUCMConfigurationComponent ) {
            ClearCaseUCMConfigurationComponent o = (ClearCaseUCMConfigurationComponent) other;

            return ( o.baseline.equals( baseline ) && ( o.plevel.equals( plevel ) ) && ( o.isFixed() == fixed ) );
        } else {
            return false;
        }
    }

    @Override
    public String getComponentName() {
        return baseline.getNormalizedName();
    }

    @Override
    public String getFeedName() {
        return baseline.getComponent().getNormalizedName();
    }

    @Override
    public String getFeedId() {
        return baseline.getComponent().getNormalizedName();
    }

    /**
     *
     * @return a html'ified version of this clearcase components. In this case it is a table row.
     */
    @Override
    public String toHtml() {
        StringBuilder builder = new StringBuilder();
        builder.append( "<tr>" );
        builder.append( "<td style=\"padding:5px 10px;\">" ).append( baseline.getComponent().getNormalizedName() ).append( "</td>" );
        builder.append( "<td style=\"padding:5px 10px;\">" ).append( baseline.getStream().getNormalizedName() ).append( "</td>" );
        if( isChangedLast() ) {
            builder.append( "<td style=\"font-weight:bold;color:#FF6633;padding:5px 10px;\">" ).append( baseline.getNormalizedName() ).append( "</td>" );
        } else {
            builder.append( "<td style=\"padding:5px 10px;\">" ).append( baseline.getNormalizedName() ).append( "</td>" );
        }
        builder.append( "<td style=\"padding:5px 10px;\">" ).append( plevel.toString() ).append( "</td>" );
        builder.append( "<td style=\"padding:5px 10px;\">" ).append( fixed ).append( "</td>" ).append( "</tr>" );
        return builder.toString();
    }


}