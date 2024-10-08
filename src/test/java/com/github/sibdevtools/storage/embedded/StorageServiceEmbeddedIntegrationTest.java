package com.github.sibdevtools.storage.embedded;

import com.github.sibdevtools.storage.api.rq.SaveFileRq;
import com.github.sibdevtools.storage.api.service.StorageBucketService;
import com.github.sibdevtools.storage.api.service.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author sibmaks
 * @since 0.1.5
 */
@ActiveProfiles("startup-test")
@SpringBootTest
class StorageServiceEmbeddedIntegrationTest {
    @Autowired
    private StorageBucketService storageBucketService;
    @Autowired
    private StorageService storageService;

    @Test
    void testSaveAndGet() {
        var bucket = UUID.randomUUID().toString();
        storageBucketService.create(bucket);

        var name = UUID.randomUUID().toString();

        var metaKey = UUID.randomUUID().toString();
        var metaValue = UUID.randomUUID().toString();
        byte[] data = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);

        var saveFileRs = storageService.save(
                SaveFileRq.builder()
                        .bucket(bucket)
                        .name(name)
                        .meta(Map.of(metaKey, metaValue))
                        .data(data)
                        .build()
        );

        assertNotNull(saveFileRs);

        var fileId = saveFileRs.getBody();
        assertNotNull(fileId);

        var bucketFileRs = storageService.get(fileId);
        assertNotNull(bucketFileRs);

        var bucketFile = bucketFileRs.getBody();

        assertArrayEquals(data, bucketFile.getData());

        var description = bucketFile.getDescription();
        assertNotNull(description);

        assertEquals(name, description.getName());
        assertNotNull(description.getCreatedAt());
        assertNotNull(description.getModifiedAt());

        var meta = description.getMeta();
        assertNotNull(meta);

        assertEquals(metaValue, meta.get(metaKey));

        var descriptionRs = storageService.getDescription(fileId);
        assertNotNull(descriptionRs);

        var descriptionRsBody = descriptionRs.getBody();

        assertEquals(name, descriptionRsBody.getName());
        assertNotNull(descriptionRsBody.getCreatedAt());
        assertNotNull(descriptionRsBody.getModifiedAt());

        meta = descriptionRsBody.getMeta();
        assertNotNull(meta);

        assertEquals(metaValue, meta.get(metaKey));
    }
}
