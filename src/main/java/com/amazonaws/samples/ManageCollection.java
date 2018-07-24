package com.amazonaws.samples;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.DeleteCollectionRequest;
import com.amazonaws.services.rekognition.model.DeleteCollectionResult;
import com.amazonaws.services.rekognition.model.DeleteFacesRequest;
import com.amazonaws.services.rekognition.model.Face;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.ListCollectionsRequest;
import com.amazonaws.services.rekognition.model.ListCollectionsResult;
import com.amazonaws.services.rekognition.model.ListFacesRequest;
import com.amazonaws.services.rekognition.model.ListFacesResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ManageCollection {
	
	final static String collectionId = "MyCollection";
	
	public void createCollection(AmazonRekognition rekognitionClient) {
		
		CreateCollectionRequest request = new CreateCollectionRequest().withCollectionId(collectionId);
		
		CreateCollectionResult result = rekognitionClient.createCollection(request);
		System.out.println("CollectionArn : " +
				result.getCollectionArn());
				System.out.println("Status code : " +
						result.getStatusCode().toString());
		
	}
	
	public void listCollection(AmazonRekognition rekognitionClient) {
		
		ListCollectionsResult result = null;
		String paginationToken = null;
		
		do {
			if(result!=null) {
				paginationToken = result.getNextToken();
			}
			ListCollectionsRequest request = new ListCollectionsRequest().withMaxResults(10)
					.withNextToken(paginationToken);
			
			result = rekognitionClient.listCollections(request);
			
			List<String> collectionIds = result.getCollectionIds();
			for(String resultId:collectionIds) {
				System.out.println(resultId);
			}
		}while(result !=null && result.getNextToken()!=null);		
	}
	
	public void deleteCollection(AmazonRekognition rekognitionClient) {
		DeleteCollectionRequest request = new DeleteCollectionRequest().withCollectionId(collectionId);
		DeleteCollectionResult result = rekognitionClient.deleteCollection(request);
		
		System.out.println(collectionId + ": " +
				result.getStatusCode()
				.toString());
	}
	
	public void addFaces(AmazonRekognition rekognitionClient, Image image, String name){
		
		String regex = "^[a-zA-Z0-9_.-:]{1,}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(name);
		
		if(!m.matches()) {
			System.out.println("Failed to satisfy name pattern, name pattern must be regular expression pattern [a-zA-Z0-9_.-]");
			return;
		}
		
		IndexFacesRequest request = new IndexFacesRequest().withImage(image)
				.withCollectionId(collectionId).withExternalImageId(name).withDetectionAttributes("ALL");
		
		IndexFacesResult result = rekognitionClient.indexFaces(request);
		
		System.out.println(name + " added");
		List<FaceRecord> faceRecords = result.getFaceRecords();
		for(FaceRecord faceRecord: faceRecords) {
			System.out.println("Face detected : Faceid is " + faceRecord.getFace().getFaceId());
		}
	}
	
	public void listFaces(AmazonRekognition rekognitionClient) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		ListFacesResult result = null;
		
		String paginationToken = null;
		
		do {
			if(result !=null) {
				paginationToken = result.getNextToken();
			}
			
			ListFacesRequest request = new ListFacesRequest().withCollectionId(collectionId)
					.withMaxResults(1)
					.withNextToken(paginationToken);
			
			result = rekognitionClient.listFaces(request);
			List<Face> faces = result.getFaces();
			for(Face face: faces) {
				try {
					System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}while(result != null && result.getNextToken()!=null);
	}

	
	
}
