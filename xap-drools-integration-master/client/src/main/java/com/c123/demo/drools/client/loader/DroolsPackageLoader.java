package com.c123.demo.drools.client.loader;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.drools.core.io.impl.UrlResource;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.io.ResourceFactory;
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
    private final static String  DRL_FILE_TYPE=".drl";
    
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
			// KieContainer contianer = droolsPackageLoader.createKieContainerForProject(packageName);
			
    	}
    	log.info("End");
    }
    
    
    public org.kie.api.io.Resource loadPackageFromFileSystem(String packageName) {
    	String path = packagePathLocation+packageName+PACKAGE_FILE_TYPE;
    	File file = new File(path);
    	org.kie.api.io.Resource loadedPackage = ResourceFactory.newFileResource(file);
    	return loadedPackage;
    }
    
    public org.kie.api.io.Resource loadDRLFromFileSystem(String packageName) {
    	String path = packagePathLocation+packageName+DRL_FILE_TYPE;
    	File file = new File(path);
    	org.kie.api.io.Resource loadedPackage = ResourceFactory.newFileResource(file);
    	return loadedPackage;
    }
    
    public org.kie.api.io.Resource loadPackageFromServer(String packageName , String username, String password) {
    	// URL location
    	UrlResource url = (UrlResource) ResourceFactory.newUrlResource(this.serverURL + "/" + packageName + "/latest");
        url.setBasicAuthentication("enabled");
        url.setUsername(username);
        url.setPassword(password);
    	return (org.kie.api.io.Resource) url;
    }
    
	public void loadPackage(String packageName , String username, String password) {
		log.info("loadPackage " + packageName);
   	
    	KieBase kbase = createKieContainerForProject(packageName);
    	
    	KnowledgeBaseWrapper wrapper = new KnowledgeBaseWrapper();

    	for( KiePackage kpkg : kbase.getKiePackages() ) {
    		log.info("Package name:" + kpkg.getName());
    		log.info("# of rules:" + kpkg.getRules().size());
        	if (kpkg.getRules().size() > 0) {
        		wrapper.setId(kpkg.getName());
            	wrapper.setKnowledgeBase(kbase);
            	wrapper.setRuleSet(kpkg.getName());
            	wrapper.setTotalKnowledgePackages(1);
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
            		wrapper.setRouting(parseNetworkId(kpkg.getName()));
        	    	gigaSpace.write(wrapper);
            	}
        	}
    		/*
            	try {
    				Thread.sleep(1500);
    				KnowledgeBaseWrapper template = new KnowledgeBaseWrapper();
    				template.setRouting(wrapper.getRouting());
    				KnowledgeBaseWrapper test = gigaSpace.read(template);
    				log.info("# of packages:" + test.getKnowledgeBase().getKiePackages().size());
    				Rule  rule = test.getKnowledgeBase().getRule("InstantGameWagerComprisedNewGen_1", "DepositMade");
    				if (rule != null) {
    					log.info(rule.getName());
    				}
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
        	}
        	*/
        	

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
    
    
    public KieBase createKieContainerForProject(String location) {
    	String path = packagePathLocation+location+DRL_FILE_TYPE;
    	log.info("path=" + path);
        KieServices kieServices = KieServices.Factory.get();
        // Create File System services
        KieFileSystem kFileSystem = kieServices.newKieFileSystem();

        // File file = new File(path);
        // org.kie.api.io.Resource resource = kieServices.getResources().newFileSystemResource(path).setResourceType(ResourceType.DRL);
        // kFileSystem.write( resource );       
        kFileSystem.write( kieServices.getResources().newFileSystemResource(path).setResourceType(ResourceType.DRL) );
        KieBuilder kbuilder = kieServices.newKieBuilder( kFileSystem );
        
        // kieModule is automatically deployed to KieRepository if successfully built.
        kbuilder.buildAll();

        if (kbuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            throw new RuntimeException("Build time Errors: " + kbuilder.getResults().toString());
        }
        KieContainer kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        return kContainer.getKieBase();
    }
}
