package br.com.valdecipedroso.bakingapp.connection;

import java.util.List;

import br.com.valdecipedroso.bakingapp.data.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by gemeos_valdeci on 18/12/2017.
 */

public interface BakingApp {
    @GET("android-baking-app-json")
    Call<List<Recipe>> getRecipes();
}
