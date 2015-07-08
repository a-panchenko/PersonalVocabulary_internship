var synonymIndex = 0;

$(document).ready(function () {
    var synonymsValues = $('.synonym');
    $('.synonym').remove();
    for (var i = 0; i < synonymsValues.length; i++) {
        var td = "<tr class='synonym' id='" + synonymIndex + "'>" +
            "<td>" +
            "<label class='synonymValue'>" + synonymsValues.eq(i).text() + "</label>" +
            "</td>" +
            "<td>" +
            "<input type='button' class='deleteSynonymButton' value='-' onclick='deleteSynonym(" +
            synonymIndex + ")' id='" + synonymIndex +
            "'>" +
            "</td>" +
            "</tr>";
        $("#synonymTD").before(td);
        synonymIndex++;
    }
});

function sendUpdateWordRequest(path, successPath, wordId) {
    var synonyms = $('.synonymValue');
    var synonymsArray = [];
    for (var i = 0; i < synonyms.length; i++) {
        synonymsArray.push(synonyms.eq(i).text());
    }
    var word = $("input[name=word]").val();
    var translation = $("input[name=translation]").val();
    var examples = $("textarea[name=example]").val();
    var description = $("textarea[name=description]").val();
    var data = "wordId=" + wordId + "&word=" + word + "&translation=" + translation
        + "&description=" + description + "&example=" + examples
        + "&synonyms=" + synonymsArray;
    $.ajax({
        type: "POST",
        url: path,
        data: data,
        success: function (result) {
            if (result == "Ok") {
                goToPage(successPath);
            } else {
                alert(result);
            }
        },
        error:function(exception){alert('Exeption:'+exception);}
    });

}