package com.nelioalves.cursomc.services.media;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nelioalves.cursomc.services.exceptions.FileException;

@Service
public class ImagemService {
	
	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		
		String extensao = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
		
		if(!extensao.equals("jpg") && !extensao.equals("png")) {
			throw new FileException("Somente imagens JPG ou PNG são permitidas!");
		}
		
		try {
			
			BufferedImage bufferedImage = ImageIO.read(uploadedFile.getInputStream());
			
			if(extensao.equals("png")) {
				bufferedImage = pngToJpg(bufferedImage);
			}
			
			return bufferedImage;
			
		} catch (IOException error) {
			throw new FileException("Erro ao Ler Arquivo!");
		}
		
	}

	
	
	public BufferedImage pngToJpg(BufferedImage bufferedImage) {
		BufferedImage jpgImage = new BufferedImage(bufferedImage.getWidth(), 
												   bufferedImage.getHeight(), 
												   BufferedImage.TYPE_INT_BGR);
		
		jpgImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
		
		return jpgImage;
	}
	
	
	public InputStream getInputStream(BufferedImage bufferedImage, String extension) {
		try {
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			ImageIO.write(bufferedImage, extension, outputStream);
			
			return new ByteArrayInputStream(outputStream.toByteArray());
			
		}catch(IOException erro) {
			throw new FileException("Erro ao Ler Arquivo!");
		}
	}
	
	
	public BufferedImage cropSquare(BufferedImage sourceImage) {
		int min = (sourceImage.getHeight() <= sourceImage.getWidth()) ? sourceImage.getHeight() : sourceImage.getWidth();
		
		return Scalr.crop(sourceImage,
						 (sourceImage.getWidth() / 2) - (min / 2),
						 (sourceImage.getHeight() / 2) - (min / 2), 
						 min, 
						 min) ;
	}
	
	
	public BufferedImage resize(BufferedImage sourceImg, int size) {
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}
}
