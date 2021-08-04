package retrofit;

import retrofit2.Call;
import retrofit2.http.*;

public interface CalculatorRestApi {

    // Ping server to see if its alive
    @GET("/calc/ping")
    Call<String> ping();

    @GET("/calc/add")
    Call<Integer> add(@Query("first") int firstParam, @Query("second") int secondParam);

}
