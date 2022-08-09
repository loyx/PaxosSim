package cn.loyx.paxos.conf;

import com.google.gson.Gson;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    @Test
    public void testFromGson() throws IOException {
        Gson gson = new Gson();
        Configuration configuration = gson.fromJson(Files.newBufferedReader(Path.of("src/test/resources/config.json")), Configuration.class);
        System.out.println(configuration);
    }

}