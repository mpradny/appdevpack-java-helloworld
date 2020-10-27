package to.pris.proton;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.hcl.domino.db.model.BulkOperationException;
import com.hcl.domino.db.model.Database;
import com.hcl.domino.db.model.Document;
import com.hcl.domino.db.model.Item;
import com.hcl.domino.db.model.Server;
import com.hcl.domino.db.model.TextItem;

public class FirstTest {

	static {
		String path;
		try {
			path = FirstTest.getFile("logging.properties").getAbsolutePath();
			System.setProperty("java.util.logging.config.file", path);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private static final Logger logger = Logger.getLogger(FirstTest.class.getName());

	public static void main(String[] args)
			throws IOException, BulkOperationException, InterruptedException, ExecutionException {
		ExecutorService pool = Executors.newFixedThreadPool(1);

		logger.info("Starting");
		try {
			Server server = new Server("srv1.pris.to", 3003, getFile("trust.crt"), getFile("protonapp1.crt"),
					getFile("protonapp1-pkcs8.key"), null, "", pool);

			Database client = server.useDatabase("protontest.nsf");

			List<Item<?>> itemList = new ArrayList<Item<?>>();
			itemList.add(new TextItem("Form", "Contact"));
			itemList.add(new TextItem("FirstName", "Aaron"));
			itemList.add(new TextItem("LastName", "Aardman"));
			itemList.add(new TextItem("City", "Arlington"));
			itemList.add(new TextItem("State", "MA"));

			Document response = client.createDocument(new Document(itemList)).get();

			server.closeServer();
		} finally {
			pool.shutdownNow();
		}
		
		logger.info("Done");
	}

	private static File getFile(String fileName) throws UnsupportedEncodingException {
		return new File(URLDecoder.decode(ClassLoader.getSystemResource(fileName).getFile(), "UTF-8"));
	}

}
