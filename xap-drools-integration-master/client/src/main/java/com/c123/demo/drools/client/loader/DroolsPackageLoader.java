package com.c123.demo.drools.client.loader;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.UrlResource;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.gigaspaces.async.AsyncFuture;
import com.gigaspaces.droolsintegration.loader.NumberOfParitionsTask;
import com.gigaspaces.droolsintegration.model.drools.KnowledgeBaseWrapper;

@Component
public class DroolsPackageLoader {
    private static Logger log = Logger.getLogger(DroolsPackageLoader.class);   

    @GigaSpaceContext(name = "gigaSpace")
	private GigaSpace gigaSpace;
    
    @Value("${droolsWebServerAddress}")
    private String serverURL;
	
    @Resource(name="loadPackageNames")
    private List<String> loadPackageNames;
    
    @Value("${packagePathLocation}")
    private String packagePathLocation;
    
	private boolean workLocally=true;
    private final static String  PACKAGE_FILE_TYPE=".pkg";
    
	public DroolsPackageLoader() {
    	
	}
	
    public String getServerURL() {
		return serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
	
    public List<String> getLoadPackageNames() {
		return loadPackageNames;
	}

	public void setLoadPackageNames(List<String> loadPackageNames) {
		this.loadPackageNames = loadPackageNames;
	}
    
    public String getPackagePathLocation() {
		return packagePathLocation;
	}

	public void setPackagePathLocation(String packagePathLocation) {
		this.packagePathLocation = packagePathLocation;
	}
	
    @SuppressWarnings("resource")
	public static void main(String[] args) {   
    	log.info("Start");
    	
    	ClassPathXmlApplicationContext applicationContext = null;
		applicationContext = new ClassPathXmlApplicationContext("applicationContextPackageLoader.xml");
		DroolsPackageLoader droolsPackageLoader = (DroolsPackageLoader) applicationContext.getBean("droolsPackageLoader");
		for (String packageName : droolsPackageLoader.getLoadPackageNames()) {
			droolsPackageLoader.loadPackage(packageName, "admin" , "admin");
    	}
    	log.info("End");
    }
    
    
    public org.drools.io.Resource loadPackageFromFileSystem(String packageName) {
    	String path = packagePathLocation+packageName+PACKAGE_FILE_TYPE;
    	File file = new File(path);
    	org.drools.io.Resource loadedPackage = ResourceFactory.newFileResource(file);
    	return loadedPackage;
    }
    
    public org.drools.io.Resource loadPackageFromServer(String packageName , String username, String password) {
    	// URL location
        UrlResource url = (UrlResource) ResourceFactory.newUrlResource(this.serverURL + "/" + packageName + "/latest");
        url.setBasicAuthentication("enabled");
        url.setUsername(username);
        url.setPassword(password);
    	return (org.drools.io.Resource) url;
    }
    
	public void loadPackage(String packageName , String username, String password){
    	
    	KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    	
    	if (this.workLocally) {
    		// load packages from file system
    		org.drools.io.Resource url = loadPackageFromFileSystem(packageName);
    		kbuilder.add( url,	ResourceType.PKG );
    	} else {
    		// load package from server
    		org.drools.io.Resource url = loadPackageFromServer(packageName , username, password);
    		kbuilder.add( url,	ResourceType.PKG );
    	}
		
		// create the knowledge base
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

		// add the package to the kbase
		kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );
		
    	KnowledgeBaseWrapper wrapper = new KnowledgeBaseWrapper();
    	// KnowledgePackage kpackage = new KnowledgePackage();

    	for( org.drools.definition.KnowledgePackage kpkg : kbuilder.getKnowledgePackages() ) {
        	wrapper.setId(packageName);
        	wrapper.setKnowledgeBase(kbase);
        	wrapper.setRouting(parseNetworkId(packageName));
        	wrapper.setRuleSet(packageName);
        	wrapper.setTotalKnowledgePackages(kbuilder.getKnowledgePackages().size());
        	wrapper.setTotalRules(kpkg.getRules().size());
        	if (parseNetworkId(packageName) == 0) {
        		// write to a all partitions
        		try {
	            	AsyncFuture<Integer> future = gigaSpace.execute(new NumberOfParitionsTask());
	        	    int numberOfPartitions = future.get(); 
	        	    
	        	    for (int cnt=1 ; cnt <= numberOfPartitions; cnt++){
	        	    	wrapper.setRouting(cnt);
	        	    	gigaSpace.write(wrapper);
	        	    }
        		} catch (Exception ex) {
        			log.error("Unable to get number of partitions so send package name: "+ packageName + " to all partitions has failed !!!");
        			ex.printStackTrace();
        		}
        	} else {
        		// write to a specific partition
        		gigaSpace.write(wrapper);
        	}
    	}
    	
    }
    
    // Package name will be Enity_Network
    // For example InstantGameWager_1
    private Integer parseNetworkId(String packageName) {
    	String[] packageElements = packageName.split("_");
    	// log.info("parseNetworkId Entity=" + packageElements[0]);
    	// log.info("parseNetworkId Network=" + packageElements[1]);
    	if (packageElements.length<2) {
    		return 0;
    	}
    	return Integer.valueOf(packageElements[1]);
    }
    
    // Package name will be Enity_Network
    // For example InstantGameWager_1
    /*
    private String parsePackageName(String packageName) {
    	String[] packageElements = packageName.split("_");
    	log.info("parseNetworkId Entity=" + packageElements[0]);
    	log.info("parseNetworkId Network=" + packageElements[1]);
    	return packageElements[0];
    }
     */
}
