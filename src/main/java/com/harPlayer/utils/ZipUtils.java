package com.harPlayer.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipUtils
{
	
private static final Logger logger = Logger.getLogger(ZipUtils.class);		

private List<String> fileList;
private String OUTPUT_ZIP_FILE = "Folder.zip";
private String SOURCE_FOLDER = "D:\\Reports"; // SourceFolder path

public ZipUtils(String sourceFolder, String outputZipFolder) throws IOException
{
   fileList = new ArrayList<String>();
   OUTPUT_ZIP_FILE = outputZipFolder;
   SOURCE_FOLDER = sourceFolder;
   
   generateFileList(new File(SOURCE_FOLDER));
   zipIt(OUTPUT_ZIP_FILE);
}


public void zipIt(String zipFile) throws IOException
{
   byte[] buffer = new byte[1024];
   String source = "";
   FileOutputStream fos = null;
   ZipOutputStream zos = null;
   try
   {
      try
      {
         source = SOURCE_FOLDER.substring(SOURCE_FOLDER.lastIndexOf("\\") + 1, SOURCE_FOLDER.length());
      }
     catch (Exception e)
     {
        source = SOURCE_FOLDER;
     }
     fos = new FileOutputStream(zipFile);
     zos = new ZipOutputStream(fos);

     logger.debug("Output to Zip : " + zipFile);
     FileInputStream in = null;

     for (String file : this.fileList)
     {
        logger.debug("File Added : " + file);
        ZipEntry ze = new ZipEntry(source + File.separator + file);
        zos.putNextEntry(ze);
        try
        {
           in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
           int len;
           while ((len = in.read(buffer)) > 0)
           {
              zos.write(buffer, 0, len);
           }
        }
        finally
        {
           in.close();
        }
     }

     zos.closeEntry();
     logger.debug("Folder successfully compressed");

  }
  catch (IOException ex)
  {
	  logger.error("IOException",ex);
     throw ex;
  }
  finally
  {
     try
     {
    	if (zos!=null)
    		zos.close();
     }
     catch (IOException e)
     {
    	 logger.error("IOException",e);
        throw e;
     }
     catch (Throwable e2)
     {
    	 logger.error("Throwable",e2);
     }     
  }
}

public void generateFileList(File node)
{

  // add file only
  if (node.isFile())
  {
     fileList.add(generateZipEntry(node.toString()));

  }

  if (node.isDirectory())
  {
     String[] subNote = node.list();
     for (String filename : subNote)
     {
        generateFileList(new File(node, filename));
     }
  }
}

private String generateZipEntry(String file)
{
   return file.substring(SOURCE_FOLDER.length() + 1, file.length());
}
}  