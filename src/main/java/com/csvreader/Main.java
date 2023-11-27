package com.csvreader;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    URL resource = Main.class.getClassLoader().getResource("file.csv");
    try (Reader reader = Files.newBufferedReader(Paths.get(resource.toURI()))) {
      CsvToBean<Customer> csvToBean = new CsvToBeanBuilder<Customer>(reader).withType(Customer.class).build();
      List<Customer> parsedCustomers = csvToBean.parse();

      parsedCustomers.forEach(Main::sendCustomerData);

    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static void sendCustomerData(Customer customer) {
    HttpClient httpClient = HttpClient.newHttpClient();
    String url = "http://localhost:8080/customer";

    Gson gson = new Gson();
    String customerJson = gson.toJson(customer);

    HttpRequest request = buildHttpRequest(url, customerJson);

    try {
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      handleResponse(response);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static HttpRequest buildHttpRequest(String url, String customerJson) {
    try {
      return HttpRequest.newBuilder()
          .uri(new URI(url))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(customerJson))
          .build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static void handleResponse(HttpResponse<String> response) {
    int statusCode = response.statusCode();
    String responseBody = response.body();
    System.out.println("Response Code: " + statusCode + " Response: " + responseBody);
  }
}
