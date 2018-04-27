package com.nelioalves.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Uri {
	
	public static List<Integer> decodeIntList(String string){
		/**String vetor [] = string.split(",");
		
		List<Integer> lista = new ArrayList<>();
		
		for (int i = 0; i < vetor.length; i++) {
			lista.add(Integer.parseInt(vetor[i]));
		}
		
		return lista;**/
		
		return Arrays.asList(string.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}
	
	
	public static String decodeParam(String string) {
		try {
			return URLDecoder.decode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
}
