package br.dcc.ufba.wiser.smartufba.reasoner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Derivation;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;


public class Reasoner {

	private static String fname = "http://192.168.0.13:3030/sistemasweb";

	private String updateTripleStore = "updateTripleStore.txt";
	
	public Reasoner(){
		
	}
	
	public void init() {
		System.out.println("init");
	}
	
	
	


    public void reasoner() throws IOException, URISyntaxException {
    	    Model data = FileManager.get().loadModel(fname );
		    System.out.println("Racionando no modelo");
		    GenericRuleReasoner reasoner = null;
		    PrintUtil.registerPrefix("j.0", "http://www.loa-cnr.it/ontologies/DUL.owl#");
		    String rules = "[rule1: (?a j.0:hasDataValue ?b) greaterThan(?b, 36) -> (?a highTemperature true)]";
		    System.out.println(rules);
		
		   
		    try{
		    	reasoner = new GenericRuleReasoner(Rule.parseRules(rules));
		    }catch(Exception x){
		    	x.printStackTrace();
		    }
		    System.out.println("Racionando no modelo2");
		    reasoner.setDerivationLogging(true);
		    
		    InfModel inf = ModelFactory.createInfModel(reasoner, data);
		    
		    PrintWriter out = new PrintWriter(System.out);
		    System.out.println("Racionando no modelo3");
		    
		     
         for (StmtIterator i = inf.listStatements(); i.hasNext(); ) {
        	 Statement s = i.nextStatement();
        	 for (Iterator<Derivation> id = inf.getDerivation(s); id.hasNext(); ) {
                    System.out.println("Statement is " + s);
		            Derivation deriv = (Derivation) id.next();
		            deriv.printTrace(out, true);
                    RDFNode object = s.getObject();
                    System.out.println("Object is " + object.toString());
                    updateModel(data, s.getSubject());
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
        updatemodel.updateTripleStore(tripleStoreURI, data,fname);
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

}
