import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 50,      // 50MB
    maxRequestSize = 1024 * 1024 * 100   // 100MB
)

@WebServlet("/uploadVideo")
public class VideoUploadSerlvet extends HttpServlet {
    private static final String TEMP_DIR = "temp";
    private static final String UPLOAD_DIR = "uploads";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        // HttpSession session = request.getSession();
        // todo:
        int userId = -1;
        // String username = (String) session.getAttribute("username");

        // Determine the folder for saving the file
        String uploadPath;
        if (userId >= 0) {
            uploadPath = getServletContext().getRealPath("") + UPLOAD_DIR + File.separator + userId;
            // uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR + File.separator + username;

        } else {
            uploadPath = getServletContext().getRealPath("") + TEMP_DIR;
            // uploadPath = getServletContext().getRealPath("") + File.separator + TEMP_DIR;

        }
        System.out.println("Upload path: " + uploadPath);

        // Create the directory if it doesn't exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Process the uploaded file
        for (Part part : request.getParts()) {
            String fileName = extractFileName(part);
            if (fileName != null && !fileName.isEmpty()) {
                String filePath = uploadPath + File.separator + fileName;
                // check if the file already existed inside the directory
                boolean isFileExists = new File(uploadPath, fileName).exists();
                if (isFileExists) { // file already inside the directory
                	// unable to upload
                	System.out.println("Video already exists. ");
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.setContentType("text/plain");
                    response.getWriter().write("Video already exists");
//                    response.getWriter().flush();
                } else { // file not inside the directory
                    part.write(filePath);
                    System.out.println("File written to: " + filePath);
                   
                    // Generate a link to the uploaded file
                    String fileLink = request.getContextPath() + "/" + (userId >= 0 ? UPLOAD_DIR + "/" + userId : TEMP_DIR) + "/" + fileName;

                    // Send the link back to the user
                    response.setContentType("text/plain");
                    response.getWriter().write(fileLink);
                }
            }
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String content : contentDisp.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return null;
    }
}