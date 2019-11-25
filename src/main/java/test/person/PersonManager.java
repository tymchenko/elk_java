package test.person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PersonManager {
	private static final String HOST = "localhost";
	private static final int PORT_ONE = 9200;
	private static final int PORT_TWO = 9201;
	private static final String SCHEME = "http";

	private static RestHighLevelClient restHighLevelClient;
	private static ObjectMapper objectMapper = new ObjectMapper();

	private static final String INDEX = "persondata";
	private static final String TYPE = "person";

		public static synchronized void closeConnection() throws IOException {
		restHighLevelClient.close();
		restHighLevelClient = null;
	}

	static Person insertPerson(Person person){
		person.setPersonId(UUID.randomUUID().toString());
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("personId", person.getPersonId());
		dataMap.put("name", person.getName());
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, person.getPersonId())
				.source(dataMap);
		try {
			IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
		} catch(ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex){
			ex.getLocalizedMessage();
		}
		return person;
	}

	public static Person getPersonById(String id){
		GetRequest getPersonRequest = new GetRequest(INDEX, TYPE, id);
		GetResponse getResponse = null;
		try {
			getResponse = restHighLevelClient.get(getPersonRequest, RequestOptions.DEFAULT);
		} catch (java.io.IOException e){
			e.getLocalizedMessage();
		}
		return getResponse != null ?
				objectMapper.convertValue(getResponse.getSourceAsMap(), Person.class) : null;
	}

	static Person updatePersonById(String id, Person person){
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
				.fetchSource(true);    // Fetch Object after its update
		try {
			String personJson = objectMapper.writeValueAsString(person);
			updateRequest.doc(personJson, XContentType.JSON);
			UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
			return objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), Person.class);
		}catch (JsonProcessingException e){
			e.getMessage();
		} catch (java.io.IOException e){
			e.getLocalizedMessage();
		}
		System.out.println("Unable to update person");
		return null;
	}

	public static synchronized RestHighLevelClient makeConnection() {

		if(restHighLevelClient == null) {
			restHighLevelClient = new RestHighLevelClient(
					RestClient.builder(
							new HttpHost(HOST, PORT_ONE, SCHEME),
							new HttpHost(HOST, PORT_TWO, SCHEME)));
		}

		return restHighLevelClient;
	}

	public static void deletePersonById(String id) {
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
		try {
			DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
		} catch (java.io.IOException e){
			e.getLocalizedMessage();
		}
	}


}
