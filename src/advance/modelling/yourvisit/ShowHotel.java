package advance.modelling.yourvisit;

import advance.modelling.yourvistit.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ShowHotel extends Activity {

	private final String TAG = "GPS-Tracking";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.show);

		String queryString = "PREFIX p: <http://dbpedia.org/property/>"
				+ "PREFIX dbpedia: <http://dbpedia.org/resource/>"
				+ "PREFIX category: <http://dbpedia.org/resource/Category:>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
				+ "PREFIX geo: <http://www.georss.org/georss/>" +

				"SELECT DISTINCT ?m ?n ?p ?d" + "WHERE {"
				+ " ?m rdfs:label ?n." + " ?m skos:subject ?c."
				+ " ?c skos:broader category:Churches_in_Paris."
				+ " ?m p:abstract ?d." + " ?m geo:point ?p" +

				" FILTER ( lang(?n) = \"fr\" )"
				+ " FILTER ( lang(?d) = \"fr\" )" + " }";

		// For the sparql query
		String sparqlQueryString1 = "PREFIX dcterms: <http://purl.org/dc/terms/>"
				+ "SELECT ?torero ?cantante WHERE{"
				+ "?torero rdf:type dbpedia-owl:BullFighter ."
				+ "?torero dbpedia-owl:spouse ?cantante ."
				+ "?cantante dcterms:subject <http://es.dbpedia.org/resource/Categoría:Cantantes_de_coplas>"
				+ "}";

		// Log.i(TAG, queryString);

		// String sparqlQueryString2 =
		// "PREFIX dcterms: <http://purl.org/dc/terms/>"
		// + "SELECT ?torero ?cantante WHERE{"
		// + "?torero rdf:type dbpedia-owl:BullFighter ."
		// + "?torero dbpedia-owl:spouse ?cantante ."
		// +
		// " ?cantante dcterms:subject <http://es.dbpedia.org/resource/Categoría:Cantantes_de_coplas>"
		// + "}";
		
		String query2 = "select distinct ?Concept where {[] a ?Concept} LIMIT 100";

//		com.hp.hpl.jena.query.Query query = QueryFactory
//				.create(query2);
//
//		QueryExecution qexec = QueryExecutionFactory.sparqlService(
//				"http://dbpedia.org/sparql", query);
//
//		// ResultSetFormatter.out(System.out, results, query);
//
//		try {
//			ResultSet results = qexec.execSelect();
//			for (; results.hasNext();) {
//				QuerySolution soln = results.nextSolution();
//				String x = soln.get("Concept").toString();
//				Log.i(TAG, x + "\n");
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			Log.e(TAG, e.toString());
//		}
//
//		finally {
//			qexec.close();
//		}

		TextView mTextView = (TextView) findViewById(R.id.textViewShowRestaurant);

		Intent myIntent = getIntent();

		Log.i(TAG, String.valueOf(myIntent.getFloatExtra("stars", 0)));

		if (myIntent.getStringExtra("Choice").equals("Hotel")) {
			mTextView.setText(myIntent.getStringExtra("Choice") + " - "
					+ myIntent.getStringExtra("Price") + "NOK - "
					+ myIntent.getStringExtra("Distance") + "m - "
					+ myIntent.getFloatExtra("Stars", 0) + " stars " + " Lat: "
					+ myIntent.getDoubleExtra("Lat", 0) + " - Long: "
					+ myIntent.getDoubleExtra("Long", 0));
		}

		if (myIntent.getStringExtra("Choice").equals("Motel")) {
			mTextView.setText(myIntent.getStringExtra("Choice") + " - "
					+ myIntent.getStringExtra("Price") + "NOK - "
					+ myIntent.getStringExtra("Distance") + "m - "
					+ "with breakfast: " + myIntent.getStringExtra("breakfast")
					+ " Lat: " + myIntent.getDoubleExtra("Lat", 0)
					+ " - Long: " + myIntent.getDoubleExtra("Long", 0));
		}
	}
}
