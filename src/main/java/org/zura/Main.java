package org.zura.XlsxLineCounter;

import java.io.IOException;
//import java.util.Properties;


public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("xlsxファイルのあるディレクトリを指定してください.");
                return;
            }
            String xlsxDirectory = args[0];
            XlsxLineCounter counter = new XlsxLineCounter(xlsxDirectory);
            counter.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
