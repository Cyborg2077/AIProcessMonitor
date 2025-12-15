package com.kyle.aigf.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
public class SQLiteInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private static final String DB_NAME = "monitor.db";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            // 用户目录
            String userHome = System.getProperty("user.home");
            Path dbPath = Paths.get(userHome, ".myapp", DB_NAME);

            // 如果数据库不存在，则从 JAR 中复制
            if (!Files.exists(dbPath)) {
                Files.createDirectories(dbPath.getParent());
                try (InputStream is = getClass().getClassLoader()
                        .getResourceAsStream("db/" + DB_NAME)) {
                    if (is == null) {
                        throw new RuntimeException("Cannot find SQLite database in resources");
                    }
                    Files.copy(is, dbPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            log.info("SQLite database ready at:{}", dbPath.toAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SQLite DB", e);
        }
    }
}
