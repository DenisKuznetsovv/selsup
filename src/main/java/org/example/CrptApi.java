package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CrptApi {

    private final String API_URL = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    private TimeUnit timeUnit;
    private static Semaphore semaphore;

    public CrptApi(TimeUnit timeUnit, Integer requestLimit) {
        this.timeUnit = timeUnit;
        semaphore = new Semaphore(requestLimit, true);
    }

    public void createDocument(Object document, String signature) {
        try {
            semaphore.tryAcquire(1, timeUnit);


            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(API_URL);
                httpPost.setHeader("Content-Type", "application/json");

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonDocument = objectMapper.writeValueAsString(document);
                System.out.println(jsonDocument);

                StringEntity entity = new StringEntity(jsonDocument);
                httpPost.setEntity(entity);

                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == HttpStatus.SC_OK) {
                        //Запись в лог
                    } else {
                        //Запись в лог
                    }
                }
            } catch (IOException e) {
                //Запись в лог
                e.printStackTrace();
            }
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}

@Data
@AllArgsConstructor
class Description {
    private String participantInn;
}


@Data
@AllArgsConstructor
class Document {
    private Description description;
    private String docId;
    private String doc_status;
    private String doc_type;
    private boolean importRequest;
    private String owner_inn;
    private String participant_inn;
    private String producer_inn;
    private String production_date;
    private String production_type;
    private List<Product> products;
    private String reg_date;
    private String reg_number;
}

@Data
@AllArgsConstructor
class Product {
    private String certificate_document;
    private String certificate_document_date;
    private String certificate_document_number;
    private String owner_inn;
    private String producer_inn;
    private String production_date;
    private String tnved_code;
    private String uit_code;
    private String uitu_code;
}





