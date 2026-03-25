package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati;
    private AuditEntry[] auditLog;

    private AngajatService() {
        angajati = new Angajat[0];
        auditLog = new AuditEntry[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    private void logAction(String action, String target) {
        AuditEntry entry = new AuditEntry(action, target, LocalDateTime.now().toString());

        AuditEntry[] copie = new AuditEntry[auditLog.length + 1];
        for (int i = 0; i < auditLog.length; i++) {
            copie[i] = auditLog[i];
        }
        copie[auditLog.length] = entry;
        auditLog = copie;
    }

    public void addAngajat(Angajat a) {
        Angajat[] copie = new Angajat[angajati.length + 1];
        for (int i = 0; i < angajati.length; i++) {
            copie[i] = angajati[i];
        }
        copie[angajati.length] = a;
        angajati = copie;
        System.out.println("Angajat adăugat: " + a.getNume());

        logAction("ADD", a.getNume());
    }

    public void listBySalary() {
        Angajat[] copie = angajati.clone();
        Arrays.sort(copie);
        for (int i = 0; i < copie.length; i++) {
            System.out.println((i + 1) + ". " + copie[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        logAction("FIND_BY_DEPT", numeDept);

        boolean gasit = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                if (!gasit) {
                    System.out.println("--- Angajați din " + numeDept + " ---");
                }
                System.out.println(a);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }

    public void printAuditLog() {
        for (AuditEntry entry : auditLog) {
            System.out.println(entry);
        }
    }
}