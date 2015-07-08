var synonymIndex = 0;
function goToPage(location) {
    window.location = location;
}

function goToWordPage(path, isEditing) {
    window.location = path;
    $(document).ready(function () {
        if (isEditing == false) {
            $(".textbox").attr('readonly', 'readonly');
        }
    });
}

function addSynonym() {
    var value = document.getElementById('synonym_input').value;
    if (value.trim()) {
        var td = "<tr class='synonym' id='" + synonymIndex + "'>" +
            "<td>" +
            "<label class='synonymValue'>" + value + "</label>" +
            "</td>" +
            "<td>" +
            "<input type='button' class='deleteSynonymButton' value='-' onclick='deleteSynonym(" +
            synonymIndex + ")' id='" + synonymIndex +
            "'>" +
            "</td>" +
            "</tr>";
        $("#synonymTD").before(td);
        $("#synonym_input").val("");
        synonymIndex++;
    } else {
        $("#synonym_input").val("");
        alert("Synonym cannot be empty!")
    }
}

function deleteSynonym(id) {
    $("#" + id + ".synonym").remove();
}

function sendAddWordRequest(path, successPath) {
    var synonyms = $('.synonymValue');
    var synonymsArray = [];
    for (var i = 0; i < synonyms.length; i++) {
        synonymsArray.push(synonyms.eq(i).text());
    }
    var word = $("input[name=word]").val();
    var translation = $("input[name=translation]").val();
    var examples = $("textarea[name=example]").val();
    var description = $("textarea[name=description]").val();
    var data = "word=" + word + "&translation=" + translation + "&description=" + description +
        "&example=" + examples + "&synonyms=" + synonymsArray;
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