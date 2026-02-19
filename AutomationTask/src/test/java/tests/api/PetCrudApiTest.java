package tests.api;

import api.client.PetApi;
import api.model.Category;
import api.model.Pet;
import api.model.Tag;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.testng.Assert.*;

public class PetCrudApiTest {

    private final PetApi petApi = new PetApi();

    @Test
    public void petCrud_shouldWork_endToEnd() {
        System.out.println("[CASE API-1] Start: Pet CRUD should work end-to-end.");

        long id = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_000_000_000L);
        System.out.println("[INFO] Generated random pet id: " + id);

        // CREATE
        System.out.println("[STEP] CREATE: Preparing pet payload.");
        Pet created = new Pet(id, "ovul-pet-" + id, "available");
        created.category = new Category(1, "dogs");
        created.tags = List.of(new Tag(1, "tag1"));
        created.photoUrls = List.of("https://example.com/pet.png");

        System.out.println("[ACTION] Sending CREATE request (POST /pet).");
        Response createRes = petApi.create(created);
        System.out.println("[INFO] CREATE response status: " + createRes.statusCode());
        System.out.println("[INFO] CREATE response body:\n" + createRes.asString());

        System.out.println("[ASSERT] Verifying CREATE status code is 200 and returned pet fields match expected.");
        assertEquals(createRes.statusCode(), 200, "Create status code should be 200");
        assertEquals(createRes.jsonPath().getLong("id"), id, "Created pet id should match");
        assertEquals(createRes.jsonPath().getString("status"), "available");

        // READ
        System.out.println("[STEP] READ: Fetching created pet by id: " + id);
        System.out.println("[ACTION] Sending READ request (GET /pet/{id}).");
        Response getRes = petApi.get(id);
        System.out.println("[INFO] READ response status: " + getRes.statusCode());
        System.out.println("[INFO] READ response body:\n" + getRes.asString());

        System.out.println("[ASSERT] Verifying READ status code is 200 and returned fields are correct.");
        assertEquals(getRes.statusCode(), 200);
        assertEquals(getRes.jsonPath().getLong("id"), id);
        assertTrue(getRes.jsonPath().getString("name").toLowerCase().contains("ovul-pet-"));

        // UPDATE
        System.out.println("[STEP] UPDATE: Updating pet name and status.");
        created.name = "ovul-pet-updated-" + id;
        created.status = "sold";
        System.out.println("[INFO] Updated payload -> name: " + created.name + ", status: " + created.status);

        System.out.println("[ACTION] Sending UPDATE request (PUT /pet).");
        Response updateRes = petApi.update(created);
        System.out.println("[INFO] UPDATE response status: " + updateRes.statusCode());
        System.out.println("[INFO] UPDATE response body:\n" + updateRes.asString());

        System.out.println("[ASSERT] Verifying UPDATE status code is 200 and status is updated to 'sold'.");
        assertEquals(updateRes.statusCode(), 200);
        assertEquals(updateRes.jsonPath().getString("status"), "sold");

        // READ AFTER UPDATE
        System.out.println("[STEP] READ AFTER UPDATE: Fetching pet again to verify persistence.");
        System.out.println("[ACTION] Sending READ request (GET /pet/{id}) after update.");
        Response getAfterUpdate = petApi.get(id);
        System.out.println("[INFO] READ AFTER UPDATE response status: " + getAfterUpdate.statusCode());
        System.out.println("[INFO] READ AFTER UPDATE response body:\n" + getAfterUpdate.asString());

        System.out.println("[ASSERT] Verifying updated name and status are returned correctly.");
        assertEquals(getAfterUpdate.statusCode(), 200);
        assertEquals(getAfterUpdate.jsonPath().getString("name"), "ovul-pet-updated-" + id);
        assertEquals(getAfterUpdate.jsonPath().getString("status"), "sold");

        // DELETE
        System.out.println("[STEP] DELETE: Deleting pet by id: " + id);
        System.out.println("[ACTION] Sending DELETE request (DELETE /pet/{id}).");
        Response deleteRes = petApi.delete(id);
        System.out.println("[INFO] DELETE response status: " + deleteRes.statusCode());
        System.out.println("[INFO] DELETE response body:\n" + deleteRes.asString());

