package com.github.sibdevtools.storage.embedded.dto;

/**
 * @author sibmaks
 * @since 0.1.0
 */
public enum ContentStorageFormat {
    /**
     * Store data in Base64 format.
     */
    BASE64,
    /**
     * Store data in raw format.
     */
    BINARY,
    /**
     * Store data in gzip format.
     */
    GZIP
}
