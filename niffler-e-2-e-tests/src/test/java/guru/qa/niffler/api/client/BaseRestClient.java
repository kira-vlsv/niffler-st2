package guru.qa.niffler.api.client;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class BaseRestClient {

    protected final String serviceBaseUrl;
    protected final Retrofit retrofit;
    protected final OkHttpClient httpClient;

    public BaseRestClient(String serviceBaseUrl) {
        this.serviceBaseUrl = serviceBaseUrl;
        this.httpClient = new OkHttpClient.Builder().build();
        this.retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(serviceBaseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
