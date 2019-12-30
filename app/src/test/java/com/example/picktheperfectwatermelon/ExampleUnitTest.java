package com.example.picktheperfectwatermelon;

import com.example.picktheperfectwatermelon.AudioAnylysis.ReadStandard;
import com.zlw.main.recorderlib.utils.Logger;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
 public class  ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void test(){
        int re ;
        int len =2437;
        if(0 < len ){

            if(len < 1500){
                re = (30/1500) *len;
            }else if( len < 4000){
                re = 30 +((70-30)/(4000-1500))*(len - 1500);
            }else if(len < 6000){
                re = 70 + ((100-70)/(6000-4000)) * (len -4000);
            }else {
                re = 100;
            }

        }else {
            re = 20;
        }

    }
    //@Test
   // public void readWav(){
//        ReadStandard rs = new ReadStandard("excellent");
        //Logger.i("yin","测试度出的数据===文件大小：", rs.getFileSize()) ;
        //Logger.i("yin","测试度出的数据===文件格式：", rs.getFileFormt()) ;
        //Logger.i("yin","测试度出的数据===声道：", rs.getNum_channels()) ;
        //Logger.i("yin","测试度出的数据===采样率：", rs.getSample_rate()) ;
        //Logger.i("yin","测试度出的数据===采样帧的大小：", rs.getBlock_align()) ;
        //Logger.i("yin","测试度出的数据===每秒数据量：", rs.getByte_rate()) ;
        //Logger.i("yin","测试度出的数据===采样位数：", rs.getBits_per_sample()) ;
        //Logger.i("yin","测试度出的数据===文件：", Arrays.toString(rs.getStandardByte()) ) ;//声音文件
//        System.out.println("测试度出的数据===文件大小："+rs.getFileSize());
//        System.out.println("测试度出的数据===文件格式："+rs.getFileFormt());
//        System.out.println("测试度出的数据===声道："+ rs.getNum_channels());
//        System.out.println("测试度出的数据===采样率："+ rs.getSample_rate());
//        System.out.println("测试度出的数据===采样帧的大小："+ rs.getBlock_align());
//        System.out.println("测试度出的数据===每秒数据量："+rs.getByte_rate());
//        System.out.println("测试度出的数据===采样位数："+rs.getBits_per_sample());
//        System.out.println("测试度出的数据===文件："+Arrays.toString(rs.getStandardByte()));



   // }
}