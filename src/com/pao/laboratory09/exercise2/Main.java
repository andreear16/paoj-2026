package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = Double.parseDouble(scanner.next());
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

                scrieTranzactie(out, id, suma, data, tip);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNext()) {
                String comanda = scanner.next();

                if (comanda.equals("READ")) {
                    int idx = scanner.nextInt();
                    afiseazaTranzactie(raf, idx);
                } else if (comanda.equals("UPDATE")) {
                    int idx = scanner.nextInt();
                    String status = scanner.next();

                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(statusLaByte(status));

                    System.out.println("Updated [" + idx + "]: " + status);
                } else if (comanda.equals("PRINT_ALL")) {
                    for (int i = 0; i < n; i++) {
                        afiseazaTranzactie(raf, i);
                    }
                }
            }
        }
    }

    private static void scrieTranzactie(DataOutputStream out, int id, double suma, String data, TipTranzactie tip) throws IOException {
        byte[] record = new byte[RECORD_SIZE];

        ByteBuffer buffer = ByteBuffer.wrap(record);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt(id);
        buffer.putDouble(suma);

        byte[] dataBytes = data.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < 10; i++) {
            if (i < dataBytes.length) {
                record[12 + i] = dataBytes[i];
            } else {
                record[12 + i] = ' ';
            }
        }

        record[22] = tipLaByte(tip);
        record[23] = statusLaByte("PENDING");

        out.write(record);
    }

    private static void afiseazaTranzactie(RandomAccessFile raf, int idx) throws IOException {
        byte[] record = new byte[RECORD_SIZE];

        raf.seek((long) idx * RECORD_SIZE);
        raf.readFully(record);

        ByteBuffer buffer = ByteBuffer.wrap(record);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int id = buffer.getInt();
        double suma = buffer.getDouble();
        String data = new String(record, 12, 10, StandardCharsets.US_ASCII).trim();
        TipTranzactie tip = byteLaTip(record[22]);
        String status = byteLaStatus(record[23]);

        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n", idx, id, data, tip, suma, status);
    }

    private static byte tipLaByte(TipTranzactie tip) {
        if (tip == TipTranzactie.CREDIT) {
            return 0;
        }

        return 1;
    }

    private static TipTranzactie byteLaTip(byte value) {
        if (value == 0) {
            return TipTranzactie.CREDIT;
        }

        return TipTranzactie.DEBIT;
    }

    private static byte statusLaByte(String status) {
        if (status.equals("PROCESSED")) {
            return 1;
        }

        if (status.equals("REJECTED")) {
            return 2;
        }

        return 0;
    }

    private static String byteLaStatus(byte value) {
        if (value == 1) {
            return "PROCESSED";
        }

        if (value == 2) {
            return "REJECTED";
        }

        return "PENDING";
    }
}