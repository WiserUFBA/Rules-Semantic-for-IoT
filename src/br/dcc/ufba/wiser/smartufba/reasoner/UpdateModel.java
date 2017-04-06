package br.dcc.ufba.wiser.smartufba.reasoner;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;

public class UpdateModel {

	
	
	private synchronized void updateTripleStore(Model model,
			String tripleStoreURI) {
		System.out.println("entrou updateTripleStore " + tripleStoreURI);
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP(tripleStoreURI);
		accessor.add(model);

	}
}
