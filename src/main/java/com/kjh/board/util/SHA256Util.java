package com.kjh.board.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class SHA256Util {
	/*암호화 정리
	 * Salt값을 임의로 생성한다.
	 * 
	 * salt값과 사용자입력 password값을 받아서  두개를 byte배열로 합친 후
	 * 시큐리티에 MessageDigest를 사용해 암호화 한 후
	 * salt값과 과 암호화된 비밀번호를 저장한다.
	 * 
	 * 
	 * 암호화 가지고 오기
	 * 다른 칼럼 값을 조회해서 DB에 있는 salt값을 구한다.
	 * 패스퉈드를 조회한다.
	 * 
	 * 
	*/
	
	// salt를 생성한다. 이값은 DB에 저장된다. 저장한 값으로 비밀번호를 구한다.
	public String generateSalt() {
		Random random = new Random();

		byte[] salt = new byte[8];
		// 랜덤한 수를 만들어 salt배열에 넣어라
		random.nextBytes(salt);

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < salt.length; i++) {
			// byte 값을 Hex(16진수) 값으로 바꾸기.
			sb.append(String.format("%02x", salt[i]));
		}

		return sb.toString();
	}

	/**
	 * salt와 타켓스트링을 받아서 바이트 배열로 변환한다.
	 * 
	 */

	public String getEncrypt(String targetString, String salt) {

		byte[] targetBytes = targetString.getBytes();
		byte[] saltBytes = salt.getBytes();

		String result = cal_encrypt(targetBytes, saltBytes);

		return result;
	}

	/**
	 * 두개의 배열을 만들어
	 */
	private  String cal_encrypt(byte[] targetBytes, byte[] saltBytes) {

		String result = "";
		// 바이트 배열에 옴겨 담기
		byte[] bytes = new byte[targetBytes.length + saltBytes.length];
		System.arraycopy(targetBytes, 0, bytes, 0, targetBytes.length);
		System.arraycopy(saltBytes, 0, bytes, targetBytes.length, saltBytes.length);

		try {
			// 해당 암호화 인스턴스를 얻는다.
			MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
			mDigest.update(bytes); // 암호화를 실행한다.

			byte[] resultBytes = mDigest.digest();// 이거 암호화 결과값 같은데?

			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < resultBytes.length; i++) {
				// resultBytes[i]&0xFF;여기서 byte범위가 -128~127까지인데 0~255범위로 표현가능하게 하는 것 16진수로 만든다.
				// 바이트가 int로 형변환되어(진수가바뀌는건 아니다.) 계산
				int s = (resultBytes[i] & 0xFF);
				System.out.println("(resultBytes[i] & 0xFF) 값 ::: "+s);
				// Integer.toString(256, 16);//앞에 값을 16진수로 만든다.
				String ss = Integer.toString(s + 0x100, 16).substring(1);
				
				sBuffer.append(ss);
			}
			result = sBuffer.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			result = null;
		}

		return result;
	}

}
