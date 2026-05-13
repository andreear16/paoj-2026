package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Transaction> data = Arrays.asList(
                new Transaction(1, new BigDecimal("7000.00"), LocalDate.of(2026, 5, 1), "RO", "WEB"),
                new Transaction(2, new BigDecimal("1500.00"), LocalDate.of(2026, 5, 2), "RO", "APP"),
                new Transaction(3, new BigDecimal("5200.00"), LocalDate.of(2026, 5, 3), "NG", "CRYPTO"),
                new Transaction(4, new BigDecimal("80.00"), LocalDate.of(2026, 5, 4), "KP", "POS"),
                new Transaction(5, new BigDecimal("1000.00"), LocalDate.of(2026, 5, 5), "RO", "ATM"),
                new Transaction(6, new BigDecimal("1000.00"), LocalDate.of(2026, 5, 6), "DE", "APP")
        );

        Snapshot snapshot = data.stream().collect(CustomCollectors.toSnapshot(5));

        System.out.println("TOTAL_AMOUNT " + money(snapshot.getTotalAmount()));

        System.out.println();
        System.out.println("TOP_TRANSACTIONS");
        for (Transaction tx : snapshot.getTopTransactions()) {
            System.out.println(tx);
        }

        System.out.println();
        System.out.println("COUNT_BY_COUNTRY");
        snapshot.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));

        System.out.println();
        System.out.println("COUNT_BY_CHANNEL");
        snapshot.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));

        System.out.println();
        System.out.println("TOTAL_BY_COUNTRY");
        snapshot.getTotalByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + " " + money(e.getValue())));
    }

    private static String money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static final class Transaction {
        private final int id;
        private final BigDecimal amount;
        private final LocalDate date;
        private final String country;
        private final String channel;

        private Transaction(int id, BigDecimal amount, LocalDate date, String country, String channel) {
            this.id = id;
            this.amount = amount;
            this.date = date;
            this.country = country;
            this.channel = channel;
        }

        public int getId() {
            return id;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getCountry() {
            return country;
        }

        public String getChannel() {
            return channel;
        }

        @Override
        public String toString() {
            return "[" + id + "] amount=" + money(amount)
                    + " date=" + date
                    + " country=" + country
                    + " channel=" + channel;
        }
    }

    private static final class Snapshot {
        private final Map<String, Long> countByCountry;
        private final Map<String, Long> countByChannel;
        private final Map<String, BigDecimal> totalByCountry;
        private final BigDecimal totalAmount;
        private final List<Transaction> topTransactions;

        private Snapshot(Map<String, Long> countByCountry,
                         Map<String, Long> countByChannel,
                         Map<String, BigDecimal> totalByCountry,
                         BigDecimal totalAmount,
                         List<Transaction> topTransactions) {
            this.countByCountry = Collections.unmodifiableMap(new HashMap<>(countByCountry));
            this.countByChannel = Collections.unmodifiableMap(new HashMap<>(countByChannel));
            this.totalByCountry = Collections.unmodifiableMap(new HashMap<>(totalByCountry));
            this.totalAmount = totalAmount;
            this.topTransactions = Collections.unmodifiableList(new ArrayList<>(topTransactions));
        }

        public Map<String, Long> getCountByCountry() {
            return countByCountry;
        }

        public Map<String, Long> getCountByChannel() {
            return countByChannel;
        }

        public Map<String, BigDecimal> getTotalByCountry() {
            return totalByCountry;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public List<Transaction> getTopTransactions() {
            return topTransactions;
        }
    }

    private static final class CustomCollectors {
        private static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
            class Agg {
                private final Map<String, Long> countByCountry = new HashMap<>();
                private final Map<String, Long> countByChannel = new HashMap<>();
                private final Map<String, BigDecimal> totalByCountry = new HashMap<>();
                private final List<Transaction> transactions = new ArrayList<>();
                private BigDecimal totalAmount = BigDecimal.ZERO;

                private void add(Transaction tx) {
                    countByCountry.merge(tx.getCountry(), 1L, Long::sum);
                    countByChannel.merge(tx.getChannel(), 1L, Long::sum);
                    totalByCountry.merge(tx.getCountry(), tx.getAmount(), BigDecimal::add);

                    totalAmount = totalAmount.add(tx.getAmount());
                    transactions.add(tx);
                }

                private Agg combine(Agg other) {
                    other.countByCountry.forEach((country, count) ->
                            countByCountry.merge(country, count, Long::sum));

                    other.countByChannel.forEach((channel, count) ->
                            countByChannel.merge(channel, count, Long::sum));

                    other.totalByCountry.forEach((country, total) ->
                            totalByCountry.merge(country, total, BigDecimal::add));

                    totalAmount = totalAmount.add(other.totalAmount);
                    transactions.addAll(other.transactions);

                    return this;
                }

                private Snapshot finish() {
                    List<Transaction> topTransactions = transactions.stream()
                            .sorted(Comparator.comparing(Transaction::getAmount).reversed()
                                    .thenComparingInt(Transaction::getId))
                            .limit(topN)
                            .collect(Collectors.toList());

                    return new Snapshot(
                            countByCountry,
                            countByChannel,
                            totalByCountry,
                            totalAmount,
                            topTransactions
                    );
                }
            }

            return Collector.of(
                    Agg::new,
                    Agg::add,
                    Agg::combine,
                    Agg::finish
            );
        }
    }
}