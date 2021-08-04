package retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.concurrent.TimeUnit;

public class CalculatorRetrofitFactory {


    public static CalculatorRestApi build(String baseUrl) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                // ScalarsConverterFactory needs to be first so that Call<String> will work
                //  otherwise the JacksonConverter will grab it and fail.
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        return retrofit.create(CalculatorRestApi.class);

    }
}
