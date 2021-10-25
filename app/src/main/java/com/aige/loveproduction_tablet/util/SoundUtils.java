package com.aige.loveproduction_tablet.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import androidx.annotation.RawRes;

import java.util.HashMap;

/**
 * 媒体提示音播放
 */
 
@SuppressLint("UseSparseArrays")
@SuppressWarnings("deprecation")
public class SoundUtils {
	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * 音频池对象
	 */
	private SoundPool soundPool;
	/**
	 * TODO 添加的声音资源参数
	 */
	private HashMap<Integer, Integer> soundPoolMap;

	/**
	 * 播放方式
	 */
	//无限循环
	public static final int INFINITE_PLAY = -1;
	//单次播放
	public static final int SINGLE_PLAY = 0;
	/**
	 * 音频音量播放类型，一般为媒体音
	 */
	private int soundVolType = 3;
	/**
	 * 音量
	 */
	public static final int RING_SOUND = 2;
	/**
	 * 媒体音量
	 */
	public static final int MEDIA_SOUND = 3;

	public SoundUtils(Context context, int soundVolType) {
		this.context = context;
		this.soundVolType = soundVolType;
		// 初始化声音池和声音参数map
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		soundPoolMap = new HashMap<Integer, Integer>();
	}
 
	/**
	 * 
	 * 添加音频文件进音频池
	 *
	 * @param order 所添加声音的编号，播放的时候指定
	 * @param soundRes 添加声音资源的id
	 */
	public void putSound(int order, @RawRes int soundRes) {
		// 上下文，声音资源id，优先级
		soundPoolMap.put(order, soundPool.load(context, soundRes, 1));
	}
 
	/**
	 * 
	 * 播放音频
	 * @param order 所添加声音的编号
	 * @param times 循环次数，0无不循环，-1无永远循环
	 * @see
	 */
	@SuppressWarnings("static-access")
	public void playSound(int order, int times) {
		// 实例化AudioManager对象
		AudioManager am = (AudioManager) context
				.getSystemService(context.AUDIO_SERVICE);
		// 返回当前AudioManager对象播放所选声音的类型的最大音量值
		float maxVolumn = am.getStreamMaxVolume(soundVolType);
		// 返回当前AudioManager对象的音量值
		float currentVolumn = am.getStreamVolume(soundVolType);
		// 比值
		float volumnRatio = currentVolumn / maxVolumn;
		soundPool.play(soundPoolMap.get(order), volumnRatio, volumnRatio, 1,
				times, 1);
	}
 
	/**
	 * TODO 设置 soundVolType 的值
	 */
	public void setSoundVolType(int soundVolType) {
		this.soundVolType = soundVolType;
	}
}