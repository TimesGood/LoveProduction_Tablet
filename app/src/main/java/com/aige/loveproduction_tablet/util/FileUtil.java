package com.aige.loveproduction_tablet.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取文件的工具类
 */
public class FileUtil {

    private Context context;

    public FileUtil() {
    }

    public FileUtil(Context context) {
        super();
        this.context = context;
    }

    /**
     * 保存文件，默认保存app目录之下
     * @param filename 文件名，只允许有一层文件夹
     * @param filecontent 文件内容
     * @throws Exception
     */
    public void saveFile(String filename, String filecontent) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String[] split = filename.split("/");
            if(split.length>1) {
                filename = context.getExternalFilesDir(split[0])+"/"+split[1];
            }else{
                filename = context.getExternalFilesDir(null)+"/"+filename;

            }
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(file,true);
            //将String字符串以字节流的形式写入到输出流中
            output.write(filecontent.getBytes());

            output.close();
            //关闭输出流
        } else Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }

    /**
     * 读取文件，默认读取app目录之下
     * @param filename 文件名，只允许有一层文件夹
     * @return 内容
     * @throws IOException
     */
    public String readFrom(String filename) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String[] split = filename.split("/");
            if(split.length>1) {
                filename = context.getExternalFilesDir(split[0])+"/"+split[1];
            }else{
                filename = context.getExternalFilesDir(null)+"/"+filename;

            }
//            //打开文件输入流
//            FileInputStream input = new FileInputStream(filename);
//            byte[] temp = new byte[1024];
//            int len = 0;
//            //读取文件内容:
//            while ((len = input.read(temp)) > 0) {
//                sb.append(new String(temp, 0, len)).append("#");
//            }
//            //关闭输入流
//            input.close();
            FileReader fr = new FileReader(filename);
            BufferedReader buff = new BufferedReader(fr);
            while (buff.ready()) {
                sb.append(buff.readLine()).append("#");
            }
        }
        return sb.toString();
    }

    /**
     * 解析mpr文件
     */
    private static int i;
    public static Map<String,List<Map<String,Float>>> readFile(File file) {
        if(!file.isFile()) return null;
        //每个图形的具体数值
        Map<String,Float> map = null;
        //矩形数值
        List<Map<String,Float>> list1 = new ArrayList<>();
        //侧钉子数值
        List<Map<String,Float>> list2 = new ArrayList<>();
        //表面钉子数值
        List<Map<String,Float>> list3 = new ArrayList<>();
        List<Map<String,Float>> list4 = new ArrayList<>();
        //切割线
        List<Map<String,Float>> list5 = new ArrayList<>();
        //最终数据整合
        Map<String,List<Map<String,Float>>> maps = new HashMap<>();
        boolean flag = false;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader buff = new BufferedReader(fr);
            String name = null;
            while (buff.ready()) {
                String readLine = buff.readLine();
                //查找组件
                if(readLine.contains("[H")) {
                    //主图数据
                    map = new HashMap<>();
                    name = "rectangle";
                    flag = true;
                }else if(readLine.contains("BM=\"XP\"") || readLine.contains("BM=\"XM\"") || readLine.contains("BM=\"YM\"")) {
                    //侧钉子数据
                    map = new HashMap<>();
                    name = "BohrHoriz1";
                    flag = true;
                }else if(readLine.contains("BM=\"LS\"")) {
                    //表面钉子样式1
                    map = new HashMap<>();
                    name = "BohrVert1";
                    flag = true;
                }else if(readLine.contains("BM=\"LSU\"")) {
                    //表面钉子样式2
                    map = new HashMap<>();
                    name = "BohrVert2";
                    flag = true;
                }else if(readLine.contains("$E0")) {
                    //切割数据
                    map = new HashMap<>();
                    name = "Cutting1";
                    flag = true;
                    i = 0;
                }
                //检索到某图形数据时开启通道
                if(flag) {
                    //判断那个图形的通道，并对数据进行整理归纳储存于集合中
                    if("rectangle".equals(name)) {
                        flag = parseData(readLine, map, list1);
                    }else if("BohrHoriz1".equals(name)) {
                        flag = parseData(readLine, map, list2);
                    }else if("BohrVert1".equals(name)) {
                        flag = parseData(readLine, map, list3);
                    }else if("BohrVert2".equals(name)) {
                        flag = parseData(readLine, map, list4);
                    }else if("Cutting1".equals(name)) {
                        flag = parseData(readLine,map,list5);
                    }
                }
            }
            //当最终数据通道都关闭时，数据解析成功，反之解析失败
            if(flag) return null;
            //解析成功储存数据
            maps.put("rectangle",list1);
            maps.put("BohrHoriz1",list2);
            maps.put("BohrVert1",list3);
            maps.put("BohrVert2",list4);
            maps.put("Cutting1",list5);
            return maps;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 整理图形数据
     * @param data 原数据
     * @param map 以键值对储存对应图形属性
     * @param list 一个图形的属性整合
     * @return 当数据正常解析完成返回false，此返回值可作为关闭解析通道的值
     */
    private static boolean parseData(String data,Map<String,Float> map,List<Map<String,Float>> list) {
        //矩形数据解析
        if(data.contains("_BSX=")) map.put("BSX",Float.parseFloat(data.split("=")[1]));
        if(data.contains("_BSY=")){
            map.put("BSY",Float.parseFloat(data.split("=")[1]));
            list.add(map);
            return false;
        }
        //钉子数据解析
        if (data.contains("XA=")) map.put("XA",patternText(data));
        if (data.contains("YA=")) map.put("YA",patternText(data));
        if (data.contains("DU=")) map.put("DU",patternText(data));
        if (data.contains("TI=")) {
            map.put("TI",patternText(data));
            list.add(map);
            return false;
        }
        //切割数据解析
        if (data.contains("X=") && !data.contains(".X=")) map.put("X"+i,Float.parseFloat(data.split("=")[1]));
        if (data.contains("Y=") && !data.contains(".Y=")) {
            map.put("Y"+i,Float.parseFloat(data.split("=")[1]));
            i++;
        }
        if(data.contains("]") || data.contains("[001")) {
            list.add(map);
            return false;
        }
        return true;
    }

    /**
     * 使用正则表达式截取双引号中的值
     * @param text
     * @return
     */
    public static Float patternText(String text) {
        Matcher m = Pattern.compile("\"(.*?)\"").matcher(text);
        if (m.find()) {
            return Float.parseFloat(m.group(1));
        }
        return null;
    }
}