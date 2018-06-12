package com.lyz.backend.framework.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FFMpegUtil {
    private static Log log = LogFactory.getLog(FFMpegUtil.class);

    // 运行命令
    public static boolean runCmd(String command) {
        long t = System.currentTimeMillis();
        boolean result = false;
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            if (log.isDebugEnabled()) {
                InputStream stderr = proc.getErrorStream();
                InputStreamReader isr = new InputStreamReader(stderr);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    log.debug(line);
                }

                br.close();
            }
            result = (proc.waitFor() == 0);
        } catch (Throwable e) {
            e.printStackTrace();
            log.error(e);
            result = false;
        } finally {
            log.info("EXEC " + command + "\tcost:" + (System.currentTimeMillis() - t) + "\tsucc:"
                    + result);
        }
        return result;
    }

    /**
     * 视频转码
     *
     * @param infile
     * @param outfile
     * @return
     */
    public static boolean libx264(String infile, String outfile) {
        return runCmd("/usr/bin/ffmpeg -i " + infile + " -vcodec libx264 " + outfile);
    }

    public static boolean amr2mp3(String infile, String outfile) {
        return runCmd("/usr/bin/ffmpeg -i " + infile + " -vcodec libx264 " + outfile);
    }

    /**
     * IOS5的mp4文件编码修正,确保android可以播放
     *
     * @param infile
     * @return
     */
    public static boolean iOS5Mp4Fix(String infile) {
        File in = new File(infile);
        if (!in.exists()) {
            return false;
        }

        String outfile = infile.substring(0, infile.lastIndexOf("."));
        outfile += "_tmp.mp4";

        File out = new File(outfile);
        if (out.exists()) {
            return false;
        }

        if (libx264(infile, outfile)) {
            in.delete();
            out.renameTo(in);
            return true;
        }
        return false;
    }

    /**
     * amr转mp3文件，<br />
     * s将新生成一个同名mp3
     *
     * @param infile
     * @return
     */
    public static boolean amr2mp3(String infile) {
        File in = new File(infile);
        if (!in.exists()) {
            return false;
        }

        String outfile = infile.substring(0, infile.lastIndexOf("."));
        outfile += ".mp3";

        File out = new File(outfile);
        if (out.exists()) {
            return true;
        }

        return runCmd("/usr/bin/ffmpeg -i " + infile + " " + outfile);
    }

    public static void main(String args[]) {
        System.out.println(FFMpegUtil.iOS5Mp4Fix("/Users/leo/tmp/5.mp4"));
        System.out.println(FFMpegUtil.amr2mp3("/Users/leo/tmp/1.amr"));
    }
}