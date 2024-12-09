import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


@WebServlet("/clearUploads")
public class ClearUploadServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "uploads";
    private static final String TEMP_DIR = "temp";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	int userId = -1;
        // Define the paths to the directories
        String tempPath = getServletContext().getRealPath("") + TEMP_DIR;
        String uploadsPath = getServletContext().getRealPath("") + UPLOAD_DIR + File.separator + userId;
        
        // Clear files in both TEMP_DIR and UPLOAD_DIR
        boolean success  = false;
        if(userId >= 0) {
            System.out.println("Delete Uploads Path: " + uploadsPath);
            success = delete(new File(uploadsPath));
        }else {
            System.out.println("Delete Temp Path: " + tempPath);
            success = delete(new File(tempPath));
        }

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Files cleared successfully.");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Failed to clear files.");
        }
    }

    private boolean delete(File directory) {
    	// non-existing directory
        if (!directory.exists()) {
            return true; 
        }
        
        // Get all files from the file directory
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
            	if (!file.delete()) {
                    System.err.println("Failed to delete file: " + file.getAbsolutePath());
                    return false;
                }
//            	// if the file is a sub directory
//                if (file.isDirectory()) {
//                    // Recursively delete this sub directory
//                    if (!delete(file)) {
//                        return false;
//                    }
//                } else { // if it is just a file
//                    // Delete the file
//                    if (!file.delete()) {
//                        System.err.println("Failed to delete file: " + file.getAbsolutePath());
//                        return false;
//                    }
//                }
            }
        }
        // delete directory
        return directory.delete(); 
    }
}
