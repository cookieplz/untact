package com.sbs.untact.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	
	// 메시지와 이전 주소로 리턴을 위한 함수
	public static String msgAndBack(String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('" + msg + "');");
		sb.append("history.back();");
		sb.append("</script>");

		return sb.toString();
	}
	
	
	// 메시지와 다른 주소로 리턴을 위한 함수
	public static String msgAndReplace(String msg, String url) {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>");
		sb.append("alert('" + msg + "');");
		sb.append("location.replace('" + url + "');");
		sb.append("</script>");

		return sb.toString();
	}
	
	
	// 객체를 Json데이터 형태로 바꿔준다
	public static String toJsonStr(Map<String, Object> param) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(param);
		}catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	// 리퀘스트에서 파라미터 뽑아서 Map형태로 바꿔줌
	public static Map<String, Object> getParamMap(HttpServletRequest request){
		Map<String, Object> param = new HashMap<>();
		
		Enumeration<String> parameterNames = request.getParameterNames();
		
		while(parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			Object paramValue = request.getParameter(paramName);
			
			param.put(paramName, paramValue);
		}
		return param;
	}
	
	
	// Url 인코딩
	public static String getUrlEncoded(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		}catch(UnsupportedEncodingException e) {
			return str;
		}
	}
	
	
	// Null인지 확인 
	public static <T> T ifNull(T data, T defaultValue) {
		return data != null ? data : defaultValue;
	}
	
	
	// Util.reqAttr(request, "encodedAfterLoginUrl", " "); request에서 encodedAfterLoginUrl을 뽑되 만약 null이면 " " 다.
	public static <T> T reqAttr(HttpServletRequest req, String attrName, T defaultValue) {
		return (T) ifNull(req.getAttribute(attrName), defaultValue);
	}
	
	
	// 공백인지 확인
	public static boolean isEmpty(Object data) {
		if (data == null) {
			return true;
		}
		if (data instanceof String) {
			String strData = (String) data;
			return strData.trim().length() == 0;
		} else if (data instanceof Integer) {
			Integer integerData = (Integer) data;
			return integerData != 0;
		} else if (data instanceof List) {
			List listData = (List) data;
			return listData.isEmpty();
		} else if (data instanceof Map) {
			Map mapData = (Map) data;
			return mapData.isEmpty();
		}
		return true;
	}
	
	//공백인지 확인해서 공백이면 디폴트데이터 넘김
	public static <T> T ifEmpty(T data, T defaultValue) {
		if ( isEmpty(data) ) {
			return defaultValue;
		}
		return data;
	}
	
	
	// 파일 확장자 체크

	public static String getFileExtTypeCodeFromFileName(String fileName) {
		String ext = getFileExtFromFileName(fileName).toLowerCase();

		switch (ext) {
		case "jpeg":
		case "jpg":
		case "gif":
		case "png":
			return "img";
		case "mp4":
		case "avi":
		case "mov":
			return "video";
		case "mp3":
			return "audio";
		}

		return "etc";
	}

	
	public static String getFileExtType2CodeFromFileName(String fileName) {
		String ext = getFileExtFromFileName(fileName).toLowerCase();

		switch (ext) {
		case "jpeg":
		case "jpg":
			return "jpg";
		case "gif":
			return ext;
		case "png":
			return ext;
		case "mp4":
			return ext;
		case "mov":
			return ext;
		case "avi":
			return ext;
		case "mp3":
			return ext;
		}
		
		return "etc";
	}

	
	//파일명에서 확장자만 따로 분리하기
	public static String getFileExtFromFileName(String fileName) {
		int pos = fileName.lastIndexOf(".");
		String ext = fileName.substring(pos + 1);
		return ext;
	}

	public static String getNowYearMonthDateStr() {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy_MM");
		String dateStr = format1.format(System.currentTimeMillis());
		return dateStr;
	}
	
	
	public static List<Integer> getListDividedBy(String str, String divideBy) {
		return Arrays.asList(str.split(divideBy)).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
	}
	
	
	public static boolean delteFile(String filePath) {
		java.io.File ioFile = new java.io.File(filePath);
		if (ioFile.exists()) {
			return ioFile.delete();
		}

		return true;
	}
}
