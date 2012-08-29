package ru.atconsulting.tests.base64;

import net.sourceforge.iharder.java.base64.Base64;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.zip.*;

/**
 * inEncodedFile -декодирование-> decodedZipFile -разархивирование-> unzippedPath
 * @author Astafyev Evgeny (astafev)
 */
public class ConsoleDriver {

    static Logger log = Logger.getLogger(Console.class.getName());

    /**файл, в который запишется поток байт после декодирования*/
    private static String decodedZipFile = "decoded.zip";
    /**файл, который будет кодирован затем*/
    private static String zipToEncode = "toEncode.zip";

    /**файл, в который будет помещено сообщения после кодирования.
     * Вот этот файл и нужно вставлять в сообщение потом.*/
    private static String outEncodedFile = "out.txt";
    /**файл, где лежит исходное сообщение, которое надо будет декодировать*/
    private static String inEncodedFile = "in.txt";
    /**файл, где лежит исходное сообщение, которое надо будет декодировать*/
    private static String unzippedPath = "unzipped";

    public static void main(String[] args) throws IOException {
        //задаем файлы по введенным опциям, мазафака
        for(int i = 0; i<args.length; i++){
            if (args[i].equals("-zipOut")) {
                decodedZipFile = args[++i];

            } else if (args[i].equals("-zipIn")) {
                zipToEncode = args[++i];

            } else if (args[i].equals("-res")) {
                outEncodedFile = args[++i];

            } else if (args[i].equals("-in")) {
                inEncodedFile = args[++i];

            } else {
                if (!args[i].equals("-h") || !args[i].equals("-help") || !args[i].equals("--h") || !args[i].equals("--help") || !args[i].equals("/?"))
                    System.out.println("You use it wrong, dump idiot!!\n");
                System.out.println("Use the following options: \n    -zipOut  to assign file, in what archive after decoding will be" + decodedZipFile +
                        "\n    -zipIn   to assign archive to encode. By default: " + zipToEncode +
                        "\n    -res     to assign file, where will be encoded string. This is the end file that we need. By default: " + outEncodedFile +
                        "\n    -in      to assign file with String to decode. By default: " + inEncodedFile
                );
                return;
            }
        }

        String advancedOptions =
                "\nTo decode and unzip type dz. "+inEncodedFile+" will be unzipped to \""+ unzippedPath +"\"." +
                        "\nTo zip and encode type ze. All files from "+unzippedPath+" will be zipped and encoded back to \""+ outEncodedFile +"\"." +
                        "\nTo decode type decode or d. \""+inEncodedFile+"\" will be decoded to \""+ decodedZipFile +"\"." +
                "\nTo encode type encode or e. \""+ zipToEncode +"\" will be encoded to \""+outEncodedFile+"\"."+
                //todo!!
                "\nTo encode type encode or e. \""+ zipToEncode +"\" will be encoded to \""+outEncodedFile+"\".";
        System.out.println("You should look to folder \"/"+unzippedPath+"\" for request. Change what you need and then archive them back (use du and ez options)" +
                "\n  To exit type quit or q." +
                "\n  To decode and unzip type du. "+inEncodedFile+" will be unzipped to \""+ unzippedPath +"\"." +
                "\n  To zip and encode type ze. All files from "+unzippedPath+" will be zipped and encoded back to \""+ outEncodedFile +"\"." +
                "\n  Probably you won't use other options. But you can see them by typing a (for advanced or all, what you like more)."
                +"\n\n");
        Console con = System.console();
        Unzipper unzipper = new Unzipper();
        while(true){
            String command = con.readLine();
            if (command.equals("q") || command.equals("quit") || command.equals("exit")) {
                System.out.println("Goodbye! Come again.");
                return;
            }
            if (command.equals("d") || command.equals("decode")){
                decode();
            }
            if (command.equals("e") || command.equals("encode"))  {
                encode();
            }

            if (command.equals("du") || command.equals("decode and unzip") || command.equals("1"))  {
                decode();
                unzipper.unzip();
            }

            if (command.equals("ze") || command.equals("zip and encode") || command.equals("2"))  {
                unzipper.zip();
                encode();
            }
            if (command.equals("a") || command.equals("advanced") || command.equals("all") || command.equals("h") || command.equals("help"))  {
                System.out.println(advancedOptions);
            }
            if (command.equals("-sfn") || command.equals("setFileName"))  {
                System.out.print("Type filename here: ");
                unzipper.setFileName(con.readLine());
            }


        }
    }

    private static void decode() throws IOException {
        Base64.decodeFileToFile(inEncodedFile, decodedZipFile);
        log.info("Decoded!");
    }
    private static void encode() throws IOException {
        Base64.encodeFileToFile(zipToEncode, outEncodedFile);
        log.info("Encoded!");

    }



    static class Unzipper{

        private String fileName;

        void setFileName(String fileName) {
            this.fileName = fileName;
        }

        /**
         * Разархивирует, положит все в unzippedPath (unzipped по умолчанию)
         * */
        void unzip() throws IOException {
            ZipFile zipFile = new ZipFile(decodedZipFile);
            Enumeration e = zipFile.entries();
            File directory = new File(unzippedPath);

            if(directory.exists())
            {
                //старые файлы скидываем в папку trash
                File[] oldFiles = directory.listFiles();
                File trashDirectory = new File("old_trash");
                if(!trashDirectory.exists())
                    trashDirectory.mkdir();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                File todaysTrash = new File(trashDirectory, dateFormat.format(new Date()));
                if(!todaysTrash.exists())
                    todaysTrash.mkdir();

                for(File f: oldFiles){
                    //перемещаем в trash
                    if(!f.renameTo(new File(todaysTrash, f.getName()))){
                        log.severe("Ow, shit! I couldn't copy " + f.getName());
                        log.severe("We all will die!!!!");
                        return;
                    }
                }
            }
            else directory.mkdir();

            while(e.hasMoreElements()){
                ZipEntry ze = (ZipEntry) e.nextElement();
                fileName = ze.getName();

                log.info("Unzipping " + ze.getName());
                FileOutputStream fout = new FileOutputStream(unzippedPath+"/"+ze.getName());
                InputStream in = zipFile.getInputStream(ze);
                for (int c = in.read(); c != -1; c = in.read()) {
                    fout.write(c);
                }
                in.close();
                fout.close();
            }
            log.info("Done!");
        }

        void zip() throws IOException {
            if(fileName==null) {
                log.warning("You should unzip before!");
                log.warning("You can bypass this, by typing -sfn (setFileName).");
                return;
            }

            FileOutputStream fout = null;
            ZipOutputStream zout = null;
            FileInputStream fis = null;
            try{
                File zipFile = new File(zipToEncode);

                zipFile.createNewFile();
                fout = new FileOutputStream(zipFile);
                zout = new ZipOutputStream(fout);
                ZipEntry zipEntry = new ZipEntry(fileName);
                fis = new FileInputStream(unzippedPath + "/" + fileName);
                zout.putNextEntry(zipEntry);
                for (int c = fis.read(); c != -1; c = fis.read()) {
                    zout.write(c);
                }
                zout.closeEntry();
                System.out.println("File "+fileName+" zipped to " + zipToEncode + " archive.");
            } finally {
                if(fis!=null)
                    fis.close();
                if(zout!=null)
                    zout.close();
                if(fout!=null)
                    fout.close();
            }
        }
    }
}



