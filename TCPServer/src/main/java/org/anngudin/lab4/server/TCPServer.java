package org.anngudin.lab4.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.lang.Runnable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import org.anngudin.lab4.server.JDBCPostgreSQL;
import org.anngudin.lab4.server.Constants;


class Client {
    Socket connection;

    int connID;

    Client(Socket conn, int id) {
        connection = conn;
        connID = id;
    }
}

public class TCPServer {

    static AtomicInteger countclients = new AtomicInteger(0);
    private static BlockingQueue<Client> socketQuene = new LinkedBlockingQueue<Client>();

    public static class Handler implements Runnable {
        private JDBCPostgreSQL connector;

        public void setConnector(JDBCPostgreSQL connector) {
            this.connector = connector;
        }

        @Override
        public void run() {
            for (;;) {
                Client client;
                try {
                    client = socketQuene.take();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    outputStream = client.connection.getOutputStream();
                    Map<String, List<HashMap<String, String>>> data = new HashMap<String, List<HashMap<String, String>>>();
                    data.put("clients", connector.addClients());
                    data.put("orders", connector.addOrders());
                    byte[] resp = JSONObject.toJSONString(data).getBytes();
                    outputStream.write(resp);
                } catch (IOException | SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("Client " + client.connID + " disconnected due to an error creating out stream");
                }
                try {
                    inputStream = client.connection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        outputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("Client " + client.connID + " disconnected due to an error in the request");
                }
                int sz;
                try {
                    sz = client.connection.getReceiveBufferSize();
                } catch (Throwable e) {
                    sz = 1024;
                }

                byte[] bytes = new byte[sz];
                // // int inLen = 0;
                Map<String, String> request = new HashMap();
                try {
                    // inLen = inputStream.available();
                    ObjectMapper mapper = new ObjectMapper();
                    inputStream.read(bytes);
                    request = mapper.readValue(bytes, Map.class);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
               
                // if (inLen > 0) {
                    // while (true) {
                    // String request = bytes.toString();
                    System.out.println(request.toString());
                    // int indexOfTypeEnd = request.indexOf(' ');
                    // if (indexOfTypeEnd < 0) {
                    //     try {
                    //         inputStream.close();
                    //         outputStream.close();
                    //         System.out.println(
                    //                 "Client " + client.connID + " disconnected due to an error in the request");
                    //     } catch (IOException e1) {
                    //         e1.printStackTrace();
                    //     }
                    //     continue;
                    // }
                    // String type = request.substring(0, indexOfTypeEnd);
                    String response = "";
                    // // List<String> responseArray = null;
                    // // type = "kek";
                    // String[] strings = request.split(" ");

                    // if (request.length() != type.length())
                    // data = request.substring(type.length() + 1);

                    // System.out.println("Query: " + request);

                    switch (request.get("type")) {
                    case Constants.CLIENTS_ADD:
                        response = connector.addClient(request.get("TIN"), request.get("country"));
                        break;
                    case Constants.CLIENTS_DELETE:
                        response = connector.deleteClientsById(request.get("id"));
                        response = connector.deleteOrdersByClientId(request.get("id"));
                        break;
                    case Constants.ORDERS_ADD:
                        response = connector.addOrder(request.get("myType"),request.get("subtype"),request.get("marking"),request.get("client_id"));
                        break;
                    case Constants.ORDERS_DELETE:
                        response = connector.deleteOrdersById(request.get("id"));
                        break;
                    case Constants.CHANGE:
                        response = connector.changeDataInTable(request.get("table"), request.get("column"), request.get("value"), request.get("id"));
                        break;
                    }
                    try {
                        
                        outputStream.write(response.getBytes());

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Ошибка при получении запроса от клиента");
                    }

                    try {
                        inputStream.close();
                        outputStream.close();
                        System.out.println("Client " + countclients + " disconnected");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
            }

        }

    }
    public static void main(String args[]) {
        try {
            for (int i = 0; i < 4; i++) {
                Handler runner = new Handler();
                JDBCPostgreSQL connector = new JDBCPostgreSQL("jdbc:postgresql://127.0.0.1:5432/military", "admin", "password");
                runner.setConnector(connector);
                Thread thread = new Thread(runner);
                thread.start();
            }
        } catch (Exception ex) {
            System.out.println("Error " + ex.toString());
        }

        ServerSocket sock = null;
        try {
            sock = new ServerSocket(9999);
        } catch (IOException e) {
            System.out.println("Could not start server: " + e.toString());
            if (sock != null) {
                try {
                    sock.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return;
        }

        while (true) {
            try {
                Socket conn = sock.accept();
                int currID = countclients.incrementAndGet();
                // if (currID % 10000 == 0) {
                System.out.println("=======================================");
                System.out.println("Client " + currID + " connected");
                // }
                socketQuene.put(new Client(conn, currID));
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }
}
