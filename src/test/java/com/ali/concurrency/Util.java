package com.ali.concurrency;

import java.util.Random;

public class Util {

    public static String generateRandomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
  
        int targetStringLength = 10;
        Random random = new Random();
        StringBuffer buffer = new StringBuffer(targetStringLength);
        
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
  
        return generatedString;
      }

    

      public static String buildAddAccountRequest(String id, String balance, String title, String owner){
        StringBuffer builder = new StringBuffer();
        builder.append("{");
        builder.append("\"id\":");
        builder.append("\"");
        builder.append(id);
        builder.append("\",");
  
        builder.append("\"balance\":");
        builder.append("\"");
        builder.append(balance);
        builder.append("\",");
  
        builder.append("\"title\":");
        builder.append("\"");
        builder.append(title);
        builder.append("\",");

        builder.append("\"owner\":");
        builder.append("\"");
        builder.append(owner);
        builder.append("\"");
        
        builder.append("}");
  
        return builder.toString();
      }

      public static String buildAddCustomerRequest(String id, String name, String lastName){
        StringBuffer builder = new StringBuffer();
        builder.append("{");
        builder.append("\"firstName\":");
        builder.append("\"");
        builder.append(name);
        builder.append("\",");
  
        builder.append("\"lastName\":");
        builder.append("\"");
        builder.append(lastName);
        builder.append("\",");
  
        builder.append("\"ssn\":");
        builder.append("\"");
        builder.append(id);
        builder.append("\"");
        
        builder.append("}");
  
        return builder.toString();
      }

      public static String buildMoneyTransferRequest(String toAccountId, String fromAccountId, String amount){
        StringBuffer builder = new StringBuffer();
        builder.append("{");
        builder.append("\"toAccountId\":");
        builder.append("\"");
        builder.append(toAccountId);
        builder.append("\",");
  
        builder.append("\"fromAccountId\":");
        builder.append("\"");
        builder.append(fromAccountId);
        builder.append("\",");
  
        builder.append("\"amount\":");
        builder.append("\"");
        builder.append(amount);
        builder.append("\"");
        
        builder.append("}");
  
        return builder.toString();
      }
}