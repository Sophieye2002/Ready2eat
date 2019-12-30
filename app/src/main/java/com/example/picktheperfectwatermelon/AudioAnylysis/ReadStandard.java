package com.example.picktheperfectwatermelon.AudioAnylysis;

import android.nfc.TagLostException;
import android.os.Environment;

import com.zlw.main.recorderlib.utils.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Locale;

public class ReadStandard {
    private static final String TAG = "yin";
    private String recordDir = String.format(Locale.getDefault(), "%s/Record/com.yin.main/", Environment.getExternalStorageDirectory().getAbsolutePath());

    private File f;//源文件
    private File outF;//写出的文件
    private RandomAccessFile rdf;
    private String standardString;//源数据二进制字符串文件
    private String fileredString ;//过滤后的二进制字符串文件
    private int fileSize;//文件大小
    private short fileFormt;//文件格式，1-pcm
    private short num_channels;//1-单声道 2-双声道
    private int sample_rate;//采样率、音频采样级别
    private int byte_rate;//每秒波形的数据量
    private short block_align;//采样帧的大小
    private short bits_per_sample;//采样位数

    public ReadStandard(String fileName) {
        //f = new File(recordDir + "/" + fileName + ".wav");

        Logger.i(TAG,"传入文件名：%s",fileName);
        f = new File(fileName);
    }


    //读取wav
    public int readFile() {
        int re = 20;
        try {
            if(!f.exists()){
                return 0;
            }

            rdf = new RandomAccessFile(f, "r");
            fileSize = toInt(read(rdf, 4, 4));//文件大小
            fileFormt = toShort(read(rdf, 20, 2));
            num_channels = toShort(read(rdf, 22, 2));
            sample_rate = toInt(read(rdf, 24, 4));
            byte_rate = toInt(read(rdf, 28, 4));
            block_align = toShort(read(rdf, 32, 2));
            bits_per_sample = toShort(read(rdf, 34, 2));
            //standardByte = new byte[fileSize];


            byte [] array = read(rdf, 36, fileSize);
            //standardString = Arrays.toString(array);

            //fileredString = Arrays.toString(filedData(array,fileSize));
            Logger.i(TAG,"测试度出的数据===文件大小：%s", getFileSize()) ;

              int len = filedAnalysis(array);
//            Logger.i(TAG,"测试度出的数据===文件格式：%s",getFileFormt()) ;
//            Logger.i(TAG,"测试度出的数据===声道：%s", getNum_channels()) ;
//            Logger.i(TAG,"测试度出的数据===采样率：%s", getSample_rate()) ;
//            Logger.i(TAG,"测试度出的数据===采样帧的大小：%s", getBlock_align()) ;
//            Logger.i(TAG,"测试度出的数据===每秒数据量：%s", getByte_rate()) ;
//            Logger.i(TAG,"测试度出的数据===采样位数：%s", getBits_per_sample()) ;
            if(0 < len ){
                if(len < 1500){
                    re = (int)((30.0/1500.0) *len);
                }else if( len < 3000){
                    re = (int)(30 +((40.0)/(3000.0-1500.0))*(len - 1500));
                }else if(len < 6000){
                    re = (int)(70 + ((30.0)/(6000.0-3000.0)) * (len -3000));
                }else {
                    re = 100;
                }

            }else {
                re = 20;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {

            if(f.isFile()){
                f.delete();//删除文件
                Logger.i(TAG,"最近一次录音文件删除成功！");
            }

            try {
                if (rdf != null) {
                    rdf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return re;


    }


    private int toInt(byte[] b) {
        return ((b[3] << 24) + (b[2] << 16) + (b[1] << 8) + (b[0] << 0));
    }

    private short toShort(byte[] b) {
        return (short) ((b[1] << 8) + (b[0] << 0));
    }


    //读取字节数组
    private byte[] read(RandomAccessFile rdf, int pos, int length) {
        byte[] result = new byte[length];
        try {
            rdf.seek(pos);
            for (int i = 0; i < length; i++) {
                result[i] = rdf.readByte();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //过滤文件
    private byte [] filedData(byte [] result,int length){
        byte [] filed = new byte[length/4];
        byte sta = 120;//设置幅值上限
        int j = 0;//符合要求的个数

        for(int i= 0 ; i < length ; i++){

            if((result[i] < (- sta)) || (result[i] > sta ) ){
                filed[j++] = result[i];
            }
        }
        Logger.i(TAG,"符合条件的字节个数：%s"+j);
        return filed;
    }
    //过滤文件
    private int filedAnalysis(byte [] result){
        byte sta = 95;//设置幅值上限
        int j = 0;//符合要求的个数
        for(int i= 0 ; i < result.length ; i++){
            if((result[i] < (- sta)) || (result[i] > sta ) ){
                j++;
            }
        }
        Logger.i(TAG,"符合条件的字节个数：%s"+j);
        return j;
    }

    public void writeFilteredFile(String outFileName){
        BufferedWriter bw = null;
        try {
            outF = new File(recordDir + "/f_" + outFileName + ".txt");
            bw = new BufferedWriter(new FileWriter(outF, true));
            bw.write(fileredString);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    //向sdcard中写入全部录音数据的二进制文件
    public void writeFile(String outFileName) {
        BufferedWriter bw = null;
        try {
            outF = new File(recordDir + "/" + outFileName + ".txt");
            bw = new BufferedWriter(new FileWriter(outF, true));
            bw.write(standardString);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public int getFileSize() {
        return fileSize;
    }

}
