function login () 
{
	event.preventDefault();
	let email = $('#email').val();
	let password = $('#password').val();
	
	
$.ajax({
		
		url: "rest/login",
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify({email: email, password: password}),
		dataType:"json",
		complete: function(data)
		{
			if(data.responseJSON == undefined)
			{
				alert("No user found.");
				return;
			}
			
			
			if(data.responseJSON.accommodation == undefined)
			{
				
				window.location.href = "userProfile.html?" + data.responseJSON.id;
			}
			else
			{
				window.location.href = "agentProfile.html?" + data.responseJSON.id;
			}
			
		}
		
	});
}