package br.dcc.ufba.wiser.smartufba.reasoner;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Properties;

import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Derivation;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Reasoner {

	private final static Logger LOGGER = Logger.getLogger(Reasoner.class.getName());
	
	private String prefix;
	private String adressPrefix;
        private String adressFile;
        private Path newFilePath;
        private Path dir;
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getAdressPrefix() {
		return adressPrefix;
	}

	public void setAdressPrefix(String adressPrefix) {
		this.adressPrefix = adressPrefix;
	}

	private String fusekiServer ;
	
	
	public String getFusekiServer() {
		return fusekiServer;
	}

	public void setFusekiServer(String fusekiServer) {
		this.fusekiServer = fusekiServer;
	}

	
	public Reasoner(){
		
	}
	
	public void init() {
		LOGGER.info("Starting the reasoner");
	}
	
	
	public void createFile(){
            LOGGER.info("Create file log ");
	          
            
            Path path = Paths.get(adressFile);
            try{
                
            if(Files.isDirectory(path)){   
                
                dir = Paths.get(path.toUri());
                
            }else{  
                dir =  Files.createDirectories(path);
            }
            
          
            this.newFilePath = path.resolve("reg.txt");
           
            Files.write(newFilePath, "{send: 1}\n".getBytes() ,StandardOpenOption.APPEND);
            
            
            }catch(IOException v){
               System.out.print(v.getMessage());
            }
        }


    public void reasoner() throws IOException, URISyntaxException {
    	    
    		LOGGER.info("Rationing on the model");
    		
    		Model data = FileManager.get().loadModel(fusekiServer );
		
                createFile();
    		
    		GenericRuleReasoner reasoner = null;
		    
		    PrintUtil.registerPrefix(prefix, adressPrefix);
		    
		    String rules = "[rule1: (?a j.0:hasDataValue ?b) greaterThan(?b, 36) -> (?a highTemperature true)]";
		    
		    LOGGER.info("Getting rules: " + rules);
		    
		    		  	   
		    try{
		    	reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		    }catch(Exception x){
		    	x.printStackTrace();
		    }
		    
		    LOGGER.info("Performing parse in rule");
		   
		    reasoner.setDerivationLogging(true);
		    
		    InfModel inf = ModelFactory.createInfModel(reasoner, data);
		    
		    PrintWriter out = new PrintWriter(System.out);
		    
		     
         for (StmtIterator i = inf.listStatements(); i.hasNext(); ) {
        	 Statement s = i.nextStatement();
        	 for (Iterator<Derivation> id = inf.getDerivation(s); id.hasNext(); ) {
        		 	LOGGER.info("Statement is " + s);
		            Derivation deriv = (Derivation) id.next();
		            deriv.printTrace(out, true);
                    RDFNode object = s.getObject();
                    LOGGER.info("Object is " + object.toString());
                    updateModel(data, s.getSubject());
                    createFile();
                }
		    }
		    out.flush();
		
		}

    private void updateModel(Model data, Resource subject) throws IOException, URISyntaxException {
    	System.out.println("updateModel");
    	UpdateModel updatemodel = new UpdateModel();
        //Caso ocorra o Match na inferência o modelo é atualizado  ()
     // StringBuilder sb = new StringBuilder();
       String updateTripleStore2 = "PREFIX  j.1: <http://purl.oclc.org/NET/ssnx/ssn#>\n" + 
	    		"PREFIX  j.0: <http://www.loa-cnr.it/ontologies/DUL.owl#>\n" +
	    		"PREFIX xsd:   <http://www.w3.org/2001/XMLSchema#>" +
	    		"DELETE { <%s>  j.0:isSettingFor  false .}\n" +
        	"INSERT { <%s>  j.0:isSettingFor  true .}\n" +
        	"WHERE { <%s> j.0:isSettingFor  false . }";
        	StringBuilder sb = new StringBuilder(updateTripleStore2);
        try{
       // 	Files.lines(Paths.get(ClassLoader.getSystemResource(updateTripleStore).toURI()))
          // .forEach(line -> sb.append(line + "\n"));
        String tripleStoreURI = String.format(sb.toString(), subject.toString(), subject.toString(), subject.toString());
        updatemodel.updateTripleStore(tripleStoreURI, data,fusekiServer);
        } catch(Exception e){
    	e.printStackTrace();
    }
    	
    
        }

    public static Properties getProp() throws IOException {
			Properties props = new Properties();
			FileInputStream file = new FileInputStream("./main/resources/br.ufba.dcc.wiser.smartufba.semanticrules.properties");
			props.load(file);
			return props;
		}
		/*
    public static void main (String args[]){
    	Reasoner r = new Reasoner();
    	try{
    	r.reasoner();
    	}catch(Exception e ){
    		e.printStackTrace();
    	}
    }
	*/

    /**
     * @return the adressFile
     */
    public String getAdressFile() {
        return adressFile;
    }

    /**
     * @param adressFile the adressFile to set
     */
    public void setAdressFile(String adressFile) {
        this.adressFile = adressFile;
    }

}
