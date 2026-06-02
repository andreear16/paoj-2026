package com.pao.laboratory13.exercise1;

import java.util.Scanner;

public class Main {
    enum State {
        INIT, AUTH, OPEN, CLOSED
    }

    static class ProtocolEngine {
        private State state = State.INIT;
        private int historyCount = 0;

        public String process(String line) {
            line = line.trim();

            if (line.isEmpty()) {
                return null;
            }

            String command;
            String rest;

            int firstSpace = line.indexOf(' ');

            if (firstSpace == -1) {
                command = line;
                rest = "";
            } else {
                command = line.substring(0, firstSpace);
                rest = line.substring(firstSpace + 1).trim();
            }

            switch (command) {
                case "AUTH":
                    return handleAuth(rest);
                case "OPEN":
                    return handleOpen(rest);
                case "SEND":
                    return handleSend(rest);
                case "BROADCAST":
                    return handleBroadcast(rest);
                case "HISTORY":
                    return handleHistory(rest);
                case "CLOSE":
                    return handleClose(rest);
                default:
                    return "ERR E_PARSE UNKNOWN_COMMAND";
            }
        }

        private String handleAuth(String user) {
            if (user.isEmpty()) {
                return "ERR E_PARSE AUTH";
            }

            if (state == State.CLOSED) {
                return "ERR E_STATE CLOSED";
            }

            state = State.AUTH;
            historyCount = 0;

            return "OK AUTH user=" + user;
        }

        private String handleOpen(String rest) {
            if (!rest.isEmpty()) {
                return "ERR E_PARSE OPEN";
            }

            if (state == State.CLOSED) {
                return "ERR E_STATE CLOSED";
            }

            if (state == State.OPEN) {
                return "ERR E_STATE ALREADY_OPEN";
            }

            if (state == State.INIT) {
                return "ERR E_STATE NOT_OPEN";
            }

            state = State.OPEN;

            return "OK OPEN";
        }

        private String handleSend(String payload) {
            if (payload.isEmpty()) {
                return "ERR E_PARSE SEND";
            }

            if (state == State.CLOSED) {
                return "ERR E_STATE CLOSED";
            }

            if (state != State.OPEN) {
                return "ERR E_STATE NOT_OPEN";
            }

            historyCount++;

            return "OK OPEN sent";
        }

        private String handleBroadcast(String payload) {
            if (payload.isEmpty()) {
                return "ERR E_PARSE BROADCAST";
            }

            if (state == State.CLOSED) {
                return "ERR E_STATE CLOSED";
            }

            if (state != State.OPEN) {
                return "ERR E_STATE NOT_OPEN";
            }

            historyCount++;

            return "OK OPEN broadcast";
        }

        private String handleHistory(String rest) {
            if (!rest.isEmpty()) {
                return "ERR E_PARSE HISTORY";
            }

            if (state == State.CLOSED) {
                return "ERR E_STATE CLOSED";
            }

            if (state != State.OPEN) {
                return "ERR E_STATE NOT_OPEN";
            }

            return "OK OPEN history=" + historyCount;
        }

        private String handleClose(String rest) {
            if (!rest.isEmpty()) {
                return "ERR E_PARSE CLOSE";
            }

            if (state == State.CLOSED) {
                return "ERR E_STATE CLOSED";
            }

            if (state != State.OPEN) {
                return "ERR E_STATE NOT_OPEN";
            }

            state = State.CLOSED;

            return "OK CLOSED";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextLine()) {
            return;
        }

        int q = Integer.parseInt(scanner.nextLine().trim());

        ProtocolEngine engine = new ProtocolEngine();

        int processed = 0;

        while (scanner.hasNextLine() && processed < q) {
            String line = scanner.nextLine();

            String result = engine.process(line);

            if (result == null) {
                continue;
            }

            System.out.println(result);
            processed++;
        }
    }
}