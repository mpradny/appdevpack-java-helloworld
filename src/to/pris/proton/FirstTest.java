package to.pris.proton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hcl.domino.db.model.BulkOperationException;
import com.hcl.domino.db.model.Database;
import com.hcl.domino.db.model.Document;
import com.hcl.domino.db.model.Item;
import com.hcl.domino.db.model.Server;
import com.hcl.domino.db.model.TextItem;

public class FirstTest {

	public static void main(String[] args) throws IOException, BulkOperationException, InterruptedException, ExecutionException {
		ExecutorService pool = Executors.newFixedThreadPool(1);
		Server server = new Server("srv1.pris.to", 3003,		              // 
		        pool);
		Database client = server.useDatabase("protontest.nsf");

		List<Item<?>> itemList = new ArrayList<Item<?>>();
		    itemList.add(new TextItem("Form","Contact"));
		    itemList.add(new TextItem("FirstName","Aaron"));
		    itemList.add(new TextItem("LastName","Aardman"));
		    itemList.add(new TextItem("City","Arlington"));
		    itemList.add(new TextItem("State","MA"));

		Document response = client.createDocument(new Document(itemList)).get();

		server.closeServer();
		pool.shutdownNow();
	}

}
