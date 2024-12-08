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
			// On success, get the response (file path) and display it
			var fileLink = xhr.responseText;
			document.getElementById('fileLink').innerText = 'Video uploaded: ' + fileLink;
		} else {
			alert('Error uploading file.');
		}
	};
	// Send the form data with the file to the server
	xhr.send(formData);
}