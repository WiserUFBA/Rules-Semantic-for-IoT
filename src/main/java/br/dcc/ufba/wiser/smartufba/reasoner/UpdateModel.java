package br.dcc.ufba.wiser.smartufba.reasoner;

import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateAction;

public class UpdateModel {


    public  void updateTripleStore(String tripleStoreURI, Model model, String adressModel) {

        UpdateAction.parseExecute(tripleStoreURI, model );

        DatasetAccessor accessor = DatasetAccessorFactory
            .createHTTP(adressModel);
        accessor.add(model);

        model.write( System.out, "TTL" );
    }
}
