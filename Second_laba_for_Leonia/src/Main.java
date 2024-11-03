import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in); // подключаем сканер
        MusicDownloads musicDownloads = new MusicDownloads(); // объявляем объект класса MusicDownloads для хранения информации о скачивании музыки

        boolean flag = true;
        while(flag){
            System.out.println("\nМеню:");
            System.out.println("1.Показать список");
            System.out.println("2.Добавить новую(ые) песню(и)");
            System.out.println("3.Проверка на существование песни в списке");
            System.out.println("4.Завершить программу");
            System.out.print("Выберите действие (1-5):");

            int choice = scan.nextInt();
            scan.nextLine();
            switch (choice){
                case 1:
                    if(musicDownloads.isEmpty()){
                        System.out.println("Список пуст");
                    }else {
                        musicDownloads.print();
                    }
                    break;
                case 2:
                    List<String> newMusic = new ArrayList<String>(); // объявляем список для хранения названий песен и последующего добавления
                    System.out.println("Введите название(я) песни(ен) для добавления(обновления информации) в список (для завершения введите 'STOP'):");
                    while (true) {
                        String str = scan.nextLine();
                        if (str.equalsIgnoreCase("STOP")) {
                            break;
                        }
                        newMusic.add(str);
                    }
                    musicDownloads.updateDownloads(newMusic); //добавляем новую музыку
                    break;
                case 3:
                    if(!musicDownloads.isEmpty()) {
                        System.out.println("Введите название песни:");
                        String title = scan.nextLine();
                        if (musicDownloads.getDownloadInfo(title) != null) {
                            System.out.println("Песня " + title + " есть в списке");
                            System.out.println("Количество скачиваний:" + musicDownloads.getDownloadInfo(title).getDownloadCount());
                        } else {
                            System.out.println("Такого песни нет в списке");
                        }
                    }else{
                        System.out.println("Список пуст");
                    }
                    break;
                case 4:
                    flag = false;
                    System.out.println("Стоп машина");
                    break;
                default:
                    System.out.println("Неверный ввод, попробуйте снова");
            }
        }

    }
}