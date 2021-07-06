package br.ind.scenario.geradorsql.utils;

import br.ind.scenario.geradorsql.models.JsonUtilModelTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class JsonUtilTest {

    private final static String JSON = """
            {"name":"gabriel","number":88}""";

    @Test
    public void mustSerializerParams() {
        Optional<JsonUtilModelTest> model = JsonUtil.fromJson(JSON, JsonUtilModelTest.class);
        Assertions.assertFalse(model.isEmpty());
        Assertions.assertEquals("gabriel", model.get().name());
        Assertions.assertEquals(88, model.get().number());
    }

    @Test
    public void mustFailSerializationReturningEmpty() {
        Optional<JsonUtilModelTest> model = JsonUtil.fromJson("JSON", JsonUtilModelTest.class);
        Assertions.assertTrue(model.isEmpty());
    }
}
