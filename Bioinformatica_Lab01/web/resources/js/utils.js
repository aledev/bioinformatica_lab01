 function ShowHideFilter(element) {
    if ($(element).parent().parent().parent().find('.panel-body').is(':visible')) {
        $(element).find('span').removeClass('glyphicon-menu-up');
        $(element).find('span').addClass('glyphicon-menu-down');
    }
    else {
        $(element).find('span').removeClass('glyphicon-menu-down');
        $(element).find('span').addClass('glyphicon-menu-up');
    }

    $(element).parent().parent().parent().find('.panel-body').toggle();
}