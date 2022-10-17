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

    public static final String REMOTE_SERVICE_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
             CloseableHttpResponse response = httpClient.execute(request)) {
            List<Cat> cats = mapper.readValue(
                    response.getEntity().getContent(),
                    new TypeReference<>() {
                    }
            );

            cats.stream().
                    filter(cat -> cat.getUpvotes() != null && Integer.parseInt(cat.getUpvotes()) > 0).
                    forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
