package test.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ConnectionManager {
	private static final String HOST = "localhost";
	private static final int PORT_ONE = 9200;
	private static final int PORT_TWO = 9201;
	private static final String SCHEME = "http";

	private static RestHighLevelClient restHighLevelClient;
	private static ObjectMapper objectMapper = new ObjectMapper();

	private static final String INDEX = "persondata";
	private static final String TYPE = "person";

	private static synchronized RestHighLevelClient makeConnection() {

		if(restHighLevelClient == null) {
			restHighLevelClient = new RestHighLevelClient(
					RestClient.builder(
							new HttpHost(HOST, PORT_ONE, SCHEME),
							new HttpHost(HOST, PORT_TWO, SCHEME)));
		}

		return restHighLevelClient;
	}

	private static synchronized void closeConnection() throws IOException {
		restHighLevelClient.close();
		restHighLevelClient = null;
	}
}
