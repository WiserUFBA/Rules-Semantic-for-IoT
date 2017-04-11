package br.dcc.ufba.wiser.smartufba.reasoner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Properties;

import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Derivation;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.update.Update;
import org.apache.jena.util.FileManager;


public class Reasoner {

	private static String fname = "http://192.168.0.109:3030/sistemasweb/";

	private String updateTripleStore = "updateTripleStore.txt";
	
	public void init() {
		
		Reasoner reasoner = new Reasoner();
		
		try{
			reasoner.reasoner();
		}catch(	IOException | URISyntaxException e){
			System.out.println("Ocorreu um erro ao iniciar o Reasoner");
		}
	
	}
	
	
	


    public void reasoner() throws IOException, URISyntaxException {
			
		    Model data = FileManager.get().loadModel(fname );

	    GenericRuleReasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL("rules.txt"));
		    reasoner.setDerivationLogging(true);
		    InfModel inf = ModelFactory.createInfModel(reasoner, data);
		    
		    PrintWriter out = new PrintWriter(System.out);
		    
         for (StmtIterator i = inf.listStatements(); i.hasNext(); ) {
		        Statement s = i.nextStatement();
		        for (Iterator id = inf.getDerivation(s); id.hasNext(); ) {
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
    	UpdateModel updatemodel = new UpdateModel();
        //Caso ocorra o Match na inferência o modelo é atualizado  ()
        StringBuilder sb = new StringBuilder();
        Files.lines(Paths.get(ClassLoader.getSystemResource(updateTripleStore).toURI()))
            .forEach(line -> sb.append(line + "\n"));
        String tripleStoreURI = String.format(sb.toString(), subject.toString(), subject.toString(), subject.toString());
        updatemodel.updateTripleStore(tripleStoreURI, data,fname);
    }

    public static Properties getProp() throws IOException {
			Properties props = new Properties();
			FileInputStream file = new FileInputStream("./main/resources/br.ufba.dcc.wiser.smartufba.semanticrules.properties");
			props.load(file);
			return props;
		}
}
