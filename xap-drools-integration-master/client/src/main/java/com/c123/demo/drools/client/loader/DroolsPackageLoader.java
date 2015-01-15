package com.c123.demo.drools.client.loader;

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

import com.gigaspaces.droolsintegration.model.drools.KnowledgeBaseWrapper;
import com.gigaspaces.droolsintegration.model.drools.KnowledgePackage;

@Component
public class DroolsPackageLoader {
    private static Logger log = Logger.getLogger(DroolsPackageLoader.class);   

    @GigaSpaceContext(name = "gigaSpace")
	private GigaSpace gigaSpace;
    
    private int numberOfPartitions=0;
    
    @Resource
    private String serverURL;
    // private String serverURL = "http://localhost:8080/guvnor-webapp/org.drools.guvnor.Guvnor/package";
	
    @Resource(name="loadPackageNames")
    private List<String> loadPackageNames;
    
    
	public DroolsPackageLoader() {
		// TODO Auto-generated constructor stub
    	
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
    


	public void loadPackage(String packageName , String username, String password){
    	long time = System.currentTimeMillis();
		// URL location
        UrlResource url = (UrlResource) ResourceFactory.newUrlResource(this.serverURL + "/" + packageName + "/latest");
        url.setBasicAuthentication("enabled");
        url.setUsername(username);
        url.setPassword(password);
        
		// load package
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add( url,	ResourceType.PKG );
		
		
		// create the knowledge base
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

		// add the package to the kbase
		kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );
		
    	KnowledgeBaseWrapper wrapper = new KnowledgeBaseWrapper();
    	KnowledgePackage kpackage = new KnowledgePackage();

    	for( org.drools.definition.KnowledgePackage kpkg : kbuilder.getKnowledgePackages() ) {
        	wrapper.setId(packageName);
        	wrapper.setKnowledgeBase(kbase);
        	wrapper.setRouting(parseNetworkId(packageName));
        	wrapper.setRuleSet(packageName);
        	wrapper.setTotalKnowledgePackages(kbuilder.getKnowledgePackages().size());
        	wrapper.setTotalRules(kpkg.getRules().size());
        	gigaSpace.write(wrapper);
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
    private String parsePackageName(String packageName) {
    	String[] packageElements = packageName.split("_");
    	log.info("parseNetworkId Entity=" + packageElements[0]);
    	log.info("parseNetworkId Network=" + packageElements[1]);
    	return packageElements[0];
    }

}
