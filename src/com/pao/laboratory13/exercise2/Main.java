package com.pao.laboratory13.exercise2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int PORT = 9000;
    private static final int CLIENT_COUNT = 2;

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

    static class Server implements Runnable {
        private final int port;
        private final int expectedClients;

        Server(int port, int expectedClients) {
            this.port = port;
            this.expectedClients = expectedClients;
        }

        @Override
        public void run() {
            ExecutorService clientPool = Executors.newCachedThreadPool();

            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("[SERVER] Listening on port " + port);

                for (int i = 1; i <= expectedClients; i++) {
                    Socket clientSocket = serverSocket.accept();
                    int clientId = i;

                    System.out.println("[SERVER] Client " + clientId + " connected");

                    clientPool.submit(new ClientHandler(clientSocket, clientId));
                }

                clientPool.shutdown();

                while (!clientPool.isTerminated()) {
                    Thread.sleep(100);
                }

                System.out.println("[SERVER] All clients done. Shutting down.");
            } catch (IOException e) {
                System.out.println("[SERVER] IOException: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[SERVER] Interrupted");
            }
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;
        private final int clientId;

        ClientHandler(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            ProtocolEngine engine = new ProtocolEngine();

            try (
                    Socket autoCloseSocket = socket;
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(autoCloseSocket.getInputStream()));
                    PrintWriter writer = new PrintWriter(autoCloseSocket.getOutputStream(), true)
            ) {
                String command;

                while ((command = reader.readLine()) != null) {
                    String response = engine.process(command);

                    if (response == null) {
                        continue;
                    }

                    System.out.println("[SERVER] CLIENT-" + clientId + " >> "
                            + command + " => " + response);

                    writer.println(response);

                    if (response.equals("OK CLOSED")) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("[SERVER] Client " + clientId
                        + " error: " + e.getMessage());
            }

            System.out.println("[SERVER] Client " + clientId + " disconnected");
        }
    }

    static class DemoClient implements Runnable {
        private final String name;
        private final String[] commands;
        private final CountDownLatch doneLatch;

        DemoClient(String name, String[] commands, CountDownLatch doneLatch) {
            this.name = name;
            this.commands = commands;
            this.doneLatch = doneLatch;
        }

        @Override
        public void run() {
            try (
                    Socket socket = new Socket("localhost", PORT);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
            ) {
                socket.setSoTimeout(3000);

                System.out.println("[" + name + "] Connected");

                for (String command : commands) {
                    writer.println(command);

                    String response = reader.readLine();

                    System.out.println("[" + name + "] >> "
                            + command + " => " + response);

                    Thread.sleep(100);
                }

                System.out.println("[" + name + "] Disconnected");
            } catch (SocketTimeoutException e) {
                System.out.println("[" + name + "] Timeout while waiting for response");
            } catch (IOException e) {
                System.out.println("[" + name + "] IOException: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[" + name + "] Interrupted");
            } finally {
                doneLatch.countDown();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread serverThread = new Thread(new Server(PORT, CLIENT_COUNT));
        serverThread.start();

        Thread.sleep(500);

        CountDownLatch clientsDone = new CountDownLatch(CLIENT_COUNT);

        Thread client1 = new Thread(new DemoClient(
                "CLIENT-1",
                new String[]{
                        "AUTH alice",
                        "OPEN",
                        "SEND hello",
                        "HISTORY",
                        "CLOSE"
                },
                clientsDone
        ));

        Thread client2 = new Thread(new DemoClient(
                "CLIENT-2",
                new String[]{
                        "AUTH bob",
                        "OPEN",
                        "BROADCAST ping",
                        "SEND second message",
                        "HISTORY",
                        "CLOSE"
                },
                clientsDone
        ));

        client1.start();
        client2.start();

        clientsDone.await();
        serverThread.join();
    }
}