/*
* As autocomplete="off" is not valid xhtml we must set this attribute
* using javascript as a workaround. Autocomplete must be disabled on
* the login form.
*/
function disable_autocomplete() {
	if (document.getElementsByTagName) { 
		var inputElements = document.getElementsByTagName("input"); 
		for (var i = 0; inputElements[i]; i++) { 
			inputElements[i].setAttribute("autocomplete","off"); 
		}
	}
}