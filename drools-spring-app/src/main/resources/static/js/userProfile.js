var user = null;
var cities = [];

$(document).ready(function () 
{
	let str = window.location.href;
	let user_id = str.substring(str.lastIndexOf('?')+1);
		
	$.ajax(
		{
		type: 'GET',
		url: 'rest/user/'+user_id,
		contentType: 'application/json',
		dataType:'json',
		complete: function(data)
		{
			
			user = data.responseJSON;
			console.log(user);
			setUserUp();
			
			
			$.ajax(
					{
					type: 'GET',
					url: 'rest/accommodation/',
					contentType: 'application/json',
					dataType:'json',
					complete: function(data)
					{
						$.each(data.responseJSON, function(i,accommodation)
						{
							addAccommodation(accommodation);
							found = false;
							for (var i = 0; i < cities.length; i++)
							{
								if(cities[i].id == accommodation.location.city.id)
								{
									found = true;
									break;
								}
							}
							if(found == false)
							{
								cities.push(accommodation.location.city);
							}
						});
						
						addCities();
					}
					});
			
			
			$.ajax(
					{
					type: 'GET',
					url: 'rest/reservation/' + user.id,
					contentType: 'application/json',
					dataType:'json',
					complete: function(data)
					{
						
						
						if(data.responseJSON.length == 0 )
							return;
						
						$.each(data.responseJSON, function(i,reservation)
								{
									console.log(i + " rez: " + reservation.state)
									addReservation(reservation);	
								});
					}
					});
			
			
			$.ajax(
					{
					type: 'GET',
					url: 'rest/recommendations/' + user.id,
					contentType: 'application/json',
					dataType:'json',
					complete: function(data)
					{
						console.log(data.responseJSON)
						$.each(data.responseJSON, function(i,recommended)
								{
									addRecommended(recommended);	
								});
					}
					});
		}
		});
	

	
	
});


function setUserUp()
{
	$('#welcome').text('Welcome ' + user.name  + '!');
	$('#category').text('category: ' + user.category);
	$('#coupons').text('number of coupons: ' + user.coupons);	
}


function addAccommodation(accommodation)
{
	$('#accommodations').append('<option value="'+ accommodation.id +'">' + accommodation.name + ' - ' + accommodation.category + " - " + accommodation.location.city.name +", "+ accommodation.location.city.country.name + '</option>')
	$('#accommodationsMessages').append('<option value="'+ accommodation.id +'">' + accommodation.name + ' - ' +  accommodation.location.city.name +", "+ accommodation.location.city.country.name + '</option>')
}

function addReservation(reservation)
{
	
	if(reservation.state == "CANCELLED")
	{
		$('#reservationList').append('<p style="color: red">CANCELLED</p>');
	}
	if(reservation.state == "FINISHED")
	{
		$('#reservationList').append('<p style="color: blue">FINISHED</p>');
	}
	
	$('#reservationList').append('<p><b>' + reservation.accommodation.name + ' in ' + reservation.accommodation.location.city.name + ' - ' + reservation.accommodation.location.city.country.name + ': ' + reservation.price + 'e </b></p>')
	if(reservation.state == "ACTIVE")
	{
		$('#reservationList').append('<button onclick="cancel(' + reservation.id + ')" class="btn btn-primary">Cancle</button>')
	}
}


function reserve()
{
	let startDate = $("#start").val();
	let endDate = $("#end").val();
	let number = $("#numOfPersons").val();
	let accommodation = $("#accommodations").val();

	if(accommodation == "x")
		return;
		
	
	$.ajax(
			{
			type: 'POST',
			url: 'rest/reservation/' + startDate + "/" + endDate + "/" + number + "/" + accommodation + "/" + user.id,
			complete: function(data)
			{
				alert(data.responseText);
				location.reload();
			}
			});
}



function cancel(val)
{
	$.ajax(
			{
			type: 'PUT',
			url: 'rest/reservation/' + val + "/" + user.id,
			contentType: 'application/json',
			dataType:'json',
			complete: function(data)
			{
				alert(data.responseText);
				location.reload();
			}
			});
}




function addRecommended(recommended)
{
	$("#recommended").append('<p><b>' + recommended.name + '</b> in ' + recommended.location.city.name + ' - ' + recommended.category + ' - only ' + recommended.location.distanceFromCity + 'km from city center!</p>');
}


function sendMessage()
{
	let accommodation = $("#accommodationsMessages").val();
	let message = $("#message").val();
	
	if(accommodation == "x")
		return;
	if(message == "")
		return;
	
	$.ajax(
			{
			type: 'PUT',
			url: 'rest/accommodation/message/' + accommodation + '/' + message + '/' + user.id,
			complete: function(data)
			{

				alert("Message sent");
				location.reload();
			}
			});
	
}

function addCities()
{
	for (var i = 0; i < cities.length; i++)
	{
		$("#cities").append(('<option value="'+ (cities[i]).id +'">' + (cities[i]).name + '</option>'));
	}

}

function defineRule()
{
	
	let rule = $("#rule").val()
	
	if(rule == "")
		return;
		
	$.ajax({
			
			url: "rest/defineRule",
			type: 'POST',
			contentType: 'application/json',
			data: rule,
			dataType:"text",
			complete: function(data)
			{
				
				alert(data.responseText);
				
			}
			
		});
}

function search()
{
	let startDate = $("#startSearch").val();
	let endDate = $("#endSearch").val();
	let city = $("#cities").val();

	if(city == "x")
		return;
	
	console.log(city + " " + startDate + " " + endDate);
		
	$.ajax({
		
		url: "rest/query/" + city + "/" + startDate + "/" + endDate,
		type: 'POST',
		complete: function(data)
		{
			
			alert("ok");
			
		}
		
	});
}

