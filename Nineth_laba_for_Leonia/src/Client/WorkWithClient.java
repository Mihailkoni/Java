package Client;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

class WorkWithClient {
    // сокет
    private Socket socket;
    // буфер для чтения
    private BufferedReader in;
    // буфер для записи
    private BufferedWriter out;
    // буфер для чтения сообщений клиента
    private BufferedReader inputUser;
    // ip-адрес сервера
    private String address;
    // порт сервера
    private int port;
    // никнейм клиента
    private String nickname;
    // класс для работы с датой
    private Date date;
    // формат даты
    private SimpleDateFormat pattern;
    // текущая дата
    private String currentDate;

    // установка никнейма клиента
    private void setNickname(){
        System.out.println("Enter nickname: ");
        try{
            nickname = inputUser.readLine();
            out.write("<" + nickname + ">" +" joined the chat\n");
            out.flush();
        } catch (IOException e){
            System.err.println("Error setting nickname.");
        }
    }

    // отсоединение
    private void serviceOff(){
        try {
            if(!socket.isClosed()){
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException _){}
    }

    public WorkWithClient(String address, int port) {
        this.address = address;
        this.port = port;

        try {
            // создаём соединение с сервером
            this.socket = new Socket(address,port);
            System.out.println("Connected to the server.");
        } catch (IOException e){
            System.err.println("Socket connection failed.");
            return;
        }

        try {
            // инициализируем потоки
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // ввод никнейма
            this.setNickname();

            // создаём поток для чтения сообщений с сервера
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String str;
                        while ((str = in.readLine()) != null) {
                            if("exit".equalsIgnoreCase(str)){
                                serviceOff();
                                break;
                            }
                            System.out.println(str);
                        }
                    } catch (IOException e) {
                        serviceOff();
                    }
                }
            }).start();
            // создаём поток для записи сообщений на сервер
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        String str;
                        while(true){
                            date = new Date();
                            pattern = new SimpleDateFormat("<dd MMM, HH:mm>");
                            currentDate = pattern.format(date);
                            str = inputUser.readLine();
                            if("exit".equalsIgnoreCase(str)){
                                out.write("stop\n");
                                out.flush();
                                serviceOff();
                                break;
                            }
                            out.write(currentDate + " " + nickname + ": " + str + "\n");
                            out.flush();
                        }
                    } catch (IOException e){
                        serviceOff();
                    }

                }
            }).start();

        }catch (IOException e){
            // закрытие соединения
        }
    }
}