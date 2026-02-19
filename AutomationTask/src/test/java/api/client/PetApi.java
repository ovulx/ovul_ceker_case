package api.client;

import api.model.Pet;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class PetApi {

    public PetApi() {
        baseURI = ApiConfig.baseUrl();
    }

    public Response create(Pet pet) {
        return given()
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post("/pet");
    }

    public Response get(long petId) {
        return given()
                .when()
                .get("/pet/{petId}", petId);
    }

    public Response update(Pet pet) {
        return given()
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .put("/pet");
    }

    public Response delete(long petId) {
        return given()
                .when()
                .delete("/pet/{petId}", petId);
    }

    public Response getInvalidId(String petId) {
        return given()
                .when()
                .get("/pet/{petId}", petId);
    }

    public Response createInvalidJson(String rawJson) {
        return given()
                .contentType(ContentType.JSON)
                .body(rawJson)
                .when()
                .post("/pet");
    }
}
