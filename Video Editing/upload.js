function uploadVideo() {
	// Get the file input element and the file
	var fileInput = document.getElementById('videoFile');
	var file = fileInput.files[0];
	
	// Prepare the form data to send
	var formData = new FormData();
	formData.append('videoFile', file);

	// Create an XMLHttpRequest to send the file
	var xhr = new XMLHttpRequest();
	xhr.open('POST', '/ClipShuffle/uploadVideo', true);
	// Set up a callback function to handle the response

	xhr.onload = function () {
		if (xhr.status === 200) {
			// On success, get the response (file path) and display it under the upload button
			var fileLink = xhr.responseText;
			document.getElementById('filePath').innerText = 'Video uploaded: ' + fileLink;
			
			// add one row for the vidoetables
			var table = document.getElementById('videoTable');
			var row = table.insertRow();
            var id = row.insertCell(0);
            var filename = row.insertCell(1);
			
			id.innerHTML = table.rows.length - 2;
			filename.innerHTML = file.name;
			
			// let txt = "";
			// txt = "<tr>" + file.name + "</tr>";
			// document.getElementById('videoTable').innerHTML = txt;
			
		} else if(xhr.status === 409){ // file already existed error
			document.getElementById('filePath').innerText = xhr.responseText;
		} else {
			alert('Error uploading file.');
		}
	};
	// Send the form data with the file to the server
	xhr.send(formData);
}