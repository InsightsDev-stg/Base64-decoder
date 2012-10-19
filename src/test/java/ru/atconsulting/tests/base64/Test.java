package ru.atconsulting.tests.base64;



import java.io.Console;
import java.util.logging.*;
import static ru.atconsulting.tests.base64.ConsoleDriver.*;

/**
 * User: Astafev Evgeny
 * Date: 22.08.12
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main (String[]args) throws Exception{

        log.info("fuck you");
        /*File file = new File("test.txt");
        File outFile = new File("outtest.txt");

        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fis.read(bytes);
        System.out.println(new String(bytes));
        OutputStream outputStream = new FileOutputStream(outFile);

        outputStream.write(Base64.decode(new String(bytes)));
        fis.close();
        outputStream.close();*/
    }

}