        System.out.println("[ASSERT] Verifying DELETE status code is 200.");
        assertEquals(deleteRes.statusCode(), 200);

        // READ AFTER DELETE -> 404 (Pet not found)
        System.out.println("[STEP] READ AFTER DELETE: Fetching pet again expecting it to be missing.");
        System.out.println("[ACTION] Sending READ request (GET /pet/{id}) after delete.");
        Response getAfterDelete = petApi.get(id);
        System.out.println("[INFO] READ AFTER DELETE response status: " + getAfterDelete.statusCode());
        System.out.println("[INFO] READ AFTER DELETE response body:\n" + getAfterDelete.asString());

        System.out.println("[ASSERT] Verifying READ AFTER DELETE returns 404 and body contains 'pet not found'.");
        assertEquals(getAfterDelete.statusCode(), 404);
        assertTrue(getAfterDelete.asString().toLowerCase().contains("pet not found"));

        System.out.println("[CASE API-1] End: Pet CRUD end-to-end flow completed successfully.");
    }

    @Test
    public void getPet_invalidId_shouldReturn400or404() {
        System.out.println("[CASE API-2] Start: GET pet with invalid id should return 400 or 404.");

        System.out.println("[ACTION] Sending GET request with invalid id: 'abc'.");
        var res = petApi.getInvalidId("abc");
        System.out.println("[INFO] Response status: " + res.statusCode());
        System.out.println("[INFO] Response body:\n" + res.asString());

        System.out.println("[ASSERT] Verifying status is one of [400, 404].");
        assertStatusIn(res, 400, 404);

        System.out.println("[CASE API-2] End: Invalid id behavior verified.");
    }

    @Test
    public void createPet_invalidJson_shouldReturn4xxOr5xx() {
        System.out.println("[CASE API-3] Start: CREATE with invalid JSON should return 4xx or 5xx.");

        System.out.println("[ACTION] Sending CREATE request with invalid JSON payload.");
        var res = petApi.createInvalidJson("{ invalid-json ");
        int code = res.statusCode();

        System.out.println("[INFO] Response status: " + code);
        System.out.println("[INFO] Response body:\n" + res.asString());

        System.out.println("[ASSERT] Verifying response code is in 4xx or 5xx range.");
        if (code < 400 || code >= 600) {
            throw new AssertionError("Expected 4xx/5xx but got: " + code + "\nBody:\n" + res.asString());
        }

        System.out.println("[CASE API-3] End: Invalid JSON behavior verified.");
    }

    @Test
    public void deletePet_nonExisting_shouldReturn404or200() {
        System.out.println("[CASE API-4] Start: DELETE non-existing pet should return 200 or 404.");

        long nonExistingId = 999999999999L;
        System.out.println("[INFO] Using non-existing id: " + nonExistingId);

        System.out.println("[ACTION] Sending DELETE request for non-existing pet.");
        var res = petApi.delete(nonExistingId);

        System.out.println("[INFO] Response status: " + res.statusCode());
        System.out.println("[INFO] Response body:\n" + res.asString());

        System.out.println("[ASSERT] Verifying status is one of [200, 404].");
        assertStatusIn(res, 200, 404);

        System.out.println("[CASE API-4] End: Non-existing delete behavior verified.");
    }

    private void assertStatusIn(io.restassured.response.Response res, int... allowed) {
        System.out.println("[HELPER] Validating response status is in allowed list: " + java.util.Arrays.toString(allowed));

        int code = res.statusCode();
        System.out.println("[HELPER] Actual status: " + code);

        for (int a : allowed) {
            if (code == a) {
                System.out.println("[HELPER] Status is allowed (" + code + ").");
                return;
            }
        }

        System.out.println("[HELPER] Status is NOT allowed (" + code + "). Throwing assertion error.");
        throw new AssertionError("Unexpected status: " + code +
                "\nAllowed: " + java.util.Arrays.toString(allowed) +
                "\nBody:\n" + res.asString());
    }
}
