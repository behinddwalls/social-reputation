package com.socialreputation.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class TestController {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httppost = new HttpPost("http://localhost:8080/search/images");
		httppost.addHeader("Authorization", "Bearer "
				+ "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJlbWFpbFwiOlwieHl6QHh5ei5jb21cIixcImhhc2hlZFBhc3N3b3JkXCI6XCIkMmEkMTAkNlk5bVdTTm1ndXBqSENoRXFNT0UzZXFCZm1mZDBoTUZ6eWgyQVl0SzlpLkxGQS5xTTY2bnFcIixcInVzZXJJZFwiOlwiZjBkZGQzNzItNGRlOC00MGFjLTljZTQtOGNjNDE1NjE3YTY5XCIsXCJhdWRpdFwiOntcImNyZWF0ZVRpbWVcIjpcIjIwMTctMDEtMDdUMDk6MDg6MDQuMzU4LTA4MDBcIixcInVwZGF0ZVRpbWVcIjpcIjIwMTctMDEtMDdUMDk6MDg6MDQuMzU4LTA4MDBcIn19IiwiZXhwIjoxNDgzODk3NTE1fQ.TfodjNdYkfFZ0E6RuOlxzT6ilXcMcQbLyBupJZoev6AeQTu2EtxebIAj__9tSmSkEnWHKrPGCNoTaPhQmxmp2Q");

		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create().addBinaryBody("file",
				new File("/Users/preetam/Downloads/14524542_10153701082086403_3685691204625618154_o.jpg"));
		httppost.setEntity(entityBuilder.build());

		final BufferedImage image = ImageIO
				.read(new File("/Users/preetam/Downloads/14524542_10153701082086403_3685691204625618154_o.jpg"));
		System.out.println(image.getWidth());
		System.out.println(image.getHeight());

		int width = (int) Math.ceil(image.getWidth() * 0.058888889849185944); // width
																				// of
																				// bounding
		// box
		int heght = (int) Math.ceil(image.getHeight() * 0.08848080039024353); // height
																				// of
		// bounding box
		int x = (int) Math.ceil(image.getWidth() * 0.4744444489479065); // left
																		// of
																		// bounding
																		// box
		int y = (int) Math.ceil(image.getHeight() * 0.45075124502182007); // top
																			// of
																			// bounding
																			// box

		BufferedImage subImage = image.getSubimage(x, y, width, heght);

		File outputfile = new File("/Users/preetam/Downloads/11111.jpg");
		ImageIO.write(subImage, "jpg", outputfile);

		// CloseableHttpResponse response = httpclient.execute(httppost);
		// System.out.println(response.getStatusLine());
		// System.out.println(CharStreams.toString(new
		// InputStreamReader(response.getEntity().getContent())));
	}

}
