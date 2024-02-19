
$(document).ready(function () {
	updateClock();
	setInterval(updateClock, 1000);
	
});

function updateClock() {
	var now = new Date();
	var formattedTime = now.toLocaleString('ja-JP', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false });
    document.getElementById('clock').innerText = formattedTime;
}

