package com.pedrorok.rpm.exception;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/11/2025
 * @project ResourcePackManager
 */
public class StorageServiceException extends RuntimeException {
    public StorageServiceException(String message) {
        super(message);
    }

    public StorageServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
