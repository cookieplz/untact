package com.sbs.untact.util;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {
	
	// 현재 시간 넣기
	public static String getNowDateStr() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
		Date time = new Date();
		return format1.format(time);
	}

	
	//mapOf
	public static Map<String, Object> mapOf(Object... args) {
		if ( args.length % 2 != 0 ) {
			throw new IllegalArgumentException("인자를 짝수개 입력해주세요.");
		}		
		int size = args.length / 2;
		
		Map<String, Object> map = new LinkedHashMap<>();
		
		for ( int i = 0; i < size; i++ ) {
			int keyIndex = i * 2;
			int valueIndex = keyIndex + 1;
			
			String key;
			Object value;
			
			try {
				key = (String)args[keyIndex];
			}
			catch ( ClassCastException e ) {
				throw new IllegalArgumentException("키는 String으로 입력해야 합니다. " + e.getMessage());
			}		
			value = args[valueIndex];			
			map.put(key, value);
		}
		return map;
	}
	
	
	// 어떠한 값이 BigInteger 타입이든 Long이든 double이든 int로 변환해주기
	public static int getAsInt(Object object, int defaultValue) {
		if (object instanceof BigInteger) {
			return ((BigInteger) object).intValue();
		} else if (object instanceof Double) {
			return (int) Math.floor((double) object);
		} else if (object instanceof Float) {
			return (int) Math.floor((float) object);
		} else if (object instanceof Long) {
			return (int) object;
		} else if (object instanceof Integer) {
			return (int) object;
		} else if (object instanceof String) {
			return Integer.parseInt((String) object);
		}
		return defaultValue;	// 변환에 실패하면 걍 defaultValue를 리턴해라
	}
	
}
