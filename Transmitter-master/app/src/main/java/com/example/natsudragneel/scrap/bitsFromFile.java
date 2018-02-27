package com.example.natsudragneel.scrap;
import java.io.*;

public class bitsFromFile {
    public static int[] bytesTobits(String fileName){
        //File file = new File(fileName);
        byte[] bytesData = fileName.getBytes();
        /*try {
            FileInputStream input = new FileInputStream( file );
            try {
                input.read(bytesData);
                input.close();
            } catch( EOFException eof ) {
            } catch( IOException e1 ) {
                e1.printStackTrace();
            }
        } catch( FileNotFoundException e2 ) {
            e2.printStackTrace();
        }*/

        int noOfBits = 8*bytesData.length;
        int[] bits = new int[noOfBits];
        int x,i = 0;
        for(int k = 0; k < fileName.length(); k++){
            x = bytesData[k];
            while(i<noOfBits){
                bits[i] = x%2;				//MSB in the last element
                x = x/2;
                i++;
                if(i % 8 == 0)
                    break;
            }
        }
        return bits;
    }
}
