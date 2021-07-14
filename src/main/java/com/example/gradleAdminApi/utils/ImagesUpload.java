package com.example.gradleAdminApi.utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.example.gradleAdminApi.model.entity.GoodsImage;

import net.coobird.thumbnailator.Thumbnails;

@Component
public class ImagesUpload {

//	private static final String uploadDir = "F:\\git\\gradleMall\\gradleFront\\static";

	static final int THUMB_WIDTH = 300;
	static final int THUMB_HEIGTH = 300;
	
	public Map<String, String> imgUpload(String oriFileName, byte[] fileData) throws IOException {
		UUID uid = UUID.randomUUID();
		
		Map<String, String> filePath = new HashMap<>();
		
		// File Folder
		String uploadFolderPath = getUploadDirPath() + File.separator + "imgUpload";
		String ymdPath = calcPath(uploadFolderPath);
		String imgPath = uploadFolderPath + ymdPath;
		
		// New File Name
		String newFileName = uid + "_" + oriFileName;
		
		File target = new File(imgPath, newFileName);
		FileCopyUtils.copy(fileData, target);
		
		// Thumb nail File Name
		String thumbFileName = "s_" + newFileName;
		// Create File for Thumb nail
		File image = new File(imgPath + File.separator + newFileName);
		// Create Thumb nail
		File thumbnail = new File(imgPath + File.separator + "s" + File.separator + thumbFileName);
		
		// Re-size thumb nail File
		if(image.exists()) {
			thumbnail.getParentFile().mkdirs();
			Thumbnails.of(image).size(THUMB_WIDTH, THUMB_HEIGTH).toFile(thumbnail);
		}
		
		filePath.put("ymdPath", ymdPath);
		filePath.put("newFileName", newFileName);
		filePath.put("uploadDir", getUploadDirPath());
		
		return filePath;
}
	
	// year, month, date Folders Path
	public static String calcPath(String uploadDir, String...paths) throws IOException {
		
		Calendar cal = Calendar.getInstance();
		String yearPath = File.separator + cal.get(Calendar.YEAR);
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		
		makeDir(uploadDir, yearPath, monthPath, datePath);
		makeDir(uploadDir, yearPath, monthPath, datePath);
		
		return datePath;
	}
	
	// Create Folder
	private static void makeDir(String uploadDir, String...paths) throws IOException {
		
		// Check folder
		if(new File(paths[paths.length - 1]).exists()) { return; }
		
		// Create folder
		for(String path: paths) {
			File dirPath = new File(uploadDir, path);
			
			if(!dirPath.exists()) {
				dirPath.mkdirs();
			}
		}
	}
	
	// Delete images in directory
	public void deleteImages(List<GoodsImage> goodsImages) throws IOException {

		String uploadPath = getUploadDirPath();

		goodsImages.forEach(imgFile -> {
			new File(uploadPath + File.separator + imgFile.getGdsImg()).delete();
			new File(uploadPath + File.separator + imgFile.getGdsThumbImg()).delete();
		});
	}

	private String getUploadDirPath() throws IOException {
		return new File("..").getCanonicalPath() + File.separator + "uploadedImages";
	}
}
