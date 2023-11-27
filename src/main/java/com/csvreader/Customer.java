package com.csvreader;


import com.opencsv.bean.CsvBindByName;

public class Customer  {

  @CsvBindByName(column = "Customer Ref")
  private Integer ref;
  @CsvBindByName(column = "Customer Name")
  private String name;
  @CsvBindByName(column = "Address Line 1")
  private String addressLineOne;
  @CsvBindByName(column = "Address Line 2")
  private String addressLineTwo;
  @CsvBindByName(column = "Town")
  private String town;
  @CsvBindByName(column = "County")
  private String county;
  @CsvBindByName(column = "Country")
  private String country;
  @CsvBindByName(column = "Postcode")
  private String postcode;
}
