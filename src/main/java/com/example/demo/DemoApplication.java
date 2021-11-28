package com.example.demo;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class DemoApplication {

	private static HttpClient client;

	public static void main(String[] args) throws IOException, JSONException {
		SpringApplication.run(DemoApplication.class, args);

		client = HttpClient.newHttpClient();

		Boolean running = true;
		do {

			HashMap<Integer, String> currencyCodes = new HashMap<Integer, String>();

			currencyCodes.put(1, "USD");
			currencyCodes.put(2, "CAD");
			currencyCodes.put(3, "EUR");

			Integer from, to;
			String fromCode, toCode;
			double amount;
			String privateKey; //You must have a private key for the transaction.

			Scanner sc = new Scanner(System.in);

			System.out.println("Welcome to the currency converter!");

			System.out.println("Currency converting from?");
			System.out.println("1-USD (US Dollar)\t 2-CAD (Canadian Dollar)\t 3-EUR (Euro)");
			from = sc.nextInt();
			 while (from < 1 || from > 3) {

				System.out.println("Please select a valid currency (1-3)");
				System.out.println("1-USD (US Dollar)\t 2-CAD (Canadian Dollar)\t 3-EUR (Euro)");
				from = sc.nextInt();
			}
			fromCode = currencyCodes.get(from);


			System.out.println("Currency converting to!");
			System.out.println("1-USD (US Dollar)\t 2-CAD (Canadian Dollar)\t 3-EUR (Euro)");
			to = sc.nextInt();
			while (to < 1 || to > 3) {

				System.out.println("Please select a valid currency (1-3)");
				System.out.println("1-USD (US Dollar)\t 2-CAD (Canadian Dollar)\t 3-EUR (Euro)");
				to = sc.nextInt();
			}
			toCode = currencyCodes.get(to);

			System.out.println("Amount to wish you convert?");
			amount = sc.nextFloat();

			sendHttpGETRequest(fromCode, toCode, amount);

			System.out.println("would you like to make another conversation?");
			System.out.println("1-Yes \t Any other Integer: No");
			if (sc.nextInt() != 1) {
				running = false;
			}

		} while (running);

		System.out.println("Thank you for using to currency converter!");
		System.out.println("Have a nice day!");

	}

	private static void sendHttpGETRequest(String fromCode, String toCode, double amount) throws IOException, JSONException {


		DecimalFormat f = new DecimalFormat("00.00");
		String GET_URL = "http://apilayer.net/api/live?access_key=" + privateKey + "&currencies=" + fromCode + "&source=" + toCode + "&format=1";
		URL url = new URL(GET_URL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		int responseCode = httpURLConnection.getResponseCode();

		if (responseCode == httpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String inputline;
			StringBuffer response = new StringBuffer();

			while ((inputline = in.readLine()) != null) {
				response.append(inputline);
			}in.close();

			JSONObject obj = new JSONObject(response.toString());
			Double exchangeRate = obj.getJSONObject("rates").getDouble(fromCode);
			System.out.println(obj.getJSONObject("rates"));
			System.out.println(exchangeRate); // keep for debugging
			System.out.println();
			System.out.println(f.format(amount) + fromCode + " = " + f.format(amount / exchangeRate) + toCode);

		} else {
			System.out.println("GET request failed");


		}

	}
}
