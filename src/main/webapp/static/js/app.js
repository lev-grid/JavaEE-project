$('html').bind('keydown', eObj => {
	if (eObj.which == '37') { // left
		if ($('#de').children().last().css('color') != $('#de').children().last().css('background-color')) {
			$('#de').children().css('color', $('#de').children().css('background-color'));
			if ($('#ru').children().last().css('color') == $('#ru').children().last().css('background-color')) {
				$('#ru').children().css('color', '');
			}
		} else {
			$('#de').children().css('color', '');
		}
	}
	if (eObj.which == '39') { // right
		if ($('#ru').children().last().css('color') != $('#ru').children().last().css('background-color')) {
			$('#ru').children().css('color', $('#ru').children().css('background-color'));
			if ($('#de').children().last().css('color') == $('#de').children().last().css('background-color')) {
				$('#de').children().css('color', '');
			}
		} else {
			$('#ru').children().css('color', '');
		}
	}
	if (eObj.which == '13') { // enter
		//console.log('enter');
		//var rumas = $('#ru').children();
		for (let i = 0; i < $('#de').children().length; ++i) {
			if ($('#de').children().eq(i).css('color') == $('#de').children().eq(i).css('background-color')) {
				$('#de').children().eq(i).css('color', '');
				break;
			}
		}
	}
	/*if (eObj.which == '32') { // пробел
		$('#cont').animate({width: '80%'}, 1000);
		$('#menu').animate({width: '20%'}, 1000);
	}*/
});


$('#knp').on('click', e => {
	if ($('#menu').css('width') == '0px') {
		$('#cont').animate({width: '80%', marginLeft: '20%'}, 1000);
		$('#menu').animate({width: '20%'}, 1000);
	} else {
		$('#cont').animate({width: '100%', marginLeft: '0%'}, 1000);
		$('#menu').animate({width: '0%'}, 1000);
	}

})
$('a').on('mouseover', eObj => {
	$(eObj.target).css('background-color', '#bbf');
	$(eObj.target).css('color', '#000');
});
$('a').on('mouseout', eObj => {
	$(eObj.target).css('background-color', '');
	$(eObj.target).css('color', '');
});