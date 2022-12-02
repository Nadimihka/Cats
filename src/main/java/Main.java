import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;

public class Main {
    public static final String URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build()) {

            HttpGet request = new HttpGet(URL);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                /*// вывод полученных заголовков
                Arrays.stream(response.getAllHeaders()).forEach(System.out::println);*/
                /*// чтение тела ответа
                String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(body);*/

                List<Data> dataList = mapper.readValue(
                        response.getEntity().getContent(), new
                                TypeReference<>() {
                                });
                dataList.stream()
                        .filter(value -> value.getUpvotes() != null)
                        .forEach(System.out::println);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
