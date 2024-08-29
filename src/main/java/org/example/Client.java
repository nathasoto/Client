package org.example;

import java.io.*;
import java.net.*;
public class Client {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader userIn;

//    public void startConnection(String ip, int port) throws IOException {
//        clientSocket = new Socket(ip, port);
//        out = new PrintWriter(clientSocket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//        userIn = new BufferedReader(new InputStreamReader(System.in));
//
//        String serverMessage = in.readLine();
//        System.out.println(serverMessage);
//        String userName = userIn.readLine();
//        out.println(userName);
//
//    }

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        userIn = new BufferedReader(new InputStreamReader(System.in));

        String serverMessage = in.readLine();
        System.out.println(serverMessage);
        String userName = userIn.readLine();
        out.println(userName);

        boolean isRegistered = false;
        while (!isRegistered) {
            serverMessage = in.readLine();
            System.out.println( serverMessage);

            if (serverMessage.startsWith("Welcome")) {
                isRegistered = true;
            } else if (serverMessage.startsWith("Name already taken") || serverMessage.startsWith("Name provided is null")) {
                userName = userIn.readLine();
                out.println(userName);
            }
        }

        new Thread(new ServerListener()).start();

        chat();
    }



//    public void chat() throws IOException {
//        String userInput;
//        String serverResponse;
//
//        boolean isRegistered = false;
//        while (!isRegistered) {
//            serverResponse = in.readLine();
//            System.out.println("Server: " + serverResponse);
//
//            if (serverResponse.startsWith("Welcome")) {
//                isRegistered = true;
//            } else if (serverResponse.startsWith("Name already taken") || serverResponse.startsWith("Name provided is null")) {
//                userInput = userIn.readLine();
//                out.println(userInput);
//            }
//        }
//
//
//        while (true) {
//            System.out.print("Enter message (type 'exit' to quit): ");
//            userInput = userIn.readLine();
//
//            if ("exit".equalsIgnoreCase(userInput)) {
//                break;
//            }
//
//            out.println(userInput);
//            serverResponse = in.readLine();
//            System.out.println("Server: " + serverResponse);
//        }
//    }

    public void chat() throws IOException {
        String userInput;
        while (true) {
//            System.out.print("Enter message (type 'exit' to quit): ");
            System.out.print(" ");
            userInput = userIn.readLine();

            if ("exit".equalsIgnoreCase(userInput)) {
                break;
            }

            out.println(userInput);
        }
    }
    private class ServerListener implements Runnable {
        @Override
        public void run() {
            String serverResponse;
            try {
                while ((serverResponse = in.readLine()) != null) {
                    System.out.println(serverResponse);
                }
            } catch (IOException e) {
                System.err.println("Error reading from server: " + e.getMessage());
            }
        }
    }


    public void stopConnection() throws IOException {
        if (userIn != null) {
            userIn.close();
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.startConnection("localhost", 6666);
//            client.chat();
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            try {
                client.stopConnection();
            } catch (IOException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
