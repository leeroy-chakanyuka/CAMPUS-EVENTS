package za.ac.cput.campus_events.seed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

/**
 * Applies db/seed.sql at startup (venues, demo organisers, starter inbox).
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static final String SEED_SCRIPT = "db/seed.sql";

    private final DataSource dataSource;

    public DataSeeder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        ClassPathResource script = new ClassPathResource(SEED_SCRIPT);
        if (!script.exists()) {
            log.warn("Seed: {} not found, skipping", SEED_SCRIPT);
            return;
        }
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection,
                    new EncodedResource(script, StandardCharsets.UTF_8));
            log.info("Seed: {} applied", SEED_SCRIPT);
        }
    }
}
