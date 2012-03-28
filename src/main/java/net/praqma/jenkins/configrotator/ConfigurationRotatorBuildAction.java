package net.praqma.jenkins.configrotator;

import java.io.IOException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import net.praqma.jenkins.configrotator.ConfigurationRotator.ResultType;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.scm.SCM;

public class ConfigurationRotatorBuildAction implements Action {
	
	private AbstractBuild<?, ?> build;
	private Class<? extends AbstractConfigurationRotatorSCM> clazz;
	private ResultType result = ResultType.UNDETERMINED;
	private AbstractConfiguration configuration;
	
	public ConfigurationRotatorBuildAction( AbstractBuild<?, ?> build, Class<? extends AbstractConfigurationRotatorSCM> clazz, AbstractConfiguration configuration ) {
		this.build = build;
		this.clazz = clazz;
		this.configuration = configuration;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	
	public boolean isFresh() {
		SCM scm = build.getProject().getScm();
		if( scm instanceof ConfigurationRotator ) {
			return ((ConfigurationRotator)scm).getAcrs().isFresh();
		} else {
			return false;
		}
	}
	
	
	public void doReset( StaplerRequest req, StaplerResponse rsp ) throws IOException, ServletException {
		SCM scm = build.getProject().getScm();
		if( scm instanceof ConfigurationRotator ) {
			((ConfigurationRotator)scm).setFresh( build.getProject(), true );
			rsp.forwardToPreviousPage( req );
		} else {
			rsp.sendError( StaplerResponse.SC_BAD_REQUEST, "Not Configuration Rotator job" );
		}
	}
	
	public void setResult( ResultType result ) {
		this.result = result;
	}
	
	public boolean isDetermined() {
		return result.equals( ResultType.COMPATIBLE ) || result.equals( ResultType.INCOMPATIBLE );
	}
	
	public ResultType getResult() {
		return result;
	}

	@Override
	public String getIconFileName() {
		return "graph.gif";
	}

	@Override
	public String getDisplayName() {
		return "Config Rotator";
	}

	@Override
	public String getUrlName() {
		return "config-rotator";
	}

	public AbstractBuild<?, ?> getBuild() {
		return build;
	}
	
	public AbstractConfiguration getConfiguration() {
		return configuration;
	}
	
	public String toString() {
		return configuration.toString();
	}
}