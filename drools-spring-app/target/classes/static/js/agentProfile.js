var agent = null;


$(document).ready(function () 
{
	
	let str = window.location.href;
	let agent_id = str.substring(str.lastIndexOf('?')+1);
	
	$.ajax(
			{
			type: 'GET',
			url: 'rest/agent/' + agent_id,
			contentType: 'application/json',
			dataType:'json',
			complete: function(data)
				{
					agent = data.responseJSON;
					setupForAgent();
					
					getGlobals();
				}
			
			});
	
	
	
});


function setupForAgent()
{
	$("#welcome").text("Welcome agent " + agent.name + " - " + agent.accommodation.name + " in " + agent.accommodation.location.city.name + ", " + agent.accommodation.location.city.country.name)

	$("#regularNumber").val(agent.accommodation.regular);
	$("#discount").val(agent.accommodation.regularDiscount);
	$("#extra").val(agent.accommodation.extraPersonPrecentage);
	
	$.each(agent.inbox, function(i,message)
			{
				$("#inbox").append('<li style="margin-top:8%">' + message.text + '</li>');
			});
}


function set()
{
	let num = $("#regularNumber").val();
	let dicount = $("#discount").val();
	let extra = $("#extra").val();
	let spam = $("#spam").val();
	
	$.ajax(
			{
			type: 'PUT',
			url: 'rest/agent/' + num + '/' + dicount + '/' + extra + '/' + spam + '/' + agent.id,
			contentType: 'application/json',
			dataType:'json',
			complete: function(data)
				{
					window.localStorage.setItem("spam",spam);
					alert("Changed!")
					location.reload();
				}
			
			});
	
}



function setPeriod()
{
	let startPeriod = $("#start").val();
	let endPeriod = $("#end").val();
	
	if(startPeriod == "" || endPeriod == "")
		return;
	
	
	$.ajax(
			{
			type: 'PUT',
			url: 'rest/agent/setPeriod/' + startPeriod + '/' + endPeriod + '/' + agent.id,
			complete: function(data)
				{
					alert("Period set!")
					location.reload();
				}
			
			});
	
}



function getGlobals()
{
	$.ajax(
			{
			type: 'GET',
			url: 'rest/agent/globals' + '/' + agent.id,
			contentType: 'application/json',
			dataType:'json',
			complete: function(data)
				{
					dto = data.responseJSON;
					
					if(dto.spam!=null)
					{
						$("#spam").val(dto.spam);
					}
					
					if(dto.startPeriod!=null)
					{
						$("#start").val(dto.startPeriod);
					}
					
					if(dto.endPeriod!=null)
					{
						$("#end").val(dto.endPeriod);
					}
					
				}
			
			});
}