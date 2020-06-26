package com.project.utilities;


import java.io.ByteArrayOutputStream;

import org.apache.commons.codec.binary.Base64;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

public class QRCodeGeneration {
	public static byte[] code(String str) throws Exception {
		
		ByteArrayOutputStream out= QRCode.from(str).to(ImageType.JPG).stream();
		byte[] ImageInString =  Base64.encodeBase64(out.toByteArray());
			
		return ImageInString;
	}
}
