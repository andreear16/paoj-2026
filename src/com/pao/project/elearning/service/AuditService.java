package com.pao.project.elearning.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public final class AuditService {
    private static AuditService instance;

    private static final String AUDIT_FILE = "audit.csv";

    private final ReentrantLock lock = new ReentrantLock();

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private AuditService() {
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }

        return instance;
    }

    public void log(String actionName) {
        lock.lock();

        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.println(actionName + "," + timestamp);
        } catch (IOException e) {
            System.out.println("Eroare audit: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}