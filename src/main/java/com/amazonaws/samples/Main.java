package com.amazonaws.samples;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ManageCollection manageCollection = new ManageCollection();
		
		AWSCredentials credentials;
		
		try{
			credentials = new ProfileCredentialsProvider().getCredentials();
			System.out.println(credentials.toString());
			System.out.println(credentials.getAWSAccessKeyId());
			System.out.println(credentials.getAWSSecretKey());

		}catch(Exception e){
			throw new AmazonClientException("cannot");
		}
		
		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.build();
		

		BufferedImage originalImage;
		ByteArrayOutputStream baos = null;
		try {
			originalImage = ImageIO.read(new File("/home/insup/Pictures/nj.jpg"));			
			baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] imageInByte = baos.toByteArray();
		ByteBuffer imageBytes = ByteBuffer.wrap(imageInByte);
		baos.reset(); 
		
		String name = "Kim_Nam_Jun";
		
		//manageCollection.createCollection(rekognitionClient);
		//manageCollection.addFaces(rekognitionClient, new Image().withBytes(imageBytes), name);
		//manageCollection.deleteCollection(rekognitionClient);
		manageCollection.listFaces(rekognitionClient);
	}

}
