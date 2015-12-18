package glory.comparedick.data;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

public class KillsTDB {

	private static final String TDB_NAME = "dicks_kills";

	private Dataset _dataset;

	public KillsTDB() {
		_dataset = TDBFactory.createDataset(TDB_NAME);
	}

	public void insertSomething() {
		_dataset.begin(ReadWrite.WRITE);

		UpdateRequest request = UpdateFactory
				.create("INSERT DATA { <http://example.org> a <http://example.org/class> }");
		UpdateProcessor proc = UpdateExecutionFactory.create(request, _dataset);
		proc.execute();

		_dataset.end();
	}
	
	public static void main(String [] args) {
		new KillsTDB().insertSomething();
	}

}
