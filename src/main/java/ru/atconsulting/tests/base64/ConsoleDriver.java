package ru.atconsulting.tests.base64;

import net.sourceforge.iharder.java.base64.Base64;

import java.io.*;

/**
 *
 * @author Astafyev Evgeny (astafev)
 */
public class ConsoleDriver {
    /**файл, в который запишется поток байт после декодирования*/
    private static String outZipFile = "out.zip";
    /**файл, который будет кодирован затем*/
    private static String inZipFile = "out.zip";
    /**файл, в который будет помещено сообщения после кодирования.
     * Вот этот файл и нужно вставлять в сообщение потом.*/
    private static String outEncodedFile = "out.txt";
    /**файл, где лежит исходное сообщение, которое надо будет декодировать*/
    private static String inEncodedFile = "in.txt";

    public static void main(String[] args) throws IOException {
        //задаем файлы по введенным опциям, мазафака
        for(int i = 0; i<args.length; i++){
            switch (args[i]){
                case "-zipOut": outZipFile = args[++i];
                             break;
                case "-zipIn": inZipFile = args[++i];
                             break;
                case "-res": outEncodedFile = args[++i];
                             break;
                case "-in": inEncodedFile = args[++i];
                            break;
                default:
                    if(!args[i].equals("-h") || !args[i].equals("-help") || !args[i].equals("--h") || !args[i].equals("--help") || !args[i].equals("/?"))
                        System.out.println("You use it wrong, dump idiot!!\n");
                    System.out.println("Use the following options: \n    -zipOut  to assign file, in what archive after decoding will be" + outZipFile +
                            "\n    -zipIn   to assign archive to encode. By default: " + inZipFile +
                            "\n    -res     to assign file, where will be encoded string. This is the end file that we need. By default: " + outEncodedFile +
                            "\n    -in      to assign file with String to decode. By default: " + inEncodedFile);
                    return;
            }
        }

        System.out.println("To exit type quit or q.\n" +
                "To decode type decode or d. \""+inEncodedFile+"\" will be decoded to \""+outZipFile+"\"." +
                "\nTo encode type encode or e. \""+inZipFile+"\" will be encoded to \""+outEncodedFile+"\".");
        Console con = System.console();
        while(true){
            String command = con.readLine();

            if (command.equals("q") || command.equals("quit") || command.equals("exit")) {
                System.out.println("Come again");
                return;
            }
            if (command.equals("d") || command.equals("decode")){
                Base64.decodeFileToFile(inEncodedFile, outZipFile);
                System.out.println("Done!");
            }
            if (command.equals("e") || command.equals("encode"))  {
                Base64.encodeFileToFile(inZipFile, outEncodedFile);
                System.out.println("Done!");
            }
        }

    }
}



