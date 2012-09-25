package net.praqma.jenkins.configrotator;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import net.praqma.util.xml.feed.AtomPublisher;
import net.praqma.util.xml.feed.Feed;
import net.praqma.util.xml.feed.FeedException;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.UnprotectedRootAction;
import hudson.model.AbstractModelObject;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import jenkins.model.Jenkins;

@Extension
public class ConfigurationRotatorReport extends AbstractModelObject implements UnprotectedRootAction {
    
    private static final String XML_EXTENSION = ".xml";
    private static final int PORT = 8080;
    private static final String DEFAULT_URL = "http://localhost:"+PORT;
     
	@Override
	public String getIconFileName() {
		return "/plugin/config-rotator/images/rotate.png";
	}

	@Override
	public String getDisplayName() {
		return "Config Rotator";
	}

	@Override	
    public String getUrlName() {		
        return "config-rotator";
	}

	@Override
	public String getSearchUrl() {
		return getUrlName();
	}




    public static Feed getFeedFromFile( File feedFile, String name, String feedId, Date feedUpdated ) throws FeedException, IOException {
        if( feedFile.exists() ) {
            return Feed.getFeed( new AtomPublisher(), feedFile );
        } else {
            return new Feed( name, feedId, feedUpdated );
        }
    }
    
    /**
     * Factory to create url when subscribing to feeds
     * 
     */ 

    public static String FeedFrontpageUrl() {
        String url = (Jenkins.getInstance() == null || Jenkins.getInstance().getRootUrl() == null) ? DEFAULT_URL : Jenkins.getInstance().getRootUrl();
        url+= "/"+ConfigurationRotator.URL_NAME+"/";
        return url;
    }

    /**
     * Factory method to create the job url for our feed.
     * 
     */
    
    public static String GenerateJobUrl(AbstractBuild<?,?> build) {
        String url = (Jenkins.getInstance() == null || Jenkins.getInstance().getRootUrl() == null) ? DEFAULT_URL : Jenkins.getInstance().getRootUrl();
        String actionLink = url + "/" + build.getUrl();
        return actionLink;
    }

    /**
     * 
     * @return a list of available feeds in link format. 
     */
    public ArrayList<String> listAvailableFeeds() {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(ConfigurationRotator.FEED_PATH.list()));
        return list;
    }


	// yourhost/config-rotator/feed/
	// eg. http://localhost:8080/config-rotator/feed/?component=hest2
    // TODO: we should do some input parameter check and validation
	public HttpResponse doFeed( @QueryParameter( required = true ) String component, @QueryParameter( required = true ) String pvob) throws ServletException, IOException {
		final String mycomp = pvob+ConfigurationRotator.SEPARATOR+component+XML_EXTENSION;
        final String fullComponentFeedPath = ConfigurationRotator.FEED_PATH+mycomp;
		return new HttpResponse() {

			@Override
			public void generateResponse( StaplerRequest req, StaplerResponse rsp, Object node ) throws IOException, ServletException {
				rsp.setStatus( StaplerResponse.SC_OK );
                
                rsp.setContentType("application/atom+xml");

                FileInputStream fis = null;
                InputStreamReader isr = null;
                PrintWriter writer = null;
                BufferedReader reader = null;
				
                //Open file to begin reading file. 
                try {
                    File feedFile = new File(fullComponentFeedPath);
                    if(feedFile.exists()) {
                        fis = new FileInputStream(new File(fullComponentFeedPath));
                        isr = new InputStreamReader(fis);

                        reader = new BufferedReader(isr);
                        writer = rsp.getWriter();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            writer.write(line);
                        }
                    } else {
                        writer.write("No such feed");
                    }
                    } catch (IOException ex) {
                        throw ex;
                    } finally {
                        if(writer != null)
                            writer.close();
                        if(reader != null)
                            reader.close();
                    }
				
			}			
		};
	}
}
